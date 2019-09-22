package com.oytu.ario;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class BroadCastWifi extends BroadcastReceiver {
    WifiManager wifiManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean atv = intent.getExtras().getBoolean("ativar");
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (atv) {
            Toast.makeText(context, "Ativando Wifi", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }else{
            Toast.makeText(context,"Desativando Wifi", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(false);
        }
    }
}
