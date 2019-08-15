package com.garry.zboot.modules.vstu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.garry.zboot.modules.vstu.bean.UserResourceBean;
import com.garry.zboot.modules.vstu.entity.UserResource;

import java.util.List;

/**
 * 设备资源接口
 * @author GaoJunZhang
 */
public interface IUserResourceService extends IService<UserResource> {
    int batchSave(List<UserResource> list);

    List<UserResourceBean> userResourceByUid(String uid);
}