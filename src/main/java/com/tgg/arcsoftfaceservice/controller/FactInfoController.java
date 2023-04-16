package com.tgg.arcsoftfaceservice.controller;


import cn.hutool.json.JSONUtil;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceSimilar;
import com.tgg.arcsoftfaceservice.pojo.FactInfo;
import com.tgg.arcsoftfaceservice.service.FactInfoService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
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
        FaceEngineUtil faceEngineUtil = new FaceEngineUtil();
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
    public ResponseEntity<Map<String, Object>> checkFaceInfo(@RequestParam MultipartFile[] files) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        for (MultipartFile e : files) {
            FileOutputStream fileOutputStream = new FileOutputStream("./1.jpg");
            fileOutputStream.write(e.getBytes());
        }
        File faceImg = new File("./1.jpg");
        FaceEngineUtil faceEngineUtil = new FaceEngineUtil();
        FaceFeature faceFeature = faceEngineUtil.getFaceFeature(faceImg);
        List<FactInfo> list = factInfoService.list();
        for (FactInfo e : list) {
            FaceFeature bean = JSONUtil.toBean(e.getFaceData(), FaceFeature.class);

            FaceSimilar faceSimilar = faceEngineUtil.compareFaceFeature(faceFeature, bean);
            if (faceSimilar.getScore() > 0.78) {
                map.put("code", 200);
                map.put("data", e);
                map.put("message", "恭喜"+e.getName()+"打卡成功！");
                map.put("userInfo", faceEngineUtil.getUserInfo(faceImg));
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
        System.out.println(files);
        map.put("code", 500);
        map.put("message", "非法用户！");
        map.put("data", faceEngineUtil.getUserInfo(faceImg));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
