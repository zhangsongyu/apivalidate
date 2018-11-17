package com.api.validate.web.util;

import java.util.Arrays;
import java.util.List;

public class StringUtil {
    //toso 正则匹配url

    /*
     */

    String url = "/project/677/versions";

    String r = "/project/{projectId}/versionfs";


    public static Boolean urlMatch(String requestUrl, String urlTemplate) {

        List<String> urlParts = Arrays.asList(requestUrl.split("/"));
        List<String> tempParts = Arrays.asList(requestUrl.split("/"));
        if (urlParts.size() != tempParts.size()) {
            return false;
        } else {
            for (int i = 0; i < urlParts.size(); i++) {
                if (!urlParts.get(i).equals(tempParts.get(i)) && !tempParts.get(i).startsWith("{")) {
                    return false;
                }
            }
        }
        return true;
    }

}
