package siyatechventures.com.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import javax.security.auth.login.LoginException;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class SimpleLocation {

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 506;
    private LocationRequest mLocationRequest;
    Context context;
    private Listener mListener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private Location mLocation;
    private LocationCallback locationCallback;
    FusedLocationProviderClient locationClient;
    private static SimpleLocation simpleLocation;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,

    };

    public static SimpleLocation getInstence(Context context) {
        if (simpleLocation != null) {
            return simpleLocation;
        } else {
            return simpleLocation = new SimpleLocation(context);
        }
    }

    public SimpleLocation(final Context context) {
        Log.e("simpleLocation","Called");
        this.context = context;
        locationClient = getFusedLocationProviderClient(context);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        getLastLocation();
    }

    public Listener getmListener() {
        return mListener;
    }

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public interface Listener {
        void onLocation(Location location);
    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        // Create the location request to start receiving updates

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // do work here
                if (mListener != null)
                    mListener.onLocation(locationResult.getLastLocation());
                mLocation = locationResult.getLastLocation();
            }
        };
        locationClient.requestLocationUpdates(mLocationRequest, locationCallback,
                Looper.myLooper());


    }


    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            mLocation = location;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    public interface ProfileSingleCallback {
        void onProfileCached(Location profile);
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        getLastLocation();
        return mLocation;
//        return mLocation;
    }

    @SuppressLint("MissingPermission")
    public void getLastUpdatedLocation(Listener listener){
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                listener.onLocation(location);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("onFailure","called");
                if (!hasPermissions(context, permissions)) {
                    ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
            }
        });
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        locationClient.removeLocationUpdates(locationCallback).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("stopLocation", "successfully");
            }
        });
    }

    public void checkPermission(){
        if (!hasPermissions(context, permissions)) {
            ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }
}
