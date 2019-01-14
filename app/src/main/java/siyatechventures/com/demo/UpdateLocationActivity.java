package siyatechventures.com.demo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import siyatechventures.com.fetchlocation.FetchLocation;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class UpdateLocationActivity extends AppCompatActivity implements  SimpleLocation.Listener, View.OnClickListener {

    TextView tvLocation,tvLastLocation;
    Button btnLastLocation;

    private ArrayList<String> permissions = new ArrayList();
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;

    FetchLocation fetchLocation;

    SimpleLocation simpleLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location);

        tvLocation=(TextView)findViewById(R.id.tv_location);
        tvLastLocation=(TextView)findViewById(R.id.tv_last_location);
        btnLastLocation=(Button)findViewById(R.id.btn_last);
        btnLastLocation.setOnClickListener(this);
        simpleLocation=SimpleLocation.getInstence(this);
        simpleLocation.setmListener(this);
        simpleLocation.checkPermission();
        simpleLocation.startLocationUpdates();
        Log.e("oncreate","called");
        simpleLocation.getLastUpdatedLocation(new SimpleLocation.Listener() {
            @Override
            public void onLocation(Location location) {
                Log.e("last location","called");
            }
        });

//        Intent intent=new Intent(this,UploadDataService.class);
//        startService(intent);


       /* simpleLocation.getLastUpdatedLocation(new SimpleLocation.Listener() {
            @Override
            public void onLocation(Location location) {

                tvLocation.setText("\t location\n\n"+location.getLatitude()+"\n\n"+location.getLongitude());
            }
        });*/



    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    public void onLocation(Location location) {
        Log.e("location",location.getLatitude()+"==="+location.getLongitude());
        tvLocation.setText("\t location\n\n"+location.getLatitude()+"\n\n"+location.getLongitude());
    }

    @Override
    public void onClick(View view) {
        Location location=simpleLocation.getLocation();
        tvLastLocation.setText("\t location\n\n"+location.getLatitude()+"\n\n"+location.getLongitude());
    }

    private int getSum(){
        final int[] sum = {0};
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sum[0] =5;
            }
        },200);
        return sum[0];
    }
}
