package com.example.fps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FpsHook {

    private static int targetFps = 60; // default

    public static void init(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            // Hook Display.getRefreshRate()
            XposedHelpers.findAndHookMethod(
                    "android.view.Display",
                    lpparam.classLoader,
                    "getRefreshRate",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult((float) targetFps);
                            XposedBridge.log("[FPSController] Display.getRefreshRate -> " + targetFps);
                        }
                    }
            );

            // Hook SurfaceControl.setFrameRate (Android 11+)
            try {
                XposedHelpers.findAndHookMethod(
                        "android.view.SurfaceControl",
                        lpparam.classLoader,
                        "setFrameRate",
                        android.view.SurfaceControl.Transaction.class,
                        float.class,
                        int.class,
                        int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                param.args[1] = (float) targetFps;
                                XposedBridge.log("[FPSController] SurfaceControl.setFrameRate -> " + targetFps);
                            }
                        }
                );
            } catch (Throwable t) {
                XposedBridge.log("[FPSController] SurfaceControl.setFrameRate hook failed: " + t);
            }

            // Daftar broadcast receiver agar realtime update FPS
            Context sysContext = (Context) XposedHelpers.callStaticMethod(
                    XposedHelpers.findClass("android.app.ActivityThread", null),
                    "currentApplication"
            );

            if (sysContext != null) {
                IntentFilter filter = new IntentFilter("com.example.miuifps.SET_FPS");
                sysContext.registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int fps = intent.getIntExtra("fps", 60);
                        targetFps = fps;
                        XposedBridge.log("[FPSController] targetFps updated to " + targetFps);
                    }
                }, filter);

                XposedBridge.log("[FPSController] BroadcastReceiver registered");
            } else {
                XposedBridge.log("[FPSController] sysContext null, receiver not registered");
            }

        } catch (Throwable t) {
            XposedBridge.log("[FPSController] init failed: " + t);
        }
    }
}
