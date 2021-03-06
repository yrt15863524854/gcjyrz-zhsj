package com.zhsj.business.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.dto.StudentDto;
import com.zhsj.business.student.dto.StudentQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface StudentMapper extends BaseMapper<StudentPO> {
    /**
     * 获取所有学生信息
     *
     * @return 学生集合
     */
    List<StudentDto> listStudent(StudentQueryDto studentQueryDto);

    /**
     * 添加学生
     *
     * @param studentPO 学生对象
     * @return 结果
     */
    int addStudent(StudentPO studentPO);

    /**
     * @description updateStudent
     * @date 2022/3/27 17:02
     * @param studentPO
     * @return int
     * @author yrt
     **/
    int updateStudent(StudentPO studentPO);

    @Select("select * from business_student where id = #{id}")
    StudentPO selectStudentById(Integer id);
    /**
     * @description getStudentById
     * @date 2022/3/27 17:02
     * @param id
     * @return com.zhsj.business.student.domain.StudentPO
     * @author yrt
     **/
    StudentPO getStudentById(Integer id);
}
