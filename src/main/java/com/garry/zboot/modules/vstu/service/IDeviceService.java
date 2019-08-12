package com.garry.zboot.modules.vstu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.entity.Device;
import com.garry.zboot.modules.vstu.entity.DeviceType;

import java.util.List;

/**
 * 设备接口
 * @author GaoJunZhang
 */
public interface IDeviceService extends IService<Device> {
    IPage<Device> selectPage(Device device, PageVo pageVo, SearchVo searchVo);
}