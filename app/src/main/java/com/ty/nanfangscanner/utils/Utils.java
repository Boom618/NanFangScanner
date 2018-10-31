package com.ty.nanfangscanner.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/8/1.
 */

public class Utils {

    /**
     * 获取版本号
     * @return
     */
    public static String getVersion(){
        String versionName="";
        PackageManager packageManager = UIUtils.getContext().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            versionName=packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "v "+versionName;
    }

    /**
     * 将实体类转换成json字符串对象
     *
     * @param obj 对象
     * @return map
     */
    public static String toJson(Object obj, int method) {
        // TODO Auto-generated method stub
        if (method == 1) {
            //字段是首字母小写，其余单词首字母大写
            Gson gson = new Gson();
            String obj2 = gson.toJson(obj);
            return obj2;
        } else if (method == 2) {
            // FieldNamingPolicy.LOWER_CASE_WITH_DASHES:全部转换为小写，并用空格或者下划线分隔
            //FieldNamingPolicy.UPPER_CAMEL_CASE    所以单词首字母大写
            Gson gson2 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            String obj2 = gson2.toJson(obj);
            return obj2;
        }
        return "";
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName() {
        PackageManager manager = UIUtils.getContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            String version = info.versionName;
            return "版本：" + version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本";
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {
        PackageManager manager = UIUtils.getContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getFilePathFromContentUri(Uri selectedVideoUri) {
        ContentResolver contentResolver = UIUtils.getContext().getContentResolver();
        String filePath = "";
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    public static String getMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取现在是夜里还是白天
     *
     * @return 0：白天，1：夜晚
     */
    public static boolean isDay() {
        int hour = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 18) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 判断是否到期
     *
     * @param currentDate 当前时间
     * @param outDate     到期时间
     * @return
     * @throws ParseException
     */
    public static boolean isOutDate(String currentDate, String outDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 当前的时刻
        Calendar pre = Calendar.getInstance();
        Date predate;
        if (TextUtils.isEmpty(currentDate)) {
            predate = new Date(System.currentTimeMillis());
        } else {
            predate = sdf.parse(currentDate);
        }
        pre.setTime(predate);


        // 设定的时刻
        Calendar cal = Calendar.getInstance();
        Date date = sdf.parse(outDate);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);
            int diffHour = cal.get(Calendar.HOUR_OF_DAY) - pre.get(Calendar.HOUR_OF_DAY);
            int diffMin = cal.get(Calendar.MINUTE) - pre.get(Calendar.MINUTE);
            if (diffDay == 0) {
                if (diffHour == 0) {
                    if (diffMin >= 0) {
                        return true;
                    }
                } else if (diffHour > 0) {
                    return true;
                }
            } else if (diffDay > 0) {
                return true;
            }
        } else if (cal.get(Calendar.YEAR) > (pre.get(Calendar.YEAR))) {
            return true;
        }
        return false;
    }

    /**
     * 根据value获取key
     *
     * @param map
     * @param value
     * @return
     */
    public static Integer getKey(Map<Integer, String> map, String value) {
        Integer key = -1;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                key = entry.getKey();
                return key;
            }
        }
        return key;
    }

    /**
     * 获取应用程序的安装路径
     *
     * @return
     */
    public static String getSystemFilePath() {
        Context context = UIUtils.getContext();
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //返回/storage/emulated/0/Android/data/packagename/files/Pictures
            //  cachePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            //返回/storage/emulated/0/Android/data/packagename/cache
            cachePath = context.getExternalCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        } else {
            //  cachePath = context.getFilesDir().getAbsolutePath();
            cachePath = context.getCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
        }
        return cachePath;
    }
}
