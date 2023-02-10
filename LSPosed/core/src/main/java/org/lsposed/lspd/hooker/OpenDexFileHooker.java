package org.lsposed.lspd.hooker;

import android.os.Build;

import org.lsposed.lspd.nativebridge.HookBridge;

import com.debin.android.fun.XC_MethodHook;
import com.debin.android.fun.XposedHelpers;

public class OpenDexFileHooker extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        ClassLoader classLoader = null;
        for (var arg : param.args) {
            if (arg instanceof ClassLoader) {
                classLoader = (ClassLoader) arg;
            }
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P && classLoader == null) {
            classLoader = XposedHelpers.class.getClassLoader();
        }
        while (classLoader != null) {
            if (classLoader == XposedHelpers.class.getClassLoader()) {
                HookBridge.setTrusted(param.getResult());
                return;
            } else {
                classLoader = classLoader.getParent();
            }
        }
    }
}
