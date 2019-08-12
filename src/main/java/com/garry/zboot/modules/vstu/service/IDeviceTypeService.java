package com.garry.zboot.modules.vstu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.entity.DeviceType;

/**
 * 设备类型接口
 * @author GaoJunZhang
 */
public interface IDeviceTypeService extends IService<DeviceType> {
    IPage<DeviceType> selectPage(DeviceType deviceType, PageVo pageVo, SearchVo searchVo);
}