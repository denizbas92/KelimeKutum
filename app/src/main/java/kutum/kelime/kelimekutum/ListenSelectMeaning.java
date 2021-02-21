package kutum.kelime.kelimekutum;

import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;
import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
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
import java.util.Locale;
import java.util.Random;

import kutum.kelime.kelimekutum.Model.SettingsPost;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.RoomDatabase.SettingsApp;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;

public class ListenSelectMeaning extends AppCompatActivity {
    private AdView adView;
    private CardView cardWord;
    private ProgressBar progressBar;
    private TextView tvStep,tvIcon,tvWord,tvAnswer;
    private Button btnOne,btnTwo,btnThree,btnFour,btnSkip;
    private Animation animLeft;
    private TextToSpeech t1;
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
     //   startActivity(new Intent(ListenSelectMeaning.this,MainActivity.class));
        startActivity(new Intent(ListenSelectMeaning.this,MainScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_select_meaning);
        counterTrue=0;
        counterFalse=0;
        getTestQuantity();
        init();
        checkWord();
        specifyWord();
        showChoice();

        MobileAds.initialize(this, String.valueOf(R.string.appID));
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

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
                getTestQuantity();
                skip();
            }
        });
        tvWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(tvAnswer.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void init() {
        adView=(AdView) findViewById(R.id.adView);
        cardWord=findViewById(R.id.cardWord);
        progressBar=findViewById(R.id.progressBar);
        tvAnswer=findViewById(R.id.tvAnswer);
        tvStep=findViewById(R.id.tvStep);
        tvStep.setText("01/"+settingsPostList.get(0).getListenSelectMeaning());
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
                                        btnOne.setEnabled(true);
                                        btnTwo.setEnabled(true);
                                        btnThree.setEnabled(true);
                                        btnFour.setVisibility(View.VISIBLE);
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
            listTrueAnswer.add(tvAnswer.getText().toString());
   //         showMessage(tvAnswer.getText().toString(),true);
            btnOne.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            listFalseAnswer.add(tvAnswer.getText().toString());
            showMessage(tvAnswer.getText().toString(),false);
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
            listTrueAnswer.add(tvAnswer.getText().toString());
     //       showMessage(tvAnswer.getText().toString(),true);
            btnTwo.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            listFalseAnswer.add(tvAnswer.getText().toString());
            showMessage(tvAnswer.getText().toString(),false);
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
            listTrueAnswer.add(tvAnswer.getText().toString());
     //       showMessage(tvAnswer.getText().toString(),true);
            btnThree.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            listFalseAnswer.add(tvAnswer.getText().toString());
            showMessage(tvAnswer.getText().toString(),false);
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
            listTrueAnswer.add(tvAnswer.getText().toString());
     //       showMessage(tvAnswer.getText().toString(),true);
            btnFour.setBackgroundResource(R.color.correct_answer);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
        }else{
            counterFalse++;
            listFalseAnswer.add(tvAnswer.getText().toString());
            showMessage(tvAnswer.getText().toString(),false);
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
        WordApp wordApp= Room.databaseBuilder(ListenSelectMeaning.this,WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
    }

    private void specifyWord(){
        Random random=new Random();
        indexWord=random.nextInt(wordList.size()-1);
        word=wordList.get(indexWord).getWord();
        tvAnswer.setText(word);

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

        int option=random.nextInt(4);
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
            if(option!=i){
                setWrongAnswer(indexWord,i,isOption);
            }
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
        int wrongAnswer=random.nextInt(wordList.size());
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
            Log.e("buttonNamelastMeaning","last1"+wrongMeaning);
        }
        if(wrongIndexMeaning==2 || wordList.get(wrongAnswer).getMeaning2().equalsIgnoreCase("")==false){
            wrongMeaning =wordList.get(wrongAnswer).getMeaning2();
            Log.e("buttonNamelastMeaning","last2"+wrongMeaning);
        }
        if(wrongIndexMeaning==3 || wordList.get(wrongAnswer).getMeaning3().equalsIgnoreCase("")==false){
            wrongMeaning =wordList.get(wrongAnswer).getMeaning3();
            Log.e("buttonNamelastMeaning","last3"+wrongMeaning);
        }
        if(wrongIndexMeaning==4 || wordList.get(wrongAnswer).getMeaning4().equalsIgnoreCase("")==false){
            wrongMeaning =wordList.get(wrongAnswer).getMeaning4();
            Log.e("buttonNamelastMeaning","last4"+wrongMeaning);
        }
        if(wrongIndexMeaning==5 || wordList.get(wrongAnswer).getMeaning5().equalsIgnoreCase("")==false){
            wrongMeaning =wordList.get(wrongAnswer).getMeaning5();
            Log.e("buttonNamelastMeaning","last5"+wrongMeaning);
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
            Log.e("buttonNamelastMeaning","last0"+wrongMeaning);

            if(isOption[option]==false){
                if(option==0){
                    if(listWrongMeaning.contains(wrongMeaning)==false){
                            btnOne.setText(wrongMeaning);
                            isOption[option]=true;
                            Log.e("buttonName1","1 "+wrongMeaning);
                            listWrongMeaning.add(wrongMeaning);
                    }else{
                        Log.e("buttonName1","1 "+wrongMeaning);
                        setWrongAnswer(indexWord,option,isOption);
                    }
                }else if(option==1){
                    if(listWrongMeaning.contains(wrongMeaning)==false){
                        btnTwo.setText(wrongMeaning);
                        isOption[option]=true;
                        Log.e("buttonName2","2 " + wrongMeaning);
                        listWrongMeaning.add(wrongMeaning);
                    }else{
                        Log.e("buttonName2","2 "+wrongMeaning);
                        setWrongAnswer(indexWord,option,isOption);
                    }
                }else if(option==2){
                    if(listWrongMeaning.contains(wrongMeaning)==false){
                        btnThree.setText(wrongMeaning);
                        isOption[option]=true;
                        Log.e("buttonName3","3 "+wrongMeaning);
                        listWrongMeaning.add(wrongMeaning);
                    }else{
                        Log.e("buttonName3","3 "+wrongMeaning);
                        setWrongAnswer(indexWord,option,isOption);
                    }
                }else if(option==3){
                    if(listWrongMeaning.contains(wrongMeaning)==false){
                        btnFour.setText(wrongMeaning);
                        isOption[option]=true;
                        Log.e("buttonName4","4 "+wrongMeaning);
                        listWrongMeaning.add(wrongMeaning);
                    }else{
                        Log.e("buttonName4","4 "+wrongMeaning);
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
                tvStep.setText("0"+Integer.toString(step)+"/"+settingsPostList.get(0).getListenSelectMeaning());
            }else{
                tvStep.setText(Integer.toString(step)+"/"+settingsPostList.get(0).getListenSelectMeaning());
            }
            setSomeFeatures();
            for(int i=0 ; i<4 ; i++){
                isOption[i]=false;
            }
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

        new ScoreActivity(ListenSelectMeaning.this,listTrueAnswer,listFalseAnswer,counterTrue,counterFalse);
        startActivity(new Intent(ListenSelectMeaning.this,ScoreActivity.class));
        finish();
        /*AlertDialog.Builder builder=new AlertDialog.Builder(ListenSelectMeaning.this);
        View view= LayoutInflater.from(ListenSelectMeaning.this).inflate(R.layout.layout_score,null);
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
                startActivity(new Intent(ListenSelectMeaning.this,MainActivity.class));
                finish();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTestQuantity();
                tvStep.setText("01/"+settingsPostList.get(0).getListenSelectMeaning());
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
        SettingsApp settingsApp= Room.databaseBuilder(ListenSelectMeaning.this,SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        settingsPostList=settingsApp.settingsDao().getSettings();
    }

    private void showMessage(String word ,boolean isCorrect){
        AlertDialog.Builder builder=new AlertDialog.Builder(ListenSelectMeaning.this);
        View view= LayoutInflater.from(ListenSelectMeaning.this).inflate(R.layout.layout_message,null);
        TextView tvMessage=view.findViewById(R.id.tvMessage);
        builder.setView(view);

        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();

        if(isCorrect){
            tvMessage.setText("DOĞRU !");
            tvMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            },1500);
        }else{
            tvMessage.setText("Doğru Cevap : " + word);
        }
    }
}
