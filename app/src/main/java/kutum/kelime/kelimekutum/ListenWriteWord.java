package kutum.kelime.kelimekutum;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import kutum.kelime.kelimekutum.Model.SettingsPost;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.RoomDatabase.SettingsApp;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;

public class ListenWriteWord extends AppCompatActivity {
    private AdView adView;
    private CardView cardWord;
    private ProgressBar progressBar;
    private Button btnSkip,btnConfirm;
    private TextView tvTrialCounter,tvAnswer,tvWord,tvIcon,tvStep;
    private EditText etWord;
    private TextToSpeech t1;

    private static int counterTrue=0;
    private static int counterFalse=0;
    private static int trialCounter=3;
    private List<Word> wordList;

    private Animation animLeft;
    private List<SettingsPost> settingsPostList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        counterTrue=0;
        counterFalse=0;
        trialCounter=3;
  //      startActivity(new Intent(ListenWriteWord.this,MainActivity.class));
        startActivity(new Intent(ListenWriteWord.this,MainScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_write_word);
        counterTrue=0;
        counterFalse=0;
        trialCounter=3;
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

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTestQuantity(); skip();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etWord.getText().toString())){
                    setNegativeToastMessage("Kelime Giriniz");
                    return;
                }

                if(etWord.getText().toString().trim().equalsIgnoreCase(tvAnswer.getText().toString())){
                    counterTrue++;
                    btnSkip.setVisibility(View.VISIBLE);
                    btnConfirm.setEnabled(false);
                    tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_true_normal,0);
                    showMessage(tvAnswer.getText().toString(),true);
                }else{
                    trialCounter--;

                    if(trialCounter==0){
                        counterFalse++;
                        btnSkip.setVisibility(View.VISIBLE);
                        btnConfirm.setEnabled(false);
                        showMessage(tvAnswer.getText().toString(),false);
                    }else{
                        tvTrialCounter.setText(Integer.toString(trialCounter) + " deneme hakkınız kaldı");
                    }
                    tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_false,0);
                }
            }
        });
    }

    private void init() {
        adView=(AdView) findViewById(R.id.adView);
        cardWord=findViewById(R.id.cardWord);
        progressBar=findViewById(R.id.progressBar);
        btnSkip=findViewById(R.id.btnSkip);
        btnConfirm=findViewById(R.id.btnConfirm);
        tvTrialCounter=findViewById(R.id.tvTrialCounter);
        tvAnswer=findViewById(R.id.tvAnswer);
        tvWord=findViewById(R.id.tvWord);
        tvIcon=findViewById(R.id.tvIcon);
        etWord=findViewById(R.id.etWord);
        tvStep=findViewById(R.id.tvStep);
        tvStep.setText("01/"+settingsPostList.get(0).getListenWriteWord());
        int lastStep=Integer.parseInt(tvStep.getText().toString().substring(3));
        progressBar.setProgress(100/lastStep);
        animLeft= AnimationUtils.loadAnimation(this,R.anim.left_to_right);
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
        btnConfirm.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnConfirm.setVisibility(View.VISIBLE);
            }
        },400);

    }

    private void checkWord() {
        WordApp wordApp= Room.databaseBuilder(ListenWriteWord.this,WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
    }

    private void specifyWord(){
        Random random=new Random();
        int indexWord=random.nextInt(wordList.size());
        tvAnswer.setText(wordList.get(indexWord).getWord());
    }

    private void setNegativeToastMessage(String message) {
        StyleableToast st=new StyleableToast(ListenWriteWord.this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#ff0000"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void setPositiveToastMessage(String message) {
        StyleableToast st=new StyleableToast(ListenWriteWord.this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#0000ff"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void showMessage(String word ,boolean isCorrect){
        AlertDialog.Builder builder=new AlertDialog.Builder(ListenWriteWord.this);
        View view= LayoutInflater.from(ListenWriteWord.this).inflate(R.layout.layout_message,null);
        TextView tvMessage=view.findViewById(R.id.tvMessage);
        builder.setView(view);

        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();

        if(isCorrect){
            etWord.setEnabled(false);
            btnConfirm.setEnabled(false);
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
            etWord.setEnabled(false);
            btnConfirm.setEnabled(false);
        }
    }

    private void skip() {
        btnConfirm.setEnabled(true);
        btnSkip.setVisibility(View.INVISIBLE);
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
                tvStep.setText("0"+Integer.toString(step)+"/"+settingsPostList.get(0).getListenWriteWord());
            }else{
                tvStep.setText(Integer.toString(step)+"/"+settingsPostList.get(0).getListenWriteWord());
            }
            trialCounter=3;
            tvTrialCounter.setText("3 deneme hakkınız kaldı");
            etWord.setText("");
            etWord.setEnabled(true);
            btnConfirm.setEnabled(true);
            btnConfirm.setBackgroundResource(R.color.default_option);
            tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            checkWord();
            specifyWord();
            showChoice();
        }
    }

    private void showScore() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ListenWriteWord.this);
        View view= LayoutInflater.from(ListenWriteWord.this).inflate(R.layout.layout_score,null);
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
                trialCounter=3;
                startActivity(new Intent(ListenWriteWord.this,MainScreen.class));
                finish();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTestQuantity();
                tvStep.setText("01/"+settingsPostList.get(0).getListenWriteWord());
                progressBar.setProgress(0);
                tvIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                btnConfirm.setBackgroundResource(R.color.default_option);
                counterFalse=0;
                counterTrue=0;
                trialCounter=3;
                tvTrialCounter.setText("3 deneme hakkınız kaldı");
                etWord.setText("");
                etWord.setEnabled(true);
                btnConfirm.setEnabled(true);
                checkWord();
                specifyWord();
                showChoice();
                dialog.dismiss();
            }
        });
    }

    private void getTestQuantity(){
        SettingsApp settingsApp= Room.databaseBuilder(ListenWriteWord.this,SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        settingsPostList=settingsApp.settingsDao().getSettings();
    }
}
