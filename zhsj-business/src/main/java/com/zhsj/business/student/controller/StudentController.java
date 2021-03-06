package com.zhsj.business.student.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhsj.business.kaoqin.domain.ClassInfoPO;
import com.zhsj.business.kaoqin.domain.KaoQinPO;
import com.zhsj.business.kaoqin.mapper.ClassInfoMapper;
import com.zhsj.business.kaoqin.mapper.KaoQinMapper;
import com.zhsj.business.manual.domain.ManualPO;
import com.zhsj.business.manual.domain.TopicDetailPO;
import com.zhsj.business.manual.mapper.ManualMapper;
import com.zhsj.business.manual.mapper.TopicDetailMapper;
import com.zhsj.business.point.domain.PointDetailPO;
import com.zhsj.business.point.domain.ScorePO;
import com.zhsj.business.point.mapper.PointDetailMapper;
import com.zhsj.business.point.mapper.ScoreMapper;
import com.zhsj.business.rate.domain.RatePO;
import com.zhsj.business.rate.mapper.RateMapper;
import com.zhsj.business.student.domain.StudentPO;
import com.zhsj.business.student.dto.StudentDto;
import com.zhsj.business.student.dto.StudentQueryDto;
import com.zhsj.business.student.mapper.StudentMapper;
import com.zhsj.business.student.service.StudentService;
import com.zhsj.business.uploadDown.domain.UploadPO;
import com.zhsj.business.uploadDown.mapper.UploadMapper;
import com.zhsj.common.config.ZhsjConfig;
import com.zhsj.common.core.controller.BaseController;
import com.zhsj.common.core.domain.AjaxResult;
import com.zhsj.common.core.domain.entity.SysUser;
import com.zhsj.common.core.page.TableDataInfo;
import com.zhsj.common.exception.base.BaseException;
import com.zhsj.common.utils.DateUtils;
import com.zhsj.common.utils.ImportUtils;
import com.zhsj.common.utils.SecurityUtils;
import com.zhsj.common.utils.StringUtils;
import com.zhsj.common.utils.uuid.IdUtils;
import com.zhsj.system.domain.SysUserRole;
import com.zhsj.system.domain.UserRolePO;
import com.zhsj.system.mapper.SysRoleMapper;
import com.zhsj.system.mapper.SysUserMapper;
import com.zhsj.system.mapper.SysUserRoleMapper;
import com.zhsj.system.service.ISysRoleService;
import com.zhsj.system.service.ISysUserService;
import jdk.nashorn.internal.objects.Global;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

import java.io.InputStream;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("business/student")
public class StudentController extends BaseController {
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private StudentService studentService;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private ClassInfoMapper classInfoMapper;
    @Resource
    private ISysRoleService iSysRoleService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private PointDetailMapper pointDetailMapper;
    @Resource
    private ManualMapper manualMapper;
    @Resource
    private TopicDetailMapper topicDetailMapper;
    @Resource
    private UploadMapper uploadMapper;
    @Resource
    private KaoQinMapper kaoQinMapper;
    @Resource
    private RateMapper rateMapper;
    @GetMapping("/get")
    public TableDataInfo get(StudentQueryDto studentQueryDto){
        startPage();
        List<StudentDto> list = studentService.listStudent(studentQueryDto);
        return getDataTable(list);
    }
    @PostMapping("/add")
    public AjaxResult add(@RequestBody StudentPO studentPO){
        return studentService.addOrUpdateStudent(studentPO);
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody StudentPO studentPO){
        return studentService.addOrUpdateStudent(studentPO);
    }

