package com.tgg.arcsoftfaceservice.utils;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tgg.arcsoftfaceservice.mapper.SysDictDataMapper;
import com.tgg.arcsoftfaceservice.pojo.SysDictData;
import jakarta.annotation.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

public class FaceEngineUtil {
    @Resource
    SysDictDataMapper sysDictDataMapper;


    public FaceEngineUtil() {
        QueryWrapper<SysDictData> sysDictDataQueryWrapper = new QueryWrapper<>();
        sysDictDataQueryWrapper.eq("dict_type","arc_saft");
        List<SysDictData> sysDictData = sysDictDataMapper.selectList(sysDictDataQueryWrapper);
            String appId = " ";
            String sdkKey = " ";
        for (SysDictData e:sysDictData) {
            if (e.getDictLabel().equals("appId")){
                appId=e.getDictValue();
            }  if (e.getDictLabel().equals("sdkKey")){
                sdkKey=e.getDictValue();
            }

        }
        //激活引擎
        errorCode = faceEngine.activeOnline(appId, sdkKey);

        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("引擎激活失败");
        }


        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("获取激活文件信息失败");
        }

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16);
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);


        //初始化引擎
        errorCode = faceEngine.init(engineConfiguration);

        if (errorCode != ErrorInfo.MOK.getValue()) {
            System.out.println("初始化引擎失败");
        }

    }

    //    获取人脸特征
    FaceEngine faceEngine = new FaceEngine("D:\\program\\NewMedia\\arcsoftFace\\arcsoftFace-service\\src\\main\\resources\\libs\\WIN64");
    int errorCode;

    public FaceFeature getFaceFeature(File file) {


        //人脸检测
        ImageInfo imageInfo = getRGBData(file);
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        System.out.println(faceInfoList);

        //特征提取
        FaceFeature faceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        return faceFeature;
    }

    //    相似度比对
    public FaceSimilar compareFaceFeature(FaceFeature targetFaceFeature0, FaceFeature sourceFaceFeature0) {
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(targetFaceFeature0.getFeatureData());
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(sourceFaceFeature0.getFeatureData());

        FaceSimilar faceSimilar = new FaceSimilar();
        faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        return faceSimilar;
    }

    //用户信息获取
    public HashMap<String, Object> getUserInfo(File file) {
        ImageInfo imageInfo = getRGBData(file);
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);

        //设置活体测试
        errorCode = faceEngine.setLivenessParam(0.5f, 0.7f);
        //人脸属性检测
        FunctionConfiguration configuration = new FunctionConfiguration();
        configuration.setSupportAge(true);
        configuration.setSupportFace3dAngle(true);
        configuration.setSupportGender(true);
        configuration.setSupportLiveness(true);
        errorCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList, configuration);
        HashMap<String, Object> map = new HashMap<>();

        //性别检测
        List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
        errorCode = faceEngine.getGender(genderInfoList);
        System.out.println("性别：" + genderInfoList.get(0).getGender());
        map.put("gender", genderInfoList.get(0).getGender());
        //年龄检测
        List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
        errorCode = faceEngine.getAge(ageInfoList);
        System.out.println("年龄：" + ageInfoList.get(0).getAge());
        map.put("age", ageInfoList.get(0).getAge());
        //3D信息检测
        List<Face3DAngle> face3DAngleList = new ArrayList<Face3DAngle>();
        errorCode = faceEngine.getFace3DAngle(face3DAngleList);
        System.out.println("3D角度：" + face3DAngleList.get(0).getPitch() + "," + face3DAngleList.get(0).getRoll() + "," + face3DAngleList.get(0).getYaw());
        map.put("3DAngle", face3DAngleList.get(0));
        //活体检测
        List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
        errorCode = faceEngine.getLiveness(livenessInfoList);
        System.out.println("活体：" + livenessInfoList.get(0).getLiveness());
        map.put("liveness", livenessInfoList.get(0).getLiveness());


        return map;
    }


}
