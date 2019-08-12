package com.garry.zboot.modules.blog.dao;

import com.garry.zboot.modules.blog.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class ArticleDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveArticle(Article article){
        mongoTemplate.save(article);
    }

    public Article findArticleByUid(String uid){
        Query query = new Query(Criteria.where("uid").is(uid));
        return mongoTemplate.findOne(query,Article.class);
    }

    public Article findArticleById(String id){
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query,Article.class);
    }

    public void updateArticle(Article article){
        Query query = new Query(Criteria.where("id").is(article.getId()));
        Update update = new Update().set("title",article.getTitle()).set("content",article.getContent()).set("updateTime",article.getUpdateTime());
        mongoTemplate.updateFirst(query,update,Article.class);
    }
    public void deleteArticleById(String id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,Article.class);
    }
    public List<Article> articlesByTitle(String title){
        Pattern pattern = Pattern.compile(".*?"+title+".*?");
        Query query = new Query(Criteria.where("title").regex(pattern));
        return mongoTemplate.find(query,Article.class);
    }
}
