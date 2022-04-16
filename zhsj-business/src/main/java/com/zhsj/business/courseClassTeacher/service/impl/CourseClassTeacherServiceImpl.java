package com.zhsj.business.courseClassTeacher.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherDto;
import com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherQueryDto;
import com.zhsj.business.courseClassTeacher.mapper.CourseClassTeacherMapper;
import com.zhsj.business.courseClassTeacher.service.CourseClassTeacherService;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.exception.base.BaseException;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class CourseClassTeacherServiceImpl extends ServiceImpl<CourseClassTeacherMapper, CourseClassTeacherPO> implements CourseClassTeacherService {
    @Resource
    private CourseClassTeacherMapper courseClassTeacherMapper;

    /**
     * @description entryInformation
     * @date 2022/3/26 19:47
     * @param courseClassTeacherDto
     * @return com.zhsj.common.core.domain.AjaxResult
     * @author yrt
     **/
    @Override
    public AjaxResult entryInformation(CourseClassTeacherDto courseClassTeacherDto) {
        CourseClassTeacherPO po = new CourseClassTeacherPO();
        BeanUtils.copyProperties(courseClassTeacherDto, po);
        if (Objects.isNull(po.getId())) {
            po.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            po.setCreateTime(DateUtils.getNowDate());
            int result = courseClassTeacherMapper.entryInformation(po);
            if (result != 1){
                throw new BaseException("添加失败");
            }
            return AjaxResult.success("添加成功");
        } else {
            po.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
            po.setUpdateTime(DateUtils.getNowDate());
            int result = courseClassTeacherMapper.modifyInformation(po);
            if (result != 1){
                throw new BaseException("修改失败");
            }
            return AjaxResult.success("修改成功");
        }
    }
    /**
     * @description getCCTList
     * @date 2022/3/27 19:27
     * @param dto
     * @return java.util.List<com.zhsj.business.courseClassTeacher.dto.CourseClassTeacherDto
     * @author yrt
     **/
    @Override
    public List<CourseClassTeacherDto> getCCTList(CourseClassTeacherQueryDto dto) {
        return courseClassTeacherMapper.getCCTList(dto);
    }

    @Override
    public CourseClassTeacherDto getSingle(CourseClassTeacherDto dto) {
        return courseClassTeacherMapper.getSingle(dto);
    }
}
