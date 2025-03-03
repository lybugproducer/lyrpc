package com.taihuafufc.lybugproducer;

/**
 * 文章 RPC 接口
 *
 * @author lybugproducer
 * @since 2025/2/6 11:04
 */
public interface ArticleLyrpc {
    /**
     * 通过文章 ID 获取文章内容
     *
     * @param articleId 文章 ID
     * @return {@link Article} 文章内容
     */
    Article getArticleById(int articleId);
}
