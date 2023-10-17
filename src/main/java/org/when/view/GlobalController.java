package org.when.view;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.when.view.domain.Navigation;
import org.when.view.domain.Site;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalController {
    @ModelAttribute("navs")
    public List<Navigation> globalNavigationItems() {
        return findNavs();
    }

    private List<Navigation> findNavs() {
        List<Navigation> navs = new ArrayList<>();
        Navigation salesNav = new Navigation(10, "销量统计", "/stats/sales");
        navs.add(salesNav);
        return navs;
    }

    @ModelAttribute("sidebar")
    private List<Navigation> findNavsByPid(Integer pid) {
        List<Navigation> navs = new ArrayList<>();
        Navigation dailyNav = new Navigation(1001, "天", "/stats/sales/daily");
        Navigation monthlyNav = new Navigation(1002, "月", "/stats/sales/monthly");
        Navigation yearlyNav = new Navigation(1003, "年", "/stats/sales/yearly");
        navs.add(dailyNav);
        navs.add(monthlyNav);
        navs.add(yearlyNav);
        return navs;
    }

    @ModelAttribute("site")
    private Site globalConfiguration() {
        return new Site("数据统计");
    }
}
