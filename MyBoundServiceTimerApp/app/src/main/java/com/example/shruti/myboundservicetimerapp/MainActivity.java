
package com.example.shruti.myboundservicetimerapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shruti.myboundservicetimerapp.BoundService.MyBinder;

public class MainActivity extends AppCompatActivity {
    Context context;
    TextView timeTextview;
    Button startServicesBtn, stopServicesBtn, startBtn, stopBtn, resetBtn;
    long startTime;
    Handler handler;
    BoundService mBoundService;
    boolean mServiceBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        timeTextview = (TextView) findViewById(R.id.timeTextview);
        startServicesBtn = (Button) findViewById(R.id.startServicesBtn);
        stopServicesBtn = (Button) findViewById(R.id.stopServicesBtn);
        startBtn = (Button) findViewById(R.id.startBtn);
        stopBtn = (Button) findViewById(R.id.stopBtn);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        handler = new Handler();

        startBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        resetBtn.setEnabled(false);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mServiceBound) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    stopBtn.setEnabled(true);
                    startBtn.setEnabled(false);
                    resetBtn.setEnabled(true);
                }
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                startBtn.setEnabled(true);
                stopBtn.setEnabled(false);
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                timeTextview.setText("0:0:0:00");
                stopBtn.setEnabled(false);
                resetBtn.setEnabled(false);
            }
        });
        startServicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BoundService.class);
                startService(intent);
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                startBtn.setEnabled(true);
            }
        });
        stopServicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                if (mServiceBound) {
                    unbindService(mServiceConnection);
                    mServiceBound = false;
                }
                Intent intent = new Intent(context, BoundService.class);
                stopService(intent);

                startBtn.setEnabled(false);
                stopBtn.setEnabled(false);
                resetBtn.setEnabled(false);
            }
        });
    }
    public Runnable runnable = new Runnable() {
        public void run() {
            timeTextview.setText(mBoundService.getTimestamp(startTime));
            handler.postDelayed(this, 0);
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder myBinder = (MyBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }
}
