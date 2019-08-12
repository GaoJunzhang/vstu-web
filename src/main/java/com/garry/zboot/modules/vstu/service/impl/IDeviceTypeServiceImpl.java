package com.garry.zboot.modules.vstu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.dao.mapper.DeviceTypeMapper;
import com.garry.zboot.modules.vstu.entity.DeviceType;
import com.garry.zboot.modules.vstu.service.IDeviceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备类型接口实现
 *
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IDeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    public IPage<DeviceType> selectPage(DeviceType deviceType, PageVo pageVo, SearchVo searchVo) {
        Page<DeviceType> page = new Page<>(pageVo.getPageNumber(), pageVo.getPageSize());
        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(deviceType.getName())) {
            queryWrapper.like("name","%"+deviceType.getName()+"%");
        }
        if (deviceType.getIsDelete()!=null){
            queryWrapper.eq("is_delete",deviceType.getIsDelete());
        }
        if (StrUtil.isNotBlank(searchVo.getStartDate())){
            queryWrapper.gt("create_time",searchVo.getStartDate());
        }
        if (StrUtil.isNotBlank(searchVo.getEndDate())){
            queryWrapper.lt("create_time",searchVo.getEndDate());
        }
        return deviceTypeMapper.selectPage(page, queryWrapper);
    }
}