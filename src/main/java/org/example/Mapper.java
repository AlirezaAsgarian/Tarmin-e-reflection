package org.example;

import java.lang.reflect.*;


public class Mapper {
    public static Object map(Object mapFrom, Class<?> classTo) {
        try {
            Class<?> clazz = classTo;
            Class<?>[] classes = {};
            Constructor<?> constructor = clazz.getConstructor();
            Object mapTo = constructor.newInstance();
            Object[] constInput = {};
            for (Field field1 : mapTo.getClass().getFields()) {
                if(hasFieldWithThisName(field1,mapFrom.getClass().getFields())) {
                    Field field = getFieldWithThisNameAndType(field1, mapFrom.getClass().getFields());
                    if (field.getType().isArray()) {
                        copyArray(mapFrom, mapTo, (Object[]) field.get(mapFrom), field1);
                    } else {
                        field1.set(mapTo, field.get(mapFrom));
                    }
                } else if(hasGetterForThisField(field1,mapFrom.getClass().getMethods())){
                    Method method = getGetterForThisField(field1,mapFrom.getClass().getMethods());
                    if(field1.getType().isArray()){
                        copyArray(mapFrom,mapTo, (Object[]) method.invoke(mapFrom,null),field1);
                    } else {
                        field1.set(mapTo,method.invoke(mapFrom,null));
                    }
                }
            }
            return mapTo;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean hasGetterForThisField(Field field1, Method[] declaredMethods) {
        String destination = "get" + capitalizeFirstString(field1.getName());
        for (Method m:
                declaredMethods) {
            if(m.getName().equals(destination)){
                return true;
            }
        }
        return false;
    }
    private static Method getGetterForThisField(Field field1, Method[] declaredMethods){
        String destination = "get" + capitalizeFirstString(field1.getName());
        for (Method m:
                declaredMethods) {
            if(m.getName().equals(destination)){
                return m;
            }
        }
        return null;
    }

    private static Field getFieldWithThisNameAndType(Field field1, Field[] fields) {
        for (Field fi:
                fields) {
            if(field1.getName().equals(fi.getName()) && field1.getAnnotatedType().equals(fi.getAnnotatedType()))
                return fi;
        }
        return null;
    }

    public static Boolean hasFieldWithThisName(Field field,Field[] fields){
        for (Field fi:
                fields) {
            if(field.getName().equals(fi.getName()) && field.getAnnotatedType().equals(fi.getAnnotatedType()))
                return true;
        }
        return false;
    }

    private static void copyArray(Object temp, Object a, Object[] field, Field field1) throws IllegalAccessException {
        Object[] array =  field;
        int size = Array.getLength(array);
        Object[] array2 = (Object[]) Array.newInstance(array.getClass().componentType(),size);
        for (int i = 0; i < size; i++) {
            Object value = Array.get(array, i);
            Array.set(array2,i, value);
        }
        field1.set(a,array2);
    }

    public static String capitalizeFirstString(String str){
        try {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }catch (Exception e){
            return "ali";
        }
    }
}
