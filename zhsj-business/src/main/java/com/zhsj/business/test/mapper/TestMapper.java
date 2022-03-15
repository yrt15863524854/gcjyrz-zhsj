package com.zhsj.business.test.mapper;

import com.zhsj.business.test.domain.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestMapper {
    List<Test> getAllTest();
}
