package com.garry.zboot.modules.blog.controller;

import com.garry.zboot.common.utils.ChinaDate;
import com.garry.zboot.common.utils.DateUtil;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.utils.SecurityUtil;
import com.garry.zboot.common.vo.PageUtil;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.base.model.User;
import com.garry.zboot.modules.blog.model.Article;
import com.garry.zboot.modules.blog.service.EsArticleService;
import com.sun.jmx.snmp.Timestamp;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/zboot/blog/es/article")
public class EsArticleController {
    @Autowired
    private EsArticleService esArticleService;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取全部")
    public Result<Object> getAllByPage(@RequestParam(required = false) Integer type,
                                       @RequestParam(required = false) String key,
                                       @ModelAttribute SearchVo searchVo,
                                       @ModelAttribute PageVo pageVo) {

        Page<Article> es = esArticleService.findByConfition(type, key, searchVo, PageUtil.initPage(pageVo));
        return new ResultUtil<Object>().setData(es);
    }

    @PutMapping(value = "/add")
    @ApiOperation("添加文章")
    public Result<Object> add(@ModelAttribute Article article) {
        User user = securityUtil.getCurrUser();
        article.setLunarCalendar(ChinaDate.today());
        article.setLike(0);
        article.setUid(user.getId());
        if (StringUtils.isEmpty(article.getAuthor())) {

            article.setAuthor(user.getUsername());
        }
        article.setPage(0);
        esArticleService.saveArticle(article);
        return new ResultUtil<Object>().setSuccessMsg("success");
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation("id删除文章")
    public Result<Object> delete(@PathVariable String id) {
        try {
            esArticleService.deleteArticle(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultUtil<Object>().setErrorMsg("error");
        }
        return new ResultUtil<Object>().setSuccessMsg("success");
    }

    @PostMapping(value = "/update")
    @ApiOperation("更新文章")
    public Result<Object> update(@RequestBody Article article) {
        esArticleService.saveArticle(article);
        return new ResultUtil<Object>().setSuccessMsg("success");
    }

    @DeleteMapping(value = "/flushall")
    public void flushall(){
        esArticleService.deleteAllArticle();
    }

//    @GetMapping(value = "/articleByUid")
//    @ApiOperation("uid查询文章")
//    public Result<Object> articleByUid(@PathVariable String uid){
//        return new ResultUtil<Object>().setData(articleDao.findArticleByUid(uid));
//    }

//    @GetMapping(value = "/articlesByTitle")
//    @ApiOperation("标题模糊查询")
//    public Result<Object> articlesByTitle(String title){
//        return new ResultUtil<Object>().setData(articleDao.articlesByTitle(title));
//    }
}
