package ru.newsagregator.App.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.newsagregator.App.model.ContentList;
import ru.newsagregator.App.model.NewsContent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/meduza")
public class NewsController {
    //сделать value
    private final String url = "http://localhost:8080/news";
    private final AtomicInteger atomicInteger;

    public NewsController(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }

    @GetMapping("/news")
    public String getNews(@RequestParam(value = "page", defaultValue = "0", required = false)
                                      String page, ModelMap modelMap) {
        String url = createUrl(this.url,page);
        ContentList responseJson = getContentJsonFromRestNewsApiController(url);
        assert responseJson != null;
        List<NewsContent> newsContentList = responseJson.getNewsContent();
        modelMap.addAttribute("newsContentList", newsContentList);

        return "news";
    }

    private String createUrl(String url, String pageNumber) {
        return new StringBuilder()
                .append(url)
                .append("?page=")
                .append(pageNumber).toString();
    }
    private ContentList getContentJsonFromRestNewsApiController(String url) {
        WebClient webClient = WebClient.create();
       return webClient.get()
                .uri(url)
                .exchange()
                .block()
                .bodyToMono(ContentList.class)
                .block();
    }

    @PostMapping("/button")
    public String page(@ModelAttribute(name = "next") String nextButton,
                       @ModelAttribute(name = "previous") String previousButton) {
        if (nextButton.equals("next")) {
            atomicInteger.incrementAndGet();
            return "redirect:/meduza/news?page=" + atomicInteger.get();
        } else {
            if (atomicInteger.get()==0){
                return "redirect:/meduza/news?page=0";
            } else
                atomicInteger.decrementAndGet();
            return "redirect:/meduza/news?page=" + atomicInteger.get();
        }
    }
}
