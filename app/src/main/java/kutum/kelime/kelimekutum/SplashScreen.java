package kutum.kelime.kelimekutum;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import kutum.kelime.kelimekutum.R;

public class SplashScreen extends AppCompatActivity {

    private LinearLayout top,bottom;
    private Animation uptoDown,downtoUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        top=findViewById(R.id.top);
        bottom=findViewById(R.id.bottom);

        uptoDown= AnimationUtils.loadAnimation(this,R.anim.upto_down);
        downtoUp= AnimationUtils.loadAnimation(this,R.anim.downto_up);
        top.setAnimation(uptoDown);
        bottom.setAnimation(downtoUp);
        final Handler handler=new Handler();
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(new PreferenceManager(SplashScreen.this).checkPreference()){
                //    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                   startActivity(new Intent(SplashScreen.this,MainScreen.class));
                }else{
                    startActivity(new Intent(SplashScreen.this,WelcomeActivity.class));
                }
                finish();
                handler.removeCallbacks(this);
            }
        },2000);*/
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1200);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(new PreferenceManager(SplashScreen.this).checkPreference()){
                        //    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                        startActivity(new Intent(SplashScreen.this,MainScreen.class));
                    }else{
                        startActivity(new Intent(SplashScreen.this,WelcomeActivity.class));
                    }
                    finish();
                }
            }
        };
        splashTread.start();
    }
}
