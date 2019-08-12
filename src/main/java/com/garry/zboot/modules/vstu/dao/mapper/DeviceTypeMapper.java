package com.garry.zboot.modules.vstu.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.entity.DeviceType;

import java.util.List;

/**
 * 设备类型数据处理层
 *
 * @author GaoJunZhang
 */
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {
    IPage<DeviceType> pageData(DeviceType deviceType, PageVo pageVo, SearchVo searchVo);
}