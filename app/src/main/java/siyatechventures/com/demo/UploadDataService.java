package siyatechventures.com.demo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class UploadDataService extends Service {

    public static final long NOTIFY_INTERVAL = 5 * 1000; // 10 seconds

    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    Context context;


    public UploadDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context=this;
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);

    }

    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    Location location=SimpleLocation.getInstence(context).getLocation();
                    if (location!=null)
                    Log.e("getLocation",location.getLatitude()+"====");
                }

            });
        }



    }
}
