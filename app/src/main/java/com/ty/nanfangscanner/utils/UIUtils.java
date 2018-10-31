package com.ty.nanfangscanner.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.ty.nanfangscanner.App;

public class UIUtils {

    /**
     * 获取Context对象
     *
     * @return
     */
    public static Context getContext() {
        return App.getApplication();
    }

    /**
     * 显示Toast
     *
     * @param text
     */
    public static void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取Resources对象
     *
     * @return
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getResource().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px转换dip
     */
    public static int px2dip(int px) {
        final float scale = getResource().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取dimens
     *
     * @param id
     * @return
     */
    public static int getDimens(int id) {
        return (int) getResource().getDimension(id);
    }

    /**
     * 获取String 资源
     *
     * @param id
     * @return
     */
    public static String getString(int id) {
        return getResource().getString(id);
    }

    /**
     * 获取Color
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

}
