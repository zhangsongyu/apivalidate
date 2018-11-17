package com.api.validate.web.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.validate.web.BodyReaderHttpServletRequestWrapper;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@Order(2)
@Component
@WebFilter(filterName = "testFilter1", urlPatterns = "/*")
public class TestFilterFirst implements Filter {
    private static Logger logger = LoggerFactory.getLogger(TestFilterFirst.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        servletRequest.getParameterMap();
        String methodType = ((RequestFacade) servletRequest).getMethod();
        String requestURI = ((RequestFacade) servletRequest).getRequestURI();

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
            filterChain.doFilter(requestWrapper, servletResponse);
            reader.close();
        }
    }

    @Override
    public void destroy() {
    }


}