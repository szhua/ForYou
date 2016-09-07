package com.szhua.foryou.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.szhua.foryou.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/2 0002.
 */
public class UiUtil {


    /*
     * 验证手机号码，（请自觉使用规范的正则表达式）
     *
     * @param mobileNo
     * @return
     */
    public static boolean isValidMobileNo(String mobileNo) {
        boolean flag = false;
        // Pattern p = Pattern.compile("^(1[358][13567890])(\\d{8})$");
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
        Matcher match = p.matcher(mobileNo);
        if (mobileNo != null) {
            flag = match.matches();
        }
        return flag;

    }

    public static boolean isValidCode(String code) {

        boolean flag = false;
        // Pattern p = Pattern.compile("^(1[358][13567890])(\\d{8})$");
        Pattern p = Pattern
                .compile(" [1-9]\\d{5}(?!\\d)");
        Matcher match = p.matcher(code);
        if (code != null) {
            flag = match.matches();
        }
        return flag;
    }


    /**
     * 隐藏从底部弹出的view
     *
     * @param context
     * @param v
     */
    public static void hide_menu_alpha(Context context, View v) {
        if (context == null || v == null) {
            return;
        }
        v.setVisibility(View.GONE);
        v.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.alpha_out));
    }


    /**
     * Scale in ；
     *
     * @param context
     * @param v
     */
    public static void show_menu_scale(Context context, View v) {
        if (context == null || v == null) {
            return;
        }
        v.setVisibility(View.VISIBLE);
        v.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.scale_in));
    }

    /**
     * Scale in ；
     *
     * @param context
     * @param v
     */
    public static void hide_menu_scale(Context context, View v) {
        if (context == null || v == null) {
            return;
        }
        v.setVisibility(View.GONE);
        v.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.scale_out));
    }


    /**
     * 渐变显示menu ；
     *
     * @param context
     * @param v
     */
    public static void show_menu_alpha(Context context, View v) {
        if (context == null || v == null) {
            return;
        }
        v.setVisibility(View.VISIBLE);
        v.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.alpha_in));


    }


    /**
     * 隐藏从底部弹出的view
     *
     * @param context
     * @param v
     */
    public static void hide_menu(Context context, View v) {
        if (context == null || v == null) {
            return;
        }
        v.setVisibility(View.GONE);
        v.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.push_top_out));
    }

    /**
     * 从底部弹出view
     *
     * @param context
     * @param v
     */
    public static void show_menu(Context context, View v) {
        if (context == null || v == null) {
            return;
        }
        v.setVisibility(View.VISIBLE);
        v.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.push_top_in));
    }
}
