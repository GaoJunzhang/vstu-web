package com.garry.zboot.modules.base.dao;

import com.garry.zboot.base.ZbootBaseDao;
import com.garry.zboot.modules.base.model.QuartzJob;

import java.util.List;

public interface QuartzJobDao extends ZbootBaseDao<QuartzJob,String> {
    /**
     * 通过类名获取
     * @param jobClassName
     * @return
     */
    List<QuartzJob> findByJobClassName(String jobClassName);
}
