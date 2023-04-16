package com.tgg.arcsoftfaceservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tgg.arcsoftfaceservice.pojo.FactInfo;
import com.tgg.arcsoftfaceservice.mapper.FactInfoMapper;
import com.tgg.arcsoftfaceservice.service.FactInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
@Service
public class FactInfoServiceImpl extends ServiceImpl<FactInfoMapper, FactInfo> implements FactInfoService {
    @Resource
    FactInfoMapper factInfoMapper;
    @Resource
    FactInfoService factInfoService;
    @Override
    public HashMap<String, Object> saveFaceInfo(FactInfo factInfo) {
        int insert = factInfoMapper.insert(factInfo);
        HashMap<String, Object> map = new HashMap<>();
        if (insert>0){
            map.put("code",200);
            map.put("message","存储用户人脸信息成功");

        }
        return map;
    }

    @Override
    public HashMap<String, Object> deleteFaceInfo(String id) {
        UpdateWrapper<FactInfo> factInfoUpdateWrapper = new UpdateWrapper<>();
        factInfoUpdateWrapper.eq("id", id);
        boolean remove = factInfoService.remove(factInfoUpdateWrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",200);
        map.put("data",remove);
        map.put("message","删除成功");

        return map;
    }

    @Override
    public HashMap<String, Object> getAllFaceInfo() {
        List<FactInfo> factInfos = factInfoMapper.selectList(null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",200);
        map.put("message","获取所有用户信息成功");
        map.put("data",factInfos);
        return map;
    }
}
