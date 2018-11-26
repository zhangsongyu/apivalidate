package com.api.validate.web.runner;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.validate.web.ApiValidateConf;
import com.api.validate.web.util.FileUtil;
import com.api.validate.web.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

/**
 * 这里通过设定value的值来指定执行顺序
 */
@Component
@Order(value = 2)
public class MyApplicationRunner implements ApplicationRunner {
    @Autowired
    private ApiValidateConf apiValidateConf;

    @Override
    public void run(ApplicationArguments var1) throws Exception {
/*        System.out.println("report swagger.json.........");
        String root = System.getProperty("user.dir");
        System.out.println(root);
        BufferedReader reader =
                new BufferedReader(new FileReader(root + "./swagger.json"));
        String swaggerJson = FileUtil.readAllLinesWithStream(reader);

        ObjectMapper mapper = new ObjectMapper();
        Map ms = mapper.readValue(swaggerJson, Map.class);
        apiValidateConf.setApiVersion(((Map<String, String>) ms.get("info")).get("version"));
        apiValidateConf.setProjectName(((Map<String, String>) ms.get("info")).get("title"));

        ms.put("projectName", apiValidateConf.getProjectName());
        HttpUtil.sendToServer(mapper.writeValueAsString(ms), apiValidateConf);*/


    }


    @Test
    public void ttt() throws Exception {
        run(null);
    }

}