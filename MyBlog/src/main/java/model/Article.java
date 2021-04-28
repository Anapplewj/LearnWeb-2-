package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Article {
    private int articleId;
    private String title;
    private String content;
    private int userId;
}
