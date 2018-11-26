package com.api.validate.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("api-validate")
@Data
public class ApiValidateConf {
    private String schema;
    private String host;
    private String path;
    private int port;
    private String apiVersion;
    private  String projectName;



}
