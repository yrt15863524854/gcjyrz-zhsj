package com.zhsj.business.test.mapper;

import com.zhsj.business.test.domain.PaperParameter;
import com.zhsj.business.test.domain.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TestMapper {
    List<Test> getAllTest();
    List<Test> dividePaper(@Param("dto")PaperParameter paperParameter);
}
