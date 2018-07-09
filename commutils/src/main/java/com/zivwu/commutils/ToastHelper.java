package com.zivwu.commutils;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

    private String oldMsg;
    private Toast toast = null;
    private long oneTime = 0;
    private long twoTime = 0;
    private Context context;

    public ToastHelper(Context context) {
        this.context = context;
    }


    public void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }


    public void showToast(int resId) {
        showToast(context.getString(resId));
    }
}
