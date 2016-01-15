package com.talanlabs.component.factory;

public class PrimitiveHelper {

    public static Object determineValue(Class<?> type) {
        if (byte.class.equals(type)) {
            return (byte) 0;
        } else if (short.class.equals(type)) {
            return (short) 0;
        } else if (int.class.equals(type)) {
            return 0;
        } else if (long.class.equals(type)) {
            return 0L;
        } else if (float.class.equals(type)) {
            return 0f;
        } else if (double.class.equals(type)) {
            return 0d;
        } else if (char.class.equals(type)) {
            return ' ';
        } else if (boolean.class.equals(type)) {
            return false;
        } else {
            return null;
        }
    }
}
