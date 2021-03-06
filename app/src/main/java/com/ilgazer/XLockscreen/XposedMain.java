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

import com.crossbowffs.remotepreferences.RemotePreferences;
import com.ilgazer.XLockscreen.ui.EditActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;

/**
 * Created by Ilgaz Er on 10.08.2017.
 * All xposed hooks and associated logic is implemented here.
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
    //Triggers making size seem low via a decorator class on list
    private static boolean hideNextPatternNumber = false;
    private static boolean resetStealthMode;

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
                    patternViewClass, "notifyPatternDetected",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            ViewParent viewParent=((View)param.thisObject).getParent();
//                            while(!viewParent.getClass().getName().equals("com.android.internal.policy.impl.keyguard.KeyguardPatternView")) {
//                                viewParent=viewParent.getParent();
//                            }
//                            log(Constants.LOG_TAG +" Keyguard: "+ viewParent.getClass().getName());
//                            Object mCallback=XposedHelpers.getObjectField(viewParent,"mCallback");
//                            Object mCallbackWrapper =getCallbackProxy(mCallback);
//                            if(!Proxy.isProxyClass(mCallback.getClass())) {
//                                XposedHelpers.setObjectField(viewParent, "mCallback", mCallbackWrapper);
//                            }
//                            for (Field field : patternViewClass.getDeclaredFields()) {
//                                fieldMap.put(field.getName(), XposedHelpers.getObjectField(param.thisObject, field.getName()));
//                            }
                            Context c = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                            SharedPreferences pref = new RemotePreferences(c, "com.ilgazer.XLockscreen.preferences", "pattern");
                            List pattern = (List) XposedHelpers.getObjectField(param.thisObject, "mPattern");
                            String patternEntered = patternToString(pattern);
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
                                        break;
                                    case "nofail":
                                        hideNextPatternNumber = true;
                                        break;
//                                    case "resetfail":
//                                        Class updateMonitorClass = XposedHelpers.findClass("com.android.internal.policy.impl.keyguard.KeyguardUpdateMonitor", param.thisObject.getClass().getClassLoader());
//                                        Object updateMonitor = XposedHelpers.callStaticMethod(updateMonitorClass, "getInstance", c);
//                                        XposedHelpers.callMethod(updateMonitor, "clearFailedUnlockAttempts");
//                                        log(Constants.LOG_TAG + " Cleared unlock attempts.");
//                                        break;
                                    case "null":
                                        break;
                                    default:
                                        log(Constants.LOG_TAG + " Unrecognised task: " + task[0]);
                                        break;
                                }
                            }
//                                        XposedHelpers.callMethod(mCallback, "reportSuccessfulUnlockAttempt");
//                                        List old=pattern.subList(0, 3);
//                                        pattern.clear();
//                                        pattern.addAll(old);
                            try {
                                XposedHelpers.findField(patternViewClass, "mBitmapCircleRed");
                            } catch (NoSuchFieldError e) {
                                log(Constants.LOG_TAG + "mBitmapCircleRed does not exist.");
                                useBitmapCircle = false;
                            }
                            Paint mPaint = (Paint) XposedHelpers.getObjectField(param.thisObject, "mPathPaint");
                            if (useBitmapCircle) {
                                if (mBitmapCircleRed == null) {
                                    mBitmapCircleRed = ((Bitmap) XposedHelpers.getObjectField(param.thisObject, "mBitmapCircleRed")).copy(Bitmap.Config.ARGB_8888, true);
                                    mBitmapArrowRedUp = ((Bitmap) XposedHelpers.getObjectField(param.thisObject, "mBitmapArrowRedUp")).copy(Bitmap.Config.ARGB_8888, true);
                                    log(Constants.LOG_TAG + "Copying mBitmapCircleRed.");
                                }
                                if (color != null) {
                                    XposedHelpers.setObjectField(param.thisObject, "mBitmapCircleRed", changeBitmapColor(mBitmapCircleRed, color));
                                    XposedHelpers.setObjectField(param.thisObject, "mBitmapArrowRedUp", changeBitmapColor(mBitmapArrowRedUp, color));
                                    mPaint.setAlpha(0x80);
                                } else if (XposedHelpers.getBooleanField(param.thisObject, "resetStealthMode")) {
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
                            } else {
                                boolean mInStealthMode =XposedHelpers.getBooleanField(param.thisObject, "mInStealthMode");
                                log(Constants.LOG_TAG + " stealth : "+ mInStealthMode);

                                if (color != null) {
                                    XposedHelpers.setIntField(param.thisObject, "mErrorColor", color);
//                                    mPaint.setColor(color);
                                    XposedHelpers.setBooleanField(param.thisObject, "mInStealthMode", false);
                                    resetStealthMode=mInStealthMode;
                                }else {
                                    XposedHelpers.setIntField(param.thisObject, "mErrorColor", Color.RED);
//                                    mPaint.setColor(Color.WHITE);
//                                    XposedHelpers.setBooleanField(param.thisObject, "resetStealthMode", resetStealthMode);
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

            XposedHelpers.findAndHookMethod(
                    patternViewClass, "notifyPatternStarted",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if(resetStealthMode)
                                XposedHelpers.setBooleanField(param.thisObject, "resetStealthMode", true);
                        }
                    });

            XposedHelpers.findAndHookMethod(patternViewClass,
                    "setOnPatternListener", XposedHelpers.findClass("com.android.internal.widget.LockPatternView$OnPatternListener", lpparam.classLoader),
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.args[0] = PatternListenerDecorator.getProxy(param.args[0]);
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
        //Use verifyPasswordAndUnlock from com.android.keyguard.KeyguardAbsKeyInputView instead to work around minimum password length.
        Method checkPassword = null;
        for (Method method : lockPatternUti̇ls.getDeclaredMethods()) {
            if("checkPassword".equals(method.getName()))
                checkPassword=method;
        }
        XposedBridge.hookMethod(checkPassword, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        Context c = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                        SharedPreferences pref = new RemotePreferences(c, "com.ilgazer.XLockscreen.preferences", "password");
                        String patternEntered = (String) param.args[0];
                        log(Constants.LOG_TAG + " PATTERN ENTERED " + patternEntered);
                        String logPrefs = "";
                        for (Map.Entry e : pref.getAll().entrySet()) {
                            logPrefs += ("[" + e.getKey().toString() + "," + e.getValue().toString() + "]");
                        }
                        log(Constants.LOG_TAG + " PREFS " + logPrefs);
                        log(Constants.LOG_TAG + " PATTERN ACTION " + pref.getString("assign_" + patternEntered, "null"));
                        String[] tasks = pref.getString("assign_" + patternEntered, "null").split(";");
                        for (String taskRaw : tasks) {
                            String[] task = taskRaw.split(",");
                            switch (task[0]) {
                                case "tasker":
                                    final int numEntered = pref.getInt("ret_" + patternEntered, 0);
                                    log(Constants.LOG_TAG + " NUM_ENTERED " + numEntered);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putInt("ret_" + patternEntered, numEntered + 1);
                                    editor.apply();
                                    c.sendBroadcast(INTENT_REQUEST_REQUERY);
                            }
                        }
                    }
                });
    }

    private static String patternToString(List pattern) {
        if (pattern == null) {
            return "";
        }
        int patternSize = pattern.size();
        StringBuilder stringBuilder = new StringBuilder();
        Method getRow = XposedHelpers.findMethodBestMatch(pattern.get(0).getClass(), "getRow");
        Method getColumn = XposedHelpers.findMethodBestMatch(pattern.get(0).getClass(), "getColumn");

        for (int i = 0; i < patternSize; i++) {
            Object dot = pattern.get(i);
            try {
                stringBuilder.append(((int)getRow.invoke(dot) * 3 + (int)getColumn.invoke(dot)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
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

    private static class PatternListenerDecorator implements InvocationHandler {
        final Object inner;

        static Object getProxy(Object o) {
            return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), new PatternListenerDecorator(o));
        }

        private PatternListenerDecorator(Object inner) {
            this.inner = inner;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            if (hideNextPatternNumber && method.getName().equals("onPatternDetected")) {
                hideNextPatternNumber = false;
                log(Constants.LOG_TAG + " Replaced list with decorated for onPatternDetected.");
                return method.invoke(inner, PatternListDecorator.getProxy((List) args[0]));
            }
//            log(Constants.LOG_TAG + " " + method.getName() + " with args: " + Arrays.toString(args));
            return method.invoke(inner, args);
        }
    }

    private static class PatternListDecorator implements InvocationHandler {
        final List inner;
        final int lieAt;
        int lieIndex = 0;

        static Object getProxy(List o) {
            return Proxy.newProxyInstance(List.class.getClassLoader(), new Class[]{List.class}, new PatternListDecorator(o, 3));
        }

        private PatternListDecorator(List inner, int lieAt) {
            this.inner = inner;
            this.lieAt = lieAt;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("size")) {
                lieIndex++;
                if (lieAt == lieIndex) {
                    return 3;
                }
            }
//            log(Constants.LOG_TAG + " " + method.getName() + " with args: " + Arrays.toString(args));
            return method.invoke(inner, args);
        }
    }

}
