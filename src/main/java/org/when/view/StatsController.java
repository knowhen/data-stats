package org.when.view;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.when.stats.service.OrderStatsService;
import org.when.stats.vo.OrderStatsFormVO;
import org.when.stats.vo.SalesVO;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping(("/stats/sales"))
public class StatsController {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private final OrderStatsService service;

    public StatsController(OrderStatsService service) {
        this.service = service;
    }

    @GetMapping
    public String stats(Model model) {
        return dailyStats(null, model);
    }

    @GetMapping("/daily")
    public String dailyStats(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                             Model model) {
        if (Objects.isNull(date)) {
            date = LocalDate.now();
        }
        OrderStatsFormVO result = service.dailyStats(date);
        return addStatsResult(result.getSales(), model);
    }

    @GetMapping("/monthly")
    public String monthlyStats(@RequestParam(value = "month", required = false) String month,
                               Model model) {
        YearMonth yearMonth;
        if (Objects.isNull(month)) {
            yearMonth = YearMonth.now();
        } else {
            try {
                yearMonth = YearMonth.parse(month, formatter);
            } catch (DateTimeParseException e) {
                System.out.println(e.getMessage());
                return "stats";
            }
        }
        OrderStatsFormVO result = service.monthlyStats(yearMonth);
        return addStatsResult(result.getSales(), model);
    }

    @GetMapping("/yearly")
    public String yearlyStats(@RequestParam(value = "year", required = false) String year,
                              Model model) {
        Year y;
        if (Objects.isNull(year)) {
            y = Year.now();
        } else {
            try {
                y = Year.parse(year);
            } catch (DateTimeParseException e) {
                System.out.println(e.getMessage());
                return "stats";
            }
        }
        OrderStatsFormVO result = service.yearlyStats(y);
        return addStatsResult(result.getSales(), model);
    }

    private String addStatsResult(List<SalesVO> sales, Model model) {
        model.addAttribute("sales", sales);

        Map<String, Integer> salesMap = sales.stream()
                .collect(Collectors.toMap(e -> e.getName() + e.getPrice(), SalesVO::getQuantity));
        List<String> xAxisData = sales.stream()
                .map(e -> e.getName() + e.getPrice())
                .collect(Collectors.toList());
        model.addAttribute("xAxisData", xAxisData);

        List<Integer> seriesData = sales.stream()
                .map(SalesVO::getQuantity)
                .collect(Collectors.toList());
        model.addAttribute("seriesData", seriesData);
//        model.addAttribute("salesMap", salesMap);
        return "stats";
    }

    @GetMapping("/bundle")
    public String bundleSalesStats(Model model) {
        List<SalesVO> bundleSales = statsBundleSales();
        model.addAttribute("stats", bundleSales);
        return "bundle";
    }

    private List<SalesVO> statsBundleSales() {
        return service.bundleSalesStats();
    }

    @GetMapping("/product")
    public String productSalesStats(Model model) {
        List<SalesVO> bundleSales = statsProductSales();
        model.addAttribute("stats", bundleSales);
        return "product";
    }

    private List<SalesVO> statsProductSales() {
        return service.productSalesStats();
    }
}
