package com.tgg.arcsoftfaceservice;

import com.tgg.arcsoftfaceservice.utils.FaceEngineUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.HashMap;


@SpringBootTest
class ArcsoftFaceServiceApplicationTests {

    @Test
    void contextLoads() {
        FaceEngineUtil faceEngineUtil = new FaceEngineUtil();
        HashMap<String, Object> userInfo = faceEngineUtil.getUserInfo(new File("D:\\FileTest\\1.jpg"));
    }

}
