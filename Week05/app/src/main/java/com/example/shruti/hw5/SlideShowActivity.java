package com.example.shruti.week05;

/**
 * Created by shruti on 2/24/18.
 */

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.viewpagerindicator.CirclePageIndicator;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.Toast;

import static android.widget.Toast.*;

public class SlideShowActivity extends AppCompatActivity {
    int interval;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    Timer swipeTimer;
    ArrayList<ImageModel> arrList;



    public static final String[] titles = new String[]{ "Harry Potter", "To kill a Mockingbird","Anne Frank","Sherlock Holmes","Pygmalion","The Alchemist","Intrepreter of Maladies ","Malgudi Days","Grapes of Wrath","Gitanjali","Major Barbara"
    };

    public static final Integer[] images = { R.drawable.harry, R.drawable.mock,R.drawable.anne,R.drawable.sherlock,R.drawable.pyg,R.drawable.alchemy,R.drawable.intre,R.drawable.magudi,R.drawable.graps,R.drawable.gita,R.drawable.barabara};

    public static final String[] desc = new String[]{"Best fiction favourite book","Best classic book ever","Touching story","Best Detective Novel","Great Drama","Philosophical Teachings","Real Life Indian Stories","Adventures of Malgudi People","Realistic Novel","Nobel Prize Winner","Powerful Women Sergeant"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_show_layout);
        interval = getIntent().getIntExtra("interval", 1);
// Log.i("interval: ",interval +"");
        arrList = new ArrayList<ImageModel>();
        populateList();
        init();
    }
    @Override
    public void onBackPressed() {
        swipeTimer.cancel();
        currentPage = 0;
        super.onBackPressed();
    }
    public void populateList() {
        for (int i = 0; i < titles.length; i++) {
            arrList.add(new ImageModel(images[i], titles[i], desc[i]));
        }
    }
    private void init() {
        mPager = (ViewPager) findViewById(R.id.pager);

        mPager.setAdapter(new SlidingImage_Adapter(SlideShowActivity.this,
                arrList));
        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
        indicator.setRadius(5 * density);
        NUM_PAGES = arrList.size();
// Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, interval * 1000);
// Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }
            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });
    }
    public class SlidingImage_Adapter extends PagerAdapter {
        private ArrayList<ImageModel> arrImageModel;
        private LayoutInflater inflater;
        private Context context;
        public SlidingImage_Adapter(Context context, ArrayList<ImageModel>
                arrImageModel) {
            this.context = context;

            this.arrImageModel = arrImageModel;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public int getCount() {
            return arrImageModel.size();
        }
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.image_layout, view,
                    false);
            assert imageLayout != null;
            ImageView imageView = imageLayout.findViewById(R.id.imageview);
            TextView titleTextview = imageLayout.findViewById(R.id.titleTextview);
            TextView descTextview = imageLayout.findViewById(R.id.descTextview);

                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                  arrImageModel.get(position).getImageId()));
                titleTextview.setText(arrImageModel.get(position).getTitle());
                descTextview.setText(arrImageModel.get(position).getDesc());




            view.addView(imageLayout, 0);
            //Toast.setGravity(Gravity.BOTTOM , 0, 0,
            //Toast.makeText(context, "Index-> " +position  + "",
                   // LENGTH_SHORT).show());
            Toast toast = Toast.makeText(getApplicationContext(), "Index->"+position, Toast.LENGTH_SHORT);

// Set the Gravity to Bottom and Right

            toast.setGravity(Gravity.BOTTOM, 0,0);

            toast.show();

            return imageLayout;



        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }
        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}
