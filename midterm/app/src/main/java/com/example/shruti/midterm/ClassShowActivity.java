package com.example.shruti.midterm;

/**
 * Created by shruti on 3/5/18.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ClassShowActivity  extends Activity {

    TextView studentname;

    ListView list;

    String[] classnames = { "Android Development", "Data Networks","Databases","Internet Security","Java Programming","Software Engineering"
    };



    String[] classdescription = {"555","485","411","510","312","511"};






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classlist);


        String data = getIntent().getExtras().getString("usernamekey");

        CustomListAdapter adapter=new CustomListAdapter(this, classnames, classdescription);
        list = (ListView) findViewById (R.id.classlist);
        studentname =(TextView) findViewById(R.id.textView4);
        studentname.setText(data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                String Slecteditem = classnames[+position];
                String Slecteddesc = classdescription[+position];
                Toast.makeText(getApplicationContext(),
                        Slecteditem, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),
                        Slecteddesc, Toast.LENGTH_LONG).show();
            }
        });
    }
}


