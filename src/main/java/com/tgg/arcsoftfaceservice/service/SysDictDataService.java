package com.tgg.arcsoftfaceservice.service;

import com.tgg.arcsoftfaceservice.pojo.DictPojo;
import com.tgg.arcsoftfaceservice.pojo.SysDictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
public interface SysDictDataService extends IService<SysDictData> {
    Map<String, Object> getChildPlanLabel(DictPojo bean);
    Map<String, Object> getSysDictDataLabelByType(String type);
    Map<String, Object> getSysDictDataByType(String type);
    Map<String, Object> saveSysDictData(SysDictData sysDictData);
    Map<String, Object> updateSysDictData(SysDictData sysDictData);
    Map<String, Object> deleteSysDictData(String id);
}
