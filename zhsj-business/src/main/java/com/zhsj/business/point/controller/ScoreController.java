package com.zhsj.business.point.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.mapper.CourseClassTeacherMapper;
import com.zhsj.business.courseClassTeacher.service.CourseClassTeacherService;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.mapper.ClassInfoMapper;
import com.zhsj.business.kaoqin.service.ClassInfoService;
import com.zhsj.business.point.dto.ScoreDto;
import com.zhsj.business.point.dto.ScoreQueryDto;
import com.zhsj.business.point.service.ScoreService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @className: ScoreController
 * @description: TODO
 * @author: yrt
 * date: 2022/4/23 15:58
 * version 1.0
 **/
@RestController
@RequestMapping("/business/score")
public class ScoreController extends BaseController {
    @Resource
    private ScoreService scoreService;
    @Resource
    private CourseClassTeacherService courseClassTeacherService;
    @Resource
    private ClassInfoService classInfoService;
    @PostMapping("/getStudentScore")
    public TableDataInfo getStudentScore(@RequestBody ScoreQueryDto dto) {
        startPage();
        List<ScoreDto> list = scoreService.getStudentScore(dto);
        return getDataTable(list);
    }
    @PostMapping("/getClassNameByTeacher")
    public Map<String, Object> getClassNameByTeacher(@RequestBody String teacherCode) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<CourseClassTeacherPO> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_code", teacherCode);
        List<CourseClassTeacherPO> list = courseClassTeacherService.list(wrapper);
        List<String> collect = list.stream().map(CourseClassTeacherPO::getClassCode).collect(Collectors.toList());
        List<ClassInfoPO> classList = new ArrayList<>();
        for (String classCode : collect) {
            QueryWrapper<ClassInfoPO> classInfoPOQueryWrapper = new QueryWrapper<>();
            classInfoPOQueryWrapper.eq("class_code", classCode);
            ClassInfoPO classInOne = classInfoService.getOne(classInfoPOQueryWrapper);
            classList.add(classInOne);
        }
        map.put("classInfo", classList);
        return map;
    }
}
