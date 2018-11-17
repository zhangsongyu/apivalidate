package com.api.validate.web;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Data
public class MethodEntity {
    private String methodName;
    private RequestMethod requestMethod;//get post
    private List<String> methodAnnotation;
    private String[] methodMapping;
    private List<ParamEntity> params;

}
