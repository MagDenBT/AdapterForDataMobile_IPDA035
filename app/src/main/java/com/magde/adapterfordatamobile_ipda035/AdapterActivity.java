package com.magde.adapterfordatamobile_ipda035;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AdapterActivity extends AppCompatActivity {
    TextView textView;
    Boolean coreEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter);
        textView = findViewById(R.id.textView);
        setText(isMyServiceRunning(ServiceCore.class));
        setReceiver();
        startCore();

        Button bStart = findViewById(R.id.bStart);
        Button bStop = findViewById(R.id.bStop);

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCore();
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coreEnabled = false;
                stopService(new Intent(AdapterActivity.this,ServiceCore.class));
            }
        });

        startCore();
    }

    private void startCore() {
        coreEnabled = true;
        startForegroundService(new Intent(AdapterActivity.this, ServiceCore.class));
    }

    private void setText(boolean myServiceRunning) {

        if (myServiceRunning) {
            textView.setText("Запущено");
            textView.setTextColor(Color.GREEN);
        } else {
            textView.setText("Остановлено");
            textView.setTextColor(Color.RED);
        }
    }

    private void setReceiver() {

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean serviceRunning = intent.getBooleanExtra("isRunning", false);
                setText(serviceRunning);
                if (!serviceRunning && coreEnabled)
                    startCore();
            }
        };

        IntentFilter filter = new IntentFilter("adaptperurovo.service.state");
        registerReceiver(receiver, filter);
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
