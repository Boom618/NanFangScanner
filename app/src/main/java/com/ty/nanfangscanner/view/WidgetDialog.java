package com.ty.nanfangscanner.view;

import android.content.Context;

import com.ty.nanfangscanner.R;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.wevey.selector.dialog.NormalSelectionDialog;

import java.util.List;

/**
 * @author TY on 2018/11/1.
 * <p>
 * 基于 NormalAlertDialog 实现的 Dialog
 */
public class WidgetDialog {


    /**
     * 标题 + 内容 + left Button + right Button
     *
     * @param context
     * @param titleText
     * @param contentText
     * @param leftButton
     * @param rightButton
     * @param listener
     */
    public static void show_title_content_left_right(Context context, String titleText, String contentText, String leftButton, String rightButton,
                                                     DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog> listener) {

        NormalAlertDialog dialog = new NormalAlertDialog.Builder(context)
                //屏幕高度*0.23
                .setHeight(0.23f)
                //屏幕宽度*0.65
                .setWidth(0.65f)
                .setTitleVisible(true)
                .setTitleText(titleText)
                .setTitleTextColor(R.color.black_light)
                .setContentText(contentText)
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText(leftButton)
                .setLeftButtonTextColor(R.color.gray)
                .setRightButtonText(rightButton)
                .setRightButtonTextColor(R.color.black_light)
                .setOnclickListener(listener)
                .build();

        dialog.show();
    }


    /**
     * 标题 + 取消按钮 + 列表
     * @param context
     * @param titleText
     * @param cancelButton
     * @param datas
     * @param listener
     */
    public static void show_title_button_list(Context context, String titleText, String cancelButton,
                                              List<String> datas, DialogInterface.OnItemClickListener<NormalSelectionDialog> listener) {


        NormalSelectionDialog.Builder builder = new NormalSelectionDialog.Builder(context);
        //设置是否显示标题
        builder.setlTitleVisible(true)
                //设置标题高度
                .setTitleHeight(50)
                //设置标题提示文本
                .setTitleText(titleText)
                .setTitleTextSize(14)
                .setTitleTextColor(R.color.main_color)
                //设置item的高度
                .setItemHeight(40)
                //屏幕宽度*0.9
                .setItemWidth(0.9f)
                //设置item字体颜色
                .setItemTextColor(R.color.black)
                //设置item字体大小
                .setItemTextSize(14)
                //设置最底部“取消”按钮文本
                .setCancleButtonText(cancelButton)
                .setOnItemListener(listener)
                //设置是否可点击其他地方取消dialog
                .setCanceledOnTouchOutside(true)
                .build()
                .setDatas(datas)
                .show();
    }
}
