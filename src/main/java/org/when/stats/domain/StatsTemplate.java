package org.when.stats.domain;

import org.springframework.stereotype.Service;
import org.when.data.staff.entity.StaffEntity;
import org.when.stats.repository.StaffRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public abstract class StatsTemplate<T, U, R> {

    private final StaffRepository staffRepository;

    public StatsTemplate(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public R statsStaffData(Integer staffType, LocalDate day) {
        List<StaffEntity> staffs = findStaffsByType(staffType);
        List<T> data = loadDataByTypeAndDate(staffs, day);
        List<U> stats = statsData(day, data, staffs);
        return summarizeStats(stats);
    }

    public R statsStaffData(String phone, LocalDate day) {
        Optional<StaffEntity> optional = findStaffByPhone(phone);
        if (!optional.isPresent()) {
            throw new RuntimeException("未知的手机号码：" + phone);
        }
        List<StaffEntity> staffs = Stream.of(optional.get()).collect(toList());

        List<T> data = loadDataByPhoneAndDate(phone, day);
        List<U> stats = statsData(day, data, staffs);
        return summarizeStats(stats);
    }

    protected List<StaffEntity> findStaffsByType(Integer staffType) {
        return staffRepository.findByType(staffType);
    }

    protected Optional<StaffEntity> findStaffByPhone(String phone) {
        StaffEntity staff = staffRepository.findByPhone(phone);
        return Optional.ofNullable(staff);
    }

    protected abstract List<T> loadDataByTypeAndDate(List<StaffEntity> staffs, LocalDate day);

    protected abstract List<T> loadDataByPhoneAndDate(String phone, LocalDate day);

    protected abstract List<U> statsData(LocalDate day, List<T> data, List<StaffEntity> staffs);

    protected R summarizeStats(List<U> stats) {
        int total = stats.stream()
                .map(summarizeFunction())
                .reduce(0, Integer::sum);
        return resultFunction().apply(total, stats);
    }

    protected abstract Function<U, Integer> summarizeFunction();

    protected abstract BiFunction<Integer, List<U>, R> resultFunction();
}
