package com.example.shruti.week005;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


public class My2ndAct extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtStatus;
    //Bundle bundle1 = getIntent().getExtras();
    //String str  = bundle1.getString("interval”);
   // int convertedVal = Integer.parseInt(str);
    //System.out.println("ki"+convertedVal);
    //Log.d("MyApp","I am here");

    Intent intent = getIntent();
    int temp = intent.getIntExtra("int_value", 0);
    System.out.println("value"+temp);
    int i=0;
    //int interval;
    //int interval = getIntent().getIntExtra("interval", 1);
    int imgid[]={R.drawable.alchemy,R.drawable.pyg,R.drawable.sherlock,
           };
    RefreshHandler refreshHandler=new RefreshHandler();
    private Handler myHandler = new Handler();

    class RefreshHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            My2ndAct.this.updateUI();
        }

        public void sleep(long delayMillis){
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };


        public void updateUI(){
          //  int currentInt=Integer.parseInt((String)txtStatus.getText())+10;
            //if(currentInt<=100){
                refreshHandler.sleep(2000);

             Runnable myRunnable = new Runnable() {
                public void run() {
                    for(i=0;i<imgid.length;i++){
                        imageView.setImageResource(imgid[i]);

                        // imageView.setPadding(left, top, right, bottom);
                        myHandler.postDelayed(this, temp*1000);
                    }
// task to be run based a timer

                }
            };



            //}
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my2nd);
        this.imageView=(ImageView)this.findViewById(R.id.imageView);
        updateUI();
    }
}
