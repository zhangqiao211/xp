package com.example.funxpDemo;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;

import com.debin.android.fun.IXposedHookLoadPackage;
import com.debin.android.fun.XC_MethodHook;
import com.debin.android.fun.XposedHelpers;
import com.debin.android.fun.callbacks.XC_LoadPackage;


public class Hook implements IXposedHookLoadPackage {
    private final String TAG = "xposed";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.tencent.mm") && lpparam.processName.equals("com.tencent.mm")) {
            Log.i(TAG, "哈哈哈哈哈哈哈进来了22222" + lpparam.packageName + "********************");



            XposedHelpers.findAndHookMethod(Activity.class, "onCreate", Bundle.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            final Activity activity = (Activity) param.thisObject;

                            Toast.makeText(activity.getApplicationContext(), "测试444：" + activity.getLocalClassName(), Toast.LENGTH_SHORT).show();
                            if (activity.getLocalClassName().equals("plugin.setting.ui.setting.SettingsUI")) {
                                Toast.makeText(activity.getApplicationContext(), "11111嘻嘻嘻", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



            XposedHelpers.findAndHookMethod("com.tencent.wcdb.database.SQLiteDatabase", lpparam.classLoader, "insertWithOnConflict"
                    , String.class, String.class, ContentValues.class, int.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            String table = (String) param.args[0];
                            final ContentValues initialValues = (ContentValues) param.args[2];
                            Log.i(TAG, "message ************数据库插入**********table============>" + table);
                            if (table.equals("message")) {
                                Log.i(TAG, "message ************数据库插入**********table============>" + table);
                                for (Map.Entry<String, Object> item : initialValues.valueSet()) {
                                    if (item.getValue() != null) {
                                        Log.i(TAG, (item.getKey() + "============>" + item.getValue()));
                                    }
                                }
                            }
                        }
                    });


            Log.i(TAG, "**********************8end***************************************");
        }
    }
}
