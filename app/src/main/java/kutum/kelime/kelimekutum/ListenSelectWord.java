package kutum.kelime.kelimekutum;

import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Room;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import kutum.kelime.kelimekutum.Model.SettingsPost;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.RoomDatabase.SettingsApp;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;

public class ListenSelectWord extends AppCompatActivity {
    private AdView adView;
    private CardView cardWord;
    private ProgressBar progressBar;
    private TextView tvStep,tvIcon,tvWord,tvAnswer;
    private Button btnOne,btnTwo,btnThree,btnFour,btnSkip;
    private Animation animLeft;
    private TextToSpeech t1;
    private List<Word> wordList;
    private static List<Integer> listOptionWord;
    private static List<Integer> listOptions;

    private static int indexWord;
    private static int correctOption;
    private static String word;
    private static String realAnswer;
    private static String wrongMeaning;
    private static int counterTrue=0;
    private static int counterFalse=0;
    private List<SettingsPost> settingsPostList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
   //     startActivity(new Intent(ListenSelectWord.this,MainActivity.class));
        startActivity(new Intent(ListenSelectWord.this,MainScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_select_word);
        getTestQuantity();
        init();
        checkWord();
        specifyWord();
        showChoice();

        MobileAds.initialize(this, String.valueOf(R.string.appID));
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        tvWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(tvAnswer.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOneMethod();
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTwoMethod();
            }
        });

        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnThreeMethod();
            }
        });

        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFourMethod();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTestQuantity(); skip();
            }
        });
    }

    private void init() {
        adView=(AdView) findViewById(R.id.adView);
        cardWord=findViewById(R.id.cardWord);
        progressBar=findViewById(R.id.progressBar);
        tvAnswer=findViewById(R.id.tvAnswer);
        tvStep=findViewById(R.id.tvStep);
        tvStep.setText("01/"+settingsPostList.get(0).getListenSelectWord());
        int lastStep=Integer.parseInt(tvStep.getText().toString().substring(3));
        progressBar.setProgress(100/lastStep);
        tvIcon=findViewById(R.id.tvIcon);
        tvWord=findViewById(R.id.tvWord);
        btnOne=findViewById(R.id.btnOne);
        btnTwo=findViewById(R.id.btnTwo);
        btnThree=findViewById(R.id.btnThree);
        btnFour=findViewById(R.id.btnFour);
        btnSkip=findViewById(R.id.btnSkip);
        animLeft= AnimationUtils.loadAnimation(this,R.anim.left_to_right);
        listOptionWord =new ArrayList<>();
        listOptions=new ArrayList<>();
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    private void showChoice(){
        cardWord.startAnimation(animLeft);
        btnOne.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnOne.setVisibility(View.VISIBLE);
                btnOne.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnTwo.setVisibility(View.VISIBLE);
                        btnOne.setEnabled(false);
                        btnTwo.setEnabled(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btnThree.setVisibility(View.VISIBLE);
                                btnOne.setEnabled(false);
                                btnTwo.setEnabled(false);
                                btnThree.setEnabled(false);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnFour.setVisibility(View.VISIBLE);
                                        btnOne.setEnabled(true);
                                        btnTwo.setEnabled(true);
                                        btnThree.setEnabled(true);
                                    }
                                },550);
                            }
                        },500);
                    }
                },450);
            }
        },400);

    }

    private void btnOneMethod() {
        setEnabledFalseButton();
        if(tvAnswer.getText().toString().equalsIgnoreCase(btnOne.getText().toString())){
            counterTrue++;
            btnOne.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            btnOne.setBackgroundResource(R.color.wrong_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_false,0);
            if(correctOption==1){
                btnTwo.setBackgroundResource(R.color.correct_answer);
            }else if(correctOption==2){
                btnThree.setBackgroundResource(R.color.correct_answer);
            }else if(correctOption==3){
                btnFour.setBackgroundResource(R.color.correct_answer);
            }
            checkWord();
        }
    }

    private void btnTwoMethod() {
        setEnabledFalseButton();
        if(tvAnswer.getText().toString().equalsIgnoreCase(btnTwo.getText().toString())){
            counterTrue++;
            btnTwo.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            btnTwo.setBackgroundResource(R.color.wrong_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_false,0);
            if(correctOption==0){
                btnOne.setBackgroundResource(R.color.correct_answer);
            }else if(correctOption==2){
                btnThree.setBackgroundResource(R.color.correct_answer);
            }else if(correctOption==3){
                btnFour.setBackgroundResource(R.color.correct_answer);
            }
            checkWord();
        }
    }

    private void btnThreeMethod() {
        setEnabledFalseButton();
        if(tvAnswer.getText().toString().equalsIgnoreCase(btnThree.getText().toString())){
            counterTrue++;
            btnThree.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            btnThree.setBackgroundResource(R.color.wrong_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_false,0);
            if(correctOption==0){
                btnOne.setBackgroundResource(R.color.correct_answer);
            }else if(correctOption==1){
                btnTwo.setBackgroundResource(R.color.correct_answer);
            }else if(correctOption==3){
                btnFour.setBackgroundResource(R.color.correct_answer);
            }
            checkWord();
        }
    }

    private void btnFourMethod() {
        setEnabledFalseButton();
        if(tvAnswer.getText().toString().equalsIgnoreCase(btnFour.getText().toString())){
            counterTrue++;
            btnFour.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            btnFour.setBackgroundResource(R.color.wrong_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_false,0);
            if(correctOption==0){
                btnOne.setBackgroundResource(R.color.correct_answer);
            }else if(correctOption==1){
                btnTwo.setBackgroundResource(R.color.correct_answer);
            }else if(correctOption==2){
                btnThree.setBackgroundResource(R.color.correct_answer);
            }
            checkWord();
        }
    }

    private void checkWord() {
        WordApp wordApp= Room.databaseBuilder(ListenSelectWord.this,WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
    }

    private void specifyWord(){
        Random random=new Random();
        indexWord=random.nextInt(wordList.size());
        word=wordList.get(indexWord).getWord();
        tvAnswer.setText(word);

        listOptionWord.add(indexWord);
        specifyOptionWord(indexWord);
        specifyOptions();
        for(int i=0 ; i<4 ; i++){
            Log.e("zzz", String.valueOf(listOptions.get(i)));
        }
        btnOne.setText(wordList.get(listOptionWord.get(listOptions.get(0))).getWord());
        btnTwo.setText(wordList.get(listOptionWord.get(listOptions.get(1))).getWord());
        btnThree.setText(wordList.get(listOptionWord.get(listOptions.get(2))).getWord());
        btnFour.setText(wordList.get(listOptionWord.get(listOptions.get(3))).getWord());
    }

    private void specifyOptions() {
        Random random=new Random();
        int option=random.nextInt(4);
        Log.e("listOptions", String.valueOf(option));
        if(option==0){
            // c şıkkı
            listOptions.add(2);
            listOptions.add(1);
            listOptions.add(0);
            listOptions.add(3);
            correctOption=2;
        }else if(option==1){
            // d şıkkı
            listOptions.add(1);
            listOptions.add(3);
            listOptions.add(2);
            listOptions.add(0);
            correctOption=3;
        }else if(option==2){
            // a şıkkı
            listOptions.add(0);
            listOptions.add(3);
            listOptions.add(2);
            listOptions.add(1);
            correctOption=0;
        }else if(option==3){
            // b şıkkı
            listOptions.add(3);
            listOptions.add(0);
            listOptions.add(1);
            listOptions.add(2);
            correctOption=1;
        }
    }

    private void specifyOptionWord(int indexWord) {
        Random random=new Random();
        int indexOption=random.nextInt(wordList.size());
        if(indexOption==indexWord){
            specifyOptionWord(indexWord);
        }else{
            if(listOptionWord.size()==0){
                listOptionWord.add(indexOption);
                specifyOptionWord(indexWord);
            }else{
                if(listOptionWord.contains(indexOption)){
                    specifyOptionWord(indexWord);
                }else{
                    listOptionWord.add(indexOption);
                    if(listOptionWord.size()!=4){
                        specifyOptionWord(indexWord);
                    }
                }
            }
        }
    }

    private void skip() {
        setEnabledTrueButton();
        listOptions.clear();
        listOptionWord.clear();
        int step=Integer.parseInt(tvStep.getText().toString().substring(0,2));
        int lastStep=Integer.parseInt(tvStep.getText().toString().substring(3));
        step=step+1;

        int rate=100/lastStep;
        int pStatus=step*rate;
        progressBar.setProgress(pStatus);

        if(step>lastStep){
            showScore();
        }else{
            if(step<10){
                tvStep.setText("0"+Integer.toString(step)+"/"+settingsPostList.get(0).getListenSelectWord());
            }else{
                tvStep.setText(Integer.toString(step)+"/"+settingsPostList.get(0).getListenSelectWord());
            }
            setSomeFeatures();
            checkWord();
            specifyWord();
            showChoice();
        }
    }

    private void setSomeFeatures() {
        btnOne.setVisibility(View.INVISIBLE);
        btnTwo.setVisibility(View.INVISIBLE);
        btnThree.setVisibility(View.INVISIBLE);
        btnFour.setVisibility(View.INVISIBLE);
        btnOne.setBackgroundResource(R.color.default_option);
        btnTwo.setBackgroundResource(R.color.default_option);
        btnThree.setBackgroundResource(R.color.default_option);
        btnFour.setBackgroundResource(R.color.default_option);
        tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    private void setEnabledFalseButton() {
        btnSkip.setVisibility(View.VISIBLE);
        btnOne.setEnabled(false);
        btnTwo.setEnabled(false);
        btnThree.setEnabled(false);
        btnFour.setEnabled(false);
    }

    private void setEnabledTrueButton() {
        btnSkip.setVisibility(View.INVISIBLE);
        btnOne.setEnabled(true);
        btnTwo.setEnabled(true);
        btnThree.setEnabled(true);
        btnFour.setEnabled(true);
    }

    private void showScore() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ListenSelectWord.this);
        View view= LayoutInflater.from(ListenSelectWord.this).inflate(R.layout.layout_score,null);
        Button btnExit=view.findViewById(R.id.btnExit);
        Button btnAgain=view.findViewById(R.id.btnAgain);
        TextView tvWrongNumber=view.findViewById(R.id.tvWrongNumber);
        TextView tvCorrectNumber=view.findViewById(R.id.tvCorrectNumber);
        tvCorrectNumber.setText("Doğru Sayısı " + Integer.toString(counterTrue));
        tvWrongNumber.setText("Yanlış Sayısı : " + Integer.toString(counterFalse));

        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.show();

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                counterFalse=0;
                counterTrue=0;
                startActivity(new Intent(ListenSelectWord.this,MainScreen.class));
                finish();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTestQuantity();
                tvStep.setText("01/"+settingsPostList.get(0).getListenSelectWord());
                progressBar.setProgress(0);
                tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                btnOne.setBackgroundResource(R.color.default_option);
                btnTwo.setBackgroundResource(R.color.default_option);
                btnThree.setBackgroundResource(R.color.default_option);
                btnFour.setBackgroundResource(R.color.default_option);
                counterFalse=0;
                counterTrue=0;
                listOptions.clear();
                listOptionWord.clear();
                checkWord();
                specifyWord();
                showChoice();
                dialog.dismiss();
            }
        });
    }
    private void getTestQuantity(){
        SettingsApp settingsApp= Room.databaseBuilder(ListenSelectWord.this,SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        settingsPostList=settingsApp.settingsDao().getSettings();
    }
}
