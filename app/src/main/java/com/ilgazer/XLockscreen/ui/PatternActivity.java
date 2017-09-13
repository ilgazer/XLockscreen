/*
 * Copyright 2013 two forty four a.m. LLC <http://www.twofortyfouram.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.ilgazer.XLockscreen.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.crossbowffs.remotepreferences.RemotePreferences;
import com.ilgazer.XLockscreen.Constants;
import com.ilgazer.XLockscreen.R;
import com.ilgazer.XLockscreen.bundle.BundleScrubber;
import com.ilgazer.XLockscreen.bundle.PluginBundleManager;

import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * This is the "Edit" activity for a Locale Plug-in.
 * <p>
 * This Activity can be started in one of two states:
 * <ul>
 * <li>New plug-in instance: The Activity's Intent will not contain
 * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE}.</li>
 * <li>Old plug-in instance: The Activity's Intent will contain
 * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} from a previously saved plug-in instance that the
 * user is editing.</li>
 * </ul>
 *
 * @see com.twofortyfouram.locale.Intent#ACTION_EDIT_CONDITION
 * @see com.twofortyfouram.locale.Intent#EXTRA_BUNDLE
 */
public final class PatternActivity extends AbstractPluginActivity {
    /**
     * ListView shown in the Activity.
     */
    private PatternLockView mLock = null;
    private String mPattern = "null";
    private String mColor="";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleScrubber.scrub(getIntent());

        final Bundle localeBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(localeBundle);

        setContentView(R.layout.pattern_activity);
        mLock = findViewById(R.id.pattern_lock_view);
        mLock.setDotSelectedSize(mLock.getDotNormalSize() * 2);
        mLock.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                mPattern = PatternLockUtils.patternToString(mLock, pattern);
            }

            @Override
            public void onCleared() {

            }
        });
//        mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,
//                                                  android.R.id.text1,
//                                                  getResources().getStringArray(R.array.display_states)));
//
//        if (null == savedInstanceState)
//        {
//            if (PluginBundleManager.isBundleValid(localeBundle))
//            {
//                final boolean isDisplayOn =
//                        localeBundle.getBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOLEAN_STATE);
//                final int position =
//                        getPositionForIdInArray(getApplicationContext(), R.array.display_states, isDisplayOn
//                                ? R.string.list_on : R.string.list_off);
//                mList.setItemChecked(position, true);
//            }
//        }
    }

    public void selectColor(final View view){
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Color.RED, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mColor="color,"+color+";";
                view.setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });

        dialog.show();
    }

    @Override
    public void finish() {
        Log.v(Constants.LOG_TAG, "Setting lockstr to '" + mPattern + "'"); //$NON-NLS-1$

        if (!isCanceled()) {
            SharedPreferences pref = new RemotePreferences(this, "com.ilgazer.XLockscreen.preferences", "pattern");
//            todo figure out why getSharedPrefs desyncs.
//            SharedPreferences pref = this.getSharedPreferences("main_prefs", MODE_PRIVATE);
//            Log.i(XposedMain.LOG_TAG, "pref"+ pref.);
            SharedPreferences.Editor editor = pref.edit();
            StringBuilder config=new StringBuilder("tasker;");
            config.append(mColor);
            CheckBox cb = findViewById(R.id.suppress_wrong);
            if(cb.isChecked())
                config.append("nofail;");
            editor.putString("assign_" + mPattern, config.toString());
            Log.i(Constants.LOG_TAG, mColor);
            editor.apply();


            final Intent resultIntent = new Intent();

            /*
             * This extra is the data to ourselves: either for the Activity or the BroadcastReceiver. Note
             * that anything placed in this Bundle must be available to Locale's class loader. So storing
             * String, int, and other standard objects will work just fine. Parcelable objects are not
             * acceptable, unless they also implement Serializable. Serializable objects must be standard
             * Android platform objects (A Serializable class private to this plug-in's APK cannot be
             * stored in the Bundle, as Locale's classloader will not recognize it).
             */
            final Bundle resultBundle =
                    PluginBundleManager.generateBundle(getApplicationContext(), PluginBundleManager.Type.PATTERN, mPattern);
            resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

                /*
                 * The blurb is concise status text to be displayed in the host's UI.
                 */
            resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, "Pattern Number:" + mPattern);

            setResult(RESULT_OK, resultIntent);
//            if (AdapterView.INVALID_POSITION != mList.getCheckedItemPosition())
//            {
//                final int selectedResourceId =
//                        getResourceIdForPositionInArray(getApplicationContext(), R.array.display_states,
//                                                        mList.getCheckedItemPosition());
//
//                final boolean isDisplayOn;
//                if (R.string.list_on == selectedResourceId)
//                {
//                    isDisplayOn = true;
//                }
//                else if (R.string.list_off == selectedResourceId)
//                {
//                    isDisplayOn = false;
//                }
//                else
//                {
//                    throw new AssertionError();
//                }
//
//                final Intent resultIntent = new Intent();
//
//                /*
//                 * This extra is the data to ourselves: either for the Activity or the BroadcastReceiver. Note
//                 * that anything placed in this Bundle m
//                final Bundle reust be available to Locale's class loader. So storing
//                 * String, int, and other standard objects will work just fine. Parcelable objects are not
//                 * acceptable, unless they also implement Serializable. Serializable objects must be standard
//                 * Android platform objects (A Serializable class private to this plug-in's APK cannot be
//                 * stored in the Bundle, as Locale's classloader will not recognize it).
//                 */sultBundle =
//                        PluginBundleManager.generateBundle(getApplicationContext(), isDisplayOn);
//                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);
//
//                /*
//                 * The blurb is concise status text to be displayed in the host's UI.
//                 */
//                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB,
//                                      generateBlurb(getApplicationContext(), isDisplayOn));
//
//                setResult(RESULT_OK, resultIntent);
//            }
        }
        super.finish();
    }

    /*/*
     * @param context     Application context.
     * @param isDisplayOn True if the plug-in detects when the display is on.
     * @return A blurb for the plug-in.
     */
    /* package */
//    static String generateBlurb(final Context context, final boolean isDisplayOn) {
//        if (isDisplayOn) {
//            return context.getString(R.string.blurb_on);
//        }
//
//        return context.getString(R.string.blurb_off);
//    }

}