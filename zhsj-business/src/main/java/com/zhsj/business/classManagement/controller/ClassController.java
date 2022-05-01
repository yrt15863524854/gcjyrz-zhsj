package com.zhsj.business.classManagement.controller;

import com.zhsj.business.classManagement.domain.ClassPO;
import com.zhsj.business.classManagement.service.ClassService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @className: ClassController
 * @description: TODO
 * @author: yrt
 * date: 2022/4/29 13:36
 * version 1.0
 **/
@RestController
@RequestMapping("/business/class")
public class ClassController extends BaseController {
    @Resource
    private ClassService classService;
    @PostMapping("/listClass")
    public List<ClassPO> list() {
        return classService.list();
    }
    @PostMapping("/addClass")
    public void addClass(@RequestBody ClassPO po) {
        po.setId(IdUtils.longID());
        po.setClassCode(IdUtils.fastSimpleUUID());
        po.setStatus("000");
        po.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        po.setCreateTime(new Date());
        classService.save(po);
    }
    @PostMapping("/updateClass")
    public void updateClass(@RequestBody ClassPO po) {
        po.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        po.setUpdateTime(new Date());
        classService.updateById(po);
    }
    @PostMapping("/deleteClass")
    public void deleteClass(@RequestBody ClassPO po) {
        classService.removeById(po);
    }
}
