import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.api.validate.web.entity.FlattenKV;
import com.api.validate.web.util.MapUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonTest {
    @Test
    public void test() {
        String json = "{\n" +
                "  \"productId\": \"GS-AS-0000-0005-0004-0001\",\n" +
                "  \"page\": {\n" +
                "    \"order\": \"start_time desc\",\n" +
                "    \"pagesize\": 10,\n" +
                "    \"start\": 2010,\n" +
                "    \"page\": {\n" +
                "      \"order\": \"nn\",\n" +
                "      \"pagesize\": 0,\n" +
                "      \"start\": 0,\n" +
                "      \"page\": 0,\n" +
                "      \"end\": 0\n" +
                "    },\n" +
                "    \"end\": null\n" +
                "  },\n" +
                "  \"taskFilter\": {\n" +
                "    \"completenessFloor\": 0.76846474,\n" +
                "    \"completenessUpper\": 0.9,\n" +
                "    \"endDateFloor\": 20181102,\n" +
                "    \"endDateUpper\": 20181102,\n" +
                "    \"startDateFloor\": 20181102,\n" +
                "    \"startDateUpper\": 20181102,\n" +
                "    \"endTimestampFloor\": 1541088000000,\n" +
                "    \"endTimestampUpper\": 1541088000000,\n" +
                "    \"startTimestampFloor\": 1541088000000,\n" +
                "    \"startTimestampUpper\": 1541088000000,\n" +
                "    \"mapName\": \"20181102\",\n" +
                "    \"month\": \"2018-11\",\n" +
                "    \"operator\": \"admin\",\n" +
                "    \"pathName\": \"\",\n" +
                "    \"week\": \"2018-11-0244\"\n" +
                "  }\n" +
                "}";

        Map<String, Object> map = JSON.parseObject(json, Map.class);
        System.out.println();

        List<FlattenKV<String, Object>> li = MapUtil.flatten(map,"tb");
        li.stream().sorted(Comparator.comparing(FlattenKV::getK)).map(m -> m.getK() + "--->>>" + m.getV()).collect(Collectors.toList());
        System.out.println(li);
    }

    @Test
    public void classToJson() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<Persion> p = Persion.class;
        Persion p1 = p.newInstance();

        System.out.println(JSON.toJSONString(p1, SerializerFeature.WriteMapNullValue));
    }

    @Test
    public void collectionToJson() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<Persion> p = Persion.class;

        Persion p1 = null;
        try {
            p1 = (Persion) p.getConstructor().newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println(JSON.toJSONString(p1, SerializerFeature.WriteMapNullValue));

        System.out.println(JSON.toJSONString(String.class.newInstance(), SerializerFeature.WriteMapNullValue));

    }


    @Test
    public void zhengzr() {
        String str = "(Ljava/util/Set<Ljava/lang/String;>;)Z\n";
        String ragex = "<.*;>";
        Pattern p = Pattern.compile(ragex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String s = m.group();
            s = s.substring(2, s.length() - 2).replace("/", ".");
            System.out.println(s);
        }

    }


    @Test
    public void reflactjsckson() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        String sss = mapper.writeValueAsString(ApiValidateEntity.class.newInstance());
        System.out.println(sss);
    }

    @Test
    public void reflact() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Object ssss = classToObject(ApiValidateEntity.class);
        System.out.println(ssss);
    }


    public JSONObject classToObject(Class c) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        JSONObject jo = new JSONObject();

        Field[] fields = c.getDeclaredFields();
        Stream.of(fields).forEach(f -> {
            String sinature = getSinature(f);

            System.out.println(f.getName() + "   " + sinature);
            if (null != sinature) {
                Object fanxingObj = genericSignaturetoInstance(sinature);
                jo.put(f.getName(), Arrays.asList(fanxingObj));

            } else {
                try {
                    jo.put(f.getName(), f.getClass().newInstance());

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println(jo);
        return jo;
    }

    public String getSinature(Field field) {
        Method getGenericSignature = null;
        String result = null;
        try {
            getGenericSignature = field.getClass().getDeclaredMethod("getGenericSignature");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        getGenericSignature.setAccessible(true);// Abracadabra
        try {
            result = (String) getGenericSignature.invoke(field);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Object genericSignaturetoInstance(String genericSignatureto) {
        String ragex = "<.*;>";
        Pattern p = Pattern.compile(ragex);
        Matcher m = p.matcher(genericSignatureto);
        String s = null;
        while (m.find()) {
            s = m.group();
            s = s.substring(2, s.length() - 2).replace("/", ".");
        }
        try {
            if (null == s) {
                System.out.println();
            }
            return Class.forName(s).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Object();
    }


    @Test
    public void classtoJson() {

        String json = "{}";

       Object obj= JSON.parseObject(json, ApiValidateEntity.class);
        System.out.println(obj);

    }

}
