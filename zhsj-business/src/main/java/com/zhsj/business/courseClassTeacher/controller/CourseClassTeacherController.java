package com.zhsj.business.courseClassTeacher.controller;

import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherDto;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherQueryDto;
import com.zhsj.business.courseClassTeacher.service.CourseClassTeacherService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
/**
 * @description
 * @date 2022/3/26 19:48
 * @author yrt
 **/
@RestController
@RequestMapping("/business/courseClassTeacher")
public class CourseClassTeacherController extends BaseController {

    @Resource
    private CourseClassTeacherService courseClassTeacherService;

    @PostMapping("/list")
    public TableDataInfo list(){
        startPage();
        List<CourseClassTeacherPO> list = courseClassTeacherService.list();
        return getDataTable(list);
    }

    @PostMapping("/entryInformation")
    public AjaxResult entryInformation(@RequestBody CourseClassTeacherDto courseClassTeacherDto) {
        return courseClassTeacherService.entryInformation(courseClassTeacherDto);
    }

    @PostMapping("/getCCTList")
    public TableDataInfo getCCTList(@RequestBody CourseClassTeacherQueryDto dto) {
        startPage();
        List<CourseClassTeacherDto> list = courseClassTeacherService.getCCTList(dto);
        return getDataTable(list);
    }

    @PostMapping("/getSingle")
    public CourseClassTeacherDto getSingle(@RequestBody CourseClassTeacherDto dto) {
        return courseClassTeacherService.getSingle(dto);
    }
}