    @PostMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable("id") Integer id){
        return AjaxResult.success(studentService.getById(id));
    }

    @PostMapping("/importTemplate")
    @ResponseBody
    public void importTemplate(HttpServletResponse response){
        studentService.importTemplate(response);
    }

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath = ZhsjConfig.getProfile();//??????????????????
        String userName = SecurityUtils.getLoginUser().getUsername();//????????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        String month = sdf.format(cal.get(Calendar.MONTH));
        String day = sdf.format(cal.get(Calendar.DAY_OF_MONTH));
        String realPath = imagePath + month + "/";
        File files = new File(realPath);
        if (!files.exists()){
            files.mkdirs();
        }
        response.setContentType("text/plain; charset = UTF-8");
        String originalFilename;
        String importFilePath = null;
        if (file.isEmpty()){
            logger.info("????????????????????????");
            return error("????????????????????????");
        } else {
            originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            if ("xls".equalsIgnoreCase(extension) || "xlsx".equalsIgnoreCase(extension)) {
                originalFilename = userName + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(originalFilename);
                try {
                    file.transferTo(new File(realPath, originalFilename));
                    //??????????????????
                    importFilePath = imagePath + month + "/" + originalFilename;
                    logger.info("load success" + request.getContextPath() + File.separator + "upload" + File.separator + day + File.separator + originalFilename);
                    logger.info("leaving upload!");
                } catch (Exception e) {
                    logger.info("??????[" + originalFilename + "]?????????????????????????????????");
                    e.printStackTrace();
                    logger.info("????????????????????????????????????");
                    return error("????????????????????????????????????");
                }
            }else {
                logger.info("load success ?????????xls,xlsx??????");
            }
        }
        List<StudentPO> list = UpdateExcelBindingContainers(importFilePath);
        String message = importVendor(list);
        return AjaxResult.success(message);
    }
    @PostMapping("/deleteStudent")
    public void deleteStudent(@RequestBody StudentQueryDto dto){
        for (Integer student : dto.getIds()) {
            StudentPO byId = studentService.getById(student);
            String studentNo = byId.getStudentNo();
            Long s = new Long(studentNo);
            //??????????????????
            sysUserMapper.deleteUserByUserId(s);
            //??????????????????
            sysUserRoleMapper.deleteUserRoleByUserId(s);
            //??????????????????
            QueryWrapper<ScorePO> scorePOQueryWrapper = new QueryWrapper<>();
            scorePOQueryWrapper.eq("student_no", studentNo);
            scoreMapper.delete(scorePOQueryWrapper);
            //?????????????????????
            QueryWrapper<PointDetailPO> pointDetailPOQueryWrapper = new QueryWrapper<>();
            pointDetailPOQueryWrapper.eq("student_no", studentNo);
            pointDetailMapper.delete(pointDetailPOQueryWrapper);
            //?????????????????????
            QueryWrapper<ManualPO> manualPOQueryWrapper = new QueryWrapper<>();
            manualPOQueryWrapper.eq("create_by", studentNo);
            manualMapper.delete(manualPOQueryWrapper);
            //????????????????????????
            QueryWrapper<TopicDetailPO> topicDetailPOQueryWrapper = new QueryWrapper<>();
            topicDetailPOQueryWrapper.eq("student_no", studentNo);
            topicDetailMapper.delete(topicDetailPOQueryWrapper);
            //??????????????????
            QueryWrapper<UploadPO> uploadPOQueryWrapper = new QueryWrapper<>();
            uploadPOQueryWrapper.eq("student_no", studentNo);
            uploadMapper.delete(uploadPOQueryWrapper);
            //??????????????????
            QueryWrapper<KaoQinPO> kaoQinPOQueryWrapper = new QueryWrapper<>();
            kaoQinPOQueryWrapper.eq("student_no", studentNo);
            kaoQinMapper.delete(kaoQinPOQueryWrapper);
            //??????????????????
            QueryWrapper<RatePO> ratePOQueryWrapper = new QueryWrapper<>();
            ratePOQueryWrapper.eq("student_no", studentNo);
            rateMapper.delete(ratePOQueryWrapper);
        }
        studentService.deleteStudent(dto);
    }
    @GetMapping("/deleteByStudentNo/{studentNo}")
    public void deleteByStudentNo(@PathVariable String studentNo) {
        QueryWrapper<StudentPO> studentPOQueryWrapper = new QueryWrapper<>();
        studentPOQueryWrapper.eq("student_no", studentNo);
        studentService.remove(studentPOQueryWrapper);
        Long s = new Long(studentNo);
        //??????????????????
        sysUserMapper.deleteUserByUserId(s);
        //??????????????????
        sysUserRoleMapper.deleteUserRoleByUserId(s);
        //??????????????????
        QueryWrapper<ScorePO> scorePOQueryWrapper = new QueryWrapper<>();
        scorePOQueryWrapper.eq("student_no", studentNo);
        scoreMapper.delete(scorePOQueryWrapper);
        //???????????????????????????
        QueryWrapper<PointDetailPO> pointDetailPOQueryWrapper = new QueryWrapper<>();
        pointDetailPOQueryWrapper.eq("student_no", studentNo);
        pointDetailMapper.delete(pointDetailPOQueryWrapper);
        //?????????????????????
        QueryWrapper<ManualPO> manualPOQueryWrapper = new QueryWrapper<>();
        manualPOQueryWrapper.eq("create_by", studentNo);
        manualMapper.delete(manualPOQueryWrapper);
        //????????????????????????
        QueryWrapper<TopicDetailPO> topicDetailPOQueryWrapper = new QueryWrapper<>();
        topicDetailPOQueryWrapper.eq("student_no", studentNo);
        topicDetailMapper.delete(topicDetailPOQueryWrapper);
        //??????????????????
        QueryWrapper<UploadPO> uploadPOQueryWrapper = new QueryWrapper<>();
        uploadPOQueryWrapper.eq("student_no", studentNo);
        uploadMapper.delete(uploadPOQueryWrapper);
        //??????????????????
        QueryWrapper<KaoQinPO> kaoQinPOQueryWrapper = new QueryWrapper<>();
        kaoQinPOQueryWrapper.eq("student_no", studentNo);
        kaoQinMapper.delete(kaoQinPOQueryWrapper);
        //??????????????????
        QueryWrapper<RatePO> ratePOQueryWrapper = new QueryWrapper<>();
        ratePOQueryWrapper.eq("student_no", studentNo);
        rateMapper.delete(ratePOQueryWrapper);
    }


    /**
     * ????????????
     */
    private List<StudentPO> UpdateExcelBindingContainers(String importFilePath) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File file = new File(importFilePath);
        InputStream inputStream = new FileInputStream(file);
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Row currentRow;
        List<StudentPO> list = new ArrayList<>();
        if (sheet.getLastRowNum() == 0) {
            throw new RuntimeException("Excel????????????");
        }
        for (int i = 1; i<= sheet.getLastRowNum(); i++){
            StudentPO detail = new StudentPO();
            SysUser user = new SysUser();
            UserRolePO userRolePO = new UserRolePO();
            currentRow = sheet.getRow(i);
            if (currentRow == null) {
                continue;
            }
//            //????????????
//            String studentCode = ImportUtils.getJavaValue(currentRow.getCell(0)).toString();
//            if (studentCode.isEmpty()) {
//                throw new BaseException("???" + (i + 1) + "????????????????????????");
//                //throw new RuntimeException("???" + (i + 1) + "????????????????????????");
//            } else {
//                StudentPO code = studentMapper.selectOne(new QueryWrapper<StudentPO>().eq("student_code", studentCode));
//                if (Objects.nonNull(code)) {
//                    throw new BaseException("???" + (i + 1) + "?????????????????????????????????");
//                    //throw new RuntimeException("???" + (i + 1) + "?????????????????????????????????");
//                }
//                detail.setStudentCode(studentCode);
//            }
            //????????????
            String studentNo = ImportUtils.getJavaValue(currentRow.getCell(0)).toString();
            if (studentNo.isEmpty()) {
                throw new BaseException("???" + (i + 1) + "????????????????????????");
                //throw new RuntimeException("???" + (i + 1) + "????????????????????????");
            } else {
                StudentPO no = studentMapper.selectOne(new QueryWrapper<StudentPO>().eq("student_no", studentNo));
                if (Objects.nonNull(no)) {
                    throw new BaseException("???" + (i + 1) + "??????????????????????????????");
                   //throw new RuntimeException("???" + (i + 1) + "??????????????????????????????");
                }
                long sno = Long.parseLong(studentNo);
                user.setUserId(sno);
                user.setUserName(studentNo);
                userRolePO.setUserId(sno);
                detail.setStudentNo(studentNo);
            }
            //????????????
            String studentName = ImportUtils.getJavaValue(currentRow.getCell(1)).toString();
            if (studentName.isEmpty()) {
                throw new BaseException("???" + (i + 1) + "????????????????????????");
                //throw new RuntimeException("???" + (i + 1) + "????????????????????????");
            } else {
                user.setNickName(studentName);
                detail.setStudentName(studentName);
            }
            //????????????
            String studentClass = ImportUtils.getJavaValue(currentRow.getCell(2)).toString();
            if (studentClass.isEmpty()) {
                throw new BaseException("???" + (i + 1) + "????????????????????????");
                //throw new RuntimeException("???" + (i + 1) + "????????????????????????");
            } else {
                ClassInfoPO classInfoPO = classInfoMapper.selectOne(new QueryWrapper<ClassInfoPO>().eq("class_name", studentClass));
                if (Objects.isNull(classInfoPO)) {
                    throw new BaseException("???" + (i + 1) + "???????????????????????????");
                    //throw new RuntimeException("???" + (i + 1) + "???????????????????????????");
                }
                detail.setStudentClass(classInfoPO.getClassCode());
            }
            //????????????
            if (Objects.isNull(currentRow.getCell(3))){
                detail.setStudentGroup(null);
            } else {
                String group = ImportUtils.getJavaValue(currentRow.getCell(3)).toString();
                detail.setStudentGroup(Integer.parseInt(group));
            }
//            //???????????????
//            if (Objects.isNull(currentRow.getCell(5))){
//                detail.setLeader(null);
//            } else {
//                String lead = ImportUtils.getJavaValue(currentRow.getCell(5)).toString();
//                detail.setLeader(Integer.parseInt(lead));
//            }
            detail.setStudentCode(IdUtils.fastSimpleUUID());
            detail.setCreateBy(SecurityUtils.getUsername());
            detail.setCreateTime(DateUtils.getNowDate());
            user.setCreateBy(getUsername());
            user.setPassword(SecurityUtils.encryptPassword("123456"));
            userRolePO.setRoleId((long)100);
            iSysUserService.insertUser(user);
            iSysRoleService.insertUserRole(userRolePO);
            list.add(detail);
        }
        return list;
    }

    /**
     * ??????????????????
     */
    private String importVendor(List<StudentPO> detailList) throws Exception{
        if (StringUtils.isNull(detailList) || detailList.size() == 0) {
            throw new RuntimeException("??????????????????????????????");
        }
        StringBuilder successMsg = new StringBuilder();
        for (StudentPO student: detailList) {
            student.setCreateBy(SecurityUtils.getLoginUser().getUsername());
            student.setCreateTime(DateUtils.getNowDate());
        }
        if (!studentService.saveBatch(detailList)) {
            throw new BaseException("??????????????????????????????");
        }
        successMsg.insert(0, "????????????????????????" + detailList.size() + "???");
        return successMsg.toString();
    }

}
