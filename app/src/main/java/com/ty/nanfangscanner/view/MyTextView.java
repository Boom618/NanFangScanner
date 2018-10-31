package com.ty.nanfangscanner.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by quning on 2017/7/22.
 */

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {

    private String checkId;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCheckId(String checkId){
        this.checkId=checkId;
    }
    public String getCheckId(){
        return checkId;
    }
}
