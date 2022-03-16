package com.zhsj.business.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.dto.StudentDto;
import com.zhsj.business.student.dto.StudentQueryDto;
import com.zhsj.common.core.domain.AjaxResult;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface StudentService extends IService<StudentPO> {
    /**
     * 获取所有学生信息
     *
     * @return 学生集合
     */
    List<StudentDto> listStudent(StudentQueryDto studentQueryDto);

    /**
     * 添加或修改学生
     *
     * @param studentPO 学生对象
     * @return 结果
     */
    AjaxResult addOrUpdateStudent(StudentPO studentPO);

    StudentPO getById(Integer id);

    /**
     * 下载模板
     */
    void importTemplate(HttpServletResponse response);

}
