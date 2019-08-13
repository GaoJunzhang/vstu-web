package com.garry.zboot.modules.vstu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.entity.Resource;

import java.util.List;

/**
 * 资源接口
 * @author GaoJunZhang
 */
public interface IResourceService extends IService<Resource> {

    IPage<Resource> selectPage(Resource resource, PageVo pageVo, SearchVo searchVo);
}