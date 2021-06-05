package ru.newsagregator.App.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import ru.newsagregator.App.mapper.ContentMapper;
import ru.newsagregator.App.model.ContentList;
import ru.newsagregator.App.model.MeduzaNews;
import ru.newsagregator.App.model.NewsContent;

import java.util.List;

@RestController
public class RestNewsApiController {

    private final String url = "https://meduza.io/api/v3/search?chrono=news&locale=ru&page=0&per_page=24";

    private final ContentMapper contentMapper;

    public RestNewsApiController(ContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    @GetMapping(value = "/news", produces = MediaType.APPLICATION_JSON_VALUE)
    public ContentList news(@RequestParam(value = "page", required = false, defaultValue = "0") String page) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("https://meduza.io/api/v3/search?chrono=news&locale=ru&page=")
                .append(page)
                .append("&per_page=24");
        String url = stringBuilder.toString();
        WebClient webClient = WebClient.create();
        String responseJson = webClient.get()
                .uri(url)
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();


        MeduzaNews meduzaNews = stringToJsonMeduza(responseJson);
        List<NewsContent> content = contentMapper.toDTO(meduzaNews);
        return new ContentList(content);
    }

    @SneakyThrows
    private MeduzaNews stringToJsonMeduza(String text) {

        ObjectMapper objectMapper = new ObjectMapper();
        //new JodaModule()
        objectMapper.registerModule(new JSR310Module());
        return  objectMapper.readValue(text, MeduzaNews.class);
    }
}
