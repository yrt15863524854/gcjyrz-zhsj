package com.zhsj.business.manual.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.business.kaoqin.mapper.ClassInfoMapper;
import com.zhsj.business.manual.domain.ManualPO;
import com.zhsj.business.manual.dto.GroupAndClassNameDto;
import com.zhsj.business.manual.dto.ManualDto;
import com.zhsj.business.manual.dto.ManualQueryDto;
import com.zhsj.business.manual.mapper.ManualMapper;
import com.zhsj.business.manual.service.ManualService;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.exception.base.BaseException;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.bean.BeanUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManualServiceImpl extends ServiceImpl<ManualMapper, ManualPO> implements ManualService {

    @Resource
    private ManualMapper manualMapper;

    @Resource
    private ClassInfoMapper classInfoMapper;
    /**
     * @description ListManualInfo
     * @date 2022/3/26 19:46
     * @param manualQueryDto
     * @return java.util.List<com.zhsj.business.manual.dto.ManualDto>
     * @author yrt
     **/
    @Override
    public List<ManualDto> ListManualInfo(ManualQueryDto manualQueryDto) {
        return manualMapper.listManualInfo(manualQueryDto);
    }
    /**
     * @description insertManual
     * @date 2022/4/2 11:36
     * @param manualDto
     * @return com.zhsj.common.core.domain.AjaxResult
     * @author yrt
     **/
    @Override
    public AjaxResult insertManual(ManualDto manualDto) {
        ManualPO manualPO = new ManualPO();
        BeanUtils.copyProperties(manualDto, manualPO);
        String classCode = classInfoMapper.getClassCode(manualDto.getClassName());
        manualPO.setId(IdUtils.longID());
        manualPO.setClassCode(classCode);
        manualPO.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        manualPO.setCreateTime(DateUtils.getNowDate());
        int result = manualMapper.insertManual(manualPO);
        if (result < 1){
            throw new BaseException("添加失败");
        }
        return AjaxResult.success("添加成功");
    }
    /**
     * @description updateManual
     * @date 2022/4/2 11:36
     * @param manualDto
     * @return com.zhsj.common.core.domain.AjaxResult
     * @author yrt
     **/
    @Override
    public AjaxResult updateManual(ManualDto manualDto) {
        ManualPO po = new ManualPO();
        BeanUtils.copyProperties(manualDto, po);
        String classCode = classInfoMapper.getClassCode(manualDto.getClassName());
        po.setClassCode(classCode);
        po.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        po.setUpdateTime(DateUtils.getNowDate());
        int result = manualMapper.updateManual(po);
        if (result < 1) {
            throw new BaseException("更新失败");
        }
        return AjaxResult.success("更新成功");
    }

    @Override
    public Long getRoleId(Long userId) {
        return manualMapper.getRoleId(userId);
    }

    @Override
    public Map<String, Object> getStudentGroup(String sno) {
        GroupAndClassNameDto groupAndClassName = manualMapper.getStudentGroup(sno);
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupAndClassName", groupAndClassName);
        return map;
    }

    @Override
    public ManualDto getManualInfoBySg(ManualQueryDto dto) {
        return manualMapper.getManualInfoBySg(dto);
    }

}