package com.zhsj.business.kaoqin.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsj.common.core.domain.BaseEntityPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("business_kaoqin")
public class KaoQinPO extends BaseEntityPO {

    private static final long serialVersionUID = 1L;
    /**
     * 考勤id
     */
    private Integer id;
    /**
     * 考勤信息
     */
    private String kaoqinInfo;
    /**
     * 考勤学生
     */
    private String kaoqinStudent;
    /**
     * 考勤状态
     */
    private Integer kaoqinStatus;
    /**
     * 学生学号
     */
    private String studentNo;
    /**
     * 班级编号
     */
    private String classCode;
}
