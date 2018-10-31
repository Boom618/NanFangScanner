package com.ty.nanfangscanner.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static  String getCurretTime(){
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        return currentTime;
    }

    public static int getTimeInterval(String newDate,String oldDate){
        try {
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            long from = simpleFormat.parse(oldDate).getTime();
            long to = simpleFormat.parse(newDate).getTime();
            int minutes = (int) ((to - from)/(1000 * 60));
            int hours=minutes/60;
            return hours;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
