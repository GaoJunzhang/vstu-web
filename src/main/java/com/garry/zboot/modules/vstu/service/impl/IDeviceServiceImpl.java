package com.garry.zboot.modules.vstu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.dao.mapper.DeviceMapper;
import com.garry.zboot.modules.vstu.entity.Device;
import com.garry.zboot.modules.vstu.entity.DeviceType;
import com.garry.zboot.modules.vstu.service.IDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IDeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    public IPage<Device> selectPage(Device device, PageVo pageVo, SearchVo searchVo){
        Page<Device> page = new Page<>(pageVo.getPageNumber(), pageVo.getPageSize());
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(device.getDeviceName())) {
            queryWrapper.like("device_name","%"+device.getDeviceName()+"%");
        }
        if (StrUtil.isNotBlank(device.getDeviceMac())) {
            queryWrapper.like("device_mac","%"+device.getDeviceMac()+"%");
        }
        if (device.getEnable()!=null){
            queryWrapper.eq("is_delete",device.getEnable());
        }
        if (device.getDeviceTypeId() !=null){
            queryWrapper.eq("device_type_id",device.getDeviceTypeId());
        }
        if (StrUtil.isNotBlank(searchVo.getStartDate())){
            queryWrapper.gt("create_time",searchVo.getStartDate());
        }
        if (StrUtil.isNotBlank(searchVo.getEndDate())){
            queryWrapper.lt("create_time",searchVo.getEndDate());
        }
        return deviceMapper.selectPage(page, queryWrapper);
    }
}