package com.tgg.arcsoftfaceservice.gen;



import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        String dir = System.getProperty("user.dir");
        String finalDir = dir;
        String finalDir1 = dir;
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/arcsoftface?serverTimezone=UTC", "root", "123456")
//                全局配置
                .globalConfig(builder -> {
                    builder.author("田国刚") // 设置创建作者
//                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() //覆盖已存在文件
                            .disableOpenDir()
                            .outputDir(finalDir +"/src/main/java")  // 指定输出目录
                            .dateType(DateType.TIME_PACK)
                            .commentDate("yyyy-MM-dd-hh:mm:ss");

                })
//                包配置
                .packageConfig(builder -> {
                    builder.parent("edu.kmu.pims") // 设置父包名
//                            .moduleName("c-pims-file-60003") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, finalDir1 + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("fact_info","check_on_work_attendance") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀

                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
