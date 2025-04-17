package com.lyrpc.demo.provider.impl;

import com.lyrpc.demo.entity.Article;
import com.lyrpc.demo.api.ArticleLyrpc;
import com.lyrpc.demo.entity.User;

/**
 * 文章 RPC 接口实现类
 *
 * @author lybugproducer
 * @since 2025/3/2 17:26
 */
public class ArticleLyrpcImpl implements ArticleLyrpc {

    @Override
    public Article getArticleById(int articleId) {
        String title = "This is a test article";
        String content = "This is the content of the test article";
        User author = new User(123456, "lybugproducer");
        return new Article(articleId, title, content, author);
    }
}
