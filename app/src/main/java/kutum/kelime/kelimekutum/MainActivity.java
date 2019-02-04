package kutum.kelime.kelimekutum;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import kutum.kelime.kelimekutum.Adapter.WordAdapter;
import kutum.kelime.kelimekutum.Model.SeeMeaningPost;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.RemoteConfigPackage.UpdateHelper;
import kutum.kelime.kelimekutum.RoomDatabase.SettingSeeMeaningApp;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;

public class MainActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener {
    private InterstitialAd interStitialAd;
    private AdView adView;
    private TextToSpeech t1;
    private FloatingActionButton fabMenu;
    private TextView tvTotalWord;
    private RecyclerView recWords;
    private GridLayoutManager gridLayoutManager;
    private WordAdapter wordAdapter;

    private static List<Word> wordList;
    private List<SeeMeaningPost> seeMeaningPostList;

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isNewSettings();
        init();
        checkFirstTime();
        getWords();
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions();
            }
        });

        UpdateHelper.with(this).onUpdateCheck(this).check();

        MobileAds.initialize(this, String.valueOf(R.string.appID));
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

       /* interStitialAd.setAdUnitId(String.valueOf(R.string.testInterstitialID));
        interStitialAd.loadAd(new AdRequest.Builder().build());
        interStitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
               *//* new StartPractice(1);
                startActivity(new Intent(MainActivity.this, StartPractice.class));
                finish();*//*
                interStitialAd.loadAd(new AdRequest.Builder().build());
            }
        });*/
    }

    private void checkFirstTime() {
        checkWord();
        if(wordList.size()==0){
            Resources resources=getResources();
            InputStream is= resources.openRawResource(R.raw.initial_words);
            Scanner scanner=new Scanner(is);
            StringBuilder builder=new StringBuilder();
            while (scanner.hasNextLine()){
                builder.append(scanner.nextLine());
            }
            parseJson(builder.toString());
        }
    }

    private void parseJson(String jsonData) {
        WordApp wordApp= Room.databaseBuilder(MainActivity.this,WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        StringBuilder builder=new StringBuilder();
        try {
            JSONObject root=new JSONObject(jsonData);
            JSONArray words=root.getJSONArray("words");
            for(int i=0 ; i<words.length() ; i++){
                JSONObject obj=words.getJSONObject(i);
                /*Log.e("jsonData",obj.getString("word") + " : " +
                            obj.getString("meaning1")+" , " +
                            obj.getString("meaning2")+" , " +
                            obj.getString("meaning3")+" , " +
                            obj.getString("meaning4")+" , " +
                            obj.getString("meaning5")+"\n");*/
                Word newWord= new Word(obj.getString("word").toLowerCase().trim()
                        ,obj.getString("meaning1").toLowerCase().trim(),
                        obj.getString("meaning2").toLowerCase().trim(),
                        obj.getString("meaning3").toLowerCase().trim(),
                        obj.getString("meaning4").toLowerCase().trim(),
                        obj.getString("meaning5").toLowerCase().trim());
                wordApp.wordDao().insertWord(newWord);
            }
            setPositiveToastMessage("Yeni Kelimeler Eklendi");
            startActivity(getIntent());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void init() {
        adView=(AdView) findViewById(R.id.adView);
        interStitialAd=new InterstitialAd(this);
        recWords=findViewById(R.id.recWords);
        tvTotalWord=findViewById(R.id.tvTotalWord);
        fabMenu=findViewById(R.id.fabMenu);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    private void getWords() {
        final DisplayMetrics metrics= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth=metrics.widthPixels;
        int screenHeight=metrics.heightPixels;
        double wi=(double)screenWidth/(double)metrics.xdpi;
        double hi=(double)screenHeight/(double)metrics.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        final double screenInches = Math.sqrt(x+y);

        if (screenInches<5){
            gridLayoutManager=new GridLayoutManager(this,1);
        }else if(screenInches>5 && screenInches<7){
            gridLayoutManager=new GridLayoutManager(this,2);
        }else{
            gridLayoutManager=new GridLayoutManager(this,3);
        }
        recWords.setLayoutManager(gridLayoutManager);
        checkWord();
        if(wordList.size()>0){
                wordAdapter=new WordAdapter(this,wordList,t1);
                recWords.setAdapter(wordAdapter);
        }

    }

    private void setNegativeToastMessage(String message) {
        StyleableToast st=new StyleableToast(MainActivity.this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#ff0000"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void setPositiveToastMessage(String message) {
        StyleableToast st=new StyleableToast(MainActivity.this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#0000ff"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void showOptions() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_options,null);
        Button btnAdd=view.findViewById(R.id.btnAdd);
        Button btnShowWord=view.findViewById(R.id.btnShowWord);
        Button btnPractice=view.findViewById(R.id.btnPractice);
        Button btnSettings=view.findViewById(R.id.btnSettings);

        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWord();
            }
        });

        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wordList.size()>19){
                    selectOption();
                }else{
                    setNegativeToastMessage("En az 20 kelime eklemeniz gerekmektedir.");
                }
            }
        });

        btnShowWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,WordList.class));
                finish();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Settings.class));
                finish();
            }
        });
    }

    private void selectOption() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_practice_option,null);
        Button btnTrueFalse=view.findViewById(R.id.btnTrueFalse);
        Button btnMultipleChoice=view.findViewById(R.id.btnMultipleChoice);
        Button btnListenMeaning=view.findViewById(R.id.btnListenMeaning);
        Button btnListenWord=view.findViewById(R.id.btnListenWord);
        Button btnListenWrite=view.findViewById(R.id.btnListenWrite);
        Button btnSeeMeaningWriteWord=view.findViewById(R.id.btnSeeMeaningWriteWord);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();

        btnTrueFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(interStitialAd.isLoaded()){
                    interStitialAd.show();
                }else{
                    new StartPractice(1);
                    startActivity(new Intent(MainActivity.this, StartPractice.class));
                    finish();
                }*/
                new StartPractice(1);
                startActivity(new Intent(MainActivity.this, StartPractice.class));
                finish();
            }
        });

        btnMultipleChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(2);
                startActivity(new Intent(MainActivity.this, StartPractice.class));
                finish();
            }
        });

        btnListenMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(3);
                startActivity(new Intent(MainActivity.this, StartPractice.class));
                finish();
            }
        });

        btnListenWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(4);
                startActivity(new Intent(MainActivity.this, StartPractice.class));
                finish();
            }
        });

        btnListenWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(5);
                startActivity(new Intent(MainActivity.this, StartPractice.class));
                finish();
            }
        });

        btnSeeMeaningWriteWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(6);
                startActivity(new Intent(MainActivity.this, StartPractice.class));
                finish();
            }
        });
    }

    private void addWord() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_add_word,null);
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

                WordApp wordApp= Room.databaseBuilder(MainActivity.this,WordApp.class,"Words")
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
                    startActivity(getIntent());
                }else{
                    for(int i=0 ; i<wordList.size() ; i++){
                        if(wordList.get(i).getWord().equalsIgnoreCase(word)){
                            Toast.makeText(MainActivity.this,"Bu kelime zaten eklenmiş",Toast.LENGTH_SHORT).show();
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
                                /*if(wordList.size()==21){
                                    if(interStitialAd.isLoaded()){
                                        interStitialAd.show();
                                    }else{
                                        startActivity(getIntent());
                                    }
                                }else{
                                    startActivity(getIntent());
                                }*/
                                startActivity(getIntent());
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
        WordApp wordApp= Room.databaseBuilder(MainActivity.this,WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
        tvTotalWord.setText(Integer.toString(wordList.size()));
        if(wordList.size()>0){
            try{
                Comparator<Word> com=new ComImp();
                Collections.sort(wordList,com);
            }catch (Exception e){

            }
        }
    }

    private void isNewSettings() {
        SettingSeeMeaningApp settingSeeMeaning= Room.databaseBuilder(MainActivity.this,SettingSeeMeaningApp.class,"SeeMeaning")
                .allowMainThreadQueries()
                .build();
        seeMeaningPostList=settingSeeMeaning.settingSeeMeaningDao().getSeeMeaningSetting();
        if(seeMeaningPostList.size()==0){
            SeeMeaningPost seeMeaningPost=new SeeMeaningPost("20");
            settingSeeMeaning.settingSeeMeaningDao().insertSettings(seeMeaningPost);
        }
    }

    @Override
    public void onUpdateCheckListener(final String urlApp,String message) {
        AlertDialog alertDialog=new AlertDialog.Builder(this)
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

    public class ComImp implements Comparator<Word>{

        @Override
        public int compare(Word word, Word t1) {
            if(word.getWord().charAt(0)>t1.getWord().charAt(0)){
                return  1;
            }else if(word.getWord().charAt(0)==t1.getWord().charAt(0)){
                if(word.getWord().length()==1 || t1.getWord().length()==1){
                    return -1;
                }else{
                    if(word.getWord().charAt(1)>t1.getWord().charAt(1)){
                        return  1;
                    }else if(word.getWord().charAt(1)==t1.getWord().charAt(1)){
                        if(word.getWord().length()==2 || t1.getWord().length()==2){
                            return -1;
                        }else{
                            if(word.getWord().charAt(2)>t1.getWord().charAt(2)){
                                return  1;
                            }else {
                                return -1;
                            }
                        }
                    }else{
                        return -1;
                    }
                }
            }else{
                return -1;
            }
        }
    }
}
