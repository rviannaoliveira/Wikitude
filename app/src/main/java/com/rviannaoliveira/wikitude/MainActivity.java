package com.rviannaoliveira.wikitude;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ArchitectView architectView;
    private int PERMISSION_CAMERA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(">>>>>>>", String.valueOf(ArchitectView.isDeviceSupported(this)) );
        requestCameraPermission();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(architectView != null){
            loadAR();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAMERA && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }else{
            initAR();
            loadAR();
        }
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            PERMISSION_CAMERA = 132;
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        } else {
            initAR();
        }
    }

    private void initAR() {
        this.architectView = this.findViewById(R.id.architectView);
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey(getString(R.string.key));
        this.architectView.onCreate(config);
    }

    private void loadAR() {
        try {
            this.architectView.onPostCreate();
            this.architectView.load("index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}