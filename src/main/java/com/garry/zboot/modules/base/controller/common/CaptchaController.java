package com.garry.zboot.modules.base.controller.common;

import com.aliyuncs.exceptions.ClientException;
import com.garry.zboot.common.utils.CreateVerifyCode;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.utils.SmsServiceUtil;
import com.garry.zboot.common.vo.Captcha;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.modules.vstu.util.FcySmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
* class_name: CaptchaController
* package: com.garry.zboot.modules.base.controller.common
* describe: 验证码接口
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 10:00
**/
@Api(description = "验证码接口")
@RequestMapping("/zboot/common/captcha")
@RestController
@Transactional
public class CaptchaController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/init",method = RequestMethod.GET)
    @ApiOperation(value = "初始化验证码")
    public Result<Object> initCaptcha() {

        String captchaId = UUID.randomUUID().toString().replace("-","");
        String code = new CreateVerifyCode().randomStr(4);
        Captcha captcha = new Captcha();
        captcha.setCaptchaId(captchaId);
        //缓存验证码
        redisTemplate.opsForValue().set(captchaId,code,3L, TimeUnit.MINUTES);
        return new ResultUtil<Object>().setData(captcha);
    }

    @RequestMapping(value = "/draw/{captchaId}", method = RequestMethod.GET)
    @ApiOperation(value = "根据验证码ID获取图片")
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletResponse response) throws IOException {

        //得到验证码 生成指定验证码
        String code=redisTemplate.opsForValue().get(captchaId);
        CreateVerifyCode vCode = new CreateVerifyCode(116,36,4,10,code);
        vCode.write(response.getOutputStream());
    }

    @RequestMapping(value = "/sendSms/{mobile}",method = RequestMethod.GET)
    @ApiOperation(value = "发送短信")
    public Result<Object> sendSms(@PathVariable(name = "mobile") String mobile){
        String code = SmsServiceUtil.setCode();
        redisTemplate.opsForValue().set(mobile,code);
        try {
            String resStr = FcySmsUtil.sendSms("【VR教育】验证码:"+code,mobile);
            System.out.println(resStr);
            JSONObject json = JSONObject.fromObject(resStr);
            if (!"1".equals(json.get("status")+"")){
                return new ResultUtil<Object>().setErrorMsg("发送失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultUtil<Object>().setErrorMsg("发送异常");
        }
        return new ResultUtil<Object>().setSuccessMsg("发送成功");
    }
}
