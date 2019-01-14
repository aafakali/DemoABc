package siyatechventures.com.demo;

import android.util.Log;

public class DemoLocation {

    private static final DemoLocation ourInstance = new DemoLocation();

    public static DemoLocation getInstance() {
        return ourInstance;
    }

    private DemoLocation() {
        Log.e("construcotor called","call");
    }
}
