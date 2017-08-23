package com.ilgazer.XLockscreen;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

/**
 * Created by ilgaz on 03.08.2017.
 */

public class MyPreferenceProvider extends RemotePreferenceProvider {
    public MyPreferenceProvider() {
        super("com.ilgazer.XLockscreen.preferences", new String[] {"main_prefs", "password", "pattern"});
    }
}
