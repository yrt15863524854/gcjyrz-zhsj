package com.zhsj.business.point.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.domain.PointPO;
import com.zhsj.business.point.domain.ScorePO;
import com.zhsj.business.point.dto.*;
import com.zhsj.business.point.mapper.PointDetailMapper;
import com.zhsj.business.point.mapper.PointMapper;
import com.zhsj.business.point.mapper.ScoreMapper;
import com.zhsj.business.point.service.PointDetailService;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.mapper.StudentMapper;
import com.zhsj.common.annotation.Excel;
import com.zhsj.common.utils.bean.BeanUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
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
            Field[] fields = excelDto.getClass().getDeclaredFields();
            Method[] methods = excelDto.getClass().getMethods();//拿到函数成员
            for (int i = 0; i < size; i++) {
                String s =  pointName.get(i);
                for (Method method : methods) {
                    String methodName = "setField" + (i + 4);
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
                for (int j = 0; j < size; j++) {
                    //获取评分点名称
                    String point = pointName.get(j);
                    //获取评分点分数
                    PointExcelDto pointExcelDto2 = pointExcelDtos.stream().filter(p -> p.getStudentNo().equals(no) && p.getPointName().equals(point)).findAny().orElse(null);
                    assert pointExcelDto2 != null;
                    String pointScore = pointExcelDto2.getPointScore();
                    //获取要赋值的字段名，调用方法
                    String methodName = "setField" + (j + 4);
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

    public static void main(String[] args) {
        try{
            int size = 6;
            ExcelDto excelDto = new ExcelDto();
            //通过getDeclaredFields()方法获取对象类中的所有属性（含私有）
            Field[] fields = excelDto.getClass().getDeclaredFields();
            Method[] methods = excelDto.getClass().getMethods();//拿到函数成员
            excelDto.setField1("班级");
            excelDto.setField2("学号");
            excelDto.setField3("姓名");
            List<String> pointName = new ArrayList<>();
            String name1 = "评分1";
            String name2 = "评分2";
            String name3 = "评分3";
            String name4 = "评分4";
            String name5 = "评分5";
            String name6 = "评分6";
            pointName.add(name1);
            pointName.add(name2);
            pointName.add(name3);
            pointName.add(name4);
            pointName.add(name5);
            pointName.add(name6);
            for (int i = 0; i < size; i++) {
                String s =  pointName.get(i);
                for (Method method : methods) {
                    String methodName = "setField" + (i + 4);
                    if (method.getName().equals(methodName)) {
                        method.invoke(excelDto, s);
                    }
                }

            }
            String user = pointName.stream()
                    .filter(u -> u.equals("评分1"))
                    .findAny()
                    .orElse(null);
            System.out.println(user);
            System.out.println(excelDto.toString());
        }
        catch (Exception ex){
            //处理异常
        }
    }

}
