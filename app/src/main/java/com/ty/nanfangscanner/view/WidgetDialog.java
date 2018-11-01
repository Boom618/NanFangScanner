package com.ty.nanfangscanner.view;

import android.content.Context;
import android.view.View;

import com.ty.nanfangscanner.R;
import com.ty.nanfangscanner.activity.StartQrCodeActivity;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;

/**
 * @author TY on 2018/11/1.
 * <p>
 * 基于 NormalAlertDialog 实现的 Dialog
 */
public class WidgetDialog {


    public static void showClearConfirmDialog(Context context, String titleText, String contentText, String leftButton, String rightButton,
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
                .setOnclickListener(listener).build();

        dialog.show();
    }
}
