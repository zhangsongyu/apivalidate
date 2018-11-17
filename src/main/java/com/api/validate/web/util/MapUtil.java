package com.api.validate.web.util;


import com.api.validate.web.entity.FlattenKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtil {

    private MapUtil() {
    }

    public static Object mapValueByPath() {
        return 0;
    }

    public static List<FlattenKV<String, Object>> flatten(Map<String, Object> map, String perfix) {
        MapFlattenWorker worker = new MapFlattenWorker(perfix);
        worker.printMap(map);
        return worker.result;
    }

    static class MapFlattenWorker {
        String lastkey = "";
        String path = "";
        String perfix = "";
        List<FlattenKV<String, Object>> result = new ArrayList<>();

        private MapFlattenWorker() {
        }

        private MapFlattenWorker(String perfix) {
            this.perfix = perfix;
        }

        public void printMap(Map<String, Object> map) {
            map.forEach((k, v) -> {
                if (v instanceof Map) {
                    lastkey = lastkey + "." + k;
                    System.out.println("map：  " + perfix + path + lastkey + "->" + v);
                    result.add(new FlattenKV<>(perfix + path + lastkey, v));
                    printMap((Map<String, Object>) v);
                } else {
                    path = path + lastkey + "." + k;
                    result.add(new FlattenKV<>(perfix + path, v));
                    result.add(new FlattenKV<>(perfix + path, v));
                    System.out.println("value :  " + perfix + path + "->" + v);
                }
                path = "";
            });
            lastkey = "";
        }
    }
}
