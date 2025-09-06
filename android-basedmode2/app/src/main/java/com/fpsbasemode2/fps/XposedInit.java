package com.fpsbasemode2.fps;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedInit implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // Kita hanya hook system/framework
        if (!"android".equals(lpparam.packageName)) return;

        FpsHook.init(lpparam);
    }
}
