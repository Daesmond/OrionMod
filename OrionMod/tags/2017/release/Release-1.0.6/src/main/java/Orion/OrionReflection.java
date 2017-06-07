package Orion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daesmond
 */
public class OrionReflection {

    public static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    public static int getIntField(Object obj, String fieldName) {
        Class clazz = obj.getClass();
        int val = Integer.MIN_VALUE;

        try {
            Field iField = OrionReflection.getField(clazz, fieldName);

            if (iField != null) {
                iField.setAccessible(true);
                val = iField.getInt(obj);
                iField.setAccessible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return val;
    }

    public static float getFloatField(Object obj, String fieldName) {
        Class clazz = obj.getClass();
        float val = Float.NaN;

        try {
            Field iField = OrionReflection.getField(clazz, fieldName);

            if (iField != null) {
                iField.setAccessible(true);
                val = iField.getFloat(obj);
                iField.setAccessible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return val;
    }


    public static void setFloatField(Object obj, String fieldName, float val) {
        Class clazz = obj.getClass();

        try {
            Field iField = OrionReflection.getField(clazz, fieldName);

            if (iField != null) {
                iField.setAccessible(true);
                iField.setFloat(obj, val);
                iField.setAccessible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
