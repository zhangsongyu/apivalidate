package com.api.validate.web;

import lombok.Data;

@Data
public class ParamEntity {
    private String paramName;
    private String paramType;
    private String simplepParamType;
    private String paramsAnnotation;

    private String value;//格式 “string”    {a:1}
    private String checkValue;// “【a-z】”   {a:d?}




}
