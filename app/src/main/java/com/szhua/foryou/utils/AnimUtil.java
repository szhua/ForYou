package com.szhua.foryou.utils;


import android.app.Activity;

import com.szhua.foryou.R;


/**
 * 2016/3/11
 *
 * 本类为activity进入退出的动画效果
 *
 *
 */
public class AnimUtil {

    /** slip in */
    public static void intentSlidIn(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

    /** Slip off */
    public static void intentSlidOut(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    /** push up */
    public static void intentPushUp(Activity activity) {
        activity.overridePendingTransition(R.anim.push_top_in,
                R.anim.push_top_out);
    }

    /** push down */
    public static void intentPushDown(Activity activity) {
        activity.overridePendingTransition(R.anim.push_top_in,
                R.anim.push_top_out);
    }
}
