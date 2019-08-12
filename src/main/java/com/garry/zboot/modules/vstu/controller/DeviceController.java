package com.garry.zboot.modules.vstu.controller;

import com.garry.zboot.common.utils.PageUtil;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.bean.DeviceBean;
import com.garry.zboot.modules.vstu.entity.Device;
import com.garry.zboot.modules.vstu.service.IDeviceService;
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
@Api(description = "设备管理接口")
@RequestMapping("/zboot/device")
@Transactional
public class DeviceController {

    @Autowired
    private IDeviceService iDeviceService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Device> get(@PathVariable String id){

        Device device = iDeviceService.getById(id);
        return new ResultUtil<Device>().setData(device);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Device>> getAll(){

        List<Device> list = iDeviceService.list();
        return new ResultUtil<List<Device>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Device>> getByPage(@ModelAttribute PageVo pageVo,
                                           @ModelAttribute Device device,
                                           @ModelAttribute SearchVo searchVo){

        IPage<Device> data = iDeviceService.selectPage(device,pageVo,searchVo);
        return new ResultUtil<IPage<Device>>().setData(data);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Device> saveOrUpdate(@ModelAttribute DeviceBean deviceBean){

        Device device = new Device();
        if(iDeviceService.saveOrUpdate(device)){
            return new ResultUtil<Device>().setData(device);
        }
        return new ResultUtil<Device>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids){

        for(String id : ids){
            iDeviceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }
}
