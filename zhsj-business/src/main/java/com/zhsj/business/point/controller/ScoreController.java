package com.zhsj.business.point.controller;

import com.zhsj.business.point.dto.ScoreDto;
import com.zhsj.business.point.dto.ScoreQueryDto;
import com.zhsj.business.point.service.ScoreService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @className: ScoreController
 * @description: TODO
 * @author: yrt
 * date: 2022/4/23 15:58
 * version 1.0
 **/
@RestController
@RequestMapping("/business/score")
public class ScoreController extends BaseController {
    @Resource
    private ScoreService scoreService;
    @PostMapping("/getStudentScore")
    public TableDataInfo getStudentScore(@RequestBody ScoreQueryDto dto) {
        startPage();
        List<ScoreDto> list = scoreService.getStudentScore(dto);
        return getDataTable(list);
    }
}
