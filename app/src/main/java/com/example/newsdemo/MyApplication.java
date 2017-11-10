package com.example.newsdemo;

import android.app.Application;
import android.content.Context;

/**
 * @version $Rev$
 * @anthor Administrator
 * @dsc ${TOOD}
 * @updateAuthor $Author
 * @updateDsc ${TOOD}
 */
public class MyApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        sContext = getApplicationContext();
    }

    public static Context getContext(){
        return sContext;
    }
}
