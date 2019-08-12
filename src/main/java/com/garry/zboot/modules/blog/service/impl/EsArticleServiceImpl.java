package com.garry.zboot.modules.blog.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.blog.dao.EsArticleDao;
import com.garry.zboot.modules.blog.model.Article;
import com.garry.zboot.modules.blog.service.EsArticleService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class EsArticleServiceImpl implements EsArticleService {
    @Autowired
    private EsArticleDao esArticleDao;

    public Page<Article> findByCategory(Integer type, Pageable pageable){
        return esArticleDao.findByCategory(type,pageable);
    }

    public Article saveArticle(Article article){
        return esArticleDao.save(article);
    }

    public void deleteArticle(String id){
        esArticleDao.deleteById(id);
    }

    public void deleteAllArticle(){
        System.out.println("全删除有危险");
        esArticleDao.deleteAll();
    }

    @Override
    public Page<Article> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable) {

        if(type==null&& StrUtil.isBlank(key)&&StrUtil.isBlank(searchVo.getStartDate())){
            // 无过滤条件获取全部
            return esArticleDao.findAll(pageable);
        }else if(type!=null&&StrUtil.isBlank(key)&&StrUtil.isBlank(searchVo.getStartDate())){
            // 仅有type
            return esArticleDao.findByCategory(type, pageable);
        }

        QueryBuilder qb;

        QueryBuilder qb0 = QueryBuilders.termQuery("category", type);
        QueryBuilder qb1 = QueryBuilders.multiMatchQuery(key, "title", "content", "category","author");
        // 在有type条件下
        if(StrUtil.isNotBlank(key)&&StrUtil.isBlank(searchVo.getStartDate())&&StrUtil.isBlank(searchVo.getEndDate())){
            // 仅有key
            qb = QueryBuilders.boolQuery().must(qb0).must(qb1);
        }else if(StrUtil.isBlank(key)&&StrUtil.isNotBlank(searchVo.getStartDate())&&StrUtil.isNotBlank(searchVo.getEndDate())){
            // 仅有时间范围
            Long start = DateUtil.parse(searchVo.getStartDate()).getTime();
            Long end = DateUtil.endOfDay(DateUtil.parse(searchVo.getEndDate())).getTime();
            QueryBuilder qb2 = QueryBuilders.rangeQuery("timeMillis").gte(start).lte(end);
            qb = QueryBuilders.boolQuery().must(qb0).must(qb2);
        }else{
            // 两者都有
            Long start = DateUtil.parse(searchVo.getStartDate()).getTime();
            Long end = DateUtil.endOfDay(DateUtil.parse(searchVo.getEndDate())).getTime();
            QueryBuilder qb2 = QueryBuilders.rangeQuery("timeMillis").gte(start).lte(end);
            qb = QueryBuilders.boolQuery().must(qb0).must(qb1).must(qb2);
        }

        //多字段搜索
        return esArticleDao.search(qb, pageable);
    }
}
