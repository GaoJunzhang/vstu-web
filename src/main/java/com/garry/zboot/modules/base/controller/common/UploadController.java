package com.garry.zboot.modules.base.controller.common;

import cn.hutool.core.util.StrUtil;
import com.garry.zboot.common.utils.*;
import com.garry.zboot.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@Slf4j
@RestController
@Api(description = "文件上传接口")
@RequestMapping("/zboot/upload")
@Transactional
public class UploadController {
    @Autowired
    private QiniuUtil qiniuUtil;

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传")
    public Result<Object> upload(@RequestParam(required = false) MultipartFile file,
                                 @RequestParam(required = false) String base64,
                                 @RequestParam(required = false) String dir,
                                 HttpServletRequest request) {

        if (StrUtil.isNotBlank(base64)) {
            // base64上传
            file = Base64DecodeMultipartFile.base64Convert(base64);
        }
        String fileName = qiniuUtil.renamePic(file.getOriginalFilename());
        String result = null;
        try {
            InputStream inputStream = file.getInputStream();
            //上传七牛云服务器
//            result = qiniuUtil.qiniuInputStreamUpload(inputStream, fileName);
//            上传到阿里oss对象存储
//            result = OSSManageUtil.uploadFile(file, "meet/img/",fileName);
            result = OssUtil.uploadFile(file,"img");
            System.out.println(result);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResultUtil<Object>().setErrorMsg("请检查阿里oss对象存储");
        }

        return new ResultUtil<Object>().setData(result);
    }
}
