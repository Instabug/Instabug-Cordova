package com.instabug.cordova.plugin.util;


import com.instabug.bug.BugReporting;

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
}
