package com.api.validate.web.util;

import com.api.validate.web.ApiValidateConf;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpUtil {
    static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static void sendToServer(String swaggerJson, ApiValidateConf apiValidateConf) {

        System.out.println(apiValidateConf);
        System.out.println("CONFIG: " + apiValidateConf.toString());
        String result = "";
        URI uri = null;
        try {
            uri = new URIBuilder()
                    .setScheme(apiValidateConf.getSchema())
                    .setHost(apiValidateConf.getHost())
                    .setPath(apiValidateConf.getPath())
                    .setPort(apiValidateConf.getPort())
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity(swaggerJson, "utf-8");
        entity.setContentEncoding("UTF-8");

        CloseableHttpResponse response2 = null;
        HttpEntity entity2 = null;
        try {
            httpPost.setEntity(entity);
            response2 = httpclient.execute(httpPost);
            if (response2.getStatusLine().getStatusCode() != 200) {
                System.out.println("获取列表数据失败");
            }
            entity2 = response2.getEntity();
            result = EntityUtils.toString(entity2);
            System.out.println(result);
            EntityUtils.consume(entity2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String loadRegularFromServer(String projectName, String version, ApiValidateConf apiValidateConf) {
        String result = "";
        URI uri = null;
        try {
            uri = new URIBuilder()
                    .setScheme(apiValidateConf.getSchema())
                    .setHost(apiValidateConf.getHost())
                    .setPath(apiValidateConf.getPath() + "/" + projectName + "/" + version)
                    .setPort(apiValidateConf.getPort())
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("Content-Type", "application/json");

        CloseableHttpResponse response2 = null;
        HttpEntity entity2 = null;
        try {
            response2 = httpclient.execute(httpGet);
            if (response2.getStatusLine().getStatusCode() != 200) {
                System.out.println("获取列表数据失败");
            }
            entity2 = response2.getEntity();
            result = EntityUtils.toString(entity2);
            System.out.println(result);
            EntityUtils.consume(entity2);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
