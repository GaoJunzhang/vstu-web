package com.garry.zboot.modules.base.service.impl;

import com.garry.zboot.modules.base.dao.RolePermissionDao;
import com.garry.zboot.modules.base.model.RolePermission;
import com.garry.zboot.modules.base.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 角色权限接口实现
 * @Author gaojunzhang
 * @Date 2019/7/8 23:25
 */
@Slf4j
@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public RolePermissionDao getRepository() {
        return rolePermissionDao;
    }

    @Override
    public List<RolePermission> findByPermissionId(String permissionId) {

        return rolePermissionDao.findByPermissionId(permissionId);
    }

    @Override
    public List<RolePermission> findByRoleId(String roleId) {

        return rolePermissionDao.findByRoleId(roleId);
    }

    @Override
    public void deleteByRoleId(String roleId) {

        rolePermissionDao.deleteByRoleId(roleId);
    }
}