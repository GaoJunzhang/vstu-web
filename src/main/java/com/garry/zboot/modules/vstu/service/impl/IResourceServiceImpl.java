package com.garry.zboot.modules.vstu.service.impl;

import com.garry.zboot.modules.vstu.dao.mapper.ResourceMapper;
import com.garry.zboot.modules.vstu.entity.Resource;
import com.garry.zboot.modules.vstu.service.IResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
}