package com.example.dusan.krokomer.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.dusan.krokomer.Service.Service;

public class SensorRestartedBroadcastReceiver  extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(new Intent(context, Service.class));
        }else{
            context.startService(new Intent(context, Service.class));
        }

    }
}
