package com.tgg.arcsoftfaceservice.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.tgg.arcsoftfaceservice.pojo.SysDictType;
import com.tgg.arcsoftfaceservice.service.impl.SysDictTypeServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/sys-dict-type")
public class SysDictTypeController {
    @Resource
    SysDictTypeServiceImpl sysDictTypeService;
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveSysDictType(@RequestBody Map<String, Object> resout) {

        Map<String, Object> stringObjectMap = null;
        SysDictType bean = BeanUtil.toBean(resout, SysDictType.class);
        if (ObjectUtil.isEmpty(bean.getDictId())) {
            stringObjectMap = sysDictTypeService.saveSysDictType(bean);
        } else {
            stringObjectMap = sysDictTypeService.updateSysDictType(bean);

        }
        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);
    }
    @PostMapping("/get")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSysDictType() {

        Map<String, Object> stringObjectMap = sysDictTypeService.getSysDictType();

        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSysDictType(@RequestBody Map<String, Object> resout) {

        String id = resout.get("id").toString();
        Map<String, Object> stringObjectMap = sysDictTypeService.deleteSysDictType(id);

        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);
    }

}
