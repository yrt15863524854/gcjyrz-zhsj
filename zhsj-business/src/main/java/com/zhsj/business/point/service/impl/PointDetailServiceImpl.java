package com.zhsj.business.point.service.impl;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.word.WordExportUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.courseClassTeacher.domain.CourseClassTeacherPO;
import com.zhsj.business.courseClassTeacher.mapper.CourseClassTeacherMapper;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.domain.KaoQinPO;
import com.zhsj.business.kaoqin.mapper.ClassInfoMapper;
import com.zhsj.business.kaoqin.mapper.KaoQinMapper;
import com.zhsj.business.manual.domain.ManualPO;
import com.zhsj.business.manual.mapper.ManualMapper;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.domain.PointPO;
import com.zhsj.business.point.domain.ScorePO;
import com.zhsj.business.point.dto.*;
import com.zhsj.business.point.mapper.PointDetailMapper;
import com.zhsj.business.point.mapper.PointMapper;
import com.zhsj.business.point.mapper.ScoreMapper;
import com.zhsj.business.point.service.PointDetailService;
import com.zhsj.business.rate.domain.RatePO;
import com.zhsj.business.rate.mapper.RateMapper;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.mapper.StudentMapper;
import com.zhsj.common.annotation.Excel;
import com.zhsj.common.utils.bean.BeanUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Resource
    private ClassInfoMapper classInfoMapper;

    @Resource
    private ManualMapper manualMapper;

    @Resource
    private KaoQinMapper kaoQinMapper;

    @Resource
    private CourseClassTeacherMapper courseClassTeacherMapper;

    @Resource
    private RateMapper rateMapper;

    @Override
    public void insertPointDetail(PointDetailDto dto) {
        QueryWrapper<PointDetailPO> pdWrapper = new QueryWrapper<>();
        QueryWrapper<PointPO> pWrapper = new QueryWrapper<>();
        PointDetailPO po = new PointDetailPO();
        BeanUtils.copyProperties(dto, po);
        pdWrapper.eq("student_no", po.getStudentNo());
        QueryWrapper<StudentPO> stuWrapper = new QueryWrapper<>();
        stuWrapper.eq("student_no", dto.getStudentNo());
        StudentPO studentPO = studentMapper.selectOne(stuWrapper);
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
                pointDetailPO.setClassCode(studentPO.getStudentClass());
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

        //????????????
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

    @Override
    public List<ExcelDto> listExcel(ExcelQueryDto dto) {
        List<ExcelDto> excelDtos = new LinkedList<>();
        List<PointExcelDto> pointExcelDtos = pointDetailMapper.listPointExcel(dto);
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        List<PointPO> list = pointMapper.selectList(wrapper);
        List<String> studentNo = pointExcelDtos.stream().map(PointExcelDto::getStudentNo).distinct().collect(Collectors.toList());
        List<String> pointName = pointExcelDtos.stream().map(PointExcelDto::getPointName).distinct().collect(Collectors.toList());
        int size = list.size();
        // voucher???????????????
        try{
            //excel????????????
            Class<?> c = Class.forName("com.zhsj.business.point.dto.ExcelDto");
            //??????getDeclaredFields()??????????????????????????????????????????????????????
            ExcelDto excelDto = new ExcelDto();
            excelDto.setField1("??????");
            excelDto.setField2("??????");
            excelDto.setField3("??????");
            excelDto.setField4("??????");
            Field[] fields = excelDto.getClass().getDeclaredFields();
            Method[] methods = excelDto.getClass().getMethods();//??????????????????
            for (int i = 0; i < size; i++) {
                String s =  pointName.get(i);
                for (Method method : methods) {
                    String methodName = "setField" + (i + 5);
                    if (method.getName().equals(methodName)) {
                        method.invoke(excelDto, s);
                    }
                }
            }
            excelDtos.add(excelDto);
            //excel????????????
            for (int i = 0; i < studentNo.size(); i++) {
                ExcelDto excelDtoTem = new ExcelDto();
                //????????????
                String no = studentNo.get(i);
                PointExcelDto pointExcelDto1 = pointExcelDtos.stream().filter(p -> p.getStudentNo().equals(no)).findAny().orElse(null);
                assert pointExcelDto1 != null;
                excelDtoTem.setField1(pointExcelDto1.getClassName());
                excelDtoTem.setField2(pointExcelDto1.getStudentNo());
                excelDtoTem.setField3(pointExcelDto1.getStudentName());
                excelDtoTem.setField4(pointExcelDto1.getStudentScore());
                for (int j = 0; j < size; j++) {
                    //?????????????????????
                    String point = pointName.get(j);
                    //?????????????????????
                    PointExcelDto pointExcelDto2 = pointExcelDtos.stream().filter(p -> p.getStudentNo().equals(no) && p.getPointName().equals(point)).findAny().orElse(null);
                    assert pointExcelDto2 != null;
                    String pointScore = pointExcelDto2.getPointScore();
                    //??????????????????????????????????????????
                    String methodName = "setField" + (j + 5);
                    for (Method method : methods) {
                        if (method.getName().equals(methodName)) {
                            method.invoke(excelDtoTem, pointScore);
                        }
                    }
                }
                excelDtos.add(excelDtoTem);
            }
        }
        catch (Exception ex){
            //????????????
        }
        return excelDtos;
    }

    @Override
    public PointComments getPointCommentsByNo(String studentNo) {
        PointComments pointComments = new PointComments();
        QueryWrapper<StudentPO> studentWrapper = new QueryWrapper<>();
        studentWrapper.eq("student_no", studentNo);
        StudentPO student = studentMapper.selectOne(studentWrapper);
        //????????????
        pointComments.setStudentNo(studentNo);
        QueryWrapper<ClassInfoPO> classWrapper = new QueryWrapper<>();
        classWrapper.eq("class_code", student.getStudentClass());
        ClassInfoPO classInfo = classInfoMapper.selectOne(classWrapper);
        //????????????
        pointComments.setClassName(classInfo.getClassName());
        //????????????
        pointComments.setStudentName(student.getStudentName());
        //????????????????????????
        QueryWrapper<ManualPO> manualWrapper = new QueryWrapper<>();
        manualWrapper.eq("student_no", studentNo);
        ManualPO manual = manualMapper.selectOne(manualWrapper);
        pointComments.setProjectName(manual.getManualName());
        //??????????????????????????????
        pointComments.setModuleName(manual.getFunctionalModule());
        //??????
        QueryWrapper<KaoQinPO> kaoQinWrapper = new QueryWrapper<>();
        kaoQinWrapper.eq("student_no", studentNo);
        List<KaoQinPO> kaoQin= kaoQinMapper.selectList(kaoQinWrapper);
        if (kaoQin.size() > 0) {
            pointComments.setAttendance("??????");
        } else {
            pointComments.setAttendance("");
        }
        //??????
        ExcelQueryDto dto = new ExcelQueryDto();
        dto.setStudentNo(studentNo);
        List<PointExcelDto> pointExcelDtos = pointDetailMapper.listPointExcel(dto);
        //??????
        String studentScore = pointExcelDtos.get(0).getStudentScore();
        pointComments.setStudentScore(studentScore);
        double score = new Double(studentScore);
        if(score > 84.99) {
            pointComments.setGrade("??????");
            pointComments.setAttitude("???????????????");
        } else if(score > 74.99) {
            pointComments.setGrade("??????");
            pointComments.setAttitude("??????");
        } else if(score > 59.99) {
            pointComments.setGrade("??????");
            pointComments.setAttitude("??????");
        } else {
            pointComments.setGrade("?????????");
            pointComments.setAttitude("??????");
        }
        //??????????????????

        //?????????????????????????????????
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        List<PointPO> pointPOS = pointMapper.selectList(wrapper);
        //???????????????????????????
        List<String> pointAttributes = pointPOS.stream().map(PointPO::getPointAttribute).distinct().collect(Collectors.toList());
        //??????????????????????????????
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < pointAttributes.size(); i++) {
            QueryWrapper<PointPO> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("point_attribute", pointAttributes.get(i));
            List<PointPO> pointPOS1 = pointMapper.selectList(wrapper1);
            List<String> list = new ArrayList<>();
            for (PointPO pointPO : pointPOS1) {
                list.add(pointPO.getPointName());
            }
            map.put(pointAttributes.get(i), list);
        }
        //??????????????????????????????????????????
        int size = pointAttributes.size();
        for (int i = 0; i < size; i++) {
            //??????????????????????????????
            List<String> pointList = map.get(pointAttributes.get(i));
            //????????????
            double pointScore = new Double("0");
            //??????????????????????????????
            BigDecimal pointRation = new BigDecimal("0");
            for (String s : pointList) {
                PointExcelDto workLoad = pointExcelDtos.stream().filter(pointExcelDto -> pointExcelDto.getPointName().equals(s)).findAny().orElse(null);
                assert workLoad != null;
                double temp = 0;
                if (Objects.nonNull(workLoad.getPointScore())) {
                    temp = new Double(workLoad.getPointScore());
                }
                PointPO pointPO = pointPOS.stream().filter(p -> p.getPointName().equals(s)).findAny().orElse(null);
                assert pointPO != null;
                BigDecimal pointRatioTem = pointPO.getPointRatio();
                pointRation = pointRation.add(pointRatioTem);
                pointScore = pointScore + temp;
            }
            BigDecimal multiply1 = pointRation.multiply(new BigDecimal("85"));
            double wscore1 = multiply1.doubleValue();
            BigDecimal multiply2 = pointRation.multiply(new BigDecimal("75"));
            double wscore2 = multiply2.doubleValue();
            BigDecimal multiply3 = pointRation.multiply(new BigDecimal("60"));
            double wscore3 = multiply3.doubleValue();
            if (pointAttributes.get(i).equals("????????????")) {
                if(pointScore >= wscore1) {
                    pointComments.setWorkLoad("??????");
                    pointComments.setCompleteness("??????????????????????????????");
                    pointComments.setKnowledge("??????");
                    pointComments.setAbility("??????");
                    pointComments.setAnalysis("????????????????????????????????????????????????");
                    pointComments.setOnTime("????????????");
                } else if(pointScore >= wscore2) {
                    pointComments.setWorkLoad("?????????");
                    pointComments.setCompleteness("???????????????????????????");
                    pointComments.setKnowledge("??????");
                    pointComments.setAbility("??????");
                    pointComments.setAnalysis("?????????????????????????????????????????????????????????");
                    pointComments.setOnTime("????????????");
                } else if(pointScore >= wscore3) {
                    pointComments.setWorkLoad("??????");
                    pointComments.setCompleteness("??????????????????");
                    pointComments.setKnowledge("??????");
                    pointComments.setAbility("??????");
                    pointComments.setAnalysis("???????????????????????????????????????????????????");
                    pointComments.setOnTime("??????");
                } else {
                    pointComments.setWorkLoad("??????");
                    pointComments.setCompleteness("?????????????????????");
                    pointComments.setKnowledge("??????");
                    pointComments.setAbility("??????");
                    pointComments.setAnalysis("????????????????????????????????????????????????");
                    pointComments.setOnTime("????????????");
                }
            } else if (pointAttributes.get(i).equals("???????????????")) {
                if (pointScore >= wscore1) {
                    pointComments.setFormat("??????");
                } else if (pointScore >= wscore2) {
                    pointComments.setFormat("?????????");
                } else if (pointScore >= wscore3) {
                    pointComments.setFormat("????????????");
                } else {
                    pointComments.setFormat("?????????");
                }
            }

        }
//        PointExcelDto workLoad = pointExcelDtos.stream().filter(pointExcelDto -> pointExcelDto.getPointName().equals("??????????????????")).findAny().orElse(null);
//        assert workLoad != null;
//        String workScore = workLoad.getPointScore();
//        double work = new Double(workScore);
//        QueryWrapper<PointPO> pointWrapper = new QueryWrapper<>();
//        List<PointPO> points = pointMapper.selectList(pointWrapper);
//        PointPO point = points.stream().filter(p -> p.getPointName().equals("??????????????????")).findAny().orElse(null);
//        assert point != null;
//        BigDecimal pointRatio = point.getPointRatio();
//        BigDecimal multiply1 = pointRatio.multiply(new BigDecimal("100"));
//        double wscore1 = multiply1.doubleValue();
//        BigDecimal multiply2 = pointRatio.multiply(new BigDecimal("85"));
//        double wscore2 = multiply2.doubleValue();
//        BigDecimal multiply3 = pointRatio.multiply(new BigDecimal("60"));
//        double wscore3 = multiply3.doubleValue();
//        if(work >= wscore1) {
//            pointComments.setWorkLoad("??????");
//            pointComments.setCompleteness("??????????????????????????????");
//            pointComments.setKnowledge("??????");
//            pointComments.setAbility("??????");
//            pointComments.setAnalysis("????????????????????????????????????????????????");
//            pointComments.setOnTime("????????????");
//        } else if(work >= wscore2) {
//            pointComments.setWorkLoad("?????????");
//            pointComments.setCompleteness("???????????????????????????");
//            pointComments.setKnowledge("??????");
//            pointComments.setAbility("??????");
//            pointComments.setAnalysis("?????????????????????????????????????????????????????????");
//            pointComments.setOnTime("????????????");
//        } else if(work >= wscore3) {
//            pointComments.setWorkLoad("??????");
//            pointComments.setCompleteness("??????????????????");
//            pointComments.setKnowledge("??????");
//            pointComments.setAbility("??????");
//            pointComments.setAnalysis("???????????????????????????????????????????????????");
//            pointComments.setOnTime("??????");
//        } else {
//            pointComments.setWorkLoad("??????");
//            pointComments.setCompleteness("?????????????????????");
//            pointComments.setKnowledge("??????");
//            pointComments.setAbility("??????");
//            pointComments.setAnalysis("????????????????????????????????????????????????");
//            pointComments.setOnTime("????????????");
//        }
//        //????????????
//        PointExcelDto format = pointExcelDtos.stream().filter(pointExcelDto -> pointExcelDto.getPointName().equals("????????????")).findAny().orElse(null);
//        assert format != null;
//        String formatScore = format.getPointScore();
//        double fscore = new Double(formatScore);
//        PointPO point2 = points.stream().filter(p -> p.getPointName().equals("????????????")).findAny().orElse(null);
//        assert point2 != null;
//        BigDecimal pointRatio2 = point2.getPointRatio();
//        BigDecimal multiply12 = pointRatio2.multiply(new BigDecimal("100"));
//        double wscore12 = multiply12.doubleValue();
//        BigDecimal multiply22 = pointRatio2.multiply(new BigDecimal("85"));
//        double wscore22 = multiply22.doubleValue();
//        BigDecimal multiply33 = pointRatio2.multiply(new BigDecimal("60"));
//        double wscore33 = multiply33.doubleValue();
//        if (fscore >= wscore12) {
//            pointComments.setFormat("??????");
//        } else if (fscore >= wscore22) {
//            pointComments.setFormat("?????????");
//        } else if (fscore >= wscore33) {
//            pointComments.setFormat("????????????");
//        } else {
//            pointComments.setFormat("?????????");
//        }
        return pointComments;
    }

    @Override
    public void degreeOfAchievement(ExcelQueryDto dto) throws IOException {
        List<PointExcelDto> pointExcel = new ArrayList<>();
        if (dto.getClassCode() != null) {
            List<PointExcelDto> pointExcelDtos = pointDetailMapper.listPointExcel(dto);
            pointExcel.addAll(pointExcelDtos);

        } else if (dto.getClassCodeList() != null && dto.getClassCodeList().size() > 0) {
            for (String s : dto.getClassCodeList()) {
                ExcelQueryDto excelQueryDto = new ExcelQueryDto();
                excelQueryDto.setClassCode(s);
                List<PointExcelDto> pointExcelDtoList = pointDetailMapper.listPointExcel(excelQueryDto);
                pointExcel.addAll(pointExcelDtoList);
            }
        } else {
            //?????????????????????????????????????????????
            String teacherCode = dto.getTeacherCode();
            QueryWrapper<CourseClassTeacherPO> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("teacher_code", teacherCode);
            List<CourseClassTeacherPO> courseClassTeacherPOS = courseClassTeacherMapper.selectList(wrapper1);
            List<String> classCodeList = courseClassTeacherPOS.stream().map(CourseClassTeacherPO::getClassCode).collect(Collectors.toList());
            for (String classCode : classCodeList) {
                ExcelQueryDto excelQueryDto = new ExcelQueryDto();
                excelQueryDto.setClassCode(classCode);
                List<PointExcelDto> pointExcelDtoList = pointDetailMapper.listPointExcel(excelQueryDto);
                pointExcel.addAll(pointExcelDtoList);
            }
        }

        //1??????????????????,??????????????????????????????
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        //2??????????????????
        SXSSFSheet sheet1 = workbook.createSheet("????????????");
        SXSSFSheet sheet2 = workbook.createSheet("????????????");
        SXSSFSheet sheet3 = workbook.createSheet("????????????");
        SXSSFSheet sheet4 = workbook.createSheet("?????????");

        CellStyle cellStyle = workbook.createCellStyle();
        //????????????
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //????????????
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //????????????
        //cellStyle.setFont();
        //??????????????????
        cellStyle.setWrapText(true);

        //sheet1????????????
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        //???????????????
        List<PointPO> pointList = pointMapper.selectList(wrapper);
        //??????????????????????????????
        List<String> studentNoList = pointExcel.stream().map(PointExcelDto::getStudentNo).distinct().collect(Collectors.toList());
        //????????????????????????
        List<String> pointNameList = pointList.stream().map(PointPO::getPointName).distinct().collect(Collectors.toList());
        //???????????????
        List<String> pointAttributeList = pointList.stream().map(PointPO::getPointAttribute).distinct().collect(Collectors.toList());
        //????????????
        List<String> pointTargetList = pointList.stream().map(PointPO::getPointTarget).distinct().collect(Collectors.toList());
        //????????????
        int pointAttributeSize = pointAttributeList.size();
        //???????????????
        int pointNameSize = pointNameList.size();
        //????????????
        int pointTargetSize = pointTargetList.size();


        for (int i = 0; i < 30; i++) {
            sheet1.setColumnWidth(i,sheet1.getColumnWidth(i)*14/10);
            sheet1.trackAllColumnsForAutoSizing();// ??????????????????????????????????????????
            sheet1.autoSizeColumn(i);// ??????????????????????????????????????????
        }
        for (int i = 0; i < 30; i++) {
            sheet2.setColumnWidth(i,sheet1.getColumnWidth(i)*19/10);
            sheet2.trackAllColumnsForAutoSizing();// ??????????????????????????????????????????
            sheet2.autoSizeColumn(i);// ??????????????????????????????????????????
        }
        for (int i = 0; i < 30; i++) {
            sheet3.setColumnWidth(i,sheet1.getColumnWidth(i)*14/10);
            sheet3.trackAllColumnsForAutoSizing();// ??????????????????????????????????????????
            sheet3.autoSizeColumn(i);// ??????????????????????????????????????????
        }
        for (int i = 0; i < 30; i++) {
            sheet4.setColumnWidth(i,sheet1.getColumnWidth(i)*14/10);
            sheet4.trackAllColumnsForAutoSizing();// ??????????????????????????????????????????
            sheet4.autoSizeColumn(i);// ??????????????????????????????????????????
        }

        //???????????????
        SXSSFRow row0 = sheet1.createRow(0);
        SXSSFCell cell1 = row0.createCell(0);
        cell1.setCellStyle(cellStyle);
        cell1.setCellValue("??????????????????");
        CellRangeAddress region = new CellRangeAddress(0,0,0,7);
        sheet1.addMergedRegion(region);
        row0.createCell(3 + pointAttributeSize + 1).setCellValue("??????");
        //???????????????
        SXSSFRow row1 = sheet1.createRow(1);
        SXSSFCell cell2 = row1.createCell(3);
        cell2.setCellStyle(cellStyle);
        cell2.setCellValue("????????????");
        CellRangeAddress region1 = new CellRangeAddress(1,1,3,3 + pointAttributeSize - 1);
        sheet1.addMergedRegion(region1);
        row1.createCell(3 + pointAttributeSize).setCellValue("?????????");
        SXSSFRow row2 = sheet1.createRow(2);
        row2.createCell(0).setCellValue("??????");
        row2.createCell(1).setCellValue("??????");
        row2.createCell(2).setCellValue("??????");
        for (int i = 3; i < pointAttributeSize + 3; i++) {
            row2.createCell(i).setCellValue(pointAttributeList.get(i - 3));
        }
        row2.createCell(pointAttributeSize + 3).setCellValue("??????");
        for (int i = 0; i < studentNoList.size(); i++) {
            SXSSFRow row = sheet1.createRow(i + 3);
            row.createCell(0).setCellValue(i + 1);
            String studentNo = studentNoList.get(i);
            //??????
            PointExcelDto pointExcelDto = pointExcel.stream().filter(p -> p.getStudentNo().equals(studentNo)).findAny().orElse(null);
            assert pointExcelDto != null;
            row.createCell(1).setCellValue(pointExcelDto.getStudentName());
            //??????
            row.createCell(2).setCellValue(studentNo);
            //??????
            for (int j = 3; j < pointAttributeSize + 3; j++) {
                //?????????????????????
                String pointAttribute = pointAttributeList.get(j - 3);
                //?????????????????????????????????
                List<PointPO> list = pointList.stream().filter(p -> p.getPointAttribute().equals(pointAttribute)).collect(Collectors.toList());
                //??????????????????????????????????????????????????????????????????
                BigDecimal sum = new BigDecimal("0");
                for (PointPO point : list) {
                    String pointName = point.getPointName();
                    PointExcelDto pointExcelDto1 = pointExcel.stream().filter(p -> p.getStudentNo().equals(studentNo) && p.getPointName().equals(pointName)).findAny().orElse(null);
                    assert pointExcelDto1 != null;
                    String pointScore = pointExcelDto1.getPointScore();
                    BigDecimal score = new BigDecimal(pointScore);
                    sum = sum.add(score);
                }
                String sumScore = sum.toString();
                row.createCell(j).setCellValue(sumScore);
            }
            //??????
            row.createCell(pointAttributeSize + 3).setCellValue(pointExcelDto.getStudentScore());
        }

        //sheet2????????????
        SXSSFRow sheet2Row0 = sheet2.createRow(0);
        SXSSFCell sheet2Row0Cell1 = sheet2Row0.createCell(0);
        sheet2Row0Cell1.setCellStyle(cellStyle);
        sheet2Row0Cell1.setCellValue("???????????????Web???????????????????????????");
        SXSSFCell sheet2Row0Cell2 = sheet2Row0.createCell(1);
        sheet2Row0Cell2.setCellStyle(cellStyle);
        sheet2Row0Cell2.setCellValue("???????????????");
        CellRangeAddress region2 = new CellRangeAddress(0,0,1,5);
        sheet2.addMergedRegion(region2);
        SXSSFRow sheet2Row1 = sheet2.createRow(1);
        SXSSFCell sheet2Row1Cell1 = sheet2Row1.createCell(0);
        sheet2Row1Cell1.setCellValue("???????????????");
        sheet2Row1.createCell(1).setCellValue("????????????");
        for (int i = 0; i < pointAttributeSize; i++) {
            sheet2Row1.createCell(i + 2).setCellValue(pointAttributeList.get(i));
        }
        sheet2Row1.createCell(2 + pointAttributeSize).setCellValue("??????");
        HashMap<String, String> map = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        String a = ":???????????????Web????????????????????????????????????????????????????????????????????????????????????Web?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Web??????????????????????????????";
        String b = ":???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Web???????????????????????????????????????????????????????????????????????????????????????????????????????????????";
        String c = ":?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
        String d = ":??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        int count1 = 0;
        for (String s : pointTargetList) {
            map.put(s, list.get(count1));
            count1++;
        }
        for (int i = 0; i < pointTargetList.size(); i++) {
            SXSSFRow sheet2Row = sheet2.createRow(i + 2);
            //???????????????
            String pointTarget = pointTargetList.get(i);
            //????????????
            SXSSFCell cell = sheet2Row.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(pointTarget + map.get(pointTarget));
            sheet2Row.createCell(1).setCellValue("-");
            for (int j = 0; j < pointAttributeSize; j++) {
                //???????????????
                String pointAttribute = pointAttributeList.get(j);
                List<PointPO> points = pointList.stream().filter(p -> p.getPointAttribute().equals(pointAttribute) && p.getPointTarget().equals(pointTarget)).collect(Collectors.toList());
                if (points.size() > 0) {
                    BigDecimal score = new BigDecimal("0");
                    for (PointPO point : points) {
                        BigDecimal pointRatio = point.getPointRatio();
                        score = score.add(pointRatio);
                    }
                    if (score.compareTo(BigDecimal.valueOf(0)) != 0) {
                        sheet2Row.createCell(j + 2).setCellValue(score.multiply(BigDecimal.valueOf(100)).doubleValue());
                    } else {
                        sheet2Row.createCell(j + 2).setCellValue("-");
                    }
                }
            }
            //??????
            List<PointPO> collect = pointList.stream().filter(p -> p.getPointTarget().equals(pointTarget)).collect(Collectors.toList());
            BigDecimal decimal = new BigDecimal("0");
            for (PointPO po : collect) {
                BigDecimal pointRatio = po.getPointRatio();
                decimal = decimal.add(pointRatio);
            }
            sheet2Row.createCell(2 + pointAttributeSize).setCellValue(decimal.multiply(BigDecimal.valueOf(100)).doubleValue());
        }
        SXSSFRow sheet2Row = sheet2.createRow(2 + pointTargetSize);
        sheet2Row.createCell(0).setCellValue("??????");
        sheet2Row.createCell(1).setCellValue(0);
        for (int i = 2; i <= 2 + pointTargetSize; i++) {
            SXSSFCell cell = sheet2Row.createCell(i);
            String colTag = CellReference.convertNumToColString(i);
            //??????????????????
            String formula = "SUM(" + colTag + "3:" + colTag + (2 + pointTargetSize) + ")";
            cell.setCellFormula(formula);
            sheet2.setForceFormulaRecalculation(true);
        }
        //sheet3????????????
        //????????? ?????????
        SXSSFRow sheet3Row1 = sheet3.createRow(0);
        SXSSFCell cell = sheet3Row1.createCell(0);
        cell.setCellValue("??????");
        sheet3Row1.createCell(1).setCellValue("??????");
        sheet3Row1.createCell(2).setCellValue("??????");
        //?????????????????????
        int count = 3;
        int tem = 0;
        for (String attribute : pointAttributeList) {
            //???????????????
            //????????????????????????????????????
            List<PointPO> pointCollect = pointList.stream().filter(p -> p.getPointAttribute().equals(attribute)).collect(Collectors.toList());
            if (pointCollect.size() > 1) {
                for (PointPO pointPO : pointCollect) {
                    sheet3Row1.createCell(count).setCellValue(pointPO.getPointName());
                    count++;
                }
            }
            sheet3Row1.createCell(count).setCellValue(attribute);
            count++;
        }
        //??????
        for (String target : pointTargetList) {
            sheet3Row1.createCell(count).setCellValue(target);
            count++;
        }
        //????????????0.65
        for (String target : pointTargetList) {
            sheet3Row1.createCell(count).setCellValue(target + "> 0.65");
            count++;
        }
        //?????????
        int row = 1;
        for (String studentNo : studentNoList) {
            SXSSFRow row3 = sheet3.createRow(row);
            //??????
            row3.createCell(0).setCellValue(row);
            //??????
            row3.createCell(1).setCellValue(studentNo);
            //??????
            //???????????????????????????
            PointExcelDto pointExcelDto = pointExcel.stream().filter(p -> p.getStudentNo().equals(studentNo)).findAny().orElse(null);
            assert pointExcelDto != null;
            row3.createCell(2).setCellValue(pointExcelDto.getStudentName());
            //??????
            int col = 3;
            for (String attribute : pointAttributeList) {
                //???????????????
                //????????????????????????????????????
                List<PointPO> pointCollect = pointList.stream().filter(p -> p.getPointAttribute().equals(attribute)).collect(Collectors.toList());
                BigDecimal bigDecimal = new BigDecimal("0");
                if (pointCollect.size() > 1) {
                    for (PointPO pointPO : pointCollect) {
                        //????????????????????????????????????
                        String pointName = pointPO.getPointName();
                        PointExcelDto pointExcelDto1 = pointExcel.stream().filter(p -> p.getPointName().equals(pointName) && p.getStudentNo().equals(studentNo)).findAny().orElse(null);
                        assert pointExcelDto1 != null;
                        String pointScore = pointExcelDto1.getPointScore();
                        row3.createCell(col).setCellValue(Double.parseDouble(pointScore));
                        BigDecimal bigDecimal1 = new BigDecimal(pointScore);
                        bigDecimal = bigDecimal.add(bigDecimal1);
                        col++;
                    }
                }
                if (pointCollect.size() == 1) {
                    PointPO pointPO = pointCollect.get(0);
                    String pointName = pointPO.getPointName();
                    PointExcelDto pointExcelDto1 = pointExcel.stream().filter(p -> p.getPointName().equals(pointName) && p.getStudentNo().equals(studentNo)).findAny().orElse(null);
                    assert pointExcelDto1 != null;
                    row3.createCell(col).setCellValue(Double.parseDouble(pointExcelDto1.getPointScore()));
                } else {
                    row3.createCell(col).setCellValue(bigDecimal.doubleValue());
                }
                col++;
            }
            //??????
            for (String target : pointTargetList) {
                BigDecimal scoreTotal = new BigDecimal("0");
                BigDecimal studentTargetScore = new BigDecimal("0");
                List<PointPO> targetCollect = pointList.stream().filter(p -> p.getPointTarget().equals(target)).collect(Collectors.toList());
                for (PointPO point : targetCollect) {
                    BigDecimal pointRatio = point.getPointRatio();
                    BigDecimal multiply = pointRatio.multiply(BigDecimal.valueOf(100));
                    scoreTotal = scoreTotal.add(multiply);
                    String pointName = point.getPointName();
                    PointExcelDto pointExcelDto1 = pointExcel.stream().filter(p -> p.getStudentNo().equals(studentNo) && p.getPointName().equals(pointName)).findAny().orElse(null);
                    assert pointExcelDto1 != null;
                    String pointScore = pointExcelDto1.getPointScore();
                    BigDecimal studentScore = new BigDecimal(pointScore);
                    studentTargetScore = studentTargetScore.add(studentScore);
                }
                BigDecimal divide = studentTargetScore.divide(scoreTotal, 4, BigDecimal.ROUND_HALF_DOWN);
                row3.createCell(col).setCellValue(divide.doubleValue());
                int result = divide.divide(BigDecimal.valueOf(0.65), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue() > 1 ? 1 : 0;
                row3.createCell(col + pointTargetSize).setCellValue(result);
                tem = col;
                col++;
            }
            row++;
        }
        SXSSFRow row3 = sheet3.createRow(1 + studentNoList.size());
        row3.createCell(0).setCellValue("??????:");
        row3.createCell(1).setCellValue("");
        for (int i = tem + 1; i <= tem + pointTargetSize; i++) {
            SXSSFCell cell11 = row3.createCell(i);
            String colTag = CellReference.convertNumToColString(i);
            //??????2?????????
            String formula = "COUNTIF(" + colTag + "2:" + colTag + (studentNoList.size() + 1) + ",1)";
            cell11.setCellFormula(formula);
        }
        SXSSFRow row4 = sheet3.createRow(2 + studentNoList.size());
        row4.createCell(0).setCellValue("??????");
        for (int i = tem + 1; i <= tem + pointTargetSize; i++) {
            SXSSFCell cell22 = row4.createCell(i);
            String colTag = CellReference.convertNumToColString(i);
            String formula = colTag + (studentNoList.size() + 2) + "/" + studentNoList.size();
            cell22.setCellFormula(formula);
        }
        SXSSFRow rowRow1 = sheet4.createRow(0);
        //??????????????????????????????
        List<String> className = new ArrayList<>();
        if (dto.getClassCodeList() != null) {
            List<String> classCodeList = dto.getClassCodeList();
            for (String s : classCodeList) {
                QueryWrapper<ClassInfoPO> classInfoPOQueryWrapper = new QueryWrapper<>();
                classInfoPOQueryWrapper.eq("class_code", s);
                ClassInfoPO classInfoPO = classInfoMapper.selectOne(classInfoPOQueryWrapper);
                className.add(classInfoPO.getClassName());
            }
        } else {
            String teacherCode = dto.getTeacherCode();
            QueryWrapper<CourseClassTeacherPO> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("teacher_code", teacherCode);
            List<CourseClassTeacherPO> courseClassTeacherPOS = courseClassTeacherMapper.selectList(wrapper1);
            List<String> classCodeList = courseClassTeacherPOS.stream().map(CourseClassTeacherPO::getClassCode).collect(Collectors.toList());
            for (String classCode : classCodeList) {
                QueryWrapper<ClassInfoPO> classInfoPOQueryWrapper = new QueryWrapper<>();
                classInfoPOQueryWrapper.eq("class_code", classCode);
                ClassInfoPO classInfoPO = classInfoMapper.selectOne(classInfoPOQueryWrapper);
                className.add(classInfoPO.getClassName());
            }
        }
        int nameIndex = 1;
        for (String name : className) {
            rowRow1.createCell(nameIndex).setCellValue(name);
            nameIndex++;
        }
        SXSSFRow sheet4Row1 = sheet4.createRow(1);
        int index = 1;
        for (String pointName : pointNameList) {
            sheet4Row1.createCell(index).setCellValue(pointName);
            index++;
        }
        int rowIndex = 2;
        for (String pointTarget : pointTargetList) {
            //?????????
            SXSSFRow sheet4Row = sheet4.createRow(rowIndex);
            //?????????????????????
            sheet4Row.createCell(0).setCellValue(pointTarget);
            //?????????,???????????????????????????????????????????????????????????????
            int colIndex = 1;//???????????????
            for (String pointName : pointNameList) {
                BigDecimal score = new BigDecimal("0");
                long size = 0;
                //??????????????????????????????
                String pp = pointList.stream().filter(p -> p.getPointName().equals(pointName)).map(PointPO::getPointTarget).findAny().orElse(null);
                //???????????????????????????????????????????????????????????????????????????????????????
                if (pointTarget.equals(pp)) {
                    List<String> collect = pointExcel.stream().filter(p -> p.getPointName().equals(pointName)).map(PointExcelDto::getPointScore).collect(Collectors.toList());
                    for (String s : collect) {
                        BigDecimal temScore = new BigDecimal(s);
                        score = score.add(temScore);
                    }
                    size = collect.size();
                }
                if (size > 0) {
                    BigDecimal divide = score.divide(BigDecimal.valueOf(size), 9, BigDecimal.ROUND_HALF_DOWN);
                    sheet4Row.createCell(colIndex).setCellValue(divide.doubleValue());
                } else {
                    sheet4Row.createCell(colIndex).setCellValue("");
                }
                colIndex++;
            }
            rowIndex++;
            //?????????
            String colStart = CellReference.convertNumToColString(2);
            //????????????
            String colEnd = CellReference.convertNumToColString(pointNameSize);
            String formula = "SUM(" + colStart + rowIndex + ":" + colEnd + rowIndex + ")";
            SXSSFCell cell3 = sheet4Row.createCell(pointNameSize + 1);
            cell3.setCellFormula(formula);
        }
        //sheet4??????
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        int nextIndex = 5 + pointTargetSize;
        SXSSFRow row5 = sheet4.createRow(nextIndex);
        nextIndex++;
        row5.createCell(1).setCellValue("????????????");
        row5.createCell(2).setCellValue("????????????");
        row5.createCell(3).setCellValue("?????????");
        int m = 3;
        for (String s : pointTargetList) {
            SXSSFRow row6 = sheet4.createRow(nextIndex);
            //?????????
            row6.createCell(0).setCellValue(s);
            //????????????
            //?????????
            String colStart = CellReference.convertNumToColString(2);
            //????????????
            String colEnd = CellReference.convertNumToColString(pointNameSize);
            String formula = "SUM(" + colStart + m + ":" + colEnd + m + ")";
            SXSSFCell cell3 = row6.createCell(1);
            cell3.setCellFormula(formula);
            //??????
            List<PointPO> collect = pointList.stream().filter(p -> p.getPointTarget().equals(s)).collect(Collectors.toList());
            BigDecimal decimal = new BigDecimal("0");
            for (PointPO po : collect) {
                BigDecimal pointRatio = po.getPointRatio();
                decimal = decimal.add(pointRatio);
            }
            row6.createCell(2).setCellValue(decimal.multiply(BigDecimal.valueOf(100)).doubleValue());
            //?????????
            String colStart1 = CellReference.convertNumToColString(1);
            //????????????
            String colEnd2 = CellReference.convertNumToColString(2);
            String formula1 = colStart1 + (nextIndex + 1) + "/" + colEnd2 + (nextIndex + 1);
            SXSSFCell cell4 = row6.createCell(3);
            cell4.setCellFormula(formula1);
            cell4.setCellStyle(cellStyle1);

            m++;
            nextIndex++;
        }
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        //????????????
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        SXSSFRow row6 = sheet4.createRow(nextIndex);
        SXSSFCell cell3 = row6.createCell(1);
        cell3.setCellStyle(cellStyle2);
        cell3.setCellValue("???????????????");
        CellRangeAddress region3 = new CellRangeAddress(nextIndex,nextIndex,1,2);
        sheet4.addMergedRegion(region3);
        SXSSFCell cell4 = row6.createCell(3);
        String rowCol = CellReference.convertNumToColString(3);
        String formula1 = "SUM(" + rowCol + (nextIndex - pointTargetSize + 1) + ":" + rowCol + (nextIndex) + ")/" + pointTargetSize;
        cell4.setCellFormula(formula1);
        cell4.setCellStyle(cellStyle1);


        sheet3.setForceFormulaRecalculation(true);
        sheet4.setForceFormulaRecalculation(true);
        workbook.setForceFormulaRecalculation(true);
        FileOutputStream fileOutputStream = new FileOutputStream("D:/zhsj/uploadPath/" + "?????????????????????.xlsx");
        workbook.write(fileOutputStream);
        workbook.dispose();
        fileOutputStream.close();

    }

    @Override
    public void achievementReport(HttpServletResponse response, ExcelQueryDto dto) throws IOException, InvalidFormatException {

        List<PointExcelDto> pointExcel = new ArrayList<>();
        List<RatePO> ratePOS = new ArrayList<>();
        if (dto.getClassCode() != null) {
            List<PointExcelDto> pointExcelDtos = pointDetailMapper.listPointExcel(dto);
            pointExcel.addAll(pointExcelDtos);
            QueryWrapper<RatePO> ratePOQueryWrapper = new QueryWrapper<>();
            ratePOQueryWrapper.eq("class_code", dto.getClassCode());
            List<RatePO> ratePOS1 = rateMapper.selectList(ratePOQueryWrapper);
            ratePOS.addAll(ratePOS1);
        } else if (dto.getClassCodeList() != null && dto.getClassCodeList().size() > 0) {
            for (String s : dto.getClassCodeList()) {
                ExcelQueryDto excelQueryDto = new ExcelQueryDto();
                excelQueryDto.setClassCode(s);
                List<PointExcelDto> pointExcelDtoList = pointDetailMapper.listPointExcel(excelQueryDto);
                pointExcel.addAll(pointExcelDtoList);
                QueryWrapper<RatePO> ratePOQueryWrapper = new QueryWrapper<>();
                ratePOQueryWrapper.eq("class_code", s);
                List<RatePO> ratePOS1 = rateMapper.selectList(ratePOQueryWrapper);
                ratePOS.addAll(ratePOS1);
            }
        } else {
            //?????????????????????????????????????????????
            String teacherCode = dto.getTeacherCode();
            QueryWrapper<CourseClassTeacherPO> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("teacher_code", teacherCode);
            List<CourseClassTeacherPO> courseClassTeacherPOS = courseClassTeacherMapper.selectList(wrapper1);
            List<String> classCodeList = courseClassTeacherPOS.stream().map(CourseClassTeacherPO::getClassCode).collect(Collectors.toList());
            for (String classCode : classCodeList) {
                ExcelQueryDto excelQueryDto = new ExcelQueryDto();
                excelQueryDto.setClassCode(classCode);
                List<PointExcelDto> pointExcelDtoList = pointDetailMapper.listPointExcel(excelQueryDto);
                pointExcel.addAll(pointExcelDtoList);
                QueryWrapper<RatePO> ratePOQueryWrapper = new QueryWrapper<>();
                ratePOQueryWrapper.eq("class_code", classCode);
                List<RatePO> ratePOS1 = rateMapper.selectList(ratePOQueryWrapper);
                ratePOS.addAll(ratePOS1);
            }
        }
        //??????????????????????????????
        List<String> studentNoList = pointExcel.stream().map(PointExcelDto::getStudentNo).distinct().collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        map.put("xueqi", dto.getXueqi());
        map.put("date", simpleDateFormat.format(new Date()));
        map.put("name", dto.getName());
        map.put("grad", dto.getGrad());
        map.put("count", studentNoList.size());
        map.put("pers", dto.getPers());
        map.put("info", "???????????????Web????????????????????????????????????????????????????????????????????????????????????Web?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Web??????????????????????????????");
        //????????????1?????????
        BigDecimal tarOne = new BigDecimal("0");
        BigDecimal tarTow = new BigDecimal("0");
        BigDecimal tarThree = new BigDecimal("0");
        BigDecimal tarFour = new BigDecimal("0");
        //??????1??????1?????????
        long size11 = ratePOS.stream().filter(r -> r.getQuestionOne() == 1).count();
        long size12 = ratePOS.stream().filter(r -> r.getQuestionTow() == 1).count();
        long size13 = ratePOS.stream().filter(r -> r.getQuestionThree() == 1).count();
        long size14 = ratePOS.stream().filter(r -> r.getQuestionFour() == 1).count();
        long size15 = ratePOS.stream().filter(r -> r.getQuestionFive() == 1).count();
        long size16 = ratePOS.stream().filter(r -> r.getQuestionSix() == 1).count();
        long size17 = ratePOS.stream().filter(r -> r.getQuestionSeven() == 1).count();
        long size18 = ratePOS.stream().filter(r -> r.getQuestionEight() == 1).count();
        long size21 = ratePOS.stream().filter(r -> r.getQuestionOne() == 2).count();
        long size22 = ratePOS.stream().filter(r -> r.getQuestionTow() == 2).count();
        long size23 = ratePOS.stream().filter(r -> r.getQuestionThree() == 2).count();
        long size24 = ratePOS.stream().filter(r -> r.getQuestionFour() == 2).count();
        long size25 = ratePOS.stream().filter(r -> r.getQuestionFive() == 2).count();
        long size26 = ratePOS.stream().filter(r -> r.getQuestionSix() == 2).count();
        long size27 = ratePOS.stream().filter(r -> r.getQuestionSeven() == 2).count();
        long size28 = ratePOS.stream().filter(r -> r.getQuestionEight() == 2).count();
        long size31 = ratePOS.stream().filter(r -> r.getQuestionOne() == 3).count();
        long size32 = ratePOS.stream().filter(r -> r.getQuestionTow() == 3).count();
        long size33 = ratePOS.stream().filter(r -> r.getQuestionThree() == 3).count();
        long size34 = ratePOS.stream().filter(r -> r.getQuestionFour() == 3).count();
        long size35 = ratePOS.stream().filter(r -> r.getQuestionFive() == 3).count();
        long size36 = ratePOS.stream().filter(r -> r.getQuestionSix() == 3).count();
        long size37 = ratePOS.stream().filter(r -> r.getQuestionSeven() == 3).count();
        long size38 = ratePOS.stream().filter(r -> r.getQuestionEight() == 3).count();
        long size41 = ratePOS.stream().filter(r -> r.getQuestionOne() == 4).count();
        long size42 = ratePOS.stream().filter(r -> r.getQuestionTow() == 4).count();
        long size43 = ratePOS.stream().filter(r -> r.getQuestionThree() == 4).count();
        long size44 = ratePOS.stream().filter(r -> r.getQuestionFour() == 4).count();
        long size45 = ratePOS.stream().filter(r -> r.getQuestionFive() == 4).count();
        long size46 = ratePOS.stream().filter(r -> r.getQuestionSix() == 4).count();
        long size47 = ratePOS.stream().filter(r -> r.getQuestionSeven() == 4).count();
        long size48 = ratePOS.stream().filter(r -> r.getQuestionEight() == 4).count();

        if (ratePOS.size() > 0) {
            int size = ratePOS.size();
            BigDecimal bigSize = BigDecimal.valueOf(size);
            BigDecimal one = BigDecimal.valueOf(size11 * 0.45 + size21 * 0.65 + size31 * 0.75 + size41 * 0.85).divide(bigSize, 4,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal tow = BigDecimal.valueOf(size12 * 0.45 + size22 * 0.65 + size32 * 0.75 + size42 * 0.85).divide(bigSize,4,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal three = BigDecimal.valueOf(size13 * 0.45 + size23 * 0.65 + size33 * 0.75 + size43 * 0.85).divide(bigSize, 4,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal four = BigDecimal.valueOf(size14 * 0.45 + size24 * 0.65 + size34 * 0.75 + size44 * 0.85).divide(bigSize, 4,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal five = BigDecimal.valueOf(size15 * 0.45 + size25 * 0.65 + size35 * 0.75 + size45 * 0.85).divide(bigSize, 4,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal six = BigDecimal.valueOf(size16 * 0.45 + size26 * 0.65 + size36 * 0.75 + size46 * 0.85).divide(bigSize, 4,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal seven = BigDecimal.valueOf(size17 * 0.45 + size27 * 0.65 + size37 * 0.75 + size47 * 0.85).divide(bigSize, 4,BigDecimal.ROUND_HALF_DOWN);
            BigDecimal eight = BigDecimal.valueOf(size18 * 0.45 + size28 * 0.65 + size38 * 0.75 + size48 * 0.85).divide(bigSize, 4,BigDecimal.ROUND_HALF_DOWN);
            tarOne = (one.add(tow)).divide(BigDecimal.valueOf(2),4,BigDecimal.ROUND_HALF_DOWN);
            tarTow = (three.add(four)).divide(BigDecimal.valueOf(2),4,BigDecimal.ROUND_HALF_DOWN);
            tarThree = (five.add(six)).divide(BigDecimal.valueOf(2),4,BigDecimal.ROUND_HALF_DOWN);
            tarFour = (seven.add(eight)).divide(BigDecimal.valueOf(2),4,BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("tarOne", tarOne);
        map.put("tarTow", tarTow);
        map.put("tarThree", tarThree);
        map.put("tarFour", tarFour);
        //??????1?????????????????????
        BigDecimal one = new BigDecimal("0");
        List<PointExcelDto> collect1 = pointExcel.stream().filter(p -> p.getPointName().equals("????????????-??????1")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect1) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            one = one.add(tem);
        }
        int oneSize = collect1.size();
        if (oneSize > 0) {
            one = one.divide(BigDecimal.valueOf(oneSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("one", one);
        //????????????-??????1
        BigDecimal tow = new BigDecimal("0");
        List<PointExcelDto> collect2 = pointExcel.stream().filter(p -> p.getPointName().equals("????????????-??????1")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect2) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            tow = tow.add(tem);
        }
        int towSize = collect2.size();
        if (towSize > 0) {
            tow = tow.divide(BigDecimal.valueOf(towSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("tow", tow);
        //?????????-??????1
        BigDecimal three = new BigDecimal("0");
        List<PointExcelDto> collect3 = pointExcel.stream().filter(p -> p.getPointName().equals("?????????-??????1")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect3) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            three = three.add(tem);
        }
        int threeSize = collect3.size();
        if (threeSize > 0) {
            three = three.divide(BigDecimal.valueOf(threeSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("three", three);
        //????????????????????????
        BigDecimal firstTar = new BigDecimal("0");
        firstTar = firstTar.add(one).add(tow).add(three).divide(BigDecimal.valueOf(50), 4, BigDecimal.ROUND_HALF_DOWN);
        map.put("firstTar", firstTar);

        //????????????-??????2
        BigDecimal four = new BigDecimal("0");
        List<PointExcelDto> collect4 = pointExcel.stream().filter(p -> p.getPointName().equals("????????????-??????2")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect4) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            four = four.add(tem);
        }
        int fourSize = collect4.size();
        if (fourSize > 0) {
            four = four.divide(BigDecimal.valueOf(fourSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("four", four);
        //????????????-??????2
        BigDecimal five = new BigDecimal("0");
        List<PointExcelDto> collect5 = pointExcel.stream().filter(p -> p.getPointName().equals("????????????-??????2")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect5) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            five = five.add(tem);
        }
        int fiveSize = collect5.size();
        if (fiveSize > 0) {
            five = five.divide(BigDecimal.valueOf(fiveSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("five", five);

        BigDecimal secondTar = new BigDecimal("0");
        secondTar = secondTar.add(four).add(five).divide(BigDecimal.valueOf(10), 4, BigDecimal.ROUND_HALF_DOWN);
        map.put("secondTar", secondTar);

        //????????????-??????3
        BigDecimal six = new BigDecimal("0");
        List<PointExcelDto> collect6 = pointExcel.stream().filter(p -> p.getPointName().equals("????????????-??????3")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect6) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            six = six.add(tem);
        }
        int sixSize = collect6.size();
        if (sixSize > 0) {
            six = six.divide(BigDecimal.valueOf(sixSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("six", six);
        //????????????-??????3
        BigDecimal seven = new BigDecimal("0");
        List<PointExcelDto> collect7 = pointExcel.stream().filter(p -> p.getPointName().equals("????????????-??????3")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect7) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            seven = seven.add(tem);
        }
        int sevenSize = collect7.size();
        if (sevenSize > 0) {
            seven = seven.divide(BigDecimal.valueOf(sevenSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("seven", seven);
        //????????????-??????3
        BigDecimal eight = new BigDecimal("0");
        List<PointExcelDto> collect8 = pointExcel.stream().filter(p -> p.getPointName().equals("????????????-??????3")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect8) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            eight = eight.add(tem);
        }
        int eightSize = collect8.size();
        if (eightSize > 0) {
            eight = eight.divide(BigDecimal.valueOf(eightSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("eight", eight);

        BigDecimal thirdTar = new BigDecimal("0");
        thirdTar = thirdTar.add(six).add(seven).add(eight).divide(BigDecimal.valueOf(18), 4, BigDecimal.ROUND_HALF_DOWN);
        map.put("thirdTar", thirdTar);

        //????????????-??????4
        BigDecimal nine = new BigDecimal("0");
        List<PointExcelDto> collect9 = pointExcel.stream().filter(p -> p.getPointName().equals("????????????-??????4")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect9) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            nine = nine.add(tem);
        }
        int nineSize = collect9.size();
        if (nineSize > 0) {
            nine = nine.divide(BigDecimal.valueOf(nineSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("nine", nine);
        //????????????-??????4
        BigDecimal ten = new BigDecimal("0");
        List<PointExcelDto> collect10 = pointExcel.stream().filter(p -> p.getPointName().equals("????????????-??????4")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect10) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            ten = ten.add(tem);
        }
        int tenSize = collect10.size();
        if (tenSize > 0) {
            ten = ten.divide(BigDecimal.valueOf(tenSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("ten", ten);
        //?????????-??????4
        BigDecimal eleven = new BigDecimal("0");
        List<PointExcelDto> collect11 = pointExcel.stream().filter(p -> p.getPointName().equals("?????????-??????4")).collect(Collectors.toList());
        for (PointExcelDto pointExcelDto : collect11) {
            String pointScore = pointExcelDto.getPointScore();
            BigDecimal tem = new BigDecimal(pointScore);
            eleven = eleven.add(tem);
        }
        int elevenSize = collect11.size();
        if (elevenSize > 0) {
            eleven = eleven.divide(BigDecimal.valueOf(elevenSize), 4, BigDecimal.ROUND_HALF_DOWN);
        }
        map.put("eleven", eleven);

        BigDecimal firthTar = new BigDecimal("0");
        firthTar = firthTar.add(nine).add(ten).add(eleven).divide(BigDecimal.valueOf(22), 4, BigDecimal.ROUND_HALF_DOWN);
        map.put("firthTar", firthTar);



        try {
            XWPFDocument doc = WordExportUtil.exportWord07(
                    "D:/zhsj/uploadPath/model/??????.docx", map);
            FileOutputStream fos = new FileOutputStream("D:/zhsj/uploadPath/model/report.docx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("report.docx","ISO8859-1"));
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
