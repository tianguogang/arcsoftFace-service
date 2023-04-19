package com.tgg.arcsoftfaceservice.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONUtil;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceSimilar;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tgg.arcsoftfaceservice.mapper.SysDictDataMapper;
import com.tgg.arcsoftfaceservice.pojo.FactInfo;
import com.tgg.arcsoftfaceservice.pojo.SysDictData;
import com.tgg.arcsoftfaceservice.service.impl.FactInfoServiceImpl;
import com.tgg.arcsoftfaceservice.utils.FaceEngineUtil;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/fact-info")
public class FactInfoController {
    @Resource
    FactInfoServiceImpl factInfoService;
    @Resource
    SysDictDataMapper sysDictDataMapper;

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveFaceInfo(@RequestParam MultipartFile[] files, String name, String remark) throws IOException {

        for (MultipartFile e : files) {
            FileOutputStream fileOutputStream = new FileOutputStream("./1.jpg");
            fileOutputStream.write(e.getBytes());


        }

        FactInfo factInfo = new FactInfo();
        factInfo.setName(name);
        factInfo.setRemark(remark);
        File faceImg = new File("./1.jpg");
        HashMap<String, String> key = this.getKey();


        FaceEngineUtil faceEngineUtil = new FaceEngineUtil(key.get("appId"), key.get("sdkKey"));
        FaceFeature faceFeature = faceEngineUtil.getFaceFeature(faceImg);
        if (faceImg.exists()) {
            faceImg.deleteOnExit();
        }
        String jsonStr = JSONUtil.toJsonStr(faceFeature);
        factInfo.setFaceData(jsonStr);
        HashMap<String, Object> map1 = factInfoService.saveFaceInfo(factInfo);
        map1.put("data", factInfo);


        return new ResponseEntity<>(map1, HttpStatus.OK);

    }

    @PostMapping("/get")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllFaceInfo() {
        HashMap<String, Object> allFaceInfo = factInfoService.getAllFaceInfo();

        return new ResponseEntity<>(allFaceInfo, HttpStatus.OK);

    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteFaceInfo(@RequestBody Map<String, Object> resout) {


        Map<String, Object> stringObjectMap = factInfoService.deleteFaceInfo(resout.get("id").toString());

        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);
    }


    @PostMapping("/check")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkFaceInfo(@RequestBody Map<String, Object> resout) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        String[] imageData = resout.get("imageData").toString().split(",");
        byte[] result = Base64.decode(imageData[1]);
        for (int i = 0; i < result.length; ++i) {
            if (result[i] < 0) {// 调整异常数据
                result[i] += 256;
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream("./1.jpg");
        fileOutputStream.write(result);
        fileOutputStream.flush();
        fileOutputStream.close();

        HashMap<String, String> key = this.getKey();
        FaceEngineUtil faceEngineUtil = new FaceEngineUtil(key.get("appId"), key.get("sdkKey"));
        File faceImg = new File("./1.jpg");
        FaceFeature faceFeature = null;

            faceFeature = faceEngineUtil.getFaceFeature(faceImg);

          if (faceFeature==null){
              map.put("code", 501);
              map.put("message", "无人脸信息！");
              map.put("data",faceFeature);
              return new ResponseEntity<>(map, HttpStatus.OK);
          }

        List<FactInfo> list = factInfoService.list();
        for (FactInfo e : list) {
            FaceFeature bean = JSONUtil.toBean(e.getFaceData(), FaceFeature.class);

            FaceSimilar faceSimilar = faceEngineUtil.compareFaceFeature(faceFeature, bean);
            if (faceSimilar.getScore() > 0.78) {
                map.put("code", 200);
                map.put("data", faceEngineUtil.getUserInfo(faceImg));
                map.put("message", "恭喜" + e.getName() + "打卡成功！");
                map.put("userInfo",e );
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
        map.put("code", 500);
        map.put("message", "非法用户！");
        map.put("data", faceEngineUtil.getUserInfo(faceImg));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    HashMap<String, String> getKey() {
        QueryWrapper<SysDictData> sysDictDataQueryWrapper = new QueryWrapper<>();
        sysDictDataQueryWrapper.eq("dict_type", "arc_saft");
        String appId = "";
        String sdkKey = "";
        List<SysDictData> sysDictData = sysDictDataMapper.selectList(sysDictDataQueryWrapper);
        for (SysDictData e : sysDictData) {
            if (e.getDictLabel().equals("appId")) {
                appId = e.getDictValue();
            }
            if (e.getDictLabel().equals("sdkKey")) {
                sdkKey = e.getDictValue();
            }

        }
        HashMap<String, String> map = new HashMap<>();
        map.put("appId", appId);
        map.put("sdkKey", sdkKey);
        return map;
    }
}
