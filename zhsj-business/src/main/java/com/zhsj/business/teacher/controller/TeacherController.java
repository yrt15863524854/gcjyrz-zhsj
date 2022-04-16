package com.zhsj.business.teacher.controller;

import com.zhsj.business.teacher.domain.TeacherPO;
import com.zhsj.business.teacher.service.TeacherService;
import com.zhsj.common.core.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @className: TeacherController
 * @description: TODO
 * @author: yrt
 * date: 2022/3/27 18:20
 * version 1.0
 **/

@RestController
@RequestMapping("/business/teacher")
public class TeacherController extends BaseController {
    @Resource
    private TeacherService teacherService;
    @PostMapping("/getTeacherList")
    private List<TeacherPO> getTeacherList(){
        return teacherService.list();
    }
}
