package com.zhsj.business.point.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.dto.PointDetailDto;
import com.zhsj.business.point.dto.PointDetailQueryDto;
import com.zhsj.business.point.service.PointDetailService;
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
    @PostMapping("/selectDetailList")
    public List<PointDetailDto> selectDetailList(@RequestBody PointDetailQueryDto queryDto) {
        QueryWrapper<PointDetailPO> wrapper = new QueryWrapper<>();
        wrapper.eq("student_no", queryDto.getStudentNo());
        List<PointDetailPO> poList = pointDetailService.list(wrapper);
        List<PointDetailDto> pointDetailDtoList = new ArrayList<>();
        QueryWrapper<StudentPO> studentWrapper = new QueryWrapper<>();
        studentWrapper.eq("student_no", queryDto.getStudentNo());
        StudentPO student = studentService.getOne(studentWrapper);
        for (PointDetailPO po : poList) {
            PointDetailDto dto = new PointDetailDto();
            dto.setId(po.getId());
            dto.setStudentNo(dto.getStudentNo());
            dto.setStudentName(student.getStudentName());
            dto.setPointCode(po.getPointCode());
            dto.setPointScore(po.getPointScore());
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
