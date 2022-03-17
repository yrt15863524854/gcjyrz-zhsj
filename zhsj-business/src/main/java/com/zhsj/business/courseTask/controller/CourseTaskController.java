package com.zhsj.business.courseTask.controller;

import com.zhsj.common.config.ZhsjConfig;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.utils.SecurityUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
@RequestMapping("business/courseTask")
public class CourseTaskController extends BaseController {

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath = ZhsjConfig.getProfile();
        String userName = SecurityUtils.getLoginUser().getUsername();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
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
}
