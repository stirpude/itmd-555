package com.example.shruti.week05;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    Context context;
    SeekBar seekbar;
    Button startButton;
    int seconds = 1;
    TextView txtvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        startButton = (Button) findViewById(R.id.startButton);
        txtvalue = (TextView) findViewById(R.id.textView2);
        //txtvalue = (TextView) findViewById(R.id.textView2);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChangedValue = i;
                seconds = progressChangedValue;
                txtvalue.setText(String.valueOf(progressChangedValue));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(context, progressChangedValue + "",
                        Toast.LENGTH_SHORT).show();
            }

        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSlideShowActivity(seconds);
                Toast.makeText(context, "Interval: " + seconds + "",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void startSlideShowActivity(int seconds) {
        if (seconds != 0) {
            Intent intent = new Intent(MainActivity.this, com.example.shruti.week05.SlideShowActivity.class);
            intent.putExtra("interval", seconds);
            this.startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please, choose a value higher than 0");
            builder.setCancelable(true);
            builder.setPositiveButton(
                    "OK",

                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder.create();
            alert11.show();
        }
    }
}
