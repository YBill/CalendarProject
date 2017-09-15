package com.example.bill.calendarproject;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("Bill", "androidId:" + androidId);

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        try {
            String imei = telephonyManager.getDeviceId();
            Log.e("Bill", "imei:" + imei);
            UUID uuid = UUID.nameUUIDFromBytes(imei.getBytes("utf8"));
            Log.e("Bill", "imei:" + uuid.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
