package com.tgg.arcsoftfaceservice.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.tgg.arcsoftfaceservice.pojo.DictPojo;
import com.tgg.arcsoftfaceservice.pojo.SysDictData;
import com.tgg.arcsoftfaceservice.service.impl.SysDictDataServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/sys-dict-data")
public class SysDictDataController {
    @Resource
    SysDictDataServiceImpl sysDictDataService;

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveSysDictType(@RequestBody Map<String, Object> resout) {

        SysDictData bean = BeanUtil.toBean(resout, SysDictData.class);
        Map<String, Object> stringObjectMap;
        if (ObjectUtil.isEmpty(bean.getDictCode())) {
            stringObjectMap = sysDictDataService.saveSysDictData(bean);
        } else {
            stringObjectMap = sysDictDataService.updateSysDictData(bean);
        }
        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);

    }

    @PostMapping("/getLabel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSysDictgetLabel(@RequestBody Map<String, Object> resout) {
        String id = null;
        if (!BeanUtil.isEmpty(resout.get("id"))) {
            id = resout.get("id").toString();
        }

        Map<String, Object> stringObjectMap = sysDictDataService.getSysDictDataLabelByType(id);

        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);
    }
    @PostMapping("/getChildPlanLabel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getChildPlanLabel(@RequestBody Map<String, Object> resout) {
        DictPojo bean = BeanUtil.toBean(resout, DictPojo.class);
        Map<String, Object> stringObjectMap = sysDictDataService.getChildPlanLabel(bean);
        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);
    }
    @PostMapping("/get")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSysDictType(@RequestBody Map<String, Object> resout) {
        String id = null;
        if ( !ObjectUtil.isEmpty(resout.get("id"))) {
            id = resout.get("id").toString();
        }

        Map<String, Object> stringObjectMap = sysDictDataService.getSysDictDataByType(id);

        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSysDictType(@RequestBody Map<String, Object> resout) {


        Map<String, Object> stringObjectMap = sysDictDataService.deleteSysDictData(resout.get("id").toString());

        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);
    }

}
