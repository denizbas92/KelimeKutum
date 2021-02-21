package kutum.kelime.kelimekutum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import kutum.kelime.kelimekutum.ViewPagerPackage.PagerViewAdapter;

public class ScoreActivity extends AppCompatActivity {
    private static Context context;
    private TextView scoreBoard;
    private TextView tvCorrectNumber;
    private TextView tvWrongNumber;
    private TextView tvTrue;
    private TextView tvFalse;

    private ViewPager mainViewPager;

    private Button btnExit;
    private Button btnAgain;

    private AdView adView;

    private PagerViewAdapter pagerViewAdapter;

    private static List<String> listTrueAnswer;
    private static List<String> listFalseAnswer;

    private static int counterTrue;
    private static int counterFalse;

    public ScoreActivity(){

    }

    public ScoreActivity(TrueFalse trueFalse,List<String> listTrueAnswer, List<String> listFalseAnswer, int counterTrue, int counterFalse) {
        this.context=trueFalse;
        this.listTrueAnswer=listTrueAnswer;
        this.listFalseAnswer=listFalseAnswer;
        this.counterTrue=counterTrue;
        this.counterFalse=counterFalse;
    }

    public ScoreActivity(MultipleChoice multipleChoice, List<String> listTrueAnswer, List<String> listFalseAnswer, int counterTrue, int counterFalse) {
        this.context=multipleChoice;
        this.listTrueAnswer=listTrueAnswer;
        this.listFalseAnswer=listFalseAnswer;
        this.counterTrue=counterTrue;
        this.counterFalse=counterFalse;
    }

    public ScoreActivity(ListenSelectMeaning listenSelectMeaning, List<String> listTrueAnswer, List<String> listFalseAnswer, int counterTrue, int counterFalse) {
        this.context=listenSelectMeaning;
        this.listTrueAnswer=listTrueAnswer;
        this.listFalseAnswer=listFalseAnswer;
        this.counterTrue=counterTrue;
        this.counterFalse=counterFalse;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        counterFalse=0;
        counterTrue=0;
        listFalseAnswer.clear();
        listTrueAnswer.clear();
        startActivity(new Intent(ScoreActivity.this,MainScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        init();

        MobileAds.initialize(this, String.valueOf(R.string.appID));
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewPager.setCurrentItem(0);
            }
        });
        tvFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewPager.setCurrentItem(1);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterFalse=0;
                counterTrue=0;
                listFalseAnswer.clear();
                listTrueAnswer.clear();
                startActivity(new Intent(ScoreActivity.this,MainScreen.class));
                finish();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterFalse=0;
                counterTrue=0;
                listFalseAnswer.clear();
                listTrueAnswer.clear();
                startActivity(new Intent(ScoreActivity.this,context.getClass()));
                finish();
            }
        });
    }

    private void init() {
        scoreBoard=findViewById(R.id.scoreBoard);
        tvCorrectNumber=findViewById(R.id.tvCorrectNumber);
        tvWrongNumber=findViewById(R.id.tvWrongNumber);
        tvTrue=findViewById(R.id.tvTrue);
        tvFalse=findViewById(R.id.tvFalse);
        mainViewPager=findViewById(R.id.mainViewPager);
        btnExit=findViewById(R.id.btnExit);
        btnAgain=findViewById(R.id.btnAgain);
        adView=findViewById(R.id.adView);
        Log.e("trueSize1","1. "+Integer.toString(listTrueAnswer.size()));
        tvCorrectNumber.setText("Doğru Sayısı : "+Integer.toString(counterTrue));
        tvWrongNumber.setText("Yanlış Sayısı : "+Integer.toString(counterFalse));
        mainViewPager.setOffscreenPageLimit(2);
        pagerViewAdapter=new PagerViewAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(pagerViewAdapter);
        mainViewPager.setCurrentItem(0);
    }

    private void changeTabs(int position) {

        DisplayMetrics metrics= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth=metrics.widthPixels;
        int screenHeight=metrics.heightPixels;
        double wi=(double)screenWidth/(double)metrics.xdpi;
        double hi=(double)screenHeight/(double)metrics.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        final double screenInches = Math.sqrt(x+y);

        if (screenInches<5){
            setPageTitle(position,22,15);
        }else if(screenInches>5 && screenInches<7){
            setPageTitle(position,32,25);
        }else{
            setPageTitle(position,42,35);
        }

    }

    private void setPageTitle(int tempPosition, int first, int second) {
        if(tempPosition==0){
            tvTrue.setTextSize(first);
            tvTrue.setTextColor(Color.parseColor("#ffffff"));
            tvFalse.setTextColor(Color.parseColor("#e9d2d2"));
            tvFalse.setTextSize(second);
        }
        if(tempPosition==1){
            tvTrue.setTextSize(second);
            tvTrue.setTextColor(Color.parseColor("#e9d2d2"));
            tvFalse.setTextSize(first);
            tvFalse.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public static List<String> getListTrueAnswer() {
        return listTrueAnswer;
    }

    public static void setListTrueAnswer(List<String> listTrueAnswer) {
        ScoreActivity.listTrueAnswer = listTrueAnswer;
    }

    public static List<String> getListFalseAnswer() {
        return listFalseAnswer;
    }

    public static void setListFalseAnswer(List<String> listFalseAnswer) {
        ScoreActivity.listFalseAnswer = listFalseAnswer;
    }
}
