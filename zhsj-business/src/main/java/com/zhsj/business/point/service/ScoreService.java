package com.zhsj.business.point.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.point.domain.ScorePO;
import com.zhsj.business.point.dto.ScoreDto;
import com.zhsj.business.point.dto.ScoreQueryDto;

import java.util.List;

/**
 * @interfaceName: ScoreService
 * @description: TODO
 * @author: yrt
 * date: 2022/4/18 15:53
 * version 1.0
 **/
public interface ScoreService extends IService<ScorePO> {
    void insertScore();
    void updateScore();
    List<ScoreDto> getStudentScore(ScoreQueryDto dto);
}
