package com.magde.adapterfordatamobile_ipda035;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class ServiceCore extends Service {

    private final static String SCAN_ACTION = "scan.rcv.message";
    private final static String ACTION_UROVO = "urovo.rcv.message";
    private final static String ACTION_SERVICE = "adaptperurovo.service.state";

    private final BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            intent.setAction(ACTION_UROVO);
            sendBroadcast(intent);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        String CHANNEL_ID = "my_channel_01";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build();

        startForeground(1,notification);

        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);

        Intent intent = new Intent(ACTION_SERVICE);
        intent.putExtra("isRunning", true);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mScanReceiver);
        Intent intent = new Intent(ACTION_SERVICE);
        intent.putExtra("isRunning", false);
        sendBroadcast(intent);

    }

}
