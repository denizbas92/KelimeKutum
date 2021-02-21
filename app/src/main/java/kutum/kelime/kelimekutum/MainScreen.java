package kutum.kelime.kelimekutum;

import android.animation.Animator;

import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.RemoteConfigPackage.UpdateHelper;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;
import kutum.kelime.kelimekutum.ViewPagerPackage.MainScreenViewPager;

public class MainScreen extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener  {
    CoordinatorLayout relativeLayout;
    private AdView adView;
    private ViewPager mainViewPager;
    private HorizontalScrollView hScroll;
    private TextView tvMain,tvWords,tvPractice,tvSettings;
    private MainScreenViewPager mainScreenViewPager;
    private List<Word> wordList;


    public MainScreen() {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    public void onBackPressed() {
        closeApp();
    }
    private void closeApp() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainScreen.this);
        View view= LayoutInflater.from(MainScreen.this).inflate(R.layout.layout_confirm_dialog,null);
        TextView tvTitle=view.findViewById(R.id.tvTitle);
        Button btnCancel=view.findViewById(R.id.btnCancel);
        Button btnConfirm=view.findViewById(R.id.btnConfirm);
        tvTitle.setText("Uygulamadan çıkılsın mı ?");
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
    }

    public void refresh(){
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        UpdateHelper.with(this).onUpdateCheck(this).check();
        MobileAds.initialize(this, String.valueOf(R.string.appID));
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                addWord();

            }
        });

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

        tvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewPager.setCurrentItem(0);
            }
        });
        tvWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewPager.setCurrentItem(1);
            }
        });
        tvPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewPager.setCurrentItem(2);
            }
        });
        tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewPager.setCurrentItem(3);
            }
        });

    }

    private void init() {
        relativeLayout=findViewById(R.id.deneme);
        adView=findViewById(R.id.adView);
        mainViewPager=findViewById(R.id.mainViewPager);
        hScroll=findViewById(R.id.hScroll);
        tvMain=findViewById(R.id.tvMain);
        tvWords=findViewById(R.id.tvWords);
        tvPractice=findViewById(R.id.tvPractice);
        tvSettings=findViewById(R.id.tvSettings);

        mainViewPager.setOffscreenPageLimit(4);
        mainScreenViewPager=new MainScreenViewPager(getSupportFragmentManager());
        mainViewPager.setAdapter(mainScreenViewPager);
        mainViewPager.setCurrentItem(0);
        tvMain.setBackgroundResource(R.color.unselected_tab);

        boolean isDownloadFromPlayStore = verifyInstallerId(this);
        if (isDownloadFromPlayStore){
            String message = "Uygulama google play üzerinden indirildi";
            AlertDialog alertDialog=new AlertDialog.Builder(MainScreen.this)
                    .setTitle("İndirme Durumu")
                    .setMessage(message).create();
            alertDialog.show();
        } else {
            String message = "Uygulama google play üzerinden indirilmedi";
            AlertDialog alertDialog=new AlertDialog.Builder(MainScreen.this)
                    .setTitle("İndirme Durumu")
                    .setMessage(message).create();
            alertDialog.show();
        }
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
            setPageTitle(position);
        }else if(screenInches>5 && screenInches<7){
            setPageTitle(position);
        }else{
            setPageTitle(position);
        }

    }

    private void setPageTitle(int tempPosition) {
        if(tempPosition==0){
            tvMain.setBackgroundResource(R.color.unselected_tab);
            tvWords.setBackground(null);
            tvPractice.setBackground(null);
            tvSettings.setBackground(null);
            hScroll.post(new Runnable() {
                @Override
                public void run() {
                    hScroll.smoothScrollTo(0,0);
                }
            });
            startActivity(getIntent());
    //        onAnimationStart();
        }
        if(tempPosition==1){
            tvWords.setBackgroundResource(R.color.unselected_tab);
            tvMain.setBackground(null);
            tvPractice.setBackground(null);
            tvSettings.setBackground(null);
            hScroll.post(new Runnable() {
                @Override
                public void run() {
                    hScroll.smoothScrollTo(tvWords.getLeft(),0);
                }
            });
        }
        if(tempPosition==2){
            tvPractice.setBackgroundResource(R.color.unselected_tab);
            tvWords.setBackground(null);
            tvMain.setBackground(null);
            tvSettings.setBackground(null);
        }
        if(tempPosition==3){
            tvSettings.setBackgroundResource(R.color.unselected_tab);
            tvWords.setBackground(null);
            tvPractice.setBackground(null);
            tvMain.setBackground(null);
            hScroll.post(new Runnable() {
                @Override
                public void run() {
                    hScroll.smoothScrollTo(tvSettings.getLeft(),0);
                }
            });
        }
    }

    private void addWord() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainScreen.this);
        View view= LayoutInflater.from(MainScreen.this).inflate(R.layout.layout_add_word,null);
        Button btnSave=view.findViewById(R.id.btnSave);
        Button btnCancel=view.findViewById(R.id.btnCancel);
        final EditText etWord=view.findViewById(R.id.etWord);
        final EditText etMeaning1=view.findViewById(R.id.etMeaning1);
        final EditText etMeaning2=view.findViewById(R.id.etMeaning2);
        final EditText etMeaning3=view.findViewById(R.id.etMeaning3);
        final EditText etMeaning4=view.findViewById(R.id.etMeaning4);
        final EditText etMeaning5=view.findViewById(R.id.etMeaning5);

        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etWord.getText().toString())){
                    setNegativeToastMessage("Kelimeyi giriniz...");
                    return;
                }
                if(TextUtils.isEmpty(etMeaning1.getText().toString())){
                    setNegativeToastMessage("1.Anlamı giriniz...");
                    return;
                }

                WordApp wordApp= Room.databaseBuilder(MainScreen.this,WordApp.class,"Words")
                        .allowMainThreadQueries()
                        .build();
                checkWord();
                final String word=etWord.getText().toString();
                final String meaning1=etMeaning1.getText().toString();
                final String meaning2=etMeaning2.getText().toString();
                final String meaning3=etMeaning3.getText().toString();
                final String meaning4=etMeaning4.getText().toString();
                final String meaning5=etMeaning5.getText().toString();
                Word objWord=new Word();
                objWord.setWord(word);

                if(wordList.size()==0){
                    Word newWord= new Word(word.toLowerCase().trim()
                            ,meaning1.toLowerCase().trim(),
                            meaning2.toLowerCase().trim(),
                            meaning3.toLowerCase().trim(),
                            meaning4.toLowerCase().trim(),
                            meaning5.toLowerCase().trim());
                    wordApp.wordDao().insertWord(newWord);
                    setPositiveToastMessage("Kelime Eklendi");
                    dialog.dismiss();
       //             startActivity(getIntent());
                }else{
                    for(int i=0 ; i<wordList.size() ; i++){
                        if(wordList.get(i).getWord().equalsIgnoreCase(word)){
                            Toast.makeText(MainScreen.this,"Bu kelime zaten eklenmiş",Toast.LENGTH_SHORT).show();
                            break;
                        }else{
                            if(i==wordList.size()-1){
                                Word newWord= new Word(word.toLowerCase().trim()
                                        ,meaning1.toLowerCase().trim(),
                                        meaning2.toLowerCase().trim(),
                                        meaning3.toLowerCase().trim(),
                                        meaning4.toLowerCase().trim(),
                                        meaning5.toLowerCase().trim());
                                wordApp.wordDao().insertWord(newWord);
                                setPositiveToastMessage("Kelime Eklendi");
                                dialog.dismiss();
                                startActivity(getIntent());
                                /*if(wordList.size()==21){
                                    if(interStitialAd.isLoaded()){
                                        interStitialAd.show();
                                    }else{
                                        startActivity(getIntent());
                                    }
                                }else{
                                    startActivity(getIntent());
                                }*/
                     //           startActivity(getIntent());
                            }
                        }
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void checkWord() {
        WordApp wordApp= Room.databaseBuilder(this,WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
    }

    private void setNegativeToastMessage(String message) {
        StyleableToast st=new StyleableToast(this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#ff0000"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void setPositiveToastMessage(String message) {
        StyleableToast st=new StyleableToast(this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#0000ff"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    @Override
    public void onUpdateCheckListener(final String urlApp,String message) {
        AlertDialog alertDialog=new AlertDialog.Builder(MainScreen.this)
                .setTitle("Yeni Versiyon !")
                .setMessage(message)
                .setPositiveButton("GÜNCELLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlApp)));
                    }
                }).setNegativeButton("DAHA SONRA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onAnimationStart() {
        Log.d("FirstFragment","onAnimationStart()");

        // get the center for the clipping circle
        int cx = (relativeLayout.getRight());
        int cy = (relativeLayout.getBottom());

        // get the final radius for the clipping circle
        int finalRadius = Math.max(relativeLayout.getWidth(), relativeLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(relativeLayout, cx, cy, 0, finalRadius);
        anim.setDuration(1000);

        // make the view visible and start the animation
        relativeLayout.setVisibility(View.VISIBLE);
        anim.start();
    }

    boolean verifyInstallerId(Context context) {
        // A list with valid installers package name
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

        // The package name of the app that has installed your app
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer);
    }
}
