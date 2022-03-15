package com.zhsj.business.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.mapper.ClassInfoMapper;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.dto.StudentDto;
import com.zhsj.business.student.dto.StudentQueryDto;
import com.zhsj.business.student.mapper.StudentMapper;
import com.zhsj.business.student.service.StudentService;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.exception.base.BaseException;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.ImportUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentPO> implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private ClassInfoMapper classInfoMapper;

    @Override
    public List<StudentDto> listStudent(StudentQueryDto studentQueryDto) {
        return studentMapper.listStudent(studentQueryDto);
    }

    @Override
    public AjaxResult addOrUpdateStudent(StudentPO studentPO) {
        if (Objects.isNull(studentPO.getId())) {
            List<StudentPO> list = studentMapper.selectList(new QueryWrapper<StudentPO>().eq("student_code", studentPO.getStudentCode()));
            if (list.size() > 0) {
                throw new BaseException("该学生编码"+studentPO.getStudentCode()+"已存在");
            }
            studentPO.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            studentPO.setCreateTime(DateUtils.getNowDate());
            int result = studentMapper.addStudent(studentPO);
            if (result != 1) {
                throw new BaseException("添加失败");
            }
            return AjaxResult.success("添加成功");
        } else {
            studentPO.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
            studentPO.setUpdateTime(DateUtils.getNowDate());
            int result = studentMapper.updateStudent(studentPO);
            if (result != 1) {
                throw new BaseException("修改失败");
            }
            return AjaxResult.success("修改成功");
        }
    }

    @Override
    public StudentPO getById(Integer id) {
        return studentMapper.getStudentById(id);
    }

    @Override
    public void importTemplate(HttpServletResponse response) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("static/model/测试模板.xlsx");
            //强制下载不打开
            response.setContentType("application/force-download");
            ServletOutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("测试模板.xlsx", "UTF-8"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request, HttpServletResponse response) {

        return AjaxResult.success();
    }

    /**
     * 学生信息
     */
    private List<StudentPO> UpdateExcelBindingContainers(String importFilePath) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File file = new File(importFilePath);
        InputStream inputStream = new FileInputStream(file);
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row currentRow;
        List<StudentPO> list = new ArrayList<>();
        if (sheet.getLastRowNum() == 0) {
            throw new RuntimeException("Excel没有数据");
        }
        for (int i = 1; i<= sheet.getLastRowNum(); i++){
            StudentPO detail = new StudentPO();
            currentRow = sheet.getRow(i);
            if (currentRow == null) {
                continue;
            }
            //学生编码
            String studentCode = ImportUtils.getJavaValue(currentRow.getCell(0)).toString();
            if (studentCode.isEmpty()) {
                throw new RuntimeException("第" + (i + 1) + "学生编码不能为空");
            } else {
                StudentPO code = studentMapper.selectOne(new QueryWrapper<StudentPO>().eq("student_code", studentCode));
                if (Objects.nonNull(code)) {
                    throw new RuntimeException("第" + (i + 1) + "行的学生编码信息已存在");
                }
                detail.setStudentCode(studentCode);
            }
            //学生学号
            String studentNo = ImportUtils.getJavaValue(currentRow.getCell(1)).toString();
            if (studentNo.isEmpty()) {
                throw new RuntimeException("第" + (i + 1) + "学生学号不能为空");
            } else {
                StudentPO no = studentMapper.selectOne(new QueryWrapper<StudentPO>().eq("student_no", studentNo));
                if (Objects.nonNull(no)) {
                    throw new RuntimeException("第" + (i + 1) + "行学生学号信息已存在");
                }
                detail.setStudentNo(Integer.parseInt(studentNo));
            }
            //学生姓名
            String studentName = ImportUtils.getJavaValue(currentRow.getCell(2)).toString();
            if (studentName.isEmpty()) {
                throw new RuntimeException("第" + (i + 1) + "学生姓名不能为空");
            } else {
                detail.setStudentName(studentName);
            }
            //学生班级
            String studentClass = ImportUtils.getJavaValue(currentRow.getCell(3)).toString();
            if (studentClass.isEmpty()) {
                throw new RuntimeException("第" + (i + 1) + "学生班级不能为空");
            } else {
                ClassInfoPO code = classInfoMapper.selectOne(new QueryWrapper<ClassInfoPO>().eq("class_code", studentClass));
                if (Objects.isNull(code)) {
                    throw new RuntimeException("第" + (i + 1) + "行的班级信息未找到");
                }
                detail.setStudentClass(studentClass);
            }
            //学生组号
            String group = ImportUtils.getJavaValue(currentRow.getCell(4)).toString();
            detail.setStudentGroup(Integer.parseInt(group));
            //是否为组长
            String lead = ImportUtils.getJavaValue(currentRow.getCell(5)).toString();
            detail.setLeader(Integer.parseInt(lead));
            detail.setCreateBy(SecurityUtils.getUsername());
            detail.setCreateTime(DateUtils.getNowDate());
            list.add(detail);
        }
        return list;
    }

    /**
     * 导入学生信息
     */
    private String importVendor(List<StudentPO> detailList) throws Exception{
        if (StringUtils.isNull(detailList) || detailList.size() == 0) {
            throw new RuntimeException("导入学生数据不能为空");
        }
        StringBuilder successMsg = new StringBuilder();
        for (StudentPO student: detailList) {
            student.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            student.setCreateTime(DateUtils.getNowDate());
        }
        studentMapper.
    }
}