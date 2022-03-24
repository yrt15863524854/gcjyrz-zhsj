package com.zhsj.business.course.controller;

import com.zhsj.business.course.domain.CoursePO;
import com.zhsj.business.course.service.CourseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/business/course")
public class CourseController {
    @Resource
    private CourseService courseService;

    @PostMapping("/getAllCourseInfo")
    public List<CoursePO> getAllCourseInfo(){
        return courseService.list();
    }
}
