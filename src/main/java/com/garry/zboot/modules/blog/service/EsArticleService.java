package com.garry.zboot.modules.blog.service;

import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EsArticleService {
    Page<Article> findByCategory(Integer type, Pageable pageable);

    Article saveArticle(Article article);

    void deleteArticle(String id);

    void deleteAllArticle();

    Page<Article> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable);
}
