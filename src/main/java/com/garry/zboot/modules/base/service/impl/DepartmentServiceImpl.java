package com.garry.zboot.modules.base.service.impl;

import com.garry.zboot.common.utils.SecurityUtil;
import com.garry.zboot.modules.base.dao.DepartmentDao;
import com.garry.zboot.modules.base.model.Department;
import com.garry.zboot.modules.base.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门接口实现
 * @Author gaojunzhang
 * @Date 2019/7/8 23:24
 */
@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public DepartmentDao getRepository() {
        return departmentDao;
    }

    @Override
    public List<Department> findByParentIdOrderBySortOrder(String parentId, Boolean openDataFilter) {

        // 数据权限
        List<String> depIds = securityUtil.getDeparmentIds();
        if(depIds!=null&&depIds.size()>0&&openDataFilter){
            return departmentDao.findByParentIdAndIdInOrderBySortOrder(parentId, depIds);
        }
        return departmentDao.findByParentIdOrderBySortOrder(parentId);
    }

    @Override
    public List<Department> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status) {

        return departmentDao.findByParentIdAndStatusOrderBySortOrder(parentId, status);
    }

    @Override
    public List<Department> findByTitleLikeOrderBySortOrder(String title, Boolean openDataFilter) {

        // 数据权限
        List<String> depIds = securityUtil.getDeparmentIds();
        if(depIds!=null&&depIds.size()>0&&openDataFilter){
            return departmentDao.findByTitleLikeAndIdInOrderBySortOrder(title, depIds);
        }
        return departmentDao.findByTitleLikeOrderBySortOrder(title);
    }
}