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

package com.ilgazer.XLockscreen.bundle;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ilgazer.XLockscreen.Constants;

import java.io.Serializable;

/**
 * Class for managing the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} for this plug-in.
 */
public final class PluginBundleManager
{
    public static final String BUNDLE_EXTRA_STRING_LOCK =
            "com.ilgazer.XLockscreen.extra.STRING_LOCK"; //$NON-NLS-1$
    public static final String BUNDLE_EXTRA_ENUM_TYPE =
            "com.ilgazer.XLockscreen.extra.ENUM_TYPE"; //$NON-NLS-1$
    public enum Type{
        PASSWORD("password"), PATTERN("pattern");

        private final String type;
        Type(String s){
         type=s;
        }

        @Override
        public String toString() {
            return type;
        }
    }
    /**
     * Type: {@code int}.
     * <p>
     * versionCode of the plug-in that saved the Bundle.
     */
    /*
     * This extra is not strictly required, however it makes backward and forward compatibility significantly
     * easier. For example, suppose a bug is found in how some version of the plug-in stored its Bundle. By
     * having the version, the plug-in can better detect when such bugs occur.
     */
    private static final String BUNDLE_EXTRA_INT_VERSION_CODE =
            "com.ilgazer.XLockscreen.extra.INT_VERSION_CODE"; //$NON-NLS-1$

    /**
     * Method to verify the content of the bundle are correct.
     * <p>
     * This method will not mutate {@code bundle}.
     *
     * @param bundle bundle to verify. May be null, which will always return false.
     * @return true if the Bundle is valid, false if the bundle is invalid.
     */
    public static boolean isBundleValid(final Bundle bundle)
    {
        if (null == bundle)
        {
            return false;
        }

        /*
         * Make sure the expected extras exist
         */
        if (!bundle.containsKey(BUNDLE_EXTRA_STRING_LOCK))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                      String.format("bundle must contain extra %s", BUNDLE_EXTRA_STRING_LOCK)); //$NON-NLS-1$
            }
            return false;
        }
        if (!bundle.containsKey(BUNDLE_EXTRA_INT_VERSION_CODE))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                      String.format("bundle must contain extra %s", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            }
            return false;
        }

        /*
         * Make sure the correct number of extras exist. Run this test after checking for specific Bundle
         * extras above so that the error message is more useful. (E.g. the caller will see what extras are
         * missing, rather than just a message that there is the wrong number).
         */
        if (2 != bundle.keySet().size())
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                      String.format("bundle must contain 2 keys, but currently contains %d keys: %s", bundle.keySet().size(), bundle.keySet())); //$NON-NLS-1$
            }
            return false;
        }

        /*
         * Make sure the extra is the correct type
         */
        if (!bundle.getString(BUNDLE_EXTRA_STRING_LOCK, "0").equals(bundle.getString(BUNDLE_EXTRA_STRING_LOCK, "1")))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                      String.format("bundle extra %s appears to be the wrong type.  It must be a boolean", BUNDLE_EXTRA_STRING_LOCK)); //$NON-NLS-1$
            }

            return false;
        }

        /*
         * Make sure the extra is the correct type
         */
        if (bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 0) != bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 1))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                      String.format("bundle extra %s appears to be the wrong type.  It must be an int", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            }

            return false;
        }

        return true;
    }

    /**
     * @param context Application context.
     * @param type Lockscreen type(password or pattern).
     * @param code The password or pattern required.
     *
     * @return A plug-in bundle.
     */
    public static Bundle generateBundle(final Context context, final PluginBundleManager.Type type, final String code)
    {
        final Bundle result = new Bundle();
        result.putString(BUNDLE_EXTRA_STRING_LOCK, code);
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(context));
        result.putInt(BUNDLE_EXTRA_ENUM_TYPE, type.ordinal());
        return result;
    }

    /**
     * Private constructor prevents instantiation
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private PluginBundleManager()
    {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}