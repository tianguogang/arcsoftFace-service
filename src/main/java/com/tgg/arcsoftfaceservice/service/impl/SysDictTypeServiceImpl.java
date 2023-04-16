package com.tgg.arcsoftfaceservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tgg.arcsoftfaceservice.mapper.SysDictDataMapper;
import com.tgg.arcsoftfaceservice.pojo.SysDictData;
import com.tgg.arcsoftfaceservice.pojo.SysDictType;
import com.tgg.arcsoftfaceservice.mapper.SysDictTypeMapper;
import com.tgg.arcsoftfaceservice.service.SysDictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Resource
    SysDictTypeMapper sysDictTypeMapper;
    @Resource
    SysDictDataMapper sysDictDataMapper;

    @Override
    public Map<String, Object> getSysDictType() {
        List<SysDictType> sysDictTypes = sysDictTypeMapper.selectList(null);
        HashMap<String, Object> map = new HashMap<>();

        map.put("data", sysDictTypes);
        map.put("code", 200);
        map.put("message", "获取字典类型列表成功");
        return map;
    }

    @Override
    public Map<String, Object> saveSysDictType(SysDictType sysDictType) {

        int insert = sysDictTypeMapper.insert(sysDictType);
        HashMap<String, Object> map = new HashMap<>();

        map.put("data", insert);

        if (insert > 0) {
            map.put("message", "新增数据字典类型成功");
            map.put("code", 200);
        } else {
            map.put("message", "新增数据字典类型失败");
            map.put("code", 500);
        }
        return map;
    }

    @Override
    public Map<String, Object> updateSysDictType(SysDictType sysDictType) {
        SysDictType sysDictType1 = sysDictTypeMapper.selectById(sysDictType.getDictId());
        UpdateWrapper<SysDictType> sysDictTypeUpdateWrapper = new UpdateWrapper<>();
        sysDictTypeUpdateWrapper.eq("dict_id", sysDictType.getDictId());
        int update = sysDictTypeMapper.update(sysDictType, sysDictTypeUpdateWrapper);
        UpdateWrapper<SysDictData> sysDictDataUpdateWrapper = new UpdateWrapper<>();
        int update1 = 0;
        if (!sysDictType1.getDictType().equals(sysDictType.getDictType())) {
            String old_type = sysDictType1.getDictType();
            String new_type = sysDictType.getDictType();
            update1 = sysDictDataMapper.updateType(old_type, new_type);
        }
        HashMap<String, Object> map = new HashMap<>();

        map.put("data", update);

        if (update > 0) {
            map.put("message", "修改数据字典类型成功,字典数据类型被修改" + update1 + "条");
            map.put("code", 200);
        } else {
            map.put("message", "修改数据字典类型失败");
            map.put("code", 500);
        }
        return map;
    }

    @Override
    public Map<String, Object> deleteSysDictType(String id) {
        SysDictType sysDictType = sysDictTypeMapper.selectById(id);
        UpdateWrapper<SysDictData> sysDictDataUpdateWrapper = new UpdateWrapper<>();
        sysDictDataUpdateWrapper.eq("dict_type", sysDictType.getDictType());
        int delete1 = sysDictDataMapper.delete(sysDictDataUpdateWrapper);

        UpdateWrapper<SysDictType> objectUpdateWrapper = new UpdateWrapper<>();
        objectUpdateWrapper.eq("dict_type", sysDictType.getDictType());
        int delete = sysDictTypeMapper.delete(objectUpdateWrapper);
        HashMap<String, Object> map = new HashMap<>();

        map.put("data", delete + "--" + delete1);

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
