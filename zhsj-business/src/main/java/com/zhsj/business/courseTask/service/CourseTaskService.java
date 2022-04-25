package com.zhsj.business.courseTask.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.courseTask.domain.CourseTaskPO;
import com.zhsj.business.courseTask.dto.CourseTaskDto;
import com.zhsj.business.courseTask.dto.CourseTaskQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @interfaceName: CourseTaskService
 * @description: TODO
 * @author: yrt
 * date: 2022/4/21 14:05
 * version 1.0
 **/
public interface CourseTaskService extends IService<CourseTaskPO> {
    Map<String, Object> insertTaskBook(CourseTaskDto dto);
    Map<String, Object> updateTaskBook(CourseTaskDto dto);
    List<CourseTaskDto> getTaskBookList();
    CourseTaskDto getTaskBook(CourseTaskQueryDto courseCode);
}
