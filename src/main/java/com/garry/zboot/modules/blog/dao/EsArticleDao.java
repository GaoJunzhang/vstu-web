package com.garry.zboot.modules.blog.dao;

import com.garry.zboot.modules.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface EsArticleDao extends ElasticsearchRepository<Article, String> {
    Page<Article> findByCategory(Integer category, Pageable pageable);
}
