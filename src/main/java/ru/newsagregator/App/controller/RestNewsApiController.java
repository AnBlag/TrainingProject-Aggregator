package ru.newsagregator.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
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

    // добавить value
    private String urlPartOne;
    private String urlPartTwo;
    private final ContentMapper contentMapper;

    public RestNewsApiController(ContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    @GetMapping(value = "/news", produces = MediaType.APPLICATION_JSON_VALUE)
    public ContentList news(@RequestParam(value = "page", required = false, defaultValue = "0") String page) {
        String url = createUrl(urlPartOne, urlPartTwo, page);
        String responseJson = getContentJsonFromMeduzaApi(url);
        MeduzaNews meduzaNews = stringToJsonMeduza(responseJson);
        List<NewsContent> content = contentMapper.toDTO(meduzaNews);
        return new ContentList(content);
    }

    private String createUrl(String urlPartOne, String urlPartTwo, String page) {
     return new StringBuilder()
                .append("https://meduza.io/api/v3/search?chrono=news&locale=ru&page=")
                .append(page)
                .append("&per_page=24").toString();
    }

    private String getContentJsonFromMeduzaApi(String url) {
       return WebClient.create().get()
                .uri(url)
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
    }


    @SneakyThrows
    private MeduzaNews stringToJsonMeduza(String text) {

        ObjectMapper objectMapper = new ObjectMapper();
        //new JodaModule()
        objectMapper.registerModule(new JSR310Module());
        return  objectMapper.readValue(text, MeduzaNews.class);
    }
}
