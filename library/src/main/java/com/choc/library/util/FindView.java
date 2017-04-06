package com.choc.library.util;

import android.view.View;

/**
 * Created by Administrator on 2017/3/8.
 */
public class FindView {
    public static <T> T findById(View layoutView, int resID) {
        //<T> 表示声明一个任意类型T
        T view = (T) layoutView.findViewById(resID);
        return view;
    }
}
