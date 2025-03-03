package com.lyrpc.demo.api;

import com.lyrpc.core.Lyrpc;
import com.lyrpc.demo.entity.Article;

/**
 * 文章 RPC 接口
 *
 * @author lybugproducer
 * @since 2025/2/6 11:04
 */
public interface ArticleLyrpc extends Lyrpc {
    /**
     * 通过文章 ID 获取文章内容
     *
     * @param articleId 文章 ID
     * @return {@link Article} 文章内容
     */
    Article getArticleById(int articleId);
}
