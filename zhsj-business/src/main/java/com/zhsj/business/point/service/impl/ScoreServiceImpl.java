package com.zhsj.business.point.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.point.domain.ScorePO;
import com.zhsj.business.point.dto.ScoreDto;
import com.zhsj.business.point.dto.ScoreQueryDto;
import com.zhsj.business.point.mapper.ScoreMapper;
import com.zhsj.business.point.service.ScoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    @Override
    public void insertScore() {
    }

    @Override
    public void updateScore() {
    }

    @Override
    public List<ScoreDto> getStudentScore(ScoreQueryDto dto) {
        return scoreMapper.getStudentScore(dto);
    }
}
