package com.garry.zboot.modules.base.dao;

import com.garry.zboot.base.ZbootBaseDao;
import com.garry.zboot.modules.base.model.UserRole;

import java.util.List;

/**
* class_name: UserRoleDao
* package: com.garry.zboot.modules.base.dao
* describe: 用户角色数据处理层
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/8
* creat_time: 17:16
**/
public interface UserRoleDao extends ZbootBaseDao<UserRole,String> {
    /**
     * 通过roleId查找
     * @param roleId
     * @return
     */
    List<UserRole> findByRoleId(String roleId);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteByUserId(String userId);
}
