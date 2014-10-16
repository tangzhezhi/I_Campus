package com.stinfo.pushme;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * ActionBarActivity extends FragmentActivity
 * @author lenovo
 *
 */
public class BaseActionBarActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new MyCustomExceptionHandler(this));
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        //退出时进行垃圾回收
        System.gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
