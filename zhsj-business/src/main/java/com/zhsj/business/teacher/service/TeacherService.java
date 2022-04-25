package com.zhsj.business.teacher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.teacher.domain.TeacherPO;
import com.zhsj.business.teacher.dto.TeacherDto;

import java.util.Map;

/**
 * @interfaceName: TeacherService
 * @description: TODO
 * @author: yrt
 * date: 2022/3/27 16:34
 * version 1.0
 **/
public interface TeacherService extends IService<TeacherPO> {
    Map<String, Object> insertOrUpdateTeacher(TeacherDto dto);
    void deleteTeacher(TeacherDto dto);
}
