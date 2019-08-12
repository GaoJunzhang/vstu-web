package com.garry.zboot.modules.blog.controller;

import com.garry.zboot.common.utils.DateUtil;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.modules.blog.dao.ArticleDao;
import com.garry.zboot.modules.blog.model.Article;
import com.sun.jmx.snmp.Timestamp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/zboot/blog/article")
public class ArticleController {
    @Autowired
    private ArticleDao articleDao;

    @PutMapping(value = "/add")
    @ApiOperation("添加文章")
    public Result<Object> add(@ModelAttribute Article article, HttpServletRequest request){
        article.setCreateTime(DateUtil.getNowDate());
        article.setLike(0);
        article.setUid("682265633886208");
        article.setPage(0);
        articleDao.saveArticle(article);
        return new ResultUtil<Object>().setSuccessMsg("success");
    }

    @GetMapping(value = "/articleById")
    @ApiOperation("id查询文章")
    public Result<Object> articleByid(@PathVariable String id){
        return new ResultUtil<Object>().setData(articleDao.findArticleById(id));
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation("id删除文章")
    public Result<Object> delete(@PathVariable String id){
        articleDao.deleteArticleById(id);
        return new ResultUtil<Object>().setSuccessMsg("success");
    }

    @PostMapping(value = "/update")
    @ApiOperation("更新文章")
    public Result<Object> update(@RequestBody Article article){
        articleDao.updateArticle(article);
        return new ResultUtil<Object>().setSuccessMsg("success");
    }

    @GetMapping(value = "/articleByUid")
    @ApiOperation("uid查询文章")
    public Result<Object> articleByUid(@PathVariable String uid){
        return new ResultUtil<Object>().setData(articleDao.findArticleByUid(uid));
    }

    @GetMapping(value = "/articlesByTitle")
    @ApiOperation("标题模糊查询")
    public Result<Object> articlesByTitle(String title){
        return new ResultUtil<Object>().setData(articleDao.articlesByTitle(title));
    }
}
