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

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ilgazer.XLockscreen.Constants;
import com.ilgazer.XLockscreen.R;
import com.ilgazer.XLockscreen.bundle.BundleScrubber;

import java.util.NoSuchElementException;

import com.ilgazer.XLockscreen.bundle.PluginBundleManager;


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
public final class EditActivity extends AbstractPluginActivity {
    /**
     * ListView shown in the Activity.
     */
    private ListView mList = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleScrubber.scrub(getIntent());

        final Bundle localeBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(localeBundle);

        setContentView(R.layout.edit_activity);
        String localeType = localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_TYPE);
        switch(localeType != null ? localeType : ""){
            case "pattern":
                Intent patternIntent = new Intent(this, PatternActivity.class);
                patternIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, localeBundle);
                startActivityForResult(patternIntent, 1);
                break;
            case "password":
                Intent passwordIntent = new Intent(this, PasswordActivity.class);
                passwordIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE,localeBundle);
                startActivityForResult(passwordIntent, 1);
                break;
        }

        mList = ((ListView) findViewById(android.R.id.list));
        ListAdapter adapter=ArrayAdapter.createFromResource(this,
                R.array.modes, android.R.layout.simple_list_item_1);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String out = ((String) adapterView.getItemAtPosition(i)).toLowerCase();

                switch(out){
                    case "pattern":
                        Intent patternIntent = new Intent(adapterView.getContext(), PatternActivity.class);
                        patternIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, localeBundle);
                        startActivityForResult(patternIntent, 1);
                        break;
                    case "password":
                        Intent passwordIntent = new Intent(adapterView.getContext(), PasswordActivity.class);
                        passwordIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE,localeBundle);
                        startActivityForResult(passwordIntent, 1);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(Constants.LOG_TAG, "got result");
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Gets the position of an element in a typed array
     *
     * @param context   Application context. Cannot be null.
     * @param arrayId   resource ID of the array.
     * @param elementId resource ID of the element in the array.
     * @return position of the {@code elementId} in the array.
     * @throws NoSuchElementException if {@code elementId} is not in the array.
     */
    /* package */
    static int getPositionForIdInArray(final Context context, final int arrayId,
                                       final int elementId) {
        if (Constants.IS_PARAMETER_CHECKING_ENABLED) {
            if (null == context) {
                throw new IllegalArgumentException("context cannot be null"); //$NON-NLS-1$
            }
        }

        TypedArray array = null;
        try {
            array = context.getResources().obtainTypedArray(arrayId);
            for (int x = 0; x < array.length(); x++) {
                if (array.getResourceId(x, 0) == elementId) {
                    return x;
                }
            }
        } finally {
            if (null != array) {
                array.recycle();
                array = null;
            }
        }

        throw new NoSuchElementException();
    }

    /**
     * Gets the position of an element in a typed array.
     *
     * @param context  Application context. Cannot be null.
     * @param arrayId  resource ID of the array.
     * @param position position in the array to retrieve.
     * @return resource id of element in {@code position}.
     * @throws IndexOutOfBoundsException if {@code position} is not in the array.
     */
    /* package */
    static int getResourceIdForPositionInArray(final Context context, final int arrayId,
                                               final int position) {
        if (Constants.IS_PARAMETER_CHECKING_ENABLED) {
            if (null == context) {
                throw new IllegalArgumentException("context cannot be null"); //$NON-NLS-1$
            }
        }

        TypedArray stateArray = null;
        try {
            stateArray = context.getResources().obtainTypedArray(arrayId);
            final int selectedResourceId = stateArray.getResourceId(position, 0);

            if (0 == selectedResourceId) {
                throw new IndexOutOfBoundsException();
            }

            return selectedResourceId;
        } finally {
            if (null != stateArray) {
                stateArray.recycle();
                stateArray = null;
            }
        }
    }
}