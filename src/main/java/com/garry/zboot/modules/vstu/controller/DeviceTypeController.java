package com.garry.zboot.modules.vstu.controller;

import com.garry.zboot.common.utils.PageUtil;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.entity.DeviceType;
import com.garry.zboot.modules.vstu.service.IDeviceTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "设备类型管理接口")
@RequestMapping("/zboot/deviceType")
@Transactional
public class DeviceTypeController {

    @Autowired
    private IDeviceTypeService iDeviceTypeService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DeviceType> get(@PathVariable String id) {

        DeviceType deviceType = iDeviceTypeService.getById(id);
        return new ResultUtil<DeviceType>().setData(deviceType);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DeviceType>> getAll() {

        List<DeviceType> list = iDeviceTypeService.list();
        return new ResultUtil<List<DeviceType>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DeviceType>> getByPage(@ModelAttribute PageVo pageVo,
                                               @ModelAttribute DeviceType deviceType,
                                               @ModelAttribute SearchVo searchVo) {

        IPage<DeviceType> page = iDeviceTypeService.selectPage(deviceType,pageVo,searchVo);
//        IPage<DeviceType> data = iDeviceTypeService.page(PageUtil.initMpPage(pageVo));
        return new ResultUtil<IPage<DeviceType>>().setData(page);
    }

    @RequestMapping(value = "/addDeviceType", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DeviceType> addDeviceType(@ModelAttribute DeviceType deviceType) {
        if (deviceType.getCreateTime() == null) {

            deviceType.setCreateTime(new Timestamp(System.currentTimeMillis()));
            deviceType.setIsDelete((short) 0);
        }
        if (iDeviceTypeService.saveOrUpdate(deviceType)) {
            return new ResultUtil<DeviceType>().setData(deviceType);
        }
        return new ResultUtil<DeviceType>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iDeviceTypeService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }
}
