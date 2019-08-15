package com.garry.zboot.modules.vstu.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.garry.zboot.modules.vstu.bean.UserResourceBean;
import com.garry.zboot.modules.vstu.entity.UserResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备资源数据处理层
 * @author GaoJunZhang
 */
public interface UserResourceMapper extends BaseMapper<UserResource> {

    int batchSave(@Param("list")List<UserResource> list);

    List<UserResourceBean> userResourceByUid(@Param("uid") String uid);
}