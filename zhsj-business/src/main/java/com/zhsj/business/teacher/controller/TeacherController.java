package com.zhsj.business.teacher.controller;

import com.zhsj.business.teacher.domain.TeacherPO;
import com.zhsj.business.teacher.dto.TeacherDto;
import com.zhsj.business.teacher.service.TeacherService;
import com.zhsj.common.core.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    public List<TeacherPO> getTeacherList(){
        return teacherService.list();
    }
    @PostMapping("/insertOrUpdateTeacher")
    public Map<String, Object> insertOrUpdateTeacher(@RequestBody TeacherDto dto) {
        return teacherService.insertOrUpdateTeacher(dto);
    }
    @PostMapping("/deleteTeacher")
    public void deleteTeacher(@RequestBody TeacherDto dto) {
        teacherService.deleteTeacher(dto);
    }
}
