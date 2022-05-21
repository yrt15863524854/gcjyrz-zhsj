package com.zhsj.business.point.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.mapper.CourseClassTeacherMapper;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.mapper.ClassInfoMapper;
import com.zhsj.business.point.domain.ScorePO;
import com.zhsj.business.point.dto.ScoreDto;
import com.zhsj.business.point.dto.ScoreQueryDto;
import com.zhsj.business.point.mapper.ScoreMapper;
import com.zhsj.business.point.service.ScoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @className: ScoreServiceImpl
 * @description: TODO
 * @author: yrt
 * date: 2022/4/18 15:53
 * version 1.0
 **/
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, ScorePO> implements ScoreService {
    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private CourseClassTeacherMapper courseClassTeacherMapper;
    @Resource
    private ClassInfoMapper classInfoMapper;
    @Override
    public void insertScore() {
    }

    @Override
    public void updateScore() {
    }

    @Override
    public List<ScoreDto> getStudentScore(ScoreQueryDto dto) {
        if (dto.getClassCode() != null) {
            return scoreMapper.getStudentScore(dto);
        }
        List<ScoreDto> scoreDtoList = new ArrayList<>();
        QueryWrapper<CourseClassTeacherPO> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_code", dto.getTeacherCode());
        List<CourseClassTeacherPO> courseClassTeacherPOS = courseClassTeacherMapper.selectList(wrapper);
        List<String> list = courseClassTeacherPOS.stream().map(CourseClassTeacherPO::getClassCode).collect(Collectors.toList());
        for (String classCode : list) {
            dto.setClassCode(classCode);
            List<ScoreDto> studentScore = scoreMapper.getStudentScore(dto);
            scoreDtoList.addAll(studentScore);
        }
//        if (Objects.nonNull(dto.getStudentName())) {
//            return scoreDtoList.stream().filter(s -> s.getStudentName().equals(dto.getStudentName())).collect(Collectors.toList());
//        }
//        if (Objects.nonNull(dto.getStudentGroup())) {
//            return scoreDtoList.stream().filter(s -> s.getStudentGroup().equals(dto.getStudentGroup().toString())).collect(Collectors.toList());
//        }
//        if (Objects.nonNull(dto.getStudentNo())) {
//            return scoreDtoList.stream().filter(s -> s.getStudentNo().equals(dto.getStudentNo().toString())).collect(Collectors.toList());
//        }

        return scoreDtoList;
    }
}
