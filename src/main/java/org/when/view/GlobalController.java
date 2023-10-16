package org.when.view;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.when.view.domain.Navigation;
import org.when.view.domain.Site;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class GlobalController {
    @ModelAttribute("navs")
    public List<Navigation> globalNavigationItems() {
        return findNavs();
    }

    private List<Navigation> findNavs() {
        return IntStream.rangeClosed(1, 5)
                .boxed()
                .map(i -> new Navigation("标题" + i, "固定链接" + i))
                .collect(toList());
    }

    @ModelAttribute("site")
    private Site globalConfiguration() {
        return new Site("数据统计");
    }
}
