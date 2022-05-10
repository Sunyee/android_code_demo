package com.suny.client;

import android.app.Application;
import android.content.Context;

import com.suny.client.hook.AMSPHookHelper;

import me.weishu.reflection.Reflection;



public class ClientApp extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // 解除 android P 上的私有 api 限制，见http://weishu.me/2018/06/07/free-reflection-above-android-p/
        Reflection.unseal(base);
        // hook
        AMSPHookHelper.hookAMSP();
        AMSPHookHelper.hookActivityThread();
    }
}
