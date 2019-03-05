package com.instabug.cordova.plugin.util;


import com.instabug.bug.BugReporting;

import java.lang.reflect.Method;

public class Util {

    public static int[] parseReportTypes(String[] stringTypeArray) throws Exception{
        int [] parsedArray = new int[stringTypeArray.length];
        for (int i = 0; i < stringTypeArray.length; i++) {
            String type = stringTypeArray[i];
            if (type.equals("bug")) {
                parsedArray[i] = (int) BugReporting.ReportType.BUG;
            } else if (type.equals("feedback")) {
                parsedArray[i] = BugReporting.ReportType.FEEDBACK;
            } else {
                throw new Exception("Invalid report type " + type);
            }
        }

        return parsedArray;
    }

    public static Method getMethod(Class clazz, String methodName, Class... parameterType) {
        final Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length ==
                    parameterType.length) {
                for (int i = 0; i < 2; i++) {
                    if (method.getParameterTypes()[i] == parameterType[i]) {
                        if (i == method.getParameterTypes().length - 1) {
                            method.setAccessible(true);
                            return method;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return null;
    }
}
