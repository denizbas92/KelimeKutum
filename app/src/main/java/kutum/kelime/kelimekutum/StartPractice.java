package kutum.kelime.kelimekutum;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartPractice extends AppCompatActivity {

    private Button btnStart;
    Animation animAlpha,animScale ;

    private static int ID;

    public StartPractice(){

    }

    public StartPractice(int ID) {
        this.ID=ID;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
 //       startActivity(new Intent(StartPractice.this,MainActivity.class));
        startActivity(new Intent(StartPractice.this,MainScreen.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_practice);

        btnStart=findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.startAnimation(animAlpha);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(ID==1){
                            startActivity(new Intent(StartPractice.this, TrueFalse.class));
                            finish();
                        } else if(ID==2){
                            startActivity(new Intent(StartPractice.this, MultipleChoice.class));
                            finish();
                        } else if(ID==3){
                            startActivity(new Intent(StartPractice.this, ListenSelectMeaning.class));
                            finish();
                        }else if(ID==4){
                            startActivity(new Intent(StartPractice.this, ListenSelectWord.class));
                            finish();
                        }else if(ID==5){
                            startActivity(new Intent(StartPractice.this, ListenWriteWord.class));
                            finish();
                        }else if(ID==6){
                            startActivity(new Intent(StartPractice.this, SeeMeaningWriteWord.class));
                            finish();
                        }
                    }
                },600);
            }
        });

        animAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_alpha);
        animScale=AnimationUtils.loadAnimation(this,R.anim.anim_scale);

        /*MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animScale.setInterpolator(interpolator);*/

    }

    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }
}
