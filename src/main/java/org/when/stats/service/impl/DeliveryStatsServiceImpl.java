package org.when.stats.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.when.data.delivery.entity.DeliveryOrderEntity;
import org.when.data.delivery.entity.DeliveryRecord;
import org.when.data.staff.domain.StaffType;
import org.when.data.staff.entity.StaffEntity;
import org.when.stats.domain.StatsTemplate;
import org.when.stats.repository.DeliveryOrderRepository;
import org.when.stats.repository.DeliveryRecordRepository;
import org.when.stats.repository.StaffRepository;
import org.when.stats.service.DeliveryStatsService;
import org.when.stats.vo.DormDeliveryStatsResult;
import org.when.stats.vo.DormDeliveryStatsVO;
import org.when.stats.vo.StaffStatsResult;
import org.when.stats.vo.StaffStatsVO;
import org.when.util.Lambdas;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class DeliveryStatsServiceImpl extends StatsTemplate<DeliveryRecord, StaffStatsVO, StaffStatsResult> implements DeliveryStatsService {

    private final DeliveryRecordRepository recordRepository;
    private final DeliveryOrderRepository orderRepository;

    @Autowired
    public DeliveryStatsServiceImpl(StaffRepository staffRepository,
                                    DeliveryRecordRepository recordRepository,
                                    DeliveryOrderRepository orderRepository) {
        super(staffRepository);
        this.recordRepository = recordRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public StaffStatsResult deliveryDailyStatsByType(Integer staffType, LocalDate day) {
        return statsStaffData(staffType, day);
    }

    @Override
    public StaffStatsResult deliveryDailyStatsByPhone(String phone, LocalDate day) {
        return statsStaffData(phone, day);
    }

    @Override
    protected List<DeliveryRecord> loadDataByTypeAndDate(List<StaffEntity> staffs, LocalDate day) {
        List<String> phones = Lambdas.map(staffs, StaffEntity::getPhone);
        return recordRepository.findAllBetween(getStartTime(day), getEndTime(day), phones);
    }

    @Override
    protected List<DeliveryRecord> loadDataByPhoneAndDate(String phone, LocalDate day) {
        return recordRepository.findAllBetween(getStartTime(day), getEndTime(day), phone);
    }

    @Override
    protected List<StaffStatsVO> statsData(LocalDate day, List<DeliveryRecord> data, List<StaffEntity> staffs) {
        List<StaffStatsVO> result = new ArrayList<>();
        Map<String, Integer> phoneQuantityMap = data.stream()
                .collect(Collectors.groupingBy(
                        DeliveryRecord::getWatermanPhone,
                        Collectors.summingInt(DeliveryRecord::getQuantity)
                ));

        Map<String, StaffEntity> staffMap = staffs.stream()
                .collect(toMap(StaffEntity::getPhone, Function.identity()));

        phoneQuantityMap.forEach(
                (checkmanPhone, quantity) -> {
                    StaffEntity staff = staffMap.get(checkmanPhone);
                    StaffStatsVO stats = new StaffStatsVO(staff.getName(), day, quantity);
                    result.add(stats);
                }
        );
        return result;
    }

    @Override
    protected Function<StaffStatsVO, Integer> summarizeFunction() {
        return StaffStatsVO::getQuantity;
    }

    @Override
    protected BiFunction<Integer, List<StaffStatsVO>, StaffStatsResult> resultFunction() {
        return (t, u) -> new StaffStatsResult(t, u);
    }

    @Override
    public DormDeliveryStatsResult dormDeliveryDailyStats(LocalDate day, String watermanId) {
        Set<String> watermanIds = findIdsOf(watermanId);
        List<DeliveryOrderEntity> orders = findDeliveryOrdersOf(day, watermanIds);
        List<DormDeliveryStatsVO> stats = statsDeliveryOrders(orders, day, StaffType.WATERMAN.getCode());
        DormDeliveryStatsVO total = summarizeTotalOrders(stats);
        return new DormDeliveryStatsResult()
                .setStats(stats)
                .setTotalRecords(total);
    }

    private Set<String> findIdsOf(String watermanId) {
        Set<String> ids = new HashSet<>();
        if (StringUtils.hasText(watermanId)) {
            ids.add(watermanId);
        } else {
            List<StaffEntity> staffs = findStaffsByType(StaffType.WATERMAN.getCode());
            List<String> staffIds = Lambdas.map(staffs, StaffEntity::getId);
            ids.addAll(staffIds);
        }
        return ids;
    }

    /**
     * 统计每个人指定日期的送水数量
     *
     * @param day    日期
     * @param orders 送水数量列表
     * @return 统计结果
     */
    private List<DormDeliveryStatsVO> statsDeliveryOrders(List<DeliveryOrderEntity> orders, LocalDate day, Integer staffType) {
        List<DormDeliveryStatsVO> result = new ArrayList<>();
        Map<String, DormDeliveryStatsVO> idQuantityMap = orders.stream()
                .collect(groupingBy(
                        DeliveryOrderEntity::getWatermanId,
                        collectingAndThen(
                                reducing((order1, order2) -> {
                                    DeliveryOrderEntity order = new DeliveryOrderEntity();
                                    order.setWatermanId(order.getWatermanId());
                                    order.setDeliveredBucket(order1.getDeliveredBucket() + order2.getDeliveredBucket());
                                    order.setRecycledBucket(order1.getRecycledBucket() + order2.getRecycledBucket());
                                    order.setRecycledETicket(order1.getRecycledETicket() + order2.getRecycledETicket());
                                    order.setRecycledPaperTicket(order1.getRecycledPaperTicket() + order2.getRecycledETicket());
                                    return order;
                                }),
                                opt -> opt.map(order -> {
                                            DormDeliveryStatsVO dormDeliveryStatsVO = new DormDeliveryStatsVO();
                                            dormDeliveryStatsVO.setDeliveredBucket(order.getDeliveredBucket());
                                            dormDeliveryStatsVO.setRecycledBucket(order.getRecycledBucket());
                                            dormDeliveryStatsVO.setRecycledETicket(order.getRecycledETicket());
                                            dormDeliveryStatsVO.setRecycledPaperTicket(order.getRecycledPaperTicket());
                                            return dormDeliveryStatsVO;
                                        }
                                ).orElse(new DormDeliveryStatsVO())
                        )
                ));
        List<StaffEntity> staffs = findStaffsByType(staffType);
        Map<String, StaffEntity> staffMap = staffs.stream()
                .collect(toMap(StaffEntity::getId, Function.identity()));

        idQuantityMap.forEach(
                (watermanId, quantity) -> {
                    StaffEntity staff = staffMap.get(watermanId);
                    quantity.setName(staff.getName());
                    quantity.setDate(day);
                    result.add(quantity);
                }
        );
        return result;
    }

    /**
     * 查找指定日期和水工的送水记录
     *
     * @param day         日期
     * @param watermanIds 水工手机号集合
     * @return 送水记录列表
     */
    private List<DeliveryOrderEntity> findDeliveryOrdersOf(LocalDate day, Set<String> watermanIds) {
        return orderRepository.findAllBetween(getStartTime(day), getEndTime(day), watermanIds);
    }

    /**
     * 计算总送水数量
     *
     * @param statsList 送水数量统计结果
     * @return 总送水数量
     */
    private DormDeliveryStatsVO summarizeTotalOrders(List<DormDeliveryStatsVO> statsList) {
        return statsList.stream()
                .reduce(new DormDeliveryStatsVO(), DormDeliveryStatsVO::accept, DormDeliveryStatsVO::combine);
    }
}
