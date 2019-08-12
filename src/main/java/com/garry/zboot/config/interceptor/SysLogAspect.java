package com.garry.zboot.config.interceptor;

import com.garry.zboot.common.annotation.SystemLog;
import com.garry.zboot.common.enums.LogType;
import com.garry.zboot.common.utils.IpInfoUtil;
import com.garry.zboot.common.utils.SecurityUtil;
import com.garry.zboot.modules.base.model.User;
import com.garry.zboot.modules.base.model.elasticsearch.EsLog;
import com.garry.zboot.modules.base.service.elasticsearch.EsLogService;
import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private EsLogService esLogService;

    @Autowired
    private SecurityUtil securityUtil;
    /**
     * 这里我们使用注解的形式
     * 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   execution(...)
     */
    @Pointcut("@annotation(com.garry.zboot.common.annotation.SystemLog)")
    public void logPointCut() {}

    /**
     * 环绕通知 @Around  ， 当然也可以使用 @Before (前置通知)  @After (后置通知)
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - beginTime;
        try {
            saveLog(point, time);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 保存日志
     * @param joinPoint
     * @param time
     */
    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        User user = securityUtil.getCurrUser();
        SystemLog systemLog = method.getAnnotation(SystemLog.class);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        EsLog esLog = new EsLog();
        if(systemLog != null){
            //注解上的描述
            esLog.setName(systemLog.description());
        }
        esLog.setCostTime(time);
        esLog.setCreateTime(new Date());
        esLog.setLogType(systemLog.type().toString().equals("OPERATION")?0:1);
        esLog.setCreateBy(user.getId());
        esLog.setUsername(user.getUsername());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        esLog.setIp(request.getRemoteAddr());
        esLog.setRequestType(request.getMethod());
//        esLog.setIpInfo(ipInfoUtil.getInfo(request));
        esLog.setRequestUrl(className+"."+methodName);
//esLog.setRequestUrl(method.type);
        Object[] args = joinPoint.getArgs();
        try{
            List<String> list = new ArrayList<String>();
            for (Object o : args) {
                list.add(new Gson().toJson(o));
            }
            esLog.setRequestParam(list.toString());
        }catch (Exception e){ }

        esLogService.saveLog(esLog);
    }
}
