package com.fpsbasemode2.fps;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FPSOverride implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("FPSOverride active for package: " + lpparam.packageName);

        try {
            Class<?> clazz = Class.forName("com.android.server.display.DisplayPowerController");
            XposedBridge.hookAllMethods(clazz, "updateDisplayModeSpecsLocked", new XC_MethodHook() {
         @Override
protected void afterHookedMethod(MethodHookParam param) throws Throwable {
    // Cast thisObject ke class yang punya baseModeId
    Object displayObj = param.thisObject;
    Class<?> clazz = displayObj.getClass();
    
    try {
        // Ambil field baseModeId
        java.lang.reflect.Field field = clazz.getDeclaredField("baseModeId");
        field.setAccessible(true);
        field.setInt(displayObj, 2); // override jadi 2
        XposedBridge.log("BaseModeId overridden to 2");
    } catch (NoSuchFieldException e) {
        XposedBridge.log("Field baseModeId tidak ditemukan: " + e.getMessage());
    }
}
            });
        } catch (Throwable t) {
            XposedBridge.log("FPSOverride error: " + t.getMessage());
        }
    }
}