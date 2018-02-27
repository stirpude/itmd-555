package com.example.shruti.week005;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView txtvalue;
    private Button myButton;
    int seconds = 1;
    int slideShowCount;
    private TextView txtvalue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        txtvalue = (TextView) findViewById(R.id.textView2);
        myButton = (Button) findViewById(R.id.button1);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    int progress = 0;
                    double decimalProgress;
                    double celcius;
                    //int slideShowCount;


                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {

                        slideShowCount = (int) progress;

                        txtvalue.setText(String.valueOf(slideShowCount));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview
                    }

                });


        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        My2ndAct.class);
               // Bundle bundle = new Bundle();
                //bundle.putString("interval",slideShowCount);
                //intent.putExtra(bundle);
                //intent.putExtra("interval", slideShowCount);
                intent.putExtra("int_value", slideShowCount);
                startActivity(intent);
            }
        });

    }
}
