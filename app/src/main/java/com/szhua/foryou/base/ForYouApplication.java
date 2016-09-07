package com.szhua.foryou.base;

import android.app.Application;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by szhua on 2016/8/15.
 */
public class ForYouApplication extends Application {
    /**
     * SDK初始化也可以放到Application中
     */
    public static String APPID ="a1ecbe3ebce18ba4f3bcfa118e5385e1";

    @Override
    public void onCreate() {
        super.onCreate();
        //提供以下两种方式进行初始化操作：
        //第一：设置BmobConfig，允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)
        BmobConfig config =new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId(APPID)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(15)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(512*1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(5500)
                .build();
        Bmob.initialize(config);
        //第二：默认初始化
        Bmob.initialize(this,APPID);
    }


}
