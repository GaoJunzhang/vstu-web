package com.garry.zboot.modules.base.controller.common;

import com.aliyun.oss.model.OSSObject;
import com.garry.zboot.common.annotation.SystemLog;
import com.garry.zboot.common.enums.LogType;
import com.garry.zboot.common.utils.OssUtil;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.OssItem;
import com.garry.zboot.common.vo.OssPage;
import com.garry.zboot.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(description = "oss对象存储接口")
@RequestMapping("/zboot/common/oss")
@Transactional
public class OssObjController {

    @RequestMapping(value = "/ossData")
    @ApiOperation(value = "获取指定目录下的oss对象")
    public Result<Object> ossData(String dir) {
        return new ResultUtil<Object>().setData(OssUtil.listAll(dir));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除")
    public Result<Object> delete(String key) {
        OssUtil.delete(key);
        return new ResultUtil<Object>().setData("删除成功");
    }

    @RequestMapping(value = "/deleteKeys", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除")
    public Result<Object> deleteKeys(String[] keyStrs) {
        if (keyStrs.length > 0) {

            List<String> keys = Arrays.asList(keyStrs);
            OssUtil.deleteKeys(keys);
            return new ResultUtil<Object>().setData("删除成功");
        }
        return new ResultUtil<Object>().setErrorMsg("失败");
    }

    @RequestMapping(value = "/pageOssData")
    @ApiOperation(value = "分页对象")
    @SystemLog(description = "分页对象",type = LogType.OPERATION)
    public Result<Object> pageOssData(@RequestParam(name = "dir",required = true) String dir, String nextMarker, @RequestParam(name = "maxKeys",defaultValue = "50")int maxKeys) {
        OssPage ossObjes = OssUtil.listPage(dir, nextMarker, maxKeys);
        List<OssItem> ossItems = OssUtil.listAll(dir);
        Map<String,Object> map = new HashMap<>();
        map.put("summaryList",ossObjes.getSummaryList());
        map.put("nextMarker",ossObjes.getNextMarker());
        map.put("total",ossItems.size());
        return new ResultUtil<Object>().setData(map);
    }

    @RequestMapping(value = "/downloadFile")
    @ApiOperation(value = "下载对象")
    public void downloadFile(@RequestParam(name = "key",required = true) String key, HttpServletResponse response) throws IOException {
        OSSObject ossObject = OssUtil.downloadFile(key);
        //通知浏览器以附件形式下载
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(key.getBytes(), "ISO-8859-1"));
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        byte[] buffer = new byte[1024];
        int lenght = 0;
        while ((lenght = in.read(buffer)) != -1) {
            out.write(buffer, 0, lenght);
        }
        if (out != null) {
            out.flush();
            out.close();
        }
        if (in != null) {
            in.close();
        }
    }
}
