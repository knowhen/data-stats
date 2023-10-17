package org.when.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.when.stats.service.OrderStatsService;
import org.when.stats.vo.OrderStatsFormVO;
import org.when.stats.vo.SalesVO;
import org.when.view.domain.Navigation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    @Autowired
    private OrderStatsService service;

    @GetMapping("/")
    public String index(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                        Model model) {
        if (Objects.isNull(date)) {
            date = LocalDate.now();
        }
        OrderStatsFormVO result = service.dailyStats(date);
        List<SalesVO> sales = result.getSales();
        model.addAttribute("sales", result.getSales());

        List<String> xAxisData = sales.stream()
                .map(e -> e.getName() + e.getPrice())
                .collect(Collectors.toList());
        model.addAttribute("xAxisData", xAxisData);

        List<Integer> seriesData = sales.stream()
                .map(SalesVO::getQuantity)
                .collect(Collectors.toList());
        model.addAttribute("seriesData", seriesData);
        return "index";
    }


}
