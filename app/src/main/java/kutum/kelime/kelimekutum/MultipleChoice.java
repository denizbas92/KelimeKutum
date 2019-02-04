package kutum.kelime.kelimekutum;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MultipleChoice extends AppCompatActivity {
    private AdView adView;
    private CardView cardWord;
    private ProgressBar progressBar;
    private TextView tvStep,tvIcon,tvWord;
    private Button btnOne,btnTwo,btnThree,btnFour,btnSkip;
    private Animation animLeft;
    private List<Word> wordList;
    private static List<String> listIndexMeaning;
    private static List<String> listWrongMeaning;

    private static int indexWord;
    private static int correctOption;
    private static String word;
    private static String realAnswer;
    private static int counterMeaningNumber=0;
    private static String wrongMeaning;
    private static int counterTrue=0;
    private static int counterFalse=0;
    private static boolean isOption[]=new boolean[4];
    private List<SettingsPost> settingsPostList;
    private static List<String> listTrueAnswer;
    private static List<String> listFalseAnswer;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        counterMeaningNumber=0;
        for(int i=0 ; i<4 ; i++){
            isOption[i]=false;
        }
  //      startActivity(new Intent(MultipleChoice.this,MainActivity.class));
        startActivity(new Intent(MultipleChoice.this,MainScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);
        counterTrue=0;
        counterFalse=0;
        getTestQuantity();
        init();
        checkWord();
        specifyWord();
        showChoice();

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
                skip();
            }
        });

        MobileAds.initialize(this, String.valueOf(R.string.appID));
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void init() {
        adView=(AdView) findViewById(R.id.adView);
        cardWord=findViewById(R.id.cardWord);
        progressBar=findViewById(R.id.progressBar);
        tvStep=findViewById(R.id.tvStep);
        tvStep.setText("01/"+settingsPostList.get(0).getMultipleChoice());
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
        listIndexMeaning=new ArrayList<>();
        listWrongMeaning=new ArrayList<>();
        listFalseAnswer=new ArrayList<>();
        listTrueAnswer=new ArrayList<>();
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

    private void btnOneMethod(){
        setEnabledFalseButton();
        if(realAnswer.equalsIgnoreCase(btnOne.getText().toString())){
            counterTrue++;
            listTrueAnswer.add(tvWord.getText().toString());
            btnOne.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            listFalseAnswer.add(tvWord.getText().toString());
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

    private void btnTwoMethod(){
        setEnabledFalseButton();
        if(realAnswer.equalsIgnoreCase(btnTwo.getText().toString())){
            counterTrue++;
            listTrueAnswer.add(tvWord.getText().toString());
            btnTwo.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            listFalseAnswer.add(tvWord.getText().toString());
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

    private void btnThreeMethod(){
        setEnabledFalseButton();
        if(realAnswer.equalsIgnoreCase(btnThree.getText().toString())){
            counterTrue++;
            listTrueAnswer.add(tvWord.getText().toString());
            btnThree.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            listFalseAnswer.add(tvWord.getText().toString());
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

    private void btnFourMethod(){
        setEnabledFalseButton();
        if(realAnswer.equalsIgnoreCase(btnFour.getText().toString())){
            counterTrue++;
            listTrueAnswer.add(tvWord.getText().toString());
            btnFour.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            listFalseAnswer.add(tvWord.getText().toString());
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
        WordApp wordApp= Room.databaseBuilder(MultipleChoice.this,WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
    }

    private void specifyWord(){
        Random random=new Random();
        indexWord=random.nextInt(wordList.size()-1);
        word=wordList.get(indexWord).getWord();
        tvWord.setText(word);

        int indexMeaning=findMeaningNumber(indexWord);
        if(indexMeaning==1){
            realAnswer=wordList.get(indexWord).getMeaning1();
        }else if(indexMeaning==2){
            realAnswer=wordList.get(indexWord).getMeaning2();
        }else if(indexMeaning==3){
            realAnswer=wordList.get(indexWord).getMeaning3();
        }else if(indexMeaning==4){
            realAnswer=wordList.get(indexWord).getMeaning4();
        }else if(indexMeaning==5){
            realAnswer=wordList.get(indexWord).getMeaning5();
        }

        int option=random.nextInt(3);
        correctOption=option;
        switch (option){
            case 0:
                Log.e("meanings","0 : "+ realAnswer);
                btnOne.setText(realAnswer);
                break;
            case 1:
                Log.e("meanings","1 : "+ realAnswer);
                btnTwo.setText(realAnswer);
                break;
            case 2:
                Log.e("meanings","2 : "+ realAnswer);
                btnThree.setText(realAnswer);
                break;
            case 3:
                Log.e("meanings","3 : "+ realAnswer);
                btnFour.setText(realAnswer);
                break;
        }
        isOption[option]=true;

        for(int i=0 ; i<4 ; i++){
            setWrongAnswer(indexWord,i,isOption);
        }
    }

    private int findMeaningNumber(int indexWord){
        counterMeaningNumber=0;
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
        Log.e("wordList",Integer.toString(wordList.size()));
        int indexMeaning=random.nextInt(counterMeaningNumber)+1;
        Log.e("counterMeaningNumber",Integer.toString(counterMeaningNumber) +" : "+ Integer.toString(indexMeaning));
        return indexMeaning;
    }

    private void setWrongAnswer(int indexWord, int option, boolean[] isOption) {
        Random random=new Random();
        int wrongAnswer=random.nextInt(wordList.size()-1);
        int wrongIndexMeaning=findMeaningNumber(wrongAnswer);
        if(wrongAnswer==indexWord){
            if(wrongAnswer>5){
                wrongAnswer=wrongIndexMeaning-1;
                wrongIndexMeaning=1;
            }else{
                wrongAnswer=wrongAnswer+wrongIndexMeaning;
                wrongIndexMeaning=1;
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

        if(wrongMeaning.equalsIgnoreCase("")){
            if(indexWord!=10){
                wrongMeaning=wordList.get(10).getMeaning1();
            }else{
                wrongMeaning=wordList.get(11).getMeaning1();
            }
        }

        listIndexMeaning.add(wordList.get(indexWord).getMeaning1());
        listIndexMeaning.add(wordList.get(indexWord).getMeaning2());
        listIndexMeaning.add(wordList.get(indexWord).getMeaning3());
        listIndexMeaning.add(wordList.get(indexWord).getMeaning4());
        listIndexMeaning.add(wordList.get(indexWord).getMeaning5());

        if(listIndexMeaning.contains(wrongMeaning)==false){
            Log.e("lastMeaning",wrongMeaning);

            if(isOption[option]==false){
                if(option==0){
                    if(listWrongMeaning.contains(wrongMeaning)==false){
                        btnOne.setText(wrongMeaning);
                        isOption[option]=true;
                        listWrongMeaning.add(wrongMeaning);
                    }else{
                        setWrongAnswer(indexWord,option,isOption);
                    }
                }else if(option==1){
                    if(listWrongMeaning.contains(wrongMeaning)==false){
                        btnTwo.setText(wrongMeaning);
                        isOption[option]=true;
                        listWrongMeaning.add(wrongMeaning);
                    }else{
                        setWrongAnswer(indexWord,option,isOption);
                    }
                }else if(option==2){
                    if(listWrongMeaning.contains(wrongMeaning)==false){
                        btnThree.setText(wrongMeaning);
                        isOption[option]=true;
                        listWrongMeaning.add(wrongMeaning);
                    }else{
                        setWrongAnswer(indexWord,option,isOption);
                    }
                }else if(option==3){
                    if(listWrongMeaning.contains(wrongMeaning)==false){
                        btnFour.setText(wrongMeaning);
                        isOption[option]=true;
                        listWrongMeaning.add(wrongMeaning);
                    }else{
                        setWrongAnswer(indexWord,option,isOption);
                    }
                }
            }

        }else{
            listIndexMeaning.clear();
            setWrongAnswer(indexWord, option, isOption);
        }
    }

    private void skip() {
        setEnabledTrueButton();
        listIndexMeaning.clear();
        listWrongMeaning.clear();
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
                tvStep.setText("0"+Integer.toString(step)+"/"+settingsPostList.get(0).getMultipleChoice());
            }else{
                tvStep.setText(Integer.toString(step)+"/"+settingsPostList.get(0).getMultipleChoice());
            }
            setButtonInvisible();

            for(int i=0 ; i<4 ; i++){
                isOption[i]=false;
            }
            checkWord();
            specifyWord();
            showChoice();
        }
    }

    private void setButtonInvisible() {
        btnOne.setVisibility(View.INVISIBLE);
        btnTwo.setVisibility(View.INVISIBLE);
        btnThree.setVisibility(View.INVISIBLE);
        btnFour.setVisibility(View.INVISIBLE);
        btnOne.setBackgroundResource(R.color.default_option);
        btnTwo.setBackgroundResource(R.color.default_option);
        btnThree.setBackgroundResource(R.color.default_option);
        btnFour.setBackgroundResource(R.color.default_option);
        tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        counterMeaningNumber=0;
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
        counterMeaningNumber=0;
        for(int i=0 ; i<4 ; i++){
            isOption[i]=false;
        }
        listIndexMeaning.clear();
        listWrongMeaning.clear();
        new ScoreActivity(MultipleChoice.this,listTrueAnswer,listFalseAnswer,counterTrue,counterFalse);
        startActivity(new Intent(MultipleChoice.this,ScoreActivity.class));
        finish();
        /*AlertDialog.Builder builder=new AlertDialog.Builder(MultipleChoice.this);
        View view= LayoutInflater.from(MultipleChoice.this).inflate(R.layout.layout_score,null);
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
                counterMeaningNumber=0;
                for(int i=0 ; i<4 ; i++){
                    isOption[i]=false;
                }
                startActivity(new Intent(MultipleChoice.this,MainActivity.class));
                finish();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTestQuantity();
                tvStep.setText("01/"+settingsPostList.get(0).getMultipleChoice());
                progressBar.setProgress(0);
                tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                btnOne.setBackgroundResource(R.color.default_option);
                btnTwo.setBackgroundResource(R.color.default_option);
                btnThree.setBackgroundResource(R.color.default_option);
                btnFour.setBackgroundResource(R.color.default_option);
                counterFalse=0;
                counterTrue=0;
                counterMeaningNumber=0;
                for(int i=0 ; i<4 ; i++){
                    isOption[i]=false;
                }
                listIndexMeaning.clear();
                listWrongMeaning.clear();
                checkWord();
                specifyWord();
                showChoice();
                dialog.dismiss();
            }
        });*/
    }

    private void getTestQuantity(){
        SettingsApp settingsApp= Room.databaseBuilder(MultipleChoice.this,SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        settingsPostList=settingsApp.settingsDao().getSettings();
    }
}
