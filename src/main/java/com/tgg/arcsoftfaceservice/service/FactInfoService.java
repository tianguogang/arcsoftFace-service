package com.tgg.arcsoftfaceservice.service;

import com.tgg.arcsoftfaceservice.pojo.FactInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
public interface FactInfoService extends IService<FactInfo> {
    HashMap<String,Object> saveFaceInfo(FactInfo factInfo);
    HashMap<String,Object> deleteFaceInfo(String id);
    HashMap<String,Object> getAllFaceInfo();
}
