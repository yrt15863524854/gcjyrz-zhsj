package com.zhsj.business.courseTask.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.course.service.CourseService;
import com.zhsj.business.courseTask.domain.CourseTaskPO;
import com.zhsj.business.courseTask.dto.CourseTaskDto;
import com.zhsj.business.courseTask.dto.CourseTaskQueryDto;
import com.zhsj.business.courseTask.service.CourseTaskService;
import com.zhsj.common.config.ZhsjConfig;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.bean.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("business/courseTask")
public class CourseTaskController extends BaseController {

    @Resource
    private CourseTaskService courseTaskService;

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath = ZhsjConfig.getProfile();
        String userName = SecurityUtils.getLoginUser().getUsername();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//        SimpleDateFormat mm = new SimpleDateFormat("MM");
//        SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
//        String month = mm.format(new Date());
//        String year = yyyy.format(new Date());
        Calendar cal = Calendar.getInstance();
        String month = sdf.format(cal.get(Calendar.MONTH));
        String day = sdf.format(cal.get(Calendar.DAY_OF_MONTH));
        String realPath = imagePath + month + "/";
        File files = new File(realPath);
        if (!files.exists()){
            files.mkdirs();
        }
        response.setContentType("test/plain; chartset = UTF-8");
        String originalFilename;
        //String importFilePath = null;
        if (file.isEmpty()){
            logger.info("请选择文件后上传");
            return error("请选择文件后上传");
        } else {
            originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            if ("doc".equalsIgnoreCase(extension) || "docx".equalsIgnoreCase(extension)) {
                originalFilename = userName + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(originalFilename);
                try {
                    file.transferTo(new File(realPath, originalFilename));
                    //importFilePath = imagePath + month + "/" + originalFilename;
                    logger.info("load success" + request.getContextPath() + File.separator + "upload" + File.separator + day + File.separator + originalFilename);
                    logger.info("leaving upload!");
                } catch (Exception e) {
                    logger.info("文件[" + originalFilename + "]上传失败，堆栈轨迹如下");
                    e.printStackTrace();
                    logger.info("文件上传失败，请重试！！");
                    return error("文件上传失败，请重试！！");
                }
            } else {
                logger.info("load success 只支持doc,docx格式");
            }
        }
        return AjaxResult.success("上传成功");
    }
    @PostMapping("/insertTaskBook")
    public Map<String, Object> insertTaskBook(@RequestBody CourseTaskDto dto) {
        return courseTaskService.insertTaskBook(dto);
    }
    @PostMapping("/updateTaskBook")
    public Map<String, Object> updateTaskBook(@RequestBody CourseTaskDto dto) {
        return courseTaskService.updateTaskBook(dto);
    }
    @PostMapping("/getTaskBook")
    public CourseTaskDto getTaskBook(@RequestBody CourseTaskQueryDto courseCode) {
        return courseTaskService.getTaskBook(courseCode);
    }
    @PostMapping("/getTaskBookList")
    public List<CourseTaskDto> getTaskBookList() {
        return courseTaskService.getTaskBookList();

    }
}
