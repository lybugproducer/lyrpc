package com.taihuafufc.lybugproducer;

import lombok.Data;

import java.io.Serializable;

/**
 * 文章类
 *
 * @author lybugproducer
 * @since 2025/3/2 17:25
 */
@Data
public class Article implements Serializable {
    private int articleId;
    private String title;
    private String content;
    private User author;

    public Article(int articleId, String title, String content, User author) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
