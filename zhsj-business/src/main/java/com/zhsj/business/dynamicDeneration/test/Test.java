package com.zhsj.business.dynamicDeneration.test;

import com.zhsj.business.dynamicDeneration.utils.BeanAddPropertiesUtil;
import com.zhsj.business.point.domain.PointPO;
import com.zhsj.business.point.dto.PointExcelDto;
import com.zhsj.business.point.service.PointService;
import org.apache.poi.util.LocaleID;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: Test
 * @description: TODO
 * @author: yrt
 * date: 2022/4/28 17:22
 * version 1.0
 **/
public class Test {
    @Resource
    private static PointService pointService;
    public static void main(String[] args) {
        PointExcelDto pointExcelDto = new PointExcelDto();
        pointExcelDto.setStudentNo("201111");
        Map<String, Object> map = new HashMap<>();
        //List<PointPO> list = pointService.list();
        PointPO pointPO = new PointPO();
        pointPO.setPointName("评分点1");
        ArrayList<PointPO> list = new ArrayList<>();
        list.add(pointPO);
        List<String> pointList = new ArrayList<>();
        list.forEach(item -> {
            pointList.add(item.getPointName());
        });
//        if (CollectionUtils.isEmpty(pointList)) {
//            for (int i = 0; i < pointList.size(); i++) {
//                map.put("pointName" + i, pointList.get(i));
//            }
//        }
        map.put("PointName1", String.class);
        PointExcelDto target = (PointExcelDto)BeanAddPropertiesUtil.getTarget(pointExcelDto, map);
        String string = target.toString();
        System.out.println(string);
    }
}
