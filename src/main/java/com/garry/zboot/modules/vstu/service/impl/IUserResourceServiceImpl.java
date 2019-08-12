package com.garry.zboot.modules.vstu.service.impl;

import com.garry.zboot.modules.vstu.dao.mapper.UserResourceMapper;
import com.garry.zboot.modules.vstu.entity.UserResource;
import com.garry.zboot.modules.vstu.service.IUserResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备资源接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IUserResourceServiceImpl extends ServiceImpl<UserResourceMapper, UserResource> implements IUserResourceService {

    @Autowired
    private UserResourceMapper userResourceMapper;
}