package siyatechventures.com.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SimpleLocation.getInstence(this).checkPermission();
        Intent intent=new Intent(this,UpdateLocationActivity.class);
        startActivity(intent);
    }
}
