package com.zhsj.business.rate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.rate.domain.RatePO;
import com.zhsj.business.rate.dto.RateDto;
import com.zhsj.business.rate.dto.RateQueryDto;
import com.zhsj.business.rate.service.RateService;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.service.StudentService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.utils.uuid.IdUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: RateController
 * @description: TODO
 * @author: yrt
 * date: 2022/5/6 23:06
 * version 1.0
 **/
@RestController
@RequestMapping("/business/rate")
public class RateController extends BaseController {
    @Resource
    private RateService rateService;
    @Resource
    private StudentService studentService;
    @PostMapping("/getRate")
    public RatePO getRate(@RequestBody RatePO rate){
        QueryWrapper<RatePO> wrapper = new QueryWrapper<>();
        wrapper.eq("student_no", rate.getStudentNo());
        return rateService.getOne(wrapper);
    }
    @PostMapping("/addRate")
    public void addRate(@RequestBody RatePO rate) {
        rate.setId(IdUtils.longID());
        QueryWrapper<StudentPO> wrapper = new QueryWrapper<>();
        wrapper.eq("student_no", rate.getStudentNo());
        StudentPO one = studentService.getOne(wrapper);
        rate.setClassCode(one.getStudentClass());
        rateService.save(rate);
    }
    @PostMapping("/list")
    public List<RatePO> list(@RequestBody RatePO rate) {
        QueryWrapper<RatePO> wrapper = new QueryWrapper<>();
        wrapper.eq("rate_status", rate.getRateStatus());
        return rateService.list(wrapper);
    }
    @PostMapping("/listRate")
    public List<RateDto> listRate(@RequestBody RateQueryDto dto) {
        return rateService.listRate(dto);
    }

}
