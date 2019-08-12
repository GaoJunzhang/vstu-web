package com.garry.zboot.modules.vstu.controller;

import com.garry.zboot.common.utils.PageUtil;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.modules.vstu.entity.DeviceResource;
import com.garry.zboot.modules.vstu.service.IDeviceResourceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GaoJunZhang
 */
@Slf4j
@RestController
@Api(description = "设备资源管理接口")
@RequestMapping("/zboot/deviceResource")
@Transactional
public class DeviceResourceController {

    @Autowired
    private IDeviceResourceService iDeviceResourceService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<DeviceResource> get(@PathVariable String id){

        DeviceResource deviceResource = iDeviceResourceService.getById(id);
        return new ResultUtil<DeviceResource>().setData(deviceResource);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<DeviceResource>> getAll(){

        List<DeviceResource> list = iDeviceResourceService.list();
        return new ResultUtil<List<DeviceResource>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<DeviceResource>> getByPage(@ModelAttribute PageVo page){

        IPage<DeviceResource> data = iDeviceResourceService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<DeviceResource>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<DeviceResource> saveOrUpdate(@ModelAttribute DeviceResource deviceResource){

        if(iDeviceResourceService.saveOrUpdate(deviceResource)){
            return new ResultUtil<DeviceResource>().setData(deviceResource);
        }
        return new ResultUtil<DeviceResource>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids){

        for(String id : ids){
            iDeviceResourceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }
}
