package kutum.kelime.kelimekutum;

import android.app.ProgressDialog;
import androidx.room.Room;
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import kutum.kelime.kelimekutum.Adapter.MyViewPagerAdapter;
import kutum.kelime.kelimekutum.Model.SettingsPost;
import kutum.kelime.kelimekutum.RoomDatabase.SettingsApp;

public class WelcomeActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ViewPager viewPager;
    private int [] layouts={R.layout.first_slide,R.layout.second_slide,R.layout.third_slide,R.layout.forth_slide};
    private MyViewPagerAdapter myViewPagerAdapter;

    private LinearLayout dotsLinear;
    private ImageView []dots;
    private Button btnBack,btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_welcome);
        init();

        createDots(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if(position==layouts.length-1){
                    btnNext.setText("BAŞLA");
                }else{
                    btnNext.setText("İLERİ");
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int next_slide=viewPager.getCurrentItem()+1;
                if(next_slide<layouts.length){
                    viewPager.setCurrentItem(next_slide);
                }else{
                    progressDialog.setMessage("Başlatılıyor...");
                    progressDialog.show();
                    saveInitialTestNumber();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int back_slide=viewPager.getCurrentItem()-1;
                if(back_slide>-1){
                    viewPager.setCurrentItem(back_slide);
                }
            }
        });
    }

    private void init() {
        progressDialog=new ProgressDialog(this);
        viewPager=findViewById(R.id.viewPager);
        myViewPagerAdapter=new MyViewPagerAdapter(layouts,WelcomeActivity.this);
        viewPager.setAdapter(myViewPagerAdapter);
        dotsLinear=findViewById(R.id.dotsLayout);
        btnBack=findViewById(R.id.btnBack);
        btnNext=findViewById(R.id.btnNext);
    }

    private void createDots(int current_position){
        if(dotsLinear!=null){
            dotsLinear.removeAllViews();
        }

        dots=new ImageView[layouts.length];

        for(int i=0 ; i<layouts.length ; i++){
            dots[i]=new ImageView(this);
            if(i==current_position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inactive_dots));
            }
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            dotsLinear.addView(dots[i],params);
        }
    }

    private void saveInitialTestNumber(){
        SettingsApp settingsApp= Room.databaseBuilder(WelcomeActivity.this,SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        SettingsPost settingsPost=new SettingsPost("20","20","20","20","20");
        settingsApp.settingsDao().insertSettings(settingsPost);
        new PreferenceManager(this).writePreference();
        progressDialog.dismiss();
        startActivity(new Intent(WelcomeActivity.this,MainScreen.class));
        finish();
    }
}
