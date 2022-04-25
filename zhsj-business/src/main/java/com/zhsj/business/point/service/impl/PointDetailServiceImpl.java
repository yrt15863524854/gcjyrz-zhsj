package com.zhsj.business.point.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.domain.PointPO;
import com.zhsj.business.point.domain.ScorePO;
import com.zhsj.business.point.dto.PointDetailDto;
import com.zhsj.business.point.dto.PointDetailQueryDto;
import com.zhsj.business.point.mapper.PointDetailMapper;
import com.zhsj.business.point.mapper.PointMapper;
import com.zhsj.business.point.mapper.ScoreMapper;
import com.zhsj.business.point.service.PointDetailService;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.mapper.StudentMapper;
import com.zhsj.common.utils.bean.BeanUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @className: PointDetailServiceImpl
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 15:38
 * version 1.0
 **/
@Service
public class PointDetailServiceImpl extends ServiceImpl<PointDetailMapper, PointDetailPO> implements PointDetailService {

    @Resource
    private PointDetailMapper pointDetailMapper;

    @Resource
    private PointMapper pointMapper;

    @Resource
    private ScoreMapper scoreMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public void insertPointDetail(PointDetailDto dto) {
        QueryWrapper<PointDetailPO> pdWrapper = new QueryWrapper<>();
        QueryWrapper<PointPO> pWrapper = new QueryWrapper<>();
        PointDetailPO po = new PointDetailPO();
        BeanUtils.copyProperties(dto, po);
        pdWrapper.eq("student_no", po.getStudentNo());
        List<PointDetailPO> pointDetailList = pointDetailMapper.selectList(pdWrapper);
        List<PointPO> pointList = pointMapper.selectList(pWrapper);
        Set<String> set = new HashSet<>();
        Set<String> setName = new HashSet<>();
        Set<String> setRatio = new HashSet<>();
        for (PointDetailPO pointDetail : pointDetailList) {
            set.add(pointDetail.getPointCode());
            setName.add(pointDetail.getPointName());
        }
        for (PointPO point : pointList) {
            boolean haveItem = set.add(point.getPointCode());
            if (haveItem) {
                PointDetailPO pointDetailPO = new PointDetailPO();
                pointDetailPO.setId(IdUtils.longID());
                pointDetailPO.setPointCode(point.getPointCode());
                pointDetailPO.setPointName(point.getPointName());
                pointDetailPO.setStudentNo(po.getStudentNo());
                pointDetailMapper.insert(pointDetailPO);
            }
        }
    }

    @Override
    public void updatePointDetail(PointDetailDto dto) {
        PointDetailPO pointDetailPO = new PointDetailPO();
        BeanUtils.copyProperties(dto, pointDetailPO);
        String score = pointDetailPO.getPointScoreRatio();
        if (Objects.isNull(score) || score.equals("")) {
            score = "0";
        }
        BigDecimal decimal = new BigDecimal(score);
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        wrapper.eq("point_code",pointDetailPO.getPointCode());
        PointPO pointPO = pointMapper.selectOne(wrapper);
        BigDecimal ratio = pointPO.getPointRatio();
        BigDecimal result = decimal.multiply(ratio);
        pointDetailPO.setPointScoreRatio(score);
        pointDetailPO.setPointScore(result.toString());
        pointDetailMapper.updateById(pointDetailPO);

        //总分操作
        QueryWrapper<StudentPO> studentWrapper = new QueryWrapper<>();
        studentWrapper.eq("student_no", dto.getStudentNo());
        StudentPO studentPO = studentMapper.selectOne(studentWrapper);
        ScorePO scorePO = new ScorePO();
        scorePO.setId(IdUtils.longID());
        scorePO.setStudentNo(dto.getStudentNo());
        scorePO.setStudentGroup(studentPO.getStudentGroup());
        QueryWrapper<PointDetailPO> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("student_no", dto.getStudentNo());
        List<PointDetailPO> details = pointDetailMapper.selectList(wrapper1);
        BigDecimal decimal1 = new BigDecimal("0");
        for (PointDetailPO detail : details) {
            if (Objects.isNull(detail.getPointScore())) {
                return;
            } else if (Objects.nonNull(detail.getPointScore()) && detail.getPointScore().equals("")){
                return;
            } else {
                BigDecimal decimal2 = new BigDecimal(detail.getPointScore());
                decimal1 = decimal1.add(decimal2);
            }
        }
        scorePO.setStudentScore(decimal1.toString());
        QueryWrapper<ScorePO> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("student_no", scorePO.getStudentNo());
        scoreMapper.delete(wrapper2);
        scoreMapper.insert(scorePO);
    }

    @Override
    public PointDetailPO getOneDataById(PointDetailQueryDto dto) {
        PointDetailPO pointDetailPO = new PointDetailPO();
        BeanUtils.copyProperties(dto, pointDetailPO);
        QueryWrapper<PointDetailPO> wrapper = new QueryWrapper<>();
        wrapper.eq("id",pointDetailPO.getId());
        return pointDetailMapper.selectOne(wrapper);
    }

}
