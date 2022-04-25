package com.zhsj.business.courseTask.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.courseTask.domain.CourseTaskPO;
import com.zhsj.business.courseTask.dto.CourseTaskDto;
import com.zhsj.business.courseTask.dto.CourseTaskQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @interfaceName: CourseTaskMapper
 * @description: TODO
 * @author: yrt
 * date: 2022/4/21 14:04
 * version 1.0
 **/
public interface CourseTaskMapper extends BaseMapper<CourseTaskPO> {
    List<CourseTaskDto> getTaskBookList();
    CourseTaskDto getTaskBook(@Param("dto") CourseTaskQueryDto courseCode);
}
