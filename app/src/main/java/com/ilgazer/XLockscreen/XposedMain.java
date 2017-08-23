package com.ilgazer.XLockscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.crossbowffs.remotepreferences.RemotePreferences;
import com.ilgazer.XLockscreen.ui.EditActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by ilgaz on 10.08.2017.
 */

public class XposedMain implements IXposedHookLoadPackage {
    private static String nextShownColor;
    private static final Intent INTENT_REQUEST_REQUERY =
            new Intent(com.twofortyfouram.locale.Intent.ACTION_REQUEST_QUERY).putExtra(com.twofortyfouram.locale.Intent.EXTRA_ACTIVITY,
                    EditActivity.class.getName());
    private static boolean notHooked = true;
    private static Bitmap mBitmapCircleRed = null;
    private static Bitmap mBitmapArrowRedUp = null;
    private static boolean useBitmapCircle = true;
    private static HashMap<String, Object> fieldMap = new HashMap<>();
    private static boolean undoColorChange;

//    @Override
//    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
//        pref = new XSharedPreferences(BuildConfig.APPLICATION_ID);
//    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
//        XposedBridge.log("Loaded app: " + lpparam.packageName);
//        if (!lpparam.packageName.equals("com.android.internal"))
//            return;
//        log("[ILGAZ] "+lpparam.packageName);
        final Class lockPatternUti̇ls = XposedHelpers.findClass("com.android.internal.widget.LockPatternUtils", lpparam.classLoader);
//        if(pref.getBoolean("useAlternativePatternHook", false)) {
        if (notHooked) {
//{
//            XposedHelpers.findAndHookMethod(
//                    "com.android.internal.widget.LockPatternUtils",
//                    lpparam.classLoader, "checkPattern", List.class, int.class, XposedHelpers.findClass("com.android.internal.widget.LockPatternUtils$CheckCredentialProgressCallback", lpparam.classLoader),
//                    new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) {
//                            Context c = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
//                            SharedPreferences pref = new RemotePreferences(c, "com.ilgazer.XLockscreen.preferences", "pattern");
//            log(Constants.LOG_TAG + " PATTERN STACK " + Arrays.toString(Thread.currentThread().getStackTrace()));
//                            if (lastCheckTimestamp + pref.getInt("debounceDuration", 400) > System.currentTimeMillis()) {
//        param.setResult(lastCheckPatternReturn);
//                            } else {
//        pref.reload();
//                                lastCheckTimestamp = System.currentTimeMillis();
//                                List res = (List) param.args[0];
//                                String patternEnteredRaw = (String) XposedHelpers.callStaticMethod(lockPatternUti̇ls, "patternToString", res);
//                                String patternEntered = patternStringToPrintable(patternEnteredRaw);
//        log(Constants.LOG_TAG + " PATTERN ENTERED " + patternEntered);
//        String logPrefs = "";
//        for (Map.Entry e : pref.getAll().entrySet()) {
//            logPrefs += ("[" + e.getKey().toString() + "," + e.getValue().toString() + "]");
//        }
//        log(Constants.LOG_TAG + " PREFS " + logPrefs);
//        log(Constants.LOG_TAG + " PATTERN ACTION " + pref.getString("assign_" + patternEntered, "null"));
//                                String[] tasks = pref.getString("assign_" + patternEntered, "null").split(";");
//                                for (String taskRaw : tasks) {
//                                    String[] task = taskRaw.split(",");
//                                    switch (task[0]) {
//                                        case "tasker":
//                                            final int numEntered = pref.getInt("ret_" + patternEntered, 0);
//                                            log(Constants.LOG_TAG + "NUM_ENTERED " + numEntered);
//                                            SharedPreferences.Editor editor = pref.edit();
//                                            editor.putInt("ret_" + patternEntered, numEntered + 1);
//                                            editor.apply();
//                                            c.sendBroadcast(INTENT_REQUEST_REQUERY);
//                                    }
//                                }
//                            }
//                        }
//                    });
//        } else
            final Class patternViewClass = XposedHelpers.findClass("com.android.internal.widget.LockPatternView", lpparam.classLoader);
            notHooked = false;
            XposedHelpers.findAndHookMethod(
                    "com.android.internal.widget.LockPatternView",
                    lpparam.classLoader, "notifyPatternDetected",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Paint mPaint = null;
                            try {
                                XposedHelpers.findField(patternViewClass, "mBitmapCircleRed");
                            } catch (NoSuchFieldError e) {
                                log(Constants.LOG_TAG + "mBitmapCircleRed does not exist.");
                                useBitmapCircle = false;
                            }
                            if (useBitmapCircle) {
                                mPaint = (Paint) XposedHelpers.getObjectField(param.thisObject, "mPathPaint");
                                if (mBitmapCircleRed == null) {
                                    mBitmapCircleRed = ((Bitmap) XposedHelpers.getObjectField(param.thisObject, "mBitmapCircleRed")).copy(Bitmap.Config.ARGB_8888, true);
                                    mBitmapArrowRedUp = ((Bitmap) XposedHelpers.getObjectField(param.thisObject, "mBitmapArrowRedUp")).copy(Bitmap.Config.ARGB_8888, true);
                                }
                            }
//                            for (Field field : patternViewClass.getDeclaredFields()) {
//                                fieldMap.put(field.getName(), XposedHelpers.getObjectField(param.thisObject, field.getName()));
//                            }
                            Context c = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                            SharedPreferences pref = new RemotePreferences(c, "com.ilgazer.XLockscreen.preferences", "pattern");
                            ArrayList pattern = (ArrayList) XposedHelpers.getObjectField(param.thisObject, "mPattern");
                            String patternEnteredRaw = (String) XposedHelpers.callStaticMethod(lockPatternUti̇ls, "patternToString", pattern);
                            String patternEntered = patternStringToPrintable(patternEnteredRaw);
                            log(Constants.LOG_TAG + " PATTERN ENTERED " + patternEntered);
//                            String logPrefs = "";
//                            for (Map.Entry e : pref.getAll().entrySet()) {
//                                logPrefs += ("[" + e.getKey().toString() + "," + e.getValue().toString() + "]");
//                            }
//                            log(Constants.LOG_TAG + " PREFS " + logPrefs);
                            log(Constants.LOG_TAG + " PATTERN ACTION " + pref.getString("assign_" + patternEntered, "null"));
                            Integer color = null;
                            String[] tasks = pref.getString("assign_" + patternEntered, "null").split(";");
                            for (String taskRaw : tasks) {
                                String[] task = taskRaw.split(",");
                                switch (task[0]) {
                                    case "tasker":
                                        final int numEntered = pref.getInt("ret_" + patternEntered, 0);
                                        log(Constants.LOG_TAG + "NUM_ENTERED " + numEntered);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putInt("ret_" + patternEntered, numEntered + 1);
                                        editor.apply();
                                        c.sendBroadcast(INTENT_REQUEST_REQUERY);
                                        break;
                                    case "color":
                                        log(Constants.LOG_TAG + "task=" + taskRaw + "," + Arrays.toString(task));
                                        color = Integer.parseInt(task[1]);
                                        //mBitmapCircleRed rengini değiştirsek nolur
//                                        break;
//                                    case "clearFails":
                                        ViewParent viewParent=((View)param.thisObject).getParent();
                                        while(!viewParent.getClass().getName().equals("com.android.internal.policy.impl.keyguard.KeyguardPatternView"))
                                            viewParent=viewParent.getParent();
                                        log(Constants.LOG_TAG +" Keyguard: "+ viewParent.getClass().getName());
                                        Object mCallback=XposedHelpers.getObjectField(viewParent,"mCallback");
                                        log(Constants.LOG_TAG +" Keyguard: "+ viewParent.getClass().getName());
//                                        XposedHelpers.callMethod(mCallback, "reportSuccessfulUnlockAttempt");
//                                        List old=pattern.subList(0, 3);
//                                        pattern.clear();
//                                        pattern.addAll(old);
                                }
                            }
                            if (useBitmapCircle && mPaint != null) {
                                if (color != null) {
                                    XposedHelpers.setObjectField(param.thisObject, "mBitmapCircleRed", changeBitmapColor(mBitmapCircleRed, color));
                                    XposedHelpers.setObjectField(param.thisObject, "mBitmapArrowRedUp", changeBitmapColor(mBitmapArrowRedUp, color));
                                    mPaint.setAlpha(0x80);
                                } else if (XposedHelpers.getBooleanField(param.thisObject, "mInStealthMode")) {
                                    Bitmap mBitmapCircleEmpty = mBitmapCircleRed.copy(mBitmapCircleRed.getConfig(), true);
                                    Bitmap mBitmapArrowEmpty = mBitmapArrowRedUp.copy(mBitmapArrowRedUp.getConfig(), true);
                                    mBitmapCircleEmpty.eraseColor(Color.TRANSPARENT);
                                    mBitmapArrowEmpty.eraseColor(Color.TRANSPARENT);
                                    XposedHelpers.setObjectField(param.thisObject, "mBitmapCircleRed", mBitmapCircleEmpty);
                                    XposedHelpers.setObjectField(param.thisObject, "mBitmapArrowRedUp", mBitmapArrowEmpty);
                                    mPaint.setAlpha(0x00);
                                } else {
                                    XposedHelpers.setObjectField(param.thisObject, "mBitmapCircleRed", mBitmapCircleRed);
                                    XposedHelpers.setObjectField(param.thisObject, "mBitmapArrowRedUp", mBitmapArrowRedUp);
                                    mPaint.setAlpha(0x80);
                                }

                            }
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            for (String field : fieldMap.keySet()) {
//                                Object after= XposedHelpers.getObjectField(param.thisObject, field);
//                                if(after==null)
//                                    XposedBridge.log(Constants.LOG_TAG + " Field '" + field + "is null");
//                                else if(!after.equals(fieldMap.get(field)))
//                                    XposedBridge.log(Constants.LOG_TAG + " Field '" + field + " 'changed from '" +fieldMap.get(field).toString()+"' to '"+ after.toString());
//                            }
                        }
                    });


//            XposedHelpers.findAndHookMethod(ViewGroup.class, "dispatchTouchEvent", MotionEvent.class, new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    log(Constants.LOG_TAG + ((ViewGroup)param.thisObject).getClass().getName());
//                }
//            });

//            log(Constants.LOG_TAG + " hookedMethod = " + unh.getHookedMethod().getName());
//            for (Method method : XposedHelpers.findClass("com.android.internal.widget.LockPatternView", lpparam.classLoader).getDeclaredMethods()) {
//                String s=" method:" + method.getName() +";";
//                for (Class<?> clazz : method.getParameterTypes()) {
//                    s+=clazz.getName();
//                    s+=",";
//                }
//                log(Constants.LOG_TAG +s);


        }
