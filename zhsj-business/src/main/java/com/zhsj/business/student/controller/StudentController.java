package com.zhsj.business.student.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.dto.StudentDto;
import com.zhsj.business.student.dto.StudentQueryDto;
import com.zhsj.business.student.service.StudentService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.core.page.TableDataInfo;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("business/student")
public class StudentController extends BaseController {
    @Resource
    private StudentService studentService;
    @GetMapping("/get")
    public TableDataInfo get(StudentQueryDto studentQueryDto){
        startPage();
        List<StudentDto> list = studentService.listStudent(studentQueryDto);
        return getDataTable(list);
    }
    @PostMapping("/add")
    public AjaxResult add(@RequestBody StudentPO studentPO){
        return studentService.addOrUpdateStudent(studentPO);
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody StudentPO studentPO){
        return studentService.addOrUpdateStudent(studentPO);
    }

    @PostMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable("id") Integer id){
        return AjaxResult.success(studentService.getById(id));
    }

    @PostMapping("/importTemplate")
    @ResponseBody
    public void importTemplate(HttpServletResponse response){
        studentService.importTemplate(response);
    }

}
