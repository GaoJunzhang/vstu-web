package com.garry.zboot.modules.vstu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.dao.mapper.ResourceMapper;
import com.garry.zboot.modules.vstu.entity.Resource;
import com.garry.zboot.modules.vstu.service.IResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 资源接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    public IPage<Resource> selectPage(Resource resource, PageVo pageVo, SearchVo searchVo){
        Page<Resource> page = new Page<>(pageVo.getPageNumber(), pageVo.getPageSize());
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(resource.getName())) {
            queryWrapper.like("name","%"+resource.getName()+"%");
        }
        if (StrUtil.isNotBlank(resource.getTitle())) {
            queryWrapper.like("title","%"+resource.getTitle()+"%");
        }
        if (resource.getIsDelete()!=null){
            queryWrapper.eq("is_delete",resource.getIsDelete());
        }
        if (resource.getContent() !=null){
            queryWrapper.like("content","%"+resource.getContent()+"%");
        }
        if (StrUtil.isNotBlank(searchVo.getStartDate())){
            queryWrapper.gt("create_time",searchVo.getStartDate());
        }
        if (StrUtil.isNotBlank(searchVo.getEndDate())){
            queryWrapper.lt("create_time",searchVo.getEndDate());
        }
        return resourceMapper.selectPage(page, queryWrapper);
    }
}