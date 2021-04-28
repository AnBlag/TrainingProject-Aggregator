package ru.newsagregator.App.model;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ContentList {
    private List<NewsContent> newsContent;

    public ContentList() {
        newsContent = new ArrayList<>();
    }
}
