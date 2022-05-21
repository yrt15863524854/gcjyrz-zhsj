package com.zhsj.business.point.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.service.ClassInfoService;
import com.zhsj.business.manual.domain.ManualPO;
import com.zhsj.business.manual.mapper.ManualMapper;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.domain.PointPO;
import com.zhsj.business.point.domain.ScorePO;
import com.zhsj.business.point.dto.*;
import com.zhsj.business.point.mapper.PointDetailMapper;
import com.zhsj.business.point.service.PointDetailService;
import com.zhsj.business.point.service.PointService;
import com.zhsj.business.point.service.ScoreService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.page.TableDataInfo;
import com.zhsj.common.utils.bean.BeanUtils;
import com.zhsj.common.utils.poi.ExcelUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
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
    @Resource
    private PointDetailService pointDetailService;
    @Resource
    private ClassInfoService classInfoService;
    @Resource
    private ScoreService scoreService;
    @Resource
    private ManualMapper manualMapper;
    @PostMapping("/getPointInfo")
    public TableDataInfo getPointInfo(@RequestBody PointQueryDto dto) {
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(dto.getPointName())) {
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
        QueryWrapper<PointDetailPO> pointDetailWrapper = new QueryWrapper<>();
        pointDetailWrapper.eq("point_code", dto.getPointCode());
        pointDetailService.remove(pointDetailWrapper);
        QueryWrapper<ScorePO> wrapper = new QueryWrapper<>();
        scoreService.remove(wrapper);
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
        QueryWrapper<PointDetailPO> pointDetailWrapper = new QueryWrapper<>();
        pointDetailWrapper.eq("point_code", dto.getPointCode());
        pointDetailService.remove(pointDetailWrapper);
        QueryWrapper<ScorePO> wrapper1 = new QueryWrapper<>();
        scoreService.remove(wrapper1);
        pointService.remove(wrapper);
    }
    @PostMapping("/getOnePoint")
    public PointPO getOnePoint(@RequestBody PointQueryDto queryDto) {
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", queryDto.getId());
        return pointService.getOne(wrapper);
    }
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExcelQueryDto dto) throws IOException {
        QueryWrapper<ClassInfoPO> wrapper = new QueryWrapper<>();
        wrapper.eq("class_code", dto.getClassCode());
        ClassInfoPO classInfo = classInfoService.getOne(wrapper);
        String className = "";
        if (Objects.nonNull(classInfo)) {
            className = classInfo.getClassName();
        }
        List<ExcelDto> list = pointDetailService.listExcel(dto);
        ExcelUtil<ExcelDto> util = new ExcelUtil<>(ExcelDto.class);
        util.exportExcel(response, list, className + "评分明细");
        pointDetailService.degreeOfAchievement(dto);
    }
    @PostMapping("/exportNew")
    public void exportNew(HttpServletResponse response, ExcelQueryDto dto)throws IOException {
        pointDetailService.degreeOfAchievement(dto);
        try {
            InputStream inputStream = new FileInputStream("D:/zhsj/uploadPath/" + "课程设计达成度.xlsx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            ServletOutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("课程设计达成度", "UTF-8"));
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) {
                    out.write(buffer, 0, b);
                }
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File file = new File("D:/zhsj/uploadPath/" + "课程设计达成度.xlsx");
            boolean delete = file.delete();
            if (delete) {
                logger.info("删除成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostMapping("/exportWord")
    public void exportWord(HttpServletResponse response, ExcelQueryDto dto) throws IOException, InvalidFormatException {
        pointDetailService.achievementReport(response,dto);
        try {
            InputStream inputStream = new FileInputStream("D:/zhsj/uploadPath/model/report.docx");
            response.setContentType("application/force-download");
            response.setCharacterEncoding("utf-8");
            ServletOutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("课程设计达成度报告模板", "UTF-8"));
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) {
                    out.write(buffer, 0, b);
                }
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File file = new File("D:/zhsj/uploadPath/model/report.docx");
            boolean delete = file.delete();
            if (delete) {
                logger.info("删除成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/pointComments")
    public PointComments getPointCommentsByNo(@RequestBody ExcelQueryDto dto) {
        return pointDetailService.getPointCommentsByNo(dto.getStudentNo());
    }

    @PostMapping("/haveTopic")
    public Map<String, Object> haveTopic(@RequestBody ExcelQueryDto dto) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<ManualPO> wrapper = new QueryWrapper<>();
        wrapper.eq("student_no", dto.getStudentNo());
        ManualPO manualPO = manualMapper.selectOne(wrapper);
        if (Objects.nonNull(manualPO)) {
            map.put("message", "查询到");
            return map;
        } else {
            map.put("message", "该学生未申报题目");
            return null;
        }
    }
}

