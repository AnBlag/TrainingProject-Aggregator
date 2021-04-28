package ru.newsagregator.App.mapper;


import org.springframework.stereotype.Component;
import ru.newsagregator.App.model.MeduzaNews;
import ru.newsagregator.App.model.NewsContent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ContentMapper implements ModelMapper<MeduzaNews, List<NewsContent>> {


    @Override
    public List<NewsContent> toDTO(MeduzaNews meduzaNews) {
        List<NewsContent> list = new ArrayList<>();
        for (Map.Entry<String, Map<Object, Object>> entry : meduzaNews.getDocuments().entrySet()) {
            Map<Object, Object> map = entry.getValue();
            NewsContent content = new NewsContent();
            for (Map.Entry<Object, Object> obj: map.entrySet()) {
                switch ((String) obj.getKey()) {
                    case "url":
                        content.setUrl((String) obj.getValue());
                        break;
                    case "title":
                        content.setTitle((String) obj.getValue());
                        break;
                    case "share_image_url":
                        content.setShareImageUrl((String) obj.getValue());
                        break;
                    case "pub_date":
                        content.setPubDate(LocalDate.parse((String) obj.getValue()));
                        break;
                }

            }
            list.add(content);
        }
        return list;
    }

    @Override
    public MeduzaNews toModel(List<NewsContent> newsContents) {
        return null;
    }
}
