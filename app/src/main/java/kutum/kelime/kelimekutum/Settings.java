package kutum.kelime.kelimekutum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Room;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.List;

import kutum.kelime.kelimekutum.Model.SeeMeaningPost;
import kutum.kelime.kelimekutum.Model.SettingsPost;
import kutum.kelime.kelimekutum.RoomDatabase.SettingSeeMeaningApp;
import kutum.kelime.kelimekutum.RoomDatabase.SettingsApp;

public class Settings extends AppCompatActivity {

    private CardView cardTrueFalseTestNumber;
    private CardView cardMultipleChoiceTestNumber;
    private CardView cardListenMeaningTestNumber;
    private CardView cardListenWordTestNumber;
    private CardView cardListenWriteTestNumber;
    private CardView cardSeeMeaningWriteWord;
    private Button btnBack;
    private List<SettingsPost> settingsPostList;
    private List<SeeMeaningPost> seeMeaningPostList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Settings.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

        cardTrueFalseTestNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettings();
                showSettings(1,"Doğru-Yanlış Test Sayısı");
            }
        });

        cardMultipleChoiceTestNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettings();
                showSettings(2,"Çoktan Seçmeli Test Sayısı");
            }
        });

        cardListenMeaningTestNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettings();
                showSettings(3,"Dinle-Anlamı Seç Test Sayısı");
            }
        });

        cardListenWordTestNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettings();
                showSettings(4,"Dinle-Kelimeyi Seç Test Sayısı");
            }
        });

        cardListenWriteTestNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettings();
                showSettings(5,"Dinle-Kelimeyi Yaz Test Sayısı");
            }
        });

        cardSeeMeaningWriteWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettings();
                showSettings(6,"Anlamı Gör-Kelimeyi Yaz Test Sayısı");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,MainActivity.class));
                finish();
            }
        });
    }

    private void init() {
        cardTrueFalseTestNumber=findViewById(R.id.cardTrueFalseTestNumber);
        cardMultipleChoiceTestNumber=findViewById(R.id.cardMultipleChoiceTestNumber);
        cardListenMeaningTestNumber=findViewById(R.id.cardListenMeaningTestNumber);
        cardListenWordTestNumber=findViewById(R.id.cardListenWordTestNumber);
        cardListenWriteTestNumber=findViewById(R.id.cardListenWriteTestNumber);
        cardSeeMeaningWriteWord=findViewById(R.id.cardSeeMeaningWriteWord);
        btnBack=findViewById(R.id.btnBack);
    }

    private void showSettings(final int ID, String title) {
        AlertDialog.Builder builder=new AlertDialog.Builder(Settings.this);
        View view= LayoutInflater.from(Settings.this).inflate(R.layout.layout_test_number,null);
        TextView tvTitle=view.findViewById(R.id.tvTitle);
        final RadioButton rb5=view.findViewById(R.id.rb5);
        final RadioButton rb10=view.findViewById(R.id.rb10);
        final RadioButton rb15=view.findViewById(R.id.rb15);
        final RadioButton rb20=view.findViewById(R.id.rb20);
        Button btnSave=view.findViewById(R.id.btnSave);
        Button btnExit=view.findViewById(R.id.btnExit);

        if(ID==1){
            tvTitle.setText(title);
            String testNumber=settingsPostList.get(0).getTrueFalse();
            setTestNumber(testNumber,rb5,rb10,rb15,rb20);
        }else if(ID==2){
            tvTitle.setText(title);
            String testNumber=settingsPostList.get(0).getMultipleChoice();
            setTestNumber(testNumber,rb5,rb10,rb15,rb20);
        }else if(ID==3){
            tvTitle.setText(title);
            String testNumber=settingsPostList.get(0).getListenSelectMeaning();
            setTestNumber(testNumber,rb5,rb10,rb15,rb20);
        }else if(ID==4){
            tvTitle.setText(title);
            String testNumber=settingsPostList.get(0).getListenSelectWord();
            setTestNumber(testNumber,rb5,rb10,rb15,rb20);
        }else if(ID==5){
            tvTitle.setText(title);
            String testNumber=settingsPostList.get(0).getListenWriteWord();
            setTestNumber(testNumber,rb5,rb10,rb15,rb20);
        }else if(ID==6){
            tvTitle.setText(title);
            String testNumber=seeMeaningPostList.get(0).getSeeMeaningTestQuantity();
            setTestNumber(testNumber,rb5,rb10,rb15,rb20);
        }

        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(rb5.isChecked()){
                        saveSettings(ID,"5",dialog);
                    }else if(rb10.isChecked()){
                        saveSettings(ID,"10",dialog);
                    }else if(rb15.isChecked()){
                        saveSettings(ID,"15",dialog);
                    }else if(rb20.isChecked()){
                        saveSettings(ID,"20",dialog);
                    }
            }
        });
    }

    private void saveSettings(int ID, String testNumber, AlertDialog dialog) {
        SettingsApp settingsApp= Room.databaseBuilder(Settings.this,SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        SettingSeeMeaningApp settingSeeMeaning= Room.databaseBuilder(Settings.this,SettingSeeMeaningApp.class,"SeeMeaning")
                .allowMainThreadQueries()
                .build();
        getSettings();
        if(ID==1){
            settingsPostList.get(0).setTrueFalse(testNumber);
            settingsApp.settingsDao().updateSettings(settingsPostList.get(0));
        }else if(ID==2){
            settingsPostList.get(0).setMultipleChoice(testNumber);
            settingsApp.settingsDao().updateSettings(settingsPostList.get(0));
        }else if(ID==3){
            settingsPostList.get(0).setListenSelectMeaning(testNumber);
            settingsApp.settingsDao().updateSettings(settingsPostList.get(0));
        }else if(ID==4){
            settingsPostList.get(0).setListenSelectWord(testNumber);
            settingsApp.settingsDao().updateSettings(settingsPostList.get(0));
        }else if(ID==5){
            settingsPostList.get(0).setListenWriteWord(testNumber);
            settingsApp.settingsDao().updateSettings(settingsPostList.get(0));
        }else if(ID==6){
            seeMeaningPostList.get(0).setSeeMeaningTestQuantity(testNumber);
            settingSeeMeaning.settingSeeMeaningDao().updateSettings(seeMeaningPostList.get(0));
        }
        setPositiveToastMessage( "Güncelleme Tamamlandı");
        dialog.dismiss();
    }

    private void setTestNumber(String testNumber, RadioButton rb5, RadioButton rb10, RadioButton rb15, RadioButton rb20) {
        if(testNumber.equalsIgnoreCase("5")){
            rb5.setChecked(true);
        }else if(testNumber.equalsIgnoreCase("10")){
            rb10.setChecked(true);
        }else if(testNumber.equalsIgnoreCase("15")){
            rb15.setChecked(true);
        }else if(testNumber.equalsIgnoreCase("20")){
            rb20.setChecked(true);
        }
    }

    private void getSettings() {
        SettingsApp settingsApp= Room.databaseBuilder(Settings.this,SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        settingsPostList=settingsApp.settingsDao().getSettings();

        SettingSeeMeaningApp settingSeeMeaning= Room.databaseBuilder(Settings.this,SettingSeeMeaningApp.class,"SeeMeaning")
                .allowMainThreadQueries()
                .build();
        seeMeaningPostList=settingSeeMeaning.settingSeeMeaningDao().getSeeMeaningSetting();
    }

    private void setNegativeToastMessage(String message) {
        StyleableToast st=new StyleableToast(Settings.this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#ff0000"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void setPositiveToastMessage(String message) {
        StyleableToast st=new StyleableToast(Settings.this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#0000ff"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }
}
