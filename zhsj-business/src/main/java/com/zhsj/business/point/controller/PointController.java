package com.zhsj.business.point.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.point.domain.PointPO;
import com.zhsj.business.point.dto.PointDto;
import com.zhsj.business.point.dto.PointQueryDto;
import com.zhsj.business.point.service.PointService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.page.TableDataInfo;
import com.zhsj.common.utils.bean.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @className: PointController
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 14:40
 * version 1.0
 **/
@RestController
@RequestMapping("/business/point")
public class PointController extends BaseController {
    @Resource
    private PointService pointService;
    @PostMapping("/getPointInfo")
    public TableDataInfo getPointInfo(@RequestBody PointQueryDto dto) {
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(dto.getPointName()) || "".equals(dto.getPointName())) {
            wrapper.eq("point_name", dto.getPointName());
        }
        List<PointPO> list = pointService.list(wrapper);
        startPage();
        return getDataTable(list);
    }
    @PostMapping("/addPoint")
    public Map<String, String> addPoint(@RequestBody PointDto dto){
        return pointService.insertPoint(dto);
    }
    @PostMapping("/updatePoint")
    public Map<String, String> updatePoint(@RequestBody PointDto dto){
        return pointService.updatePoint(dto);
    }
    @PostMapping("/deletePoint")
    public void DeletePoint(@RequestBody PointDto dto){
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        PointPO pointPO = new PointPO();
        BeanUtils.copyProperties(dto, pointPO);
        if (Objects.nonNull(pointPO.getId())) {
            wrapper.eq("id", pointPO.getId());
        }
        pointService.remove(wrapper);
    }
    @PostMapping("/getOnePoint")
    public PointPO getOnePoint(@RequestBody PointQueryDto queryDto) {
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", queryDto.getId());
        return pointService.getOne(wrapper);
    }
}
