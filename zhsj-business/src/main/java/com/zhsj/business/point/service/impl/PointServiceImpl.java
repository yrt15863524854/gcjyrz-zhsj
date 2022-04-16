package com.zhsj.business.point.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.point.domain.PointPO;
import com.zhsj.business.point.dto.PointDto;
import com.zhsj.business.point.mapper.PointMapper;
import com.zhsj.business.point.service.PointService;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.bean.BeanUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import com.zhsj.common.utils.uuid.UUID;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.Query;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: PointServiceImpl
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 14:38
 * version 1.0
 **/
@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, PointPO> implements PointService {

    @Resource
    private PointMapper pointMapper;

    @Override
    public Map<String, String> insertPoint(PointDto dto) {
        Map<String, String> map = new HashMap<>();
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        List<PointPO> list = pointMapper.selectList(wrapper);
        BigDecimal decimal = new BigDecimal("0");
        BigDecimal pointRatio = dto.getPointRatio();
        for (PointPO point : list) {
            decimal = decimal.add(point.getPointRatio());
        }
        decimal = decimal.add(pointRatio);
        int compareResult = decimal.compareTo(new BigDecimal("1"));
        if (compareResult > 0) {
            map.put("error1Code", "1");
            map.put("errorMessage", "新增失败，评分占比总和已超过1，请检查新增数据评分点比率" + dto.getPointRatio());
        } else {
            PointPO po = new PointPO();
            BeanUtils.copyProperties(dto, po);
            po.setId(IdUtils.longID());
            po.setPointCode(IdUtils.simpleUUID());
            po.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            po.setCreateTime(DateUtils.getNowDate());
            int result = pointMapper.insertPoint(po);
            if (result <= 0){
                map.put("error1Code", "1");
            } else {
                map.put("error1Code", "2");
            }
        }
        return map;
    }

    @Override
    public Map<String, String> updatePoint(PointDto dto) {
        Map<String, String> map = new HashMap<>();
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        List<PointPO> list = pointMapper.selectList(wrapper);
        BigDecimal decimal = new BigDecimal("0");
        for (PointPO point : list) {
            String pointCode = dto.getPointCode();
            String pointCode1 = point.getPointCode();
            BigDecimal pointRatio = dto.getPointRatio();
            if (pointCode.equals(pointCode1)) {
                decimal = decimal.add(pointRatio);
            }
            else {
                decimal = decimal.add(point.getPointRatio());
            }
        }
        int compareResult = decimal.compareTo(new BigDecimal("1"));
        if (compareResult > 0){
            map.put("error1Code", "1");
            map.put("errorMessage", "修改失败，评分占比总和已超过1，请检查修改数据评分点比率" + dto.getPointRatio());
        } else {
            PointPO po = new PointPO();
            BeanUtils.copyProperties(dto, po);
            po.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
            po.setUpdateTime(DateUtils.getNowDate());
            int result = pointMapper.updatePoint(po);
            if (result <= 0){
                map.put("error1Code", "1");
            } else {
                map.put("error1Code", "2");
            }
        }
        return map;
    }
}
