


package com.example.shruti.midterm;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Parcelable;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    Context context;

    Button loginButton;

    EditText username;
    EditText password;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        loginButton = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClassActivity();

            }
        });
    }
    public void startClassActivity() {
        String s =username.getText().toString();


        if (TextUtils.isEmpty(s)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Fields are empty,Please, fill in all the fields");
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





       else if(username.getText().toString().equals("admin") &&
                password.getText().toString().equals("admin")||username.getText().toString().equals("shruti") &&
                password.getText().toString().equals("shruti")||username.getText().toString().equals("ABC") &&
                password.getText().toString().equals("xyz")) {
            Toast.makeText(getApplicationContext(),
                    "Redirecting...",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, com.example.shruti.midterm.ClassShowActivity.class);
            intent.putExtra("usernamekey", username.getText().toString());
            this.startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Login Failed ",Toast.LENGTH_SHORT).show();
            counter++;
            Toast.makeText(getApplicationContext(), "Wrong Credential entered "+counter +" times" ,Toast.LENGTH_SHORT).show();
        }

    }
}




