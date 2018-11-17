package com.api.validate.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Lazy
public class ApiRepoter implements BeanPostProcessor {

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
            return Class.forName(s).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Object();
    }

    LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("apiValidateConf")) {
            System.out.println("*******************");
        }
        List<String> classAnnotations = Stream.of(bean.getClass().getAnnotations()).map(m ->
                m.annotationType().getSimpleName()).collect(Collectors.toList());
        if (classAnnotations.contains("RequestMapping")) {
            System.out.println("Bean>>>>>>>>>>>>>" + beanName + "  類註解>>>>>>>" + classAnnotations);
            RequestMapping requestMapping = bean.getClass().getAnnotation(RequestMapping.class);

            List<Method> lim = Stream.of(ReflectionUtils.getAllDeclaredMethods(bean.getClass())).filter(f ->
                    f.isAnnotationPresent(ApiOperation.class)).collect(Collectors.toList());

            List<MethodEntity> methodEntities = lim.stream().map(method -> {
                        MethodEntity methodEntity = new MethodEntity();
                        System.out.println("方法名》》》" + method.getName());

                        List<String> methodAnnotations = Stream.of(method.getAnnotations()).map(m ->
                                m.annotationType().getSimpleName()).collect(Collectors.toList());

                        methodAnnotations.forEach(f -> {
                            switch (f) {
                                case SB.GetMapping:
                                    methodEntity.setRequestMethod(RequestMethod.GET);
                                    methodEntity.setMethodMapping(method.getAnnotation(GetMapping.class).value());
                                    break;
                                case SB.PostMapping:
                                    methodEntity.setRequestMethod(RequestMethod.POST);
                                    methodEntity.setMethodMapping(method.getAnnotation(PostMapping.class).value());
                                    break;
                                case SB.RequestMapping:
                                    methodEntity.setRequestMethod(method.getAnnotation(RequestMapping.class).method()[0]);
                                    break;
                                case SB.PutMapping:
                                    methodEntity.setRequestMethod(RequestMethod.PUT);
                                    methodEntity.setMethodMapping(method.getAnnotation(PutMapping.class).value());
                                    break;
                                case SB.DeleteMapping:
                                    methodEntity.setRequestMethod(RequestMethod.DELETE);
                                    methodEntity.setMethodMapping(method.getAnnotation(DeleteMapping.class).value());
                                    break;
                                default:
                            }
                        });
                        methodEntity.setMethodName(method.getName());
                        methodEntity.setMethodAnnotation(methodAnnotations);

                        String genericSignature = null;
                        try {
                            Method getGenericSignature = Method.class.getDeclaredMethod("getGenericSignature");
                            getGenericSignature.setAccessible(true);// Abracadabra
                            try {
                                genericSignature = (String) getGenericSignature.invoke(method);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        List<ParamEntity> paramEntities = paramEntitiesBuild(method, genericSignature);

                        methodEntity.setParams(paramEntities);
                        return methodEntity;
                    }
            ).collect(Collectors.toList());

            ApiValidateEntity apiValidateEntity =
                    new ApiValidateEntity("first", beanName, classAnnotations, requestMapping.value(), methodEntities);
            System.out.println("send to server...");
        }

        return bean;
    }

    private List<ParamEntity> paramEntitiesBuild(Method method, String genericSignature) {

        String[] paramNames = u.getParameterNames(method);
        Class[] classes = method.getParameterTypes();
        Annotation[][] an = method.getParameterAnnotations();
        List<String> paramAnnotations = Stream.of(an).flatMap(m ->
                m.length == 0 ? Stream.of("") :
                        Stream.of(m).map(m2 -> m2.annotationType().getSimpleName()))
                .collect(Collectors.toList());


        AtomicInteger ai = new AtomicInteger();
        List<ParamEntity> result = Arrays.stream(paramNames).map(m -> {
            int i = ai.get();
            ParamEntity paramEntity = new ParamEntity();
            paramEntity.setParamName(paramNames[i]);
            paramEntity.setParamType(classes[i].getName());
            paramEntity.setSimplepParamType(classes[i].getSimpleName());

            paramEntity.setParamsAnnotation(paramAnnotations.get(i));

            System.out.println("參數名》》》" + paramNames[i] + "  類型》》》" + classes[i].getSimpleName() +
                    "   註解》》》》》" + paramAnnotations.get(i));
/*            try {
                switch (paramAnnotations.get(i)) {
                    case SB.RequestBody:
                        //遭遇泛型
                        if (null != genericSignature) {
                            Object genericList = new Object();
                            switch (paramEntity.getSimplepParamType()) {
                                case "List":
                                    genericList = Arrays.asList(genericSignaturetoInstance(genericSignature));
                                    break; //可选
                                default: //可选
                                    //语句
                            }
                            paramEntity.setValue(JSON.toJSONString(genericList, SerializerFeature.WriteMapNullValue));
                        } else {
                            paramEntity.setValue(JSON.toJSONString(classes[i].newInstance(), SerializerFeature.WriteMapNullValue));
                        }
                        break;
                    default:
                        paramEntity.setValue(paramEntity.getSimplepParamType());
                }
            } catch (InstantiationException | IllegalAccessException ignored) {
            }*/
            ai.getAndIncrement();
            return paramEntity;
        }).collect(Collectors.toList());
        return result;
    }
}
