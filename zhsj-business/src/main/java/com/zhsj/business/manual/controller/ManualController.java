package com.zhsj.business.manual.controller;

import com.zhsj.business.manual.dto.ManualDto;
import com.zhsj.business.manual.dto.ManualQueryDto;
import com.zhsj.business.manual.dto.TopicDetailDto;
import com.zhsj.business.manual.dto.TopicDetailQueryDto;
import com.zhsj.business.manual.service.ManualService;
import com.zhsj.business.manual.service.TopicDetailService;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.page.TableDataInfo;
import com.zhsj.common.utils.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/business/manual")
public class ManualController extends BaseController {

    @Resource
    private ManualService manualService;

    @Resource
    private TopicDetailService topicDetailService;

    @PostMapping("/importTemplate")
    @ResponseBody
    public void importTemplate(HttpServletResponse response) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("static/model/课程设计说明书模板.docx");
            //强制下载打不开
            response.setContentType("application/force-download");
            ServletOutputStream out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("课程设计说明书模板.docx", "UTF-8"));
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
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @PostMapping("/listManualInfo")
    public TableDataInfo listManualInfo(@RequestBody ManualQueryDto manualQueryDto) {
        startPage();
        List<ManualDto> list = manualService.ListManualInfo(manualQueryDto);
        return getDataTable(list);
    }
    @PostMapping("/getUserInfo")
    public Map<String, Object> getUserInfo(){
        Map<String, Object> map = new HashMap<>();
        String username = SecurityUtils.getLoginUser().getUsername();
        map.put("username", username);
        Long userId = SecurityUtils.getLoginUser().getUserId();
        map.put("userId", userId);
        return map;
    }
    @GetMapping("/getRoleId/{id}")
    public Map<String, Object> getRoleId(@PathVariable("id")Long userId){
        Map<String, Object> map = new HashMap<>();
        Long roleId = manualService.getRoleId(userId);
        map.put("roleId", roleId);
        return map;
    }
    @PostMapping("/insertManual")
    public void insertManual(@RequestBody ManualDto dto){
        manualService.insertManual(dto);
    }
    @PostMapping("/updateManual")
    public void updateManual(@RequestBody ManualDto dto){
        manualService.updateManual(dto);
    }
    @GetMapping("/getStudentGroup/{sno}")
    public Map<String, Object> getStudentGroup(@PathVariable("sno") String sno){
        return manualService.getStudentGroup(sno);
    }
    @PostMapping("/getManualInfoBySg")
    public ManualDto getManualInfoBySg(@RequestBody ManualQueryDto dto){
        return manualService.getManualInfoBySg(dto);
    }
    @PostMapping("/getTopicDetail")
    public Map<String, Object> getTopicDetail(@RequestBody TopicDetailQueryDto dto){
        Map<String, Object> map = new HashMap<>();
        TopicDetailDto topicDetail = topicDetailService.getTopicDetail(dto);
        map.put("topicDetail", topicDetail);
        return map;
    }
    @PostMapping("/insertTopicDetail")
    public void insertTopicDetail(@RequestBody TopicDetailDto dto){
        topicDetailService.insertTopicDetail(dto);
    }
    @PostMapping("/updateTopicDetail")
    public void updateTopicDetail(@RequestBody TopicDetailDto dto){
        topicDetailService.updateTopicDetail(dto);
    }
}
