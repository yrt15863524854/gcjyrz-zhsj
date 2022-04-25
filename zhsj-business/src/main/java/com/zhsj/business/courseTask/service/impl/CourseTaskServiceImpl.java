package com.zhsj.business.courseTask.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.courseTask.domain.CourseTaskPO;
import com.zhsj.business.courseTask.dto.CourseTaskDto;
import com.zhsj.business.courseTask.dto.CourseTaskQueryDto;
import com.zhsj.business.courseTask.mapper.CourseTaskMapper;
import com.zhsj.business.courseTask.service.CourseTaskService;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.bean.BeanUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: CourseTaskServiceImpl
 * @description: TODO
 * @author: yrt
 * date: 2022/4/21 14:06
 * version 1.0
 **/
@Service
public class CourseTaskServiceImpl extends ServiceImpl<CourseTaskMapper, CourseTaskPO> implements CourseTaskService {

    @Resource
    private CourseTaskMapper courseTaskMapper;

    @Override
    public Map<String, Object> insertTaskBook(CourseTaskDto dto) {
        Map<String, Object> map = new HashMap<>();
        CourseTaskPO courseTaskPO = new CourseTaskPO();
        BeanUtils.copyProperties(dto, courseTaskPO);
        courseTaskPO.setId(IdUtils.longID());
        courseTaskPO.setCourseTaskCode(IdUtils.fastSimpleUUID());
        courseTaskPO.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        courseTaskPO.setCreateTime(DateUtils.getNowDate());
        int result = courseTaskMapper.insert(courseTaskPO);
        if (result <= 0) {
            map.put("message", "添加失败");
        } else {
            map.put("message", "添加成功");
        }
        return map;
    }

    @Override
    public Map<String, Object> updateTaskBook(CourseTaskDto dto) {
        Map<String, Object> map = new HashMap<>();
        CourseTaskPO courseTaskPO = new CourseTaskPO();
        BeanUtils.copyProperties(dto, courseTaskPO);
        courseTaskPO.setUpdateBy((SecurityUtils.getLoginUser().getUsername()));
        courseTaskPO.setUpdateTime(DateUtils.getNowDate());
        int result = courseTaskMapper.updateById(courseTaskPO);
        if (result <= 0) {
            map.put("message", "修改失败");
        } else {
            map.put("message", "修改成功");
        }
        return map;
    }

    @Override
    public List<CourseTaskDto> getTaskBookList() {
        return courseTaskMapper.getTaskBookList();
    }

    @Override
    public CourseTaskDto getTaskBook(CourseTaskQueryDto courseCode) {
        return courseTaskMapper.getTaskBook(courseCode);
    }
}
