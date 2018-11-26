package com.api.validate.web.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.validate.web.ApiValidateConf;
import com.api.validate.web.BodyReaderHttpServletRequestWrapper;
import com.api.validate.web.entity.FlattenKV;
import com.api.validate.web.util.FileUtil;
import com.api.validate.web.util.HttpUtil;
import com.api.validate.web.util.MapUtil;
import com.api.validate.web.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(1)
@Component
@WebFilter(filterName = "testFilter1", urlPatterns = "/*")
public class TestFilterFirst implements Filter {
    private static Logger logger = LoggerFactory.getLogger(TestFilterFirst.class);
    @Autowired
    private ApiValidateConf apiValidateConf;

    Map swaggerValidate = new HashMap();

    @Override
    public void init(FilterConfig filterConfig) {
        try {
            sendToServer();
            String res = HttpUtil.loadRegularFromServer(apiValidateConf.getProjectName(), apiValidateConf.getApiVersion(), apiValidateConf);
            swaggerValidate = new ObjectMapper().readValue(res, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if (null == requestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = requestWrapper.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            JSONObject obj = JSON.parseObject(sb.toString());
            System.out.println(sb.toString());

            try {
                paramsCompare(servletRequest, servletResponse, sb.toString());
                filterChain.doFilter(requestWrapper, servletResponse);
            } catch (Exception ex) {
                servletResponse.setCharacterEncoding("utf8");
                servletResponse.getWriter().println(ex.getMessage());
            }
            reader.close();
        }
    }

    @Override
    public void destroy() {
    }

    public void paramsCompare(ServletRequest servletRequest, ServletResponse servletResponse, String body) throws IOException {

        String methodType = ((RequestFacade) servletRequest).getMethod();
        String requestURI = ((RequestFacade) servletRequest).getRequestURI();
        Map requestBody = JSON.parseObject(body, HashMap.class);


        List<Map<String, Map<String, Object>>> ss = (List<Map<String, Map<String, Object>>>) swaggerValidate.get("paths");
        String requestUrl = requestURI;
        String beanName = ss.stream().filter(f ->
                StringUtil.urlMatch(requestUrl,
                        f.get("path").get("requestMapping").toString()) &&
                        f.get("path").get("requestMethod").toString().equalsIgnoreCase(methodType)).collect(Collectors.toList())
                .get(0)
                .get("path").get("definition").toString();
        List<FlattenKV<String, Object>> req = MapUtil.flatten(requestBody, beanName);

        Map<String, Object> regularMap = ((Map<String, Map<String, Object>>) swaggerValidate.get("bodyRegulars")).get(beanName);
        List<FlattenKV<String, Object>> listRegular = MapUtil.flatten(regularMap, beanName);

        Map<String, Object> regualrMap = new HashMap<>();
        listRegular.stream().filter(f -> null != f.getV() &&
                req.stream().map(m -> m.getK()).collect(Collectors.toList()).contains(f.getK()))
                .forEach(m -> regualrMap.put(m.getK(), m.getV()));

        req.forEach(q -> {
            String regx=regualrMap.getOrDefault(q.getK(), "").toString();
            if (!q.getV().toString().matches(regx.isEmpty()?".*":regx)) {
                throw new RuntimeException("参数不符合规范：" + q.getK() + " -- " + q.getV());
            }
        });
        System.out.println(req);


    }

    public void sendToServer() throws Exception {
        System.out.println("report swagger.json.........");
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
        HttpUtil.sendToServer(mapper.writeValueAsString(ms), apiValidateConf);
    }
}