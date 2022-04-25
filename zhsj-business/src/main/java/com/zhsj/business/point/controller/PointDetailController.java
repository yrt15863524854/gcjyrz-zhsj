package com.zhsj.business.point.controller;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.service.ClassInfoService;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.domain.PointPO;
import com.zhsj.business.point.dto.PointDetailDto;
import com.zhsj.business.point.dto.PointDetailQueryDto;
import com.zhsj.business.point.service.PointDetailService;
import com.zhsj.business.point.service.PointService;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.service.StudentService;
import com.zhsj.common.core.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: PointDetailController
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 17:27
 * version 1.0
 **/
@RestController
@RequestMapping("/business/pointDetail")
public class PointDetailController extends BaseController {
    @Resource
    private PointDetailService pointDetailService;
    @Resource
    private StudentService studentService;
    @Resource
    private PointService pointService;
    @Resource
    private ClassInfoService classInfoService;
    @PostMapping("/selectDetailList")
    public List<PointDetailDto> selectDetailList(@RequestBody PointDetailQueryDto queryDto) {
        PointDetailDto pointDetailDto = new PointDetailDto();
        pointDetailDto.setStudentNo(queryDto.getStudentNo());
        pointDetailService.insertPointDetail(pointDetailDto);
        QueryWrapper<PointDetailPO> wrapper = new QueryWrapper<>();
        wrapper.eq("student_no", queryDto.getStudentNo());
        List<PointDetailPO> poList = pointDetailService.list(wrapper);
        List<PointDetailDto> pointDetailDtoList = new ArrayList<>();
        QueryWrapper<StudentPO> studentWrapper = new QueryWrapper<>();
        studentWrapper.eq("student_no", queryDto.getStudentNo());
        StudentPO student = studentService.getOne(studentWrapper);
        QueryWrapper<ClassInfoPO> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("class_code", student.getStudentClass());
        ClassInfoPO classInfo = classInfoService.getOne(wrapper2);
        for (PointDetailPO po : poList) {
            PointDetailDto dto = new PointDetailDto();
            dto.setId(po.getId());
            dto.setStudentNo(po.getStudentNo());
            dto.setStudentName(student.getStudentName());
            dto.setPointName(po.getPointName());
            dto.setPointCode(po.getPointCode());
            dto.setPointScore(po.getPointScore());
            dto.setPointScoreRatio(po.getPointScoreRatio());
            QueryWrapper<PointPO> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("point_code", po.getPointCode());
            PointPO one = pointService.getOne(wrapper1);
            dto.setPointRatio(one.getPointRatio().toString());
            dto.setStudentGroup(student.getStudentGroup().toString());
            dto.setStudentClass(classInfo.getClassName());
            pointDetailDtoList.add(dto);
        }
        return pointDetailDtoList;
    }
    @PostMapping("/getOneDataById")
    public PointDetailPO getOneDataById(@RequestBody PointDetailQueryDto queryDto) {
        return pointDetailService.getOneDataById(queryDto);
    }
    @PostMapping("/updatePointDetail")
    public void updatePointDetail(@RequestBody PointDetailDto dto) {
        pointDetailService.updatePointDetail(dto);
    }

}
