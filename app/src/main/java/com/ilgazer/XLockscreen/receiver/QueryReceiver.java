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

package com.ilgazer.XLockscreen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;
import java.util.Map;

import com.crossbowffs.remotepreferences.RemotePreferences;
import com.ilgazer.XLockscreen.Constants;
import com.ilgazer.XLockscreen.bundle.BundleScrubber;
import com.ilgazer.XLockscreen.bundle.PluginBundleManager;
import com.ilgazer.XLockscreen.ui.EditActivity;

/**
 * This is the "query" BroadcastReceiver for a Locale Plug-in condition.
 *
 * @see com.twofortyfouram.locale.Intent#ACTION_QUERY_CONDITION
 * @see com.twofortyfouram.locale.Intent#EXTRA_BUNDLE
 */
public final class QueryReceiver extends BroadcastReceiver {

    /**
     * @param c      {@inheritDoc}.
     * @param intent the incoming {@link com.twofortyfouram.locale.Intent#ACTION_QUERY_CONDITION} Intent. This
     *               should always contain the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} that was
     *               saved by {@link EditActivity} and later broadcast by Locale.
     */
    @Override
    public void onReceive(final Context c, final Intent intent) {

        /*
         * Always be strict on input parameters! A malicious third-party app could send a malformed Intent.
         */

        if (!com.twofortyfouram.locale.Intent.ACTION_QUERY_CONDITION.equals(intent.getAction())) {
            if (Constants.IS_LOGGABLE) {
                Log.e(Constants.LOG_TAG,
                        String.format(Locale.US, "Received unexpected Intent action %s", intent.getAction())); //$NON-NLS-1$
            }
            return;
        }

        BundleScrubber.scrub(intent);

        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(bundle);
//        com.twofortyfouram.locale.Intent.
//        if (PluginBundleManager.isBundleValid(bundle)){
        final String requestedPattern = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_LOCK);
        final String requestedType = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_TYPE);

        Log.i(Constants.LOG_TAG, "requested " + requestedType + " : " + requestedPattern);

        SharedPreferences pref = new RemotePreferences(c, "com.ilgazer.XLockscreen.preferences", requestedType);
//        todo figure out why getSharedPrefs desyncs.
//        SharedPreferences pref = c.getSharedPreferences("main_prefs", Context.MODE_PRIVATE);
                //PreferenceManager.getSharedPreferences
        String logPrefs = "";
        for (Map.Entry e : pref.getAll().entrySet()) {
            logPrefs += ("[" + e.getKey().toString() + "," + e.getValue().toString() + "]");
        }
        Log.i(Constants.LOG_TAG, "prefs " + logPrefs);
//            Log.i(Constants.LOG_TAG,"sharedpref = "+pref.);
        final int numEntered = pref.getInt("ret_" + requestedPattern, 0);
        Log.i(Constants.LOG_TAG, "numEntered from bundle side " + numEntered);
        if (numEntered > 0) {
            SharedPreferences.Editor editor = pref.edit();
            setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_SATISFIED);
            editor.putInt("ret_" + requestedPattern, 0);
            editor.apply();
        } else {
            setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNSATISFIED);
        }
        Log.i(Constants.LOG_TAG, "postactnum from bundle side " + pref.getInt("ret_" + requestedPattern, 0));
//            final boolean isScreenOn =
//                    (((PowerManager) c.getSystemService(Context.POWER_SERVICE)).isScreenOn());
//            final boolean conditionState = bundle.getBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOLEAN_STATE);
//
//            if (Constants.IS_LOGGABLE)
//            {
//                Log.v(Constants.LOG_TAG,
//                      String.format(Locale.US,
//                                    "Screen state is %b and condition state is %b", isScreenOn, conditionState)); //$NON-NLS-1$
//            }
//            if (isScreenOn)
//            {
//                if (conditionState)
//                {
//                    setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_SATISFIED);
//                }
//                else
//                {
//                    setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNSATISFIED);
//                }
//            }
//            else
//            {
//                if (conditionState)
//                {
//                    setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNSATISFIED);
//                }
//                else
//                {
//                    setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_SATISFIED);
//                }
//            }
//
//            /*
//             * Because conditions are queried in the background and possibly while the phone is asleep, it is
//             * necessary to acquire a WakeLock in order to guarantee that the service is started.
//             */
//
//            /*
//             * To detect screen changes as they happen, a service must be running because the SCREEN_ON/OFF
//             * Intents are REGISTERED_RECEIVER_ONLY.
//             *
//             * To avoid a gap in detecting screen on/off changes, the current state of the screen needs to be
//             * sent to the service.
//             */
//            c.startService(new Intent(c, BackgroundService.class).putExtra(BackgroundService.EXTRA_BOOLEAN_WAS_SCREEN_ON,
//                                                                                       isScreenOn));
//        }
    }
}