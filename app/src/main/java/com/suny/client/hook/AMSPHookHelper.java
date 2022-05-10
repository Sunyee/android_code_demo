package com.suny.client.hook;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Handler;

import com.suny.client.hook.reflect.FieldUtils;

import java.lang.reflect.Proxy;

public class AMSPHookHelper {
    static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    public static void hookAMSP() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            hookAMSPBefore26();
        } else {
            hookAMSPSince26();
        }
    }


    /**
     * android 26 以下版本 AMSP 的 hook
     */
    private static void hookAMSPBefore26() {
        try {
            Class classActivityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Object gDefault = FieldUtils.readStaticField(classActivityManagerNative, "gDefault");
            Object mInstance = FieldUtils.readField(gDefault, "mInstance");
            Class classIActivityManager = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{classIActivityManager},
                    new MockAMSP(mInstance)
            );

            FieldUtils.writeField(gDefault, "mInstance", proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * android 26 及以上版本 AMSP 的 hook
     */
    private static void hookAMSPSince26() {
        try {
            Object IActivityManagerSingleton = FieldUtils.readStaticField(ActivityManager.class, "IActivityManagerSingleton");
            Object mInstance = FieldUtils.readField(IActivityManagerSingleton, "mInstance");
            Class classIActivityManager = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{classIActivityManager},
                    new MockAMSP(mInstance)
            );

            FieldUtils.writeField(IActivityManagerSingleton, "mInstance", proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hookActivityThread() {
        try {
            Class classActivityThread = Class.forName("android.app.ActivityThread");
            Object currentActivityThread = FieldUtils.readStaticField(classActivityThread, "sCurrentActivityThread");
            Handler mH = (Handler) FieldUtils.readField(currentActivityThread, "mH");
            FieldUtils.writeField(mH, "mCallback", new MockHCallback(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
