package com.example.myblog.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {

    public static <T> T mapToBean(Map<String, Object> map, Class<T> Beanclazz) throws IllegalAccessException, InstantiationException {
        T obj = Beanclazz.newInstance();

        Field[] fields = Beanclazz.getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) continue;
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }

    public static Map<String, Object> beanToMap(Object obj) throws IllegalAccessException {
        if (obj == null) return null;
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
}
