package com.garry.zboot.modules.base.controller.manage;

import com.garry.zboot.common.constant.CommonConstant;
import com.garry.zboot.common.exception.ZbootException;
import com.garry.zboot.common.utils.ResultUtil;
import com.garry.zboot.common.vo.PageUtil;
import com.garry.zboot.common.vo.PageVo;
import com.garry.zboot.common.vo.Result;
import com.garry.zboot.modules.base.model.QuartzJob;
import com.garry.zboot.modules.base.service.QuartzJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(description = "定时任务管理接口")
@RequestMapping("/zboot/quartzJob")
@Transactional
public class QuartzJobController {
    @Autowired
    private QuartzJobService quartzJobService;

    @Autowired
    private Scheduler scheduler;


    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有定时任务")
    public Result<Page<QuartzJob>> getAll(@ModelAttribute PageVo page) {

        Page<QuartzJob> data = quartzJobService.findAll(PageUtil.initPage(page));
        return new ResultUtil<Page<QuartzJob>>().setData(data);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加定时任务")
    public Result<Object> addJob(@ModelAttribute QuartzJob job) {

        List<QuartzJob> list = quartzJobService.findByJobClassName(job.getJobClassName());
        if (list != null && list.size() > 0) {
            return new ResultUtil<Object>().setErrorMsg("该定时任务类名已存在");
        }
        add(job.getJobClassName(), job.getCronExpression(), job.getParameter());
        quartzJobService.save(job);
        return new ResultUtil<Object>().setSuccessMsg("创建定时任务成功");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "更新定时任务")
    public Result<Object> editJob(@ModelAttribute QuartzJob job) {

        delete(job.getJobClassName());
        add(job.getJobClassName(), job.getCronExpression(), job.getParameter());
        job.setStatus(CommonConstant.STATUS_NORMAL);
        quartzJobService.update(job);
        return new ResultUtil<Object>().setSuccessMsg("更新定时任务成功");
    }

    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    @ApiOperation(value = "暂停定时任务")
    public Result<Object> pauseJob(@ModelAttribute QuartzJob job) {

        try {
            scheduler.pauseJob(JobKey.jobKey(job.getJobClassName()));
        } catch (SchedulerException e) {
            throw new ZbootException("暂停定时任务失败");
        }
        job.setStatus(CommonConstant.STATUS_DISABLE);
        quartzJobService.update(job);
        return new ResultUtil<Object>().setSuccessMsg("暂停定时任务成功");
    }

    @RequestMapping(value = "/resume", method = RequestMethod.POST)
    @ApiOperation(value = "恢复定时任务")
    public Result<Object> resumeJob(@ModelAttribute QuartzJob job) {

        try {
            scheduler.resumeJob(JobKey.jobKey(job.getJobClassName()));
        } catch (SchedulerException e) {
            throw new ZbootException("恢复定时任务失败");
        }
        job.setStatus(CommonConstant.STATUS_NORMAL);
        quartzJobService.update(job);
        return new ResultUtil<Object>().setSuccessMsg("恢复定时任务成功");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除定时任务")
    public Result<Object> deleteJob(@PathVariable String[] ids) {

        for (String id : ids) {
            QuartzJob job = quartzJobService.get(id);
            delete(job.getJobClassName());
            quartzJobService.delete(job);
        }
        return new ResultUtil<Object>().setSuccessMsg("删除定时任务成功");
    }

    /**
     * 添加定时任务
     *
     * @param jobClassName
     * @param cronExpression
     * @param parameter
     */
    public void add(String jobClassName, String cronExpression, String parameter) {

        try {
            // 启动调度器
            scheduler.start();

            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())
                    .withIdentity(jobClassName)
                    .usingJobData("parameter", parameter)
                    .build();

            //表达式调度构建器(即任务执行的时间) 使用withMisfireHandlingInstructionDoNothing() 忽略掉调度暂停过程中没有执行的调度
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName)
                    .withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.toString());
            throw new ZbootException("创建定时任务失败");
        } catch (Exception e) {
            throw new ZbootException("后台找不到该类名任务");
        }
    }

    /**
     * 删除定时任务
     *
     * @param jobClassName
     */
    public void delete(String jobClassName) {

        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName));
        } catch (Exception e) {
            throw new ZbootException("删除定时任务失败");
        }
    }

    public static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job) class1.newInstance();
    }
}
