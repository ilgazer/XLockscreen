package com.ilgazer.XLockscreen.ui;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ilgazer.XLockscreen.R;

public class MainActivity extends Activity {

    private ListView mList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = ((ListView) findViewById(android.R.id.list));
        ListAdapter adapter= ArrayAdapter.createFromResource(this,
                R.array.modes, android.R.layout.simple_list_item_1);
        mList.setAdapter(adapter);
    }

}