//        XposedHelpers.findAndHookMethod(
//                "com.android.internal.widget.LockPatternUtils",
//                lpparam.classLoader, "checkPassword",
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) {
//                        Context c = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
//                        SharedPreferences pref = new RemotePreferences(c, "com.ilgazer.XLockscreen.preferences", "password");
//                        String patternEntered = (String) param.args[0];
//                            log(Constants.LOG_TAG + " PATTERN TIME " + System.currentTimeMillis());
//                            log(Constants.LOG_TAG + " PATTERN ENTERED " + patternEntered);
//                            String logPrefs = "";
//                            for (Map.Entry e : pref.getAll().entrySet()) {
//                                logPrefs += ("[" + e.getKey().toString() + "," + e.getValue().toString() + "]");
//                            }
//                            log(Constants.LOG_TAG + " PREFS " + logPrefs);
//                            log(Constants.LOG_TAG + " PATTERN ACTION " + pref.getString("assign_" + patternEntered, "null"));
//                        String[] tasks = pref.getString("assign_" + patternEntered, "null").split(";");
//                        for (String taskRaw : tasks) {
//                            String[] task = taskRaw.split(",");
//                            switch (task[0]) {
//                                case "tasker":
//                                    final int numEntered = pref.getInt("ret_" + patternEntered, 0);
//
//                                    log(Constants.LOG_TAG + "NUM_ENTERED " + numEntered);
//                                    SharedPreferences.Editor editor = pref.edit();
//                                    editor.putInt("ret_" + patternEntered, numEntered + 1);
//                                    editor.apply();
//                                    c.sendBroadcast(INTENT_REQUEST_REQUERY);
//
//                            }
//                        }
//                    }
//                });
    }

    private static String patternStringToPrintable(String pattern) {
        if (pattern == null) {
            return "";
        }
        final int patternSize = pattern.length();

        byte[] res = new byte[patternSize];
        final byte[] bytes = pattern.getBytes();
        for (int i = 0; i < patternSize; i++) {
            res[i] = (byte) (bytes[i] + '0');
        }
        return new String(res);
    }

    private static Bitmap changeBitmapColor(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;
    }
    

}
