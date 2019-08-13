package com.garry.zboot.modules.vstu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.common.vo.SearchVo;
import com.garry.zboot.modules.vstu.bean.DeviceBean;
import com.garry.zboot.modules.vstu.entity.Device;
import com.garry.zboot.modules.vstu.entity.DeviceType;
import com.garry.zboot.modules.vstu.service.IDeviceService;
import com.garry.zboot.modules.vstu.service.IDeviceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private IDeviceTypeService iDeviceTypeService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Device> get(@PathVariable String id) {

        Device device = iDeviceService.getById(id);
        return new ResultUtil<Device>().setData(device);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Device>> getAll() {

        List<Device> list = iDeviceService.list();
        return new ResultUtil<List<Device>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<Map> getByPage(@ModelAttribute PageVo pageVo,
                                 @ModelAttribute Device device,
                                 @ModelAttribute SearchVo searchVo,String typeId) {

        if (!StringUtils.isEmpty(typeId)){
            device.setDeviceTypeId(typeId);
        }
        IPage<Device> data = iDeviceService.selectPage(device, pageVo, searchVo);
        Map<String, Object> map = new HashMap<>();
        List<DeviceBean> deviceBeans = new ArrayList<>(data.getRecords().size());
        List<DeviceType> deviceTypes = iDeviceTypeService.list();
        Map<String, String> map1 = new HashMap<>();
        for (DeviceType deviceType : deviceTypes) {
            map1.put(deviceType.getId(), deviceType.getName());
        }
        for (Device device1 : data.getRecords()) {
            DeviceBean deviceBean = new DeviceBean();
            deviceBean.inject(device1);
            deviceBean.setDeviceTypeName(map1.get(device1.getDeviceTypeId()));
            deviceBeans.add(deviceBean);
        }
        map.put("records", deviceBeans);
        map.put("total", data.getTotal());
        return new ResultUtil<Map>().setData(map);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Device> saveOrUpdate(@ModelAttribute Device device) {

        if (device.getCreateTime() == null) {
            device.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (iDeviceService.saveOrUpdate(device)) {
            return new ResultUtil<Device>().setData(device);
        }
        return new ResultUtil<Device>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            iDeviceService.removeById(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }
}
