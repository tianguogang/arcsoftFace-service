package com.tgg.arcsoftfaceservice.service;

import com.tgg.arcsoftfaceservice.pojo.SysDictType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
public interface SysDictTypeService extends IService<SysDictType> {
    Map<String, Object> getSysDictType();
    Map<String, Object> saveSysDictType(SysDictType sysDictType);
    Map<String, Object> updateSysDictType(SysDictType sysDictType);
    Map<String, Object> deleteSysDictType(String id);
}
