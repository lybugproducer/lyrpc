package com.taihuafufc.lybugproducer;

/**
 * TODO
 *
 * @author lybugproducer
 * @since 2025/2/6 11:04
 */
public interface ArticleLyrpc {
    /**
     * 获取文章内容
     * @param articleId 文章 ID
     * @return String 文章内容
     */
    String getArticle(String articleId);
}
