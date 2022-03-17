package com.zhsj.business.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.dto.StudentDto;
import com.zhsj.business.student.dto.StudentQueryDto;
import com.zhsj.business.student.mapper.StudentMapper;
import com.zhsj.business.student.service.StudentService;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.exception.base.BaseException;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentPO> implements StudentService {

    @Resource
    private StudentMapper studentMapper;

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

}
