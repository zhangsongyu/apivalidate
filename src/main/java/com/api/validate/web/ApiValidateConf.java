package com.api.validate.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("api-validate")
public class ApiValidateConf {
    private String schema;
    private String host;
    private String path;
    private int port;

    @Override
    public String toString() {
        return "ApiValidateConf{" +
                "schema='" + schema + '\'' +
                ", host='" + host + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
