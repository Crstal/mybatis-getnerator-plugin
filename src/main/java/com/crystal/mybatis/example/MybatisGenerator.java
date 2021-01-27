package com.crystal.mybatis.example;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author xiaoe
 * @Date 2021/1/27
 * @Version 1.0
 **/
public class MybatisGenerator {

    public static void main(String[] args) {
        try {
            System.out.println("**************start generator***************");
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            File configFile = new File(MybatisGenerator.class.getResource("/generatorConfig.xml").getFile());
            ConfigurationParser cp = new ConfigurationParser(warnings);

            Configuration config =cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator =new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            System.out.println("*******************end generator*************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
