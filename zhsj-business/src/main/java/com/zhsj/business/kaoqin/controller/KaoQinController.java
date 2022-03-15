package com.zhsj.business.kaoqin.controller;

import com.zhsj.business.kaoqin.domain.KaoQinPO;
import com.zhsj.business.kaoqin.dto.ClassInfoDto;
import com.zhsj.business.kaoqin.dto.KaoQinDto;
import com.zhsj.business.kaoqin.dto.KaoQinQueryDto;
import com.zhsj.business.kaoqin.service.ClassInfoService;
import com.zhsj.business.kaoqin.service.KaoQinService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/business/kaoqin")
public class KaoQinController extends BaseController {
    @Resource
    private KaoQinService kaoQinService;
    @Resource
    private ClassInfoService classInfoService;
    @PostMapping("/add")
    public AjaxResult add(@RequestBody KaoQinDto kaoQinDto){
        kaoQinService.addKaoQinInfo(kaoQinDto);
        return AjaxResult.success("录入成功！");
    }
    @PostMapping("/get")
    public TableDataInfo get(@RequestBody KaoQinQueryDto kaoQinQueryDto) {
        startPage();
        List<KaoQinDto> list = kaoQinService.selectKaoQinInfo(kaoQinQueryDto);
        return getDataTable(list);
    }
    @PostMapping("/getClassInfo")
    public Map<String, Object> getClassInfo(){
        List<ClassInfoDto> classInfoDtos = classInfoService.listClassInfo();
        Map<String, Object> classInfoMap = new HashMap<>();
        classInfoMap.put("classInfo", classInfoDtos);
        return classInfoMap;
    }
}
