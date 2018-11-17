package com.api.validate.web.runner;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.validate.web.ApiValidateConf;
import com.api.validate.web.util.FileUtil;
import com.api.validate.web.util.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

/**
 * 这里通过设定value的值来指定执行顺序
 */
@Component
@Order(value = 1)
public class MyApplicationRunner implements ApplicationRunner {
    @Autowired
    private ApiValidateConf apiValidateConf;

    @Override
    public void run(ApplicationArguments var1) throws Exception {
        System.out.println("report swagger.json.........");
        String root = System.getProperty("user.dir");
        System.out.println(root);
        BufferedReader reader =
                new BufferedReader(new FileReader(root + "./swagger.json"));
        String swaggerJson = FileUtil.readAllLinesWithStream(reader);
      JSONObject jsonObject= com.alibaba.fastjson.JSON.parseObject(swaggerJson);
      jsonObject.put("projectName","testproject");
        System.out.println(jsonObject.toString());
        HttpUtil.sendToServer(swaggerJson, apiValidateConf);

    }

    @Test
    public void ttt() throws Exception {
        run(null);
    }

}