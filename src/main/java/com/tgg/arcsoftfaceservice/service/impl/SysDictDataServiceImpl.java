package com.tgg.arcsoftfaceservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tgg.arcsoftfaceservice.mapper.SysDictTypeMapper;
import com.tgg.arcsoftfaceservice.pojo.DictPojo;
import com.tgg.arcsoftfaceservice.pojo.SysDictData;
import com.tgg.arcsoftfaceservice.mapper.SysDictDataMapper;
import com.tgg.arcsoftfaceservice.pojo.SysDictType;
import com.tgg.arcsoftfaceservice.service.SysDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {
    @Resource
    SysDictDataMapper sysDictDataMapper;
    @Resource
    SysDictTypeMapper sysDictTypeMapper;

    @Override
    public Map<String, Object> getChildPlanLabel(DictPojo bean) {

        ArrayList<String> strings = new ArrayList<>();
        strings.add(bean.getClass_num());
        strings.add(bean.getSort_batch());
        strings.add(bean.getPims_breed());
        strings.add(bean.getSort_finished_class());
        strings.add(bean.getPims_umbrella_shape());
        strings.add(bean.getProject_type());
        strings.add(bean.getProduct_place());


        HashMap<String, Object> map = new HashMap<>();
        for (String dictType : strings) {
            QueryWrapper<SysDictData> sysDictDataQueryWrapper = new QueryWrapper<>();

            if (StringUtils.isNotEmpty(dictType)) {
                sysDictDataQueryWrapper.eq("dict_type", dictType);
                List<SysDictData> sysDictData = sysDictDataMapper.selectList(sysDictDataQueryWrapper);
                ArrayList<Object> objects = new ArrayList<>();

                for (SysDictData e : sysDictData) {
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("value", e.getDictValue());
                    map1.put("label", e.getDictLabel());
                    objects.add(map1);
                }
                map.put(dictType, objects);

            } else {
                map.put(dictType, null);
            }
        }
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("code", 200);
        map1.put("message", "获取字典数据列表成功");
        map1.put("data",map);
        return map1;
    }

    @Override
    public Map<String, Object> getSysDictDataLabelByType(String type) {
        QueryWrapper<SysDictData> sysDictDataQueryWrapper = new QueryWrapper<>();
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isNotEmpty(type)) {
            sysDictDataQueryWrapper.eq("dict_type", type);
            List<SysDictData> sysDictData = sysDictDataMapper.selectList(sysDictDataQueryWrapper);
            ArrayList<Object> objects = new ArrayList<>();

            for (SysDictData e : sysDictData) {
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("value", e.getDictValue());
                map1.put("label", e.getDictLabel());
                objects.add(map1);
            }
            map.put("data", objects);
            map.put("code", 200);
            map.put("message", "获取字典数据列表成功");
        } else {
            map.put("data", null);
            map.put("code", 500);
            map.put("message", "数据字典类型为空，请联系技术人员");
        }
        return map;
    }

    @Override
    public Map<String, Object> getSysDictDataByType(String type) {
        QueryWrapper<SysDictData> sysDictDataQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(type)) {
            sysDictDataQueryWrapper.eq("dict_type", type);
        }
        List<SysDictData> sysDictData = sysDictDataMapper.selectList(sysDictDataQueryWrapper);
        HashMap<String, Object> map = new HashMap<>();

        List<SysDictType> sysDictTypes = sysDictTypeMapper.selectList(null);
        ArrayList<Object> objects = new ArrayList<>();
        for (SysDictType e : sysDictTypes) {
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("value", e.getDictType());
            map1.put("label", e.getDictName());
            objects.add(map1);
        }


        map.put("data", sysDictData);
        map.put("code", 200);
        map.put("options", objects);
        map.put("message", "获取字典数据列表成功");
        return map;
    }

    @Override
    public Map<String, Object> saveSysDictData(SysDictData sysDictData) {
        int insert = sysDictDataMapper.insert(sysDictData);
        HashMap<String, Object> map = new HashMap<>();
        if (insert > 0) {
            map.put("message", "新增数据字典成功");
            map.put("code", 200);
        } else {
            map.put("message", "新增数据字典失败");
            map.put("code", 500);
        }
        return map;
    }

    @Override
    public Map<String, Object> updateSysDictData(SysDictData sysDictData) {
        UpdateWrapper<SysDictData> sysDictDataUpdateWrapper = new UpdateWrapper<>();
        sysDictDataUpdateWrapper.eq("dict_code", sysDictData.getDictCode());
        int update = sysDictDataMapper.update(sysDictData, sysDictDataUpdateWrapper);
        HashMap<String, Object> map = new HashMap<>();

        map.put("data", update);

        if (update > 0) {
            map.put("message", "修改数据字典成功");
            map.put("code", 200);
        } else {
            map.put("message", "修改数据字典失败");
            map.put("code", 500);
        }
        return map;
    }

    @Override
    public Map<String, Object> deleteSysDictData(String id) {
        UpdateWrapper<SysDictData> sysDictDataUpdateWrapper = new UpdateWrapper<>();
        sysDictDataUpdateWrapper.eq("dict_code", id);
        int delete = sysDictDataMapper.delete(sysDictDataUpdateWrapper);
        HashMap<String, Object> map = new HashMap<>();

        map.put("data", delete);

        if (delete > 0) {
            map.put("message", "删除数据字典类型成功");
            map.put("code", 200);
        } else {
            map.put("message", "删除数据字典类型失败");
            map.put("code", 500);
        }
        return map;
    }
}
