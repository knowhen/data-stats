package org.when.stats.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.when.data.order.entity.VerifyRecord;
import org.when.data.staff.entity.StaffEntity;
import org.when.stats.domain.StatsTemplate;
import org.when.stats.repository.StaffRepository;
import org.when.stats.repository.VerifyRepository;
import org.when.stats.service.VerifyStatsService;
import org.when.stats.vo.StaffStatsResult;
import org.when.stats.vo.StaffStatsVO;
import org.when.util.Lambdas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Service
public class VerifyStatsServiceImpl extends StatsTemplate<VerifyRecord, StaffStatsVO, StaffStatsResult> implements VerifyStatsService {

    private final VerifyRepository verifyRepository;

    @Autowired
    public VerifyStatsServiceImpl(StaffRepository staffRepository, VerifyRepository verifyRepository) {
        super(staffRepository);
        this.verifyRepository = verifyRepository;
    }

    @Override
    public StaffStatsResult dailyStats(Integer staffType, LocalDate day) {
        return statsStaffData(staffType, day);
    }

    @Override
    public StaffStatsResult dailyStats(String phone, LocalDate day) {
        return statsStaffData(phone, day);
    }

    @Override
    protected List<VerifyRecord> loadDataByTypeAndDate(List<StaffEntity> staffs, LocalDate day) {
        List<String> phones = Lambdas.map(staffs, StaffEntity::getPhone);
        return verifyRepository.findAllBetween(getStartTime(day), getEndTime(day), phones);
    }

    @Override
    protected List<VerifyRecord> loadDataByPhoneAndDate(String phone, LocalDate day) {
        return verifyRepository.findAllBetween(getStartTime(day), getEndTime(day), phone);
    }

    @Override
    protected List<StaffStatsVO> statsData(LocalDate day, List<VerifyRecord> data, List<StaffEntity> staffs) {
        List<StaffStatsVO> statsList = new ArrayList<>();

        Map<String, Integer> phoneQuantityMap = data.stream()
                .collect(groupingBy(
                        VerifyRecord::getCheckmanPhone,
                        summingInt(VerifyRecord::getQuantity)
                ));

        Map<String, StaffEntity> staffMap = staffs.stream()
                .collect(toMap(StaffEntity::getPhone, Function.identity()));

        phoneQuantityMap.forEach(
                (checkmanPhone, quantity) -> {
                    StaffEntity staff = staffMap.get(checkmanPhone);
                    StaffStatsVO stats = new StaffStatsVO(staff.getName(), day, quantity);
                    statsList.add(stats);
                }
        );
        return statsList;
    }

    @Override
    protected Function<StaffStatsVO, Integer> summarizeFunction() {
        return StaffStatsVO::getQuantity;
    }

    @Override
    protected BiFunction<Integer, List<StaffStatsVO>, StaffStatsResult> resultFunction() {
        return (t, u) -> new StaffStatsResult(t, u);
    }

}
