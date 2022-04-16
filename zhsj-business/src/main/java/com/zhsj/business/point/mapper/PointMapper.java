package com.zhsj.business.point.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsj.business.point.domain.PointPO;
import org.apache.ibatis.annotations.Param;

/**
 * @interfaceName: PointMapper
 * @description: TODO
 * @author: yrt
 * date: 2022/4/14 14:37
 * version 1.0
 **/
public interface PointMapper extends BaseMapper<PointPO> {
    int insertPoint(@Param("PO") PointPO po);
    int updatePoint(@Param("PO") PointPO po);
}
