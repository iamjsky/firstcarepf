package kr.firstcare.android.app;

import android.app.Application;

/**
 * ClassName            MyApplication
 * Created by JSky on   2020-06-30
 * 
 * Description          싱글톤, 사용안함
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getMyApplicationContext() {
        if (instance == null) {
            throw new IllegalStateException("This Application does not inherit ");
        }

        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


    }
    
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
    
}
