package kutum.kelime.kelimekutum;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Room;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kutum.kelime.kelimekutum.Model.SettingsPost;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.RoomDatabase.SettingsApp;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;

import static kutum.kelime.kelimekutum.R.color.correct_answer;
import static kutum.kelime.kelimekutum.R.color.wrong_answer;

public class TrueFalse extends AppCompatActivity {
    private AdView adView;
    private ProgressBar progressBar;
    private TextView tvStep,tvIcon,tvWord,tvMeaning,tvRealAnswer;
    private Button btnFalse,btnTrue,btnSkip;
    private CardView card;
    private Animation animLeft;
    private List<Word> wordList;
    private static List<String> listIndexMeaning;

    private static int counterMeaningNumber=0;
    private static int indexWord;
    private static String word;
    private static String realAnswer;
    private static String trueMeaning;
    private static String wrongMeaning;
    private static int isAnswer;
    private static int counterTrue=0;
    private static int counterFalse=0;
    private List<SettingsPost> settingsPostList;
    private static List<String> listTrueAnswer;
    private static List<String> listFalseAnswer;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
   //     startActivity(new Intent(TrueFalse.this,MainActivity.class));
        startActivity(new Intent(TrueFalse.this,MainScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false);
        counterTrue=0;
        counterFalse=0;
        getTestQuantity();
        init();

        btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                falsePart();
            }
        });

        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                truePart();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skip();
            }
        });

        MobileAds.initialize(this, String.valueOf(R.string.appID));
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void init() {
        adView=(AdView) findViewById(R.id.adView);
        card=findViewById(R.id.card);
        progressBar=findViewById(R.id.progressBar);
        tvStep=findViewById(R.id.tvStep);
        tvStep.setText("01/"+settingsPostList.get(0).getTrueFalse());
        int lastStep=Integer.parseInt(tvStep.getText().toString().substring(3));
        progressBar.setProgress(100/lastStep);
        tvIcon=findViewById(R.id.tvIcon);
        tvWord=findViewById(R.id.tvWord);
        tvMeaning=findViewById(R.id.tvMeaning);
        tvRealAnswer=findViewById(R.id.tvRealAnswer);
        btnFalse=findViewById(R.id.btnFalse);
        btnTrue=findViewById(R.id.btnTrue);
        btnSkip=findViewById(R.id.btnSkip);
        animLeft= AnimationUtils.loadAnimation(this,R.anim.left_to_right);
        card.startAnimation(animLeft);
        listIndexMeaning=new ArrayList<>();
        listFalseAnswer=new ArrayList<>();
        listTrueAnswer=new ArrayList<>();
        checkWord();
        specifyWord();
    }

    private void truePart() {
        btnSkip.setVisibility(View.VISIBLE);
        btnFalse.setEnabled(false);
        btnFalse.setEnabled(false);
        if(isAnswer==1){
            counterTrue++;
            listTrueAnswer.add(tvWord.getText().toString());
            btnTrue.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            listFalseAnswer.add(tvWord.getText().toString());
            btnTrue.setBackgroundResource(R.color.wrong_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_false,0);
            checkWord();
            setRealAnswer();
        }
        Log.e("isAnswer",Integer.toString(isAnswer));
    }

    private void falsePart() {
        btnSkip.setVisibility(View.VISIBLE);
        btnTrue.setEnabled(false);
        btnFalse.setEnabled(false);
        if(isAnswer==2){
            counterTrue++;
            listTrueAnswer.add(tvWord.getText().toString());
            btnFalse.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
            setRealAnswer();
        }else{
            counterFalse++;
            listFalseAnswer.add(tvWord.getText().toString());
            btnFalse.setBackgroundResource(R.color.wrong_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_false,0);
            checkWord();
            setRealAnswer();
        }
        Log.e("isAnswer",Integer.toString(isAnswer));
    }

    private void skip() {
        getTestQuantity();
        btnSkip.setVisibility(View.INVISIBLE);
        btnTrue.setEnabled(true);
        btnFalse.setEnabled(true);
        listIndexMeaning.clear();
        int step=Integer.parseInt(tvStep.getText().toString().substring(0,2));
        int lastStep=Integer.parseInt(tvStep.getText().toString().substring(3));
        step=step+1;

        tvRealAnswer.setText("");
        int rate=100/lastStep;
        int pStatus=step*rate;
        progressBar.setProgress(pStatus);

        if(step>lastStep){
            showScore();
        }else{
            if(step<10){
                tvStep.setText("0"+Integer.toString(step)+"/"+settingsPostList.get(0).getTrueFalse());
            }else{
                tvStep.setText(Integer.toString(step)+"/"+settingsPostList.get(0).getTrueFalse());
            }
            btnTrue.setBackgroundResource(R.color.default_option);
            btnFalse.setBackgroundResource(R.color.default_option);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            counterMeaningNumber=0;
            card.startAnimation(animLeft);
            isAnswer=0;
            checkWord();
            specifyWord();
        }
    }

    private void checkWord() {
        WordApp wordApp= Room.databaseBuilder(TrueFalse.this,WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
    }

    private void specifyWord(){
        Random random=new Random();
        indexWord=random.nextInt(wordList.size());
        word=wordList.get(indexWord).getWord();
        tvWord.setText(word);
        isAnswer=trueFalse();
        if(isAnswer==1){
            int indexMeaning=findMeaningNumber(indexWord);
            if(indexMeaning==1 || wordList.get(indexWord).getMeaning1().equalsIgnoreCase("")==false){
                trueMeaning =wordList.get(indexWord).getMeaning1();
            }
            if(indexMeaning==2 || wordList.get(indexWord).getMeaning2().equalsIgnoreCase("")==false){
                trueMeaning =wordList.get(indexWord).getMeaning2();
            }
            if(indexMeaning==3 || wordList.get(indexWord).getMeaning3().equalsIgnoreCase("")==false){
                trueMeaning =wordList.get(indexWord).getMeaning3();
            }
            if(indexMeaning==4 || wordList.get(indexWord).getMeaning4().equalsIgnoreCase("")==false){
                trueMeaning =wordList.get(indexWord).getMeaning4();
            }
            if(indexMeaning==5 || wordList.get(indexWord).getMeaning5().equalsIgnoreCase("")==false){
                trueMeaning =wordList.get(indexWord).getMeaning5();
            }
            tvMeaning.setText(trueMeaning);
            Log.e("lastMeaning",trueMeaning);
        }else{
            setWrongAnswer(indexWord);
        }
    }

    private void setWrongAnswer(int indexWord) {
        Random random=new Random();
        int wrongAnswer=random.nextInt(wordList.size()-1);
            int wrongIndexMeaning=findMeaningNumber(wrongAnswer);
            if(wrongAnswer==indexWord){
                if(wrongAnswer>5){
                    wrongAnswer=wrongIndexMeaning-1;
                }else{
                    wrongAnswer=wrongAnswer+wrongIndexMeaning;
                }
            }
            if(wrongIndexMeaning==1 || wordList.get(wrongAnswer).getMeaning1().equalsIgnoreCase("")==false){
                wrongMeaning =wordList.get(wrongAnswer).getMeaning1();
            }
            if(wrongIndexMeaning==2 || wordList.get(wrongAnswer).getMeaning2().equalsIgnoreCase("")==false){
                wrongMeaning =wordList.get(wrongAnswer).getMeaning2();
            }
            if(wrongIndexMeaning==3 || wordList.get(wrongAnswer).getMeaning3().equalsIgnoreCase("")==false){
                wrongMeaning =wordList.get(wrongAnswer).getMeaning3();
            }
            if(wrongIndexMeaning==4 || wordList.get(wrongAnswer).getMeaning4().equalsIgnoreCase("")==false){
                wrongMeaning =wordList.get(wrongAnswer).getMeaning4();
            }
            if(wrongIndexMeaning==5 || wordList.get(wrongAnswer).getMeaning5().equalsIgnoreCase("")==false){
                wrongMeaning =wordList.get(wrongAnswer).getMeaning5();
            }

        listIndexMeaning.add(wordList.get(indexWord).getMeaning1());
        listIndexMeaning.add(wordList.get(indexWord).getMeaning2());
        listIndexMeaning.add(wordList.get(indexWord).getMeaning3());
        listIndexMeaning.add(wordList.get(indexWord).getMeaning4());
        listIndexMeaning.add(wordList.get(indexWord).getMeaning5());

        if(listIndexMeaning.contains(wrongMeaning)==false){
            tvMeaning.setText(wrongMeaning);
            Log.e("lastMeaning",wrongMeaning);
        }else{
            listIndexMeaning.clear();
            setWrongAnswer(indexWord);
        }
    }

    private int trueFalse(){
        Random random=new Random();
        int value=random.nextInt(10)+1;
        if(value<=5){
            return 1;
        }else{
            return 2;
        }
    }

    private int findMeaningNumber(int indexWord){
            Random random=new Random();
            if(wordList.get(indexWord).getMeaning1().equalsIgnoreCase("")==false){
                counterMeaningNumber++;
            }
            if(wordList.get(indexWord).getMeaning2().equalsIgnoreCase("")==false){
                counterMeaningNumber++;
            }
            if(wordList.get(indexWord).getMeaning3().equalsIgnoreCase("")==false){
                counterMeaningNumber++;
            }
            if(wordList.get(indexWord).getMeaning4().equalsIgnoreCase("")==false){
                counterMeaningNumber++;
            }
            if(wordList.get(indexWord).getMeaning5().equalsIgnoreCase("")==false){
                counterMeaningNumber++;
            }

        int indexMeaning=random.nextInt(counterMeaningNumber)+1;
        Log.e("counterMeaningNumber",Integer.toString(counterMeaningNumber) +" : "+ Integer.toString(indexMeaning));
        return indexMeaning;
    }

    private void setRealAnswer() {
        if(wordList.get(indexWord).getMeaning1().equalsIgnoreCase("")==false){
            realAnswer=wordList.get(indexWord).getMeaning1();
        }
        if(wordList.get(indexWord).getMeaning2().equalsIgnoreCase("")==false){
            trueMeaning =wordList.get(indexWord).getMeaning2();
            realAnswer+=","+trueMeaning;
        }
        if(wordList.get(indexWord).getMeaning3().equalsIgnoreCase("")==false){
            trueMeaning =wordList.get(indexWord).getMeaning3();
            realAnswer+=","+trueMeaning;
        }
        if(wordList.get(indexWord).getMeaning4().equalsIgnoreCase("")==false){
            trueMeaning =wordList.get(indexWord).getMeaning4();
            realAnswer+=","+trueMeaning;
        }
        if(wordList.get(indexWord).getMeaning5().equalsIgnoreCase("")==false){
            trueMeaning =wordList.get(indexWord).getMeaning5();
            realAnswer+=","+trueMeaning;
        }
        tvRealAnswer.setText(realAnswer);
    }

    private void showScore() {
        counterMeaningNumber=0;
        listIndexMeaning.clear();
        new ScoreActivity(TrueFalse.this,listTrueAnswer,listFalseAnswer,counterTrue,counterFalse);
        startActivity(new Intent(TrueFalse.this,ScoreActivity.class));
        finish();
        /*AlertDialog.Builder builder=new AlertDialog.Builder(TrueFalse.this);
        View view= LayoutInflater.from(TrueFalse.this).inflate(R.layout.layout_score,null);
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
                counterTrue=0;
                counterFalse=0;
                counterMeaningNumber=0;
                startActivity(new Intent(TrueFalse.this,MainActivity.class));
                finish();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTestQuantity();
                tvStep.setText("01/"+settingsPostList.get(0).getTrueFalse());
                progressBar.setProgress(0);
                card.startAnimation(animLeft);
                tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                btnFalse.setBackgroundResource(R.color.default_option);
                btnTrue.setBackgroundResource(R.color.default_option);
                counterFalse=0;
                counterTrue=0;
                counterMeaningNumber=0;
                listIndexMeaning.clear();
                checkWord();
                specifyWord();
                dialog.dismiss();
            }
        });*/
    }

    private void getTestQuantity(){
        SettingsApp settingsApp= Room.databaseBuilder(TrueFalse.this,SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        settingsPostList=settingsApp.settingsDao().getSettings();
    }
}
