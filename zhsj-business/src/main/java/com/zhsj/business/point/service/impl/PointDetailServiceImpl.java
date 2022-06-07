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

    @Override
    public List<ExcelDto> listExcel(ExcelQueryDto dto) {
        List<ExcelDto> excelDtos = new LinkedList<>();
        List<PointExcelDto> pointExcelDtos = pointDetailMapper.listPointExcel(dto);
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        List<PointPO> list = pointMapper.selectList(wrapper);
        List<String> studentNo = pointExcelDtos.stream().map(PointExcelDto::getStudentNo).distinct().collect(Collectors.toList());
        List<String> pointName = pointExcelDtos.stream().map(PointExcelDto::getPointName).distinct().collect(Collectors.toList());
        int size = list.size();
        // voucher是目标对象
        try{
            //excel标题处理
            Class<?> c = Class.forName("com.zhsj.business.point.dto.ExcelDto");
            //通过getDeclaredFields()方法获取对象类中的所有属性（含私有）
            ExcelDto excelDto = new ExcelDto();
            excelDto.setField1("班级");
            excelDto.setField2("学号");
            excelDto.setField3("姓名");
            excelDto.setField4("总分");
            Field[] fields = excelDto.getClass().getDeclaredFields();
            Method[] methods = excelDto.getClass().getMethods();//拿到函数成员
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
            //excel分数处理
            for (int i = 0; i < studentNo.size(); i++) {
                ExcelDto excelDtoTem = new ExcelDto();
                //获取学号
                String no = studentNo.get(i);
                PointExcelDto pointExcelDto1 = pointExcelDtos.stream().filter(p -> p.getStudentNo().equals(no)).findAny().orElse(null);
                assert pointExcelDto1 != null;
                excelDtoTem.setField1(pointExcelDto1.getClassName());
                excelDtoTem.setField2(pointExcelDto1.getStudentNo());
                excelDtoTem.setField3(pointExcelDto1.getStudentName());
                excelDtoTem.setField4(pointExcelDto1.getStudentScore());
                for (int j = 0; j < size; j++) {
                    //获取评分点名称
                    String point = pointName.get(j);
                    //获取评分点分数
                    PointExcelDto pointExcelDto2 = pointExcelDtos.stream().filter(p -> p.getStudentNo().equals(no) && p.getPointName().equals(point)).findAny().orElse(null);
                    assert pointExcelDto2 != null;
                    String pointScore = pointExcelDto2.getPointScore();
                    //获取要赋值的字段名，调用方法
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
            //处理异常
        }
        return excelDtos;
    }

    @Override
    public PointComments getPointCommentsByNo(String studentNo) {
        PointComments pointComments = new PointComments();
        QueryWrapper<StudentPO> studentWrapper = new QueryWrapper<>();
        studentWrapper.eq("student_no", studentNo);
        StudentPO student = studentMapper.selectOne(studentWrapper);
        //学生学号
        pointComments.setStudentNo(studentNo);
        QueryWrapper<ClassInfoPO> classWrapper = new QueryWrapper<>();
        classWrapper.eq("class_code", student.getStudentClass());
        ClassInfoPO classInfo = classInfoMapper.selectOne(classWrapper);
        //学生班级
        pointComments.setClassName(classInfo.getClassName());
        //学生姓名
        pointComments.setStudentName(student.getStudentName());
        //根据学号获得题目
        QueryWrapper<ManualPO> manualWrapper = new QueryWrapper<>();
        manualWrapper.eq("student_no", studentNo);
        ManualPO manual = manualMapper.selectOne(manualWrapper);
        pointComments.setProjectName(manual.getManualName());
        //根据学号获得功能模块
        pointComments.setModuleName(manual.getFunctionalModule());
        //考勤
        QueryWrapper<KaoQinPO> kaoQinWrapper = new QueryWrapper<>();
        kaoQinWrapper.eq("student_no", studentNo);
        List<KaoQinPO> kaoQin= kaoQinMapper.selectList(kaoQinWrapper);
        if (kaoQin.size() > 0) {
            pointComments.setAttendance("基本");
        } else {
            pointComments.setAttendance("");
        }
        //分数
        ExcelQueryDto dto = new ExcelQueryDto();
        dto.setStudentNo(studentNo);
        List<PointExcelDto> pointExcelDtos = pointDetailMapper.listPointExcel(dto);
        //总分
        String studentScore = pointExcelDtos.get(0).getStudentScore();
        pointComments.setStudentScore(studentScore);
        double score = new Double(studentScore);
        if(score > 84.99) {
            pointComments.setGrade("优秀");
            pointComments.setAttitude("认真、积极");
        } else if(score > 74.99) {
            pointComments.setGrade("良好");
            pointComments.setAttitude("认真");
        } else if(score > 59.99) {
            pointComments.setGrade("及格");
            pointComments.setAttitude("较好");
        } else {
            pointComments.setGrade("不及格");
            pointComments.setAttitude("较差");
        }
        //功能完成程度

        //查询出所有的评分点信息
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        List<PointPO> pointPOS = pointMapper.selectList(wrapper);
        //筛选出共有几种规范
        List<String> pointAttributes = pointPOS.stream().map(PointPO::getPointAttribute).distinct().collect(Collectors.toList());
        //根据规范筛选出评分点
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
        //根据每个规范集合计算出分数点
        int size = pointAttributes.size();
        for (int i = 0; i < size; i++) {
            //一个规范所有的评分点
            List<String> pointList = map.get(pointAttributes.get(i));
            //获得总分
            double pointScore = new Double("0");
            //第一个规范的标准分值
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
            if (pointAttributes.get(i).equals("团队成绩")) {
                if(pointScore >= wscore1) {
                    pointComments.setWorkLoad("饱满");
                    pointComments.setCompleteness("已较好实现大部分功能");
                    pointComments.setKnowledge("良好");
                    pointComments.setAbility("良好");
                    pointComments.setAnalysis("独立工作和分析解决问题的能力较好");
                    pointComments.setOnTime("能够按时");
                } else if(pointScore >= wscore2) {
                    pointComments.setWorkLoad("较饱满");
                    pointComments.setCompleteness("大部分功能实现正确");
                    pointComments.setKnowledge("较好");
                    pointComments.setAbility("较好");
                    pointComments.setAnalysis("具有一定的独立工作和分析解决问题的能力");
                    pointComments.setOnTime("能够按时");
                } else if(pointScore >= wscore3) {
                    pointComments.setWorkLoad("偏少");
                    pointComments.setCompleteness("功能实现一般");
                    pointComments.setKnowledge("一般");
                    pointComments.setAbility("一般");
                    pointComments.setAnalysis("独立工作和分析解决问题的能力待提高");
                    pointComments.setOnTime("基本");
                } else {
                    pointComments.setWorkLoad("欠缺");
                    pointComments.setCompleteness("功能实现不完整");
                    pointComments.setKnowledge("较差");
                    pointComments.setAbility("不足");
                    pointComments.setAnalysis("独立工作和分析解决问题的能力较差");
                    pointComments.setOnTime("不能按时");
                }
            } else if (pointAttributes.get(i).equals("说明书成绩")) {
                if (pointScore >= wscore1) {
                    pointComments.setFormat("规范");
                } else if (pointScore >= wscore2) {
                    pointComments.setFormat("较规范");
                } else if (pointScore >= wscore3) {
                    pointComments.setFormat("基本规范");
                } else {
                    pointComments.setFormat("不规范");
                }
            }

        }
//        PointExcelDto workLoad = pointExcelDtos.stream().filter(pointExcelDto -> pointExcelDto.getPointName().equals("功能完成程度")).findAny().orElse(null);
//        assert workLoad != null;
//        String workScore = workLoad.getPointScore();
//        double work = new Double(workScore);
//        QueryWrapper<PointPO> pointWrapper = new QueryWrapper<>();
//        List<PointPO> points = pointMapper.selectList(pointWrapper);
//        PointPO point = points.stream().filter(p -> p.getPointName().equals("功能完成程度")).findAny().orElse(null);
//        assert point != null;
//        BigDecimal pointRatio = point.getPointRatio();
//        BigDecimal multiply1 = pointRatio.multiply(new BigDecimal("100"));
//        double wscore1 = multiply1.doubleValue();
//        BigDecimal multiply2 = pointRatio.multiply(new BigDecimal("85"));
//        double wscore2 = multiply2.doubleValue();
//        BigDecimal multiply3 = pointRatio.multiply(new BigDecimal("60"));
//        double wscore3 = multiply3.doubleValue();
//        if(work >= wscore1) {
//            pointComments.setWorkLoad("饱满");
//            pointComments.setCompleteness("已较好实现大部分功能");
//            pointComments.setKnowledge("良好");
//            pointComments.setAbility("良好");
//            pointComments.setAnalysis("独立工作和分析解决问题的能力较好");
//            pointComments.setOnTime("能够按时");
//        } else if(work >= wscore2) {
//            pointComments.setWorkLoad("较饱满");
//            pointComments.setCompleteness("大部分功能实现正确");
//            pointComments.setKnowledge("较好");
//            pointComments.setAbility("较好");
//            pointComments.setAnalysis("具有一定的独立工作和分析解决问题的能力");
//            pointComments.setOnTime("能够按时");
//        } else if(work >= wscore3) {
//            pointComments.setWorkLoad("偏少");
//            pointComments.setCompleteness("功能实现一般");
//            pointComments.setKnowledge("一般");
//            pointComments.setAbility("一般");
//            pointComments.setAnalysis("独立工作和分析解决问题的能力待提高");
//            pointComments.setOnTime("基本");
//        } else {
//            pointComments.setWorkLoad("欠缺");
//            pointComments.setCompleteness("功能实现不完整");
//            pointComments.setKnowledge("较差");
//            pointComments.setAbility("不足");
//            pointComments.setAnalysis("独立工作和分析解决问题的能力较差");
//            pointComments.setOnTime("不能按时");
//        }
//        //规范程度
//        PointExcelDto format = pointExcelDtos.stream().filter(pointExcelDto -> pointExcelDto.getPointName().equals("规范程度")).findAny().orElse(null);
//        assert format != null;
//        String formatScore = format.getPointScore();
//        double fscore = new Double(formatScore);
//        PointPO point2 = points.stream().filter(p -> p.getPointName().equals("规范程度")).findAny().orElse(null);
//        assert point2 != null;
//        BigDecimal pointRatio2 = point2.getPointRatio();
//        BigDecimal multiply12 = pointRatio2.multiply(new BigDecimal("100"));
//        double wscore12 = multiply12.doubleValue();
//        BigDecimal multiply22 = pointRatio2.multiply(new BigDecimal("85"));
//        double wscore22 = multiply22.doubleValue();
//        BigDecimal multiply33 = pointRatio2.multiply(new BigDecimal("60"));
//        double wscore33 = multiply33.doubleValue();
//        if (fscore >= wscore12) {
//            pointComments.setFormat("规范");
//        } else if (fscore >= wscore22) {
//            pointComments.setFormat("较规范");
//        } else if (fscore >= wscore33) {
//            pointComments.setFormat("基本规范");
//        } else {
//            pointComments.setFormat("不规范");
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
            //根据教师查询出管理的哪几个班级
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

        //1、创建工作瀑,会生成临时文件速度快
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        //2、创建工作表
        SXSSFSheet sheet1 = workbook.createSheet("学生成绩");
        SXSSFSheet sheet2 = workbook.createSheet("分值分布");
        SXSSFSheet sheet3 = workbook.createSheet("得分情况");
        SXSSFSheet sheet4 = workbook.createSheet("达成度");

        CellStyle cellStyle = workbook.createCellStyle();
        //垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置字体
        //cellStyle.setFont();
        //设置自动换行
        cellStyle.setWrapText(true);

        //sheet1学生成绩
        QueryWrapper<PointPO> wrapper = new QueryWrapper<>();
        //评分点内容
        List<PointPO> pointList = pointMapper.selectList(wrapper);
        //查询出的学生得分信息
        List<String> studentNoList = pointExcel.stream().map(PointExcelDto::getStudentNo).distinct().collect(Collectors.toList());
        //评分点的具体名称
        List<String> pointNameList = pointList.stream().map(PointPO::getPointName).distinct().collect(Collectors.toList());
        //评分点属性
        List<String> pointAttributeList = pointList.stream().map(PointPO::getPointAttribute).distinct().collect(Collectors.toList());
        //所属目标
        List<String> pointTargetList = pointList.stream().map(PointPO::getPointTarget).distinct().collect(Collectors.toList());
        //属性个数
        int pointAttributeSize = pointAttributeList.size();
        //评分点个数
        int pointNameSize = pointNameList.size();
        //目标个数
        int pointTargetSize = pointTargetList.size();


        for (int i = 0; i < 30; i++) {
            sheet1.setColumnWidth(i,sheet1.getColumnWidth(i)*14/10);
            sheet1.trackAllColumnsForAutoSizing();// 跟踪所有用于自动调整大小的列
            sheet1.autoSizeColumn(i);// 让列宽随着导出的列长自动适应
        }
        for (int i = 0; i < 30; i++) {
            sheet2.setColumnWidth(i,sheet1.getColumnWidth(i)*19/10);
            sheet2.trackAllColumnsForAutoSizing();// 跟踪所有用于自动调整大小的列
            sheet2.autoSizeColumn(i);// 让列宽随着导出的列长自动适应
        }
        for (int i = 0; i < 30; i++) {
            sheet3.setColumnWidth(i,sheet1.getColumnWidth(i)*14/10);
            sheet3.trackAllColumnsForAutoSizing();// 跟踪所有用于自动调整大小的列
            sheet3.autoSizeColumn(i);// 让列宽随着导出的列长自动适应
        }
        for (int i = 0; i < 30; i++) {
            sheet4.setColumnWidth(i,sheet1.getColumnWidth(i)*14/10);
            sheet4.trackAllColumnsForAutoSizing();// 跟踪所有用于自动调整大小的列
            sheet4.autoSizeColumn(i);// 让列宽随着导出的列长自动适应
        }

        //第一行处理
        SXSSFRow row0 = sheet1.createRow(0);
        SXSSFCell cell1 = row0.createCell(0);
        cell1.setCellStyle(cellStyle);
        cell1.setCellValue("课程设计成绩");
        CellRangeAddress region = new CellRangeAddress(0,0,0,7);
        sheet1.addMergedRegion(region);
        row0.createCell(3 + pointAttributeSize + 1).setCellValue("备注");
        //第二行处理
        SXSSFRow row1 = sheet1.createRow(1);
        SXSSFCell cell2 = row1.createCell(3);
        cell2.setCellStyle(cellStyle);
        cell2.setCellValue("答辩成绩");
        CellRangeAddress region1 = new CellRangeAddress(1,1,3,3 + pointAttributeSize - 1);
        sheet1.addMergedRegion(region1);
        row1.createCell(3 + pointAttributeSize).setCellValue("总成绩");
        SXSSFRow row2 = sheet1.createRow(2);
        row2.createCell(0).setCellValue("序号");
        row2.createCell(1).setCellValue("姓名");
        row2.createCell(2).setCellValue("学号");
        for (int i = 3; i < pointAttributeSize + 3; i++) {
            row2.createCell(i).setCellValue(pointAttributeList.get(i - 3));
        }
        row2.createCell(pointAttributeSize + 3).setCellValue("总分");
        for (int i = 0; i < studentNoList.size(); i++) {
            SXSSFRow row = sheet1.createRow(i + 3);
            row.createCell(0).setCellValue(i + 1);
            String studentNo = studentNoList.get(i);
            //姓名
            PointExcelDto pointExcelDto = pointExcel.stream().filter(p -> p.getStudentNo().equals(studentNo)).findAny().orElse(null);
            assert pointExcelDto != null;
            row.createCell(1).setCellValue(pointExcelDto.getStudentName());
            //学号
            row.createCell(2).setCellValue(studentNo);
            //分数
            for (int j = 3; j < pointAttributeSize + 3; j++) {
                //获得评分点属性
                String pointAttribute = pointAttributeList.get(j - 3);
                //根据属性找到评分点集合
                List<PointPO> list = pointList.stream().filter(p -> p.getPointAttribute().equals(pointAttribute)).collect(Collectors.toList());
                //获得该学生该评分属性的所对应评分点的得分总和
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
            //总分
            row.createCell(pointAttributeSize + 3).setCellValue(pointExcelDto.getStudentScore());
        }

        //sheet2分值分布
        SXSSFRow sheet2Row0 = sheet2.createRow(0);
        SXSSFCell sheet2Row0Cell1 = sheet2Row0.createCell(0);
        sheet2Row0Cell1.setCellStyle(cellStyle);
        sheet2Row0Cell1.setCellValue("课程名：《Web应用综合课程设计》");
        SXSSFCell sheet2Row0Cell2 = sheet2Row0.createCell(1);
        sheet2Row0Cell2.setCellStyle(cellStyle);
        sheet2Row0Cell2.setCellValue("考核评分点");
        CellRangeAddress region2 = new CellRangeAddress(0,0,1,5);
        sheet2.addMergedRegion(region2);
        SXSSFRow sheet2Row1 = sheet2.createRow(1);
        SXSSFCell sheet2Row1Cell1 = sheet2Row1.createCell(0);
        sheet2Row1Cell1.setCellValue("课程指标点");
        sheet2Row1.createCell(1).setCellValue("学习表现");
        for (int i = 0; i < pointAttributeSize; i++) {
            sheet2Row1.createCell(i + 2).setCellValue(pointAttributeList.get(i));
        }
        sheet2Row1.createCell(2 + pointAttributeSize).setCellValue("总分");
        HashMap<String, String> map = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        String a = ":针对给定的Web应用需求描述，应用规范的软件工程设计方法、工具和技术进行Web应用软件系统的需求分析、功能概要设计、数据库概要设计和详细设计，并能够根据设计结果进行Web应用软件系统的开发。";
        String b = ":理解软件设计中的易用性、健壮性和可扩展性原则，掌握在软件设计方案中应用这些原则的方法，理解不同开发模式和开发框架对软件复杂度的影响，能够为Web应用软件系统选择复杂度低的开发实施方案，并在设计或实现方法中体现创新意识。";
        String c = ":通过团队合作共同设计和开发一个完整的软件系统，具备一定的计划制定能力，能够在小组内分工合作完成组内任务及各自单独任务。";
        String d = ":具备撰写规范的课程设计说明书的能力，清晰地表达一个软件系统完整的设计和开发方案，并能够通过软件系统真实演示的形式展现系统的运行结果，陈述设计理念，回应质疑。";
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
            //评分点目标
            String pointTarget = pointTargetList.get(i);
            //获得描述
            SXSSFCell cell = sheet2Row.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(pointTarget + map.get(pointTarget));
            sheet2Row.createCell(1).setCellValue("-");
            for (int j = 0; j < pointAttributeSize; j++) {
                //评分点属性
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
            //总分
            List<PointPO> collect = pointList.stream().filter(p -> p.getPointTarget().equals(pointTarget)).collect(Collectors.toList());
            BigDecimal decimal = new BigDecimal("0");
            for (PointPO po : collect) {
                BigDecimal pointRatio = po.getPointRatio();
                decimal = decimal.add(pointRatio);
            }
            sheet2Row.createCell(2 + pointAttributeSize).setCellValue(decimal.multiply(BigDecimal.valueOf(100)).doubleValue());
        }
        SXSSFRow sheet2Row = sheet2.createRow(2 + pointTargetSize);
        sheet2Row.createCell(0).setCellValue("总计");
        sheet2Row.createCell(1).setCellValue(0);
        for (int i = 2; i <= 2 + pointTargetSize; i++) {
            SXSSFCell cell = sheet2Row.createCell(i);
            String colTag = CellReference.convertNumToColString(i);
            //从第三行开始
            String formula = "SUM(" + colTag + "3:" + colTag + (2 + pointTargetSize) + ")";
            cell.setCellFormula(formula);
            sheet2.setForceFormulaRecalculation(true);
        }
        //sheet3得分情况
        //第一行 标题列
        SXSSFRow sheet3Row1 = sheet3.createRow(0);
        SXSSFCell cell = sheet3Row1.createCell(0);
        cell.setCellValue("序号");
        sheet3Row1.createCell(1).setCellValue("学号");
        sheet3Row1.createCell(2).setCellValue("姓名");
        //计数，到那一列
        int count = 3;
        int tem = 0;
        for (String attribute : pointAttributeList) {
            //评分点属性
            //根据属性找到相应的评分点
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
        //目标
        for (String target : pointTargetList) {
            sheet3Row1.createCell(count).setCellValue(target);
            count++;
        }
        //目标大于0.65
        for (String target : pointTargetList) {
            sheet3Row1.createCell(count).setCellValue(target + "> 0.65");
            count++;
        }
        //数据列
        int row = 1;
        for (String studentNo : studentNoList) {
            SXSSFRow row3 = sheet3.createRow(row);
            //序号
            row3.createCell(0).setCellValue(row);
            //学号
            row3.createCell(1).setCellValue(studentNo);
            //姓名
            //根据学号查找出姓名
            PointExcelDto pointExcelDto = pointExcel.stream().filter(p -> p.getStudentNo().equals(studentNo)).findAny().orElse(null);
            assert pointExcelDto != null;
            row3.createCell(2).setCellValue(pointExcelDto.getStudentName());
            //分数
            int col = 3;
            for (String attribute : pointAttributeList) {
                //评分点属性
                //根据属性找到相应的评分点
                List<PointPO> pointCollect = pointList.stream().filter(p -> p.getPointAttribute().equals(attribute)).collect(Collectors.toList());
                BigDecimal bigDecimal = new BigDecimal("0");
                if (pointCollect.size() > 1) {
                    for (PointPO pointPO : pointCollect) {
                        //根据学号和评分点找到分数
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
            //目标
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
        row3.createCell(0).setCellValue("总计:");
        row3.createCell(1).setCellValue("");
        for (int i = tem + 1; i <= tem + pointTargetSize; i++) {
            SXSSFCell cell11 = row3.createCell(i);
            String colTag = CellReference.convertNumToColString(i);
            //从第2行开始
            String formula = "COUNTIF(" + colTag + "2:" + colTag + (studentNoList.size() + 1) + ",1)";
            cell11.setCellFormula(formula);
        }
        SXSSFRow row4 = sheet3.createRow(2 + studentNoList.size());
        row4.createCell(0).setCellValue("结果");
        for (int i = tem + 1; i <= tem + pointTargetSize; i++) {
            SXSSFCell cell22 = row4.createCell(i);
            String colTag = CellReference.convertNumToColString(i);
            String formula = colTag + (studentNoList.size() + 2) + "/" + studentNoList.size();
            cell22.setCellFormula(formula);
        }
        SXSSFRow rowRow1 = sheet4.createRow(0);
        //第一行表示哪几个班级
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
            //第一行
            SXSSFRow sheet4Row = sheet4.createRow(rowIndex);
            //第一行的第一列
            sheet4Row.createCell(0).setCellValue(pointTarget);
            //其他列,根据评分点名称和评分点的目标计算出平均分数
            int colIndex = 1;//记录第几列
            for (String pointName : pointNameList) {
                BigDecimal score = new BigDecimal("0");
                long size = 0;
                //找到评分点对应的目标
                String pp = pointList.stream().filter(p -> p.getPointName().equals(pointName)).map(PointPO::getPointTarget).findAny().orElse(null);
                //如果能找到，计算找出该目标下的评分点所对应的分数求和取平均
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
            //第一列
            String colStart = CellReference.convertNumToColString(2);
            //最后一列
            String colEnd = CellReference.convertNumToColString(pointNameSize);
            String formula = "SUM(" + colStart + rowIndex + ":" + colEnd + rowIndex + ")";
            SXSSFCell cell3 = sheet4Row.createCell(pointNameSize + 1);
            cell3.setCellFormula(formula);
        }
        //sheet4下表
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        int nextIndex = 5 + pointTargetSize;
        SXSSFRow row5 = sheet4.createRow(nextIndex);
        nextIndex++;
        row5.createCell(1).setCellValue("得分总分");
        row5.createCell(2).setCellValue("分值总分");
        row5.createCell(3).setCellValue("达成度");
        int m = 3;
        for (String s : pointTargetList) {
            SXSSFRow row6 = sheet4.createRow(nextIndex);
            //第一列
            row6.createCell(0).setCellValue(s);
            //得分总分
            //第一列
            String colStart = CellReference.convertNumToColString(2);
            //最后一列
            String colEnd = CellReference.convertNumToColString(pointNameSize);
            String formula = "SUM(" + colStart + m + ":" + colEnd + m + ")";
            SXSSFCell cell3 = row6.createCell(1);
            cell3.setCellFormula(formula);
            //总分
            List<PointPO> collect = pointList.stream().filter(p -> p.getPointTarget().equals(s)).collect(Collectors.toList());
            BigDecimal decimal = new BigDecimal("0");
            for (PointPO po : collect) {
                BigDecimal pointRatio = po.getPointRatio();
                decimal = decimal.add(pointRatio);
            }
            row6.createCell(2).setCellValue(decimal.multiply(BigDecimal.valueOf(100)).doubleValue());
            //第一列
            String colStart1 = CellReference.convertNumToColString(1);
            //最后一列
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
        //水平居中
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        SXSSFRow row6 = sheet4.createRow(nextIndex);
        SXSSFCell cell3 = row6.createCell(1);
        cell3.setCellStyle(cellStyle2);
        cell3.setCellValue("课程达成度");
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
        FileOutputStream fileOutputStream = new FileOutputStream("D:/zhsj/uploadPath/" + "课程设计达成度.xlsx");
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
            //根据教师查询出管理的哪几个班级
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
        //查询出的学生得分信息
        List<String> studentNoList = pointExcel.stream().map(PointExcelDto::getStudentNo).distinct().collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        map.put("xueqi", dto.getXueqi());
        map.put("date", simpleDateFormat.format(new Date()));
        map.put("name", dto.getName());
        map.put("grad", dto.getGrad());
        map.put("count", studentNoList.size());
        map.put("pers", dto.getPers());
        map.put("info", "针对给定的Web应用需求描述，应用规范的软件工程设计方法、工具和技术进行Web应用软件系统的需求分析、功能概要设计、数据库概要设计和详细设计，并能够根据设计结果进行Web应用软件系统的开发。");
        //课程目标1达成度
        BigDecimal tarOne = new BigDecimal("0");
        BigDecimal tarTow = new BigDecimal("0");
        BigDecimal tarThree = new BigDecimal("0");
        BigDecimal tarFour = new BigDecimal("0");
        //问题1等于1的个数
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
        //目标1团队表现平均分
        BigDecimal one = new BigDecimal("0");
        List<PointExcelDto> collect1 = pointExcel.stream().filter(p -> p.getPointName().equals("团队成绩-目标1")).collect(Collectors.toList());
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
        //个人成绩-目标1
        BigDecimal tow = new BigDecimal("0");
        List<PointExcelDto> collect2 = pointExcel.stream().filter(p -> p.getPointName().equals("个人成绩-目标1")).collect(Collectors.toList());
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
        //说明书-目标1
        BigDecimal three = new BigDecimal("0");
        List<PointExcelDto> collect3 = pointExcel.stream().filter(p -> p.getPointName().equals("说明书-目标1")).collect(Collectors.toList());
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
        //目标一达成评价值
        BigDecimal firstTar = new BigDecimal("0");
        firstTar = firstTar.add(one).add(tow).add(three).divide(BigDecimal.valueOf(50), 4, BigDecimal.ROUND_HALF_DOWN);
        map.put("firstTar", firstTar);

        //团队成绩-目标2
        BigDecimal four = new BigDecimal("0");
        List<PointExcelDto> collect4 = pointExcel.stream().filter(p -> p.getPointName().equals("团队成绩-目标2")).collect(Collectors.toList());
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
        //个人成绩-目标2
        BigDecimal five = new BigDecimal("0");
        List<PointExcelDto> collect5 = pointExcel.stream().filter(p -> p.getPointName().equals("个人成绩-目标2")).collect(Collectors.toList());
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

        //团队成绩-目标3
        BigDecimal six = new BigDecimal("0");
        List<PointExcelDto> collect6 = pointExcel.stream().filter(p -> p.getPointName().equals("团队成绩-目标3")).collect(Collectors.toList());
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
        //个人成绩-目标3
        BigDecimal seven = new BigDecimal("0");
        List<PointExcelDto> collect7 = pointExcel.stream().filter(p -> p.getPointName().equals("个人成绩-目标3")).collect(Collectors.toList());
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
        //组内自评-目标3
        BigDecimal eight = new BigDecimal("0");
        List<PointExcelDto> collect8 = pointExcel.stream().filter(p -> p.getPointName().equals("组内自评-目标3")).collect(Collectors.toList());
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

        //团队成绩-目标4
        BigDecimal nine = new BigDecimal("0");
        List<PointExcelDto> collect9 = pointExcel.stream().filter(p -> p.getPointName().equals("团队成绩-目标4")).collect(Collectors.toList());
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
        //个人成绩-目标4
        BigDecimal ten = new BigDecimal("0");
        List<PointExcelDto> collect10 = pointExcel.stream().filter(p -> p.getPointName().equals("个人成绩-目标4")).collect(Collectors.toList());
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
        //说明书-目标4
        BigDecimal eleven = new BigDecimal("0");
        List<PointExcelDto> collect11 = pointExcel.stream().filter(p -> p.getPointName().equals("说明书-目标4")).collect(Collectors.toList());
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
                    "D:/zhsj/uploadPath/model/模板.docx", map);
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
