package com.greathari.networksensingjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.greathari.networksensingjava.util.CustomSnackbar;
import com.greathari.networksensingjava.util.NetworkUtil;

public class NetworkSensing extends AppCompatActivity {
    CustomSnackbar customSnackbar;
    private BroadcastReceiver mNetworkReceiver;
    ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mNetworkReceiver);
        mNetworkReceiver = null;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rootView == null) rootView = findViewById(android.R.id.content);
        if (customSnackbar == null){
            customSnackbar = CustomSnackbar.make(rootView, customSnackbar.LENGTH_INDEFINITE).setText("No internet connection.");
            customSnackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
        }
        if (mNetworkReceiver == null) mNetworkReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public class NetworkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "networkStsts received", Toast.LENGTH_LONG).show();
            String status = NetworkUtil.getConnectivityStatusString(context);
            showSnacks(status);
        }
    }

    private void showSnacks(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (status.equals("Not connected to Internet")){
                    customSnackbar.show();
                }else {
                    customSnackbar.dismiss();
                }
            }
        });
    }
}
