package com.zhsj.business.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.dto.StudentDto;
import com.zhsj.business.student.dto.StudentQueryDto;
import com.zhsj.business.student.mapper.StudentMapper;
import com.zhsj.business.student.service.StudentService;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.core.domain.entity.SysUser;
import com.zhsj.common.exception.base.BaseException;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import com.zhsj.system.domain.UserRolePO;
import com.zhsj.system.service.ISysRoleService;
import com.zhsj.system.service.ISysUserService;
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
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private ISysRoleService iSysRoleService;

    @Override
    public List<StudentDto> listStudent(StudentQueryDto studentQueryDto) {
        return studentMapper.listStudent(studentQueryDto);
    }

    @Override
    public AjaxResult addOrUpdateStudent(StudentPO studentPO) {
        if (Objects.isNull(studentPO.getId())) {
            SysUser user = new SysUser();
            UserRolePO userRolePO = new UserRolePO();
            studentPO.setStudentCode(IdUtils.fastSimpleUUID());
            List<StudentPO> list = studentMapper.selectList(new QueryWrapper<StudentPO>().eq("student_code", studentPO.getStudentCode()));
            if (list.size() > 0) {
                throw new BaseException("???????????????" + studentPO.getStudentCode() + "?????????");
            }
            List<StudentPO> list1 = studentMapper.selectList(new QueryWrapper<StudentPO>().eq("student_no", studentPO.getStudentNo()));
            if (list1.size() > 0) {
                throw new BaseException("???????????????" + studentPO.getStudentNo() + "?????????");
            }
            String sno = studentPO.getStudentNo() + "";
            user.setUserId(Long.parseLong(sno));
            user.setUserName(sno);
            userRolePO.setUserId(Long.parseLong(sno));
            user.setNickName(studentPO.getStudentName());
            user.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            user.setPassword(SecurityUtils.encryptPassword("123456"));
            userRolePO.setRoleId((long)100);
            iSysUserService.insertUser(user);
            iSysRoleService.insertUserRole(userRolePO);
            user.setCreateTime(DateUtils.getNowDate());
            studentPO.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            studentPO.setCreateTime(DateUtils.getNowDate());
            int result = studentMapper.addStudent(studentPO);
            if (result != 1) {
                throw new BaseException("????????????");
            }
            return AjaxResult.success("????????????");
        } else {
            studentPO.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
            studentPO.setUpdateTime(DateUtils.getNowDate());
            int result = studentMapper.updateStudent(studentPO);
            if (result != 1) {
                throw new BaseException("????????????");
            }
            return AjaxResult.success("????????????");
        }
    }

    @Override
    public StudentPO getById(Integer id) {
        return studentMapper.getStudentById(id);
    }

    @Override
    public void importTemplate(HttpServletResponse response) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("static/model/????????????.xlsx");
            //?????????????????????
            response.setContentType("application/force-download");
            ServletOutputStream out = response.getOutputStream();
            //??????URLEncoder??????????????????????????????????????????
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("????????????.xlsx", "UTF-8"));
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
    public void deleteStudent(StudentQueryDto dto) {
        for (Integer student : dto.getIds()) {
            QueryWrapper<StudentPO> studentPOQueryWrapper = new QueryWrapper<>();
            studentPOQueryWrapper.eq("id", student);
            studentMapper.delete(studentPOQueryWrapper);
        }
    }

}
