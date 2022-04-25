package com.zhsj.business.teacher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.teacher.domain.TeacherPO;
import com.zhsj.business.teacher.dto.TeacherDto;
import com.zhsj.business.teacher.mapper.TeacherMapper;
import com.zhsj.business.teacher.service.TeacherService;
import com.zhsj.business.test.domain.Test;
import com.zhsj.business.test.service.TestService;
import com.zhsj.common.core.domain.entity.SysUser;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.bean.BeanUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import com.zhsj.system.domain.SysUserRole;
import com.zhsj.system.domain.UserRolePO;
import com.zhsj.system.mapper.SysUserMapper;
import com.zhsj.system.service.ISysRoleService;
import com.zhsj.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @className: TeacherServiceImpl
 * @description: TODO
 * @author: yrt
 * date: 2022/3/27 16:34
 * version 1.0
 **/
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, TeacherPO> implements TeacherService {
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private ISysRoleService iSysRoleService;
    @Resource
    private SysUserMapper userMapper;
    @Override
    public Map<String, Object> insertOrUpdateTeacher(TeacherDto dto) {
        Map<String, Object> map = new HashMap<>();
        if (Objects.isNull(dto.getId())) {
            QueryWrapper<TeacherPO> wrapper = new QueryWrapper<>();
            wrapper.eq("teacher_code", dto.getTeacherCode());
            List<TeacherPO> list = teacherMapper.selectList(wrapper);
            if (list.size() > 0) {
                map.put("message", dto.getTeacherCode() + "已存在");
                return map;
            }
            SysUser user = new SysUser();
            UserRolePO userRolePO = new UserRolePO();
            TeacherPO po = new TeacherPO();
            BeanUtils.copyProperties(dto, po);
            Long userId = IdUtils.longID();
            po.setId(userId);
            po.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            po.setCreateTime(DateUtils.getNowDate());
            user.setUserId(userId);
            user.setUserName(po.getTeacherCode());
            user.setNickName(po.getTeacherName());
            user.setPassword(SecurityUtils.encryptPassword("123456"));
            user.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            user.setCreateTime(DateUtils.getNowDate());
            userRolePO.setUserId(userId);
            userRolePO.setRoleId((long) 101);
            int i = iSysUserService.insertUser(user);
            iSysRoleService.insertUserRole(userRolePO);
            int insert = teacherMapper.insert(po);
            if (insert > 0 && i > 0) {
                map.put("message", "添加成功");
            } else {
                map.put("message", "修改成功");
            }
        } else {
            TeacherPO po = new TeacherPO();
            BeanUtils.copyProperties(dto, po);
            po.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
            po.setUpdateTime(DateUtils.getNowDate());
            SysUser user = new SysUser();
            user.setUserId(dto.getId());
            user.setUserName(po.getTeacherCode());
            user.setNickName(po.getTeacherName());
            int update = teacherMapper.updateById(po);
            int updateUser = userMapper.updateUser(user);
            if (update > 0 && updateUser > 0) {
                map.put("message", "修改成功");
            } else {
                map.put("message", "修改失败");
            }
        }
        return map;
    }
    @Override
    public void deleteTeacher(TeacherDto dto) {
        TeacherPO po = new TeacherPO();
        BeanUtils.copyProperties(dto, po);
        iSysUserService.deleteUserByUserId(dto.getId());
        iSysRoleService.deleteUserRoleByUserId(dto.getId());
        teacherMapper.deleteById(po);
    }
}
