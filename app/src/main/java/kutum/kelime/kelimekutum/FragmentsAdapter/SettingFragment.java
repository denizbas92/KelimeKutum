package kutum.kelime.kelimekutum.FragmentsAdapter;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.w3c.dom.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import kutum.kelime.kelimekutum.Constants.NodeName;
import kutum.kelime.kelimekutum.InternetService.ConnectionDetector;
import kutum.kelime.kelimekutum.MainActivity;
import kutum.kelime.kelimekutum.Model.SeeMeaningPost;
import kutum.kelime.kelimekutum.Model.SettingsPost;
import kutum.kelime.kelimekutum.Model.User;
import kutum.kelime.kelimekutum.R;
import kutum.kelime.kelimekutum.RoomDatabase.SettingSeeMeaningApp;
import kutum.kelime.kelimekutum.RoomDatabase.SettingsApp;
import kutum.kelime.kelimekutum.RoomDatabase.UserApp;
import kutum.kelime.kelimekutum.Settings;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    private TextView tvUserName;
    private TextView tvPassword;
    private CardView cardTrueFalseTestNumber;
    private CardView cardMultipleChoiceTestNumber;
    private CardView cardListenMeaningTestNumber;
    private CardView cardListenWordTestNumber;
    private CardView cardListenWriteTestNumber;
    private CardView cardSeeMeaningWriteWord;
    private CardView cardUserName;
    private Button btnBack;
    private List<SettingsPost> settingsPostList;
    private List<SeeMeaningPost> seeMeaningPostList;

    private DatabaseReference databaseReference;
    private List<User> userList;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_setting, container, false);
        tvUserName=view.findViewById(R.id.tvUserName);
        tvPassword=view.findViewById(R.id.tvPassword);
        cardTrueFalseTestNumber=view.findViewById(R.id.cardTrueFalseTestNumber);
        cardMultipleChoiceTestNumber=view.findViewById(R.id.cardMultipleChoiceTestNumber);
        cardListenMeaningTestNumber=view.findViewById(R.id.cardListenMeaningTestNumber);
        cardListenWordTestNumber=view.findViewById(R.id.cardListenWordTestNumber);
        cardListenWriteTestNumber=view.findViewById(R.id.cardListenWriteTestNumber);
        cardSeeMeaningWriteWord=view.findViewById(R.id.cardSeeMeaningWriteWord);
        cardUserName=view.findViewById(R.id.cardUserName);
        btnBack=view.findViewById(R.id.btnBack);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        getUserInfo();
        settingsOption(container);
        return view;
    }

    private void settingsOption(final ViewGroup container) {
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
        cardUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionDetector connectionDetector= new ConnectionDetector(getContext());
                if(!connectionDetector.isConnection()){
                    setNegativeToastMessage("İnternet Bağlantınızı Kontrol Ediniz");
                }else{
                    userNameInfo();
                }
            }
        });
    }

    private void userNameInfo() {
        getUserInfo();
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=LayoutInflater.from(getContext()).inflate(R.layout.layout_user_register,null);
        final EditText etUserName=view.findViewById(R.id.etUserName);
        final EditText etPassword=view.findViewById(R.id.etPassword);
        if(userList.size()>0){
            etPassword.setText(userList.get(0).getPassword());
            etUserName.setText(userList.get(0).getUserName());
        }
        etPassword.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
        Button btnExit=view.findViewById(R.id.btnExit);
        Button btnSave=view.findViewById(R.id.btnSave);
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
            public void onClick(final View view) {
                if(TextUtils.isEmpty(etUserName.getText().toString())){
                    setNegativeToastMessage("Kullanıcı Adı Giriniz");
                    return;
                }
                if(TextUtils.isEmpty(etPassword.getText().toString())){
                    setNegativeToastMessage("Şifre Giriniz");
                    return;
                }

                if(userList.size()>0){
                    updateRegister(etUserName,etPassword,dialog);
                }else{
                    newRegister(etUserName,etPassword,dialog);
                }
            }
        });
    }

    private void updateRegister(final EditText etUserName, final EditText etPassword, final AlertDialog dialog) {
        if(etUserName.getText().toString().equals(userList.get(0).getUserName())){
            User user=new User();
            user.setPassword(etPassword.getText().toString());
            databaseReference.child(NodeName.USERS).child(etUserName.getText().toString()).child(NodeName.USER_INFO).child("password").setValue(etPassword.getText().toString());
            UserApp userApp= Room.databaseBuilder(getContext(),UserApp.class,"UserInfo")
                    .allowMainThreadQueries()
                    .build();
            userList.get(0).setPassword(etPassword.getText().toString());
            userApp.userDao().updateUser(userList.get(0));
            setPositiveToastMessage("Güncelleme Tamamlandı");
            tvUserName.setText("Kullanıcı Adı : " + etUserName.getText().toString());
            tvPassword.setText("Şifre : " + etPassword.getText().toString());
            dialog.dismiss();
        }else{
            User user=new User();
            user.setPassword(etPassword.getText().toString());
            user.setUserName(etUserName.getText().toString());
            databaseReference.child(NodeName.USERS).child(userList.get(0).getUserName()).child(NodeName.USER_INFO).setValue(user);
            final DatabaseReference dbFrom=databaseReference.child(NodeName.USERS).child(userList.get(0).getUserName());
            final DatabaseReference dbTo=databaseReference.child(NodeName.USERS).child(etUserName.getText().toString());

            dbFrom.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dbTo.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, final DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Copy failed");
                            } else {
                                Handler handler= new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dbFrom.removeValue();
                                        setPositiveToastMessage("Güncelleme Tamamlandı");
                                        UserApp userApp= Room.databaseBuilder(getContext(),UserApp.class,"UserInfo")
                                                .allowMainThreadQueries()
                                                .build();
                                        userList.get(0).setPassword(etPassword.getText().toString());
                                        userList.get(0).setUserName(etUserName.getText().toString());
                                        userApp.userDao().updateUser(userList.get(0));

                                        tvUserName.setText("Kullanıcı Adı : " + etUserName.getText().toString());
                                        tvPassword.setText("Şifre : " + etPassword.getText().toString());
                                        dialog.dismiss();
                                    }
                                },1500);
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void newRegister(final EditText etUserName, final EditText etPassword, final AlertDialog dialog) {
        databaseReference
                .child(NodeName.USERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(etUserName.getText().toString())){
                            databaseReference
                                    .child(NodeName.USERS)
                                    .child(etUserName.getText().toString())
                                    .child(NodeName.USER_INFO)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User user=dataSnapshot.getValue(User.class);
                                            if(user.getPassword().equals(etPassword.getText().toString())){
                                                User saveUser=new User();
                                                saveUser.setUserName(etUserName.getText().toString());
                                                saveUser.setPassword(etPassword.getText().toString());
                                                UserApp userApp= Room.databaseBuilder(getContext(),UserApp.class,"UserInfo")
                                                        .allowMainThreadQueries()
                                                        .build();
                                                userApp.userDao().insertUser(saveUser);
                                                setPositiveToastMessage("Kayıt Tamamlandı");
                                                tvUserName.setText("Kullanıcı Adı : " + etUserName.getText().toString());
                                                tvPassword.setText("Şifre : " + etPassword.getText().toString());
                                                dialog.dismiss();
                                            }else{
                                                setNegativeToastMessage("Kayıtlı Kullanıcı Adı");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }else{
                            try {
                                String userName=etUserName.getText().toString();
                                String password=etPassword.getText().toString();
                                if(password.contains(".")){
                                    setNegativeToastMessage("  .  " +"karakterini kullanmayınız");
                                }else{
                                    User user=new User();
                                    user.setPassword(password);
                                    user.setUserName(userName);
                                    databaseReference.child(NodeName.USERS).child(userName).child(NodeName.USER_INFO).setValue(user);
                                    setPositiveToastMessage("Kayıt Tamamlandı");
                                    UserApp userApp= Room.databaseBuilder(getContext(),UserApp.class,"UserInfo")
                                            .allowMainThreadQueries()
                                            .build();
                                    userApp.userDao().insertUser(user);
                                    tvUserName.setText("Kullanıcı Adı : " + userName);
                                    tvPassword.setText("Şifre : " + password);
                                    dialog.dismiss();
                                }
                            }catch (Exception e){
                                Toast.makeText(getContext(),"Kullanıcı isminiz '.', '#', '$', '[', ya da '] gibi karakterler içeremez.",Toast.LENGTH_LONG).show();
                                setNegativeToastMessage(e.getMessage().toString());
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void showSettings(final int ID, String title) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_test_number,null);
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
        SettingsApp settingsApp= Room.databaseBuilder(getContext(),SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        SettingSeeMeaningApp settingSeeMeaning= Room.databaseBuilder(getContext(),SettingSeeMeaningApp.class,"SeeMeaning")
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
            setPositiveToastMessage(testNumber);
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
        SettingsApp settingsApp= Room.databaseBuilder(getContext(),SettingsApp.class,"Settings")
                .allowMainThreadQueries()
                .build();
        settingsPostList=settingsApp.settingsDao().getSettings();

        SettingSeeMeaningApp settingSeeMeaning= Room.databaseBuilder(getContext(),SettingSeeMeaningApp.class,"SeeMeaning")
                .allowMainThreadQueries()
                .build();
        seeMeaningPostList=settingSeeMeaning.settingSeeMeaningDao().getSeeMeaningSetting();
    }

    private void setNegativeToastMessage(String message) {
        StyleableToast st=new StyleableToast(getContext(),message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#ff0000"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void setPositiveToastMessage(String message) {
        StyleableToast st=new StyleableToast(getContext(),message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#0000ff"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void getUserInfo(){
        UserApp userApp= Room.databaseBuilder(getContext(),UserApp.class,"UserInfo")
                .allowMainThreadQueries()
                .build();
        userList=userApp.userDao().getUserInfo();
        if(userList.size()>0){
            tvUserName.setText("Kullanıcı Adı : "+userList.get(0).getUserName());
            tvPassword.setText("Şifre : " + userList.get(0).getPassword());
        }
    }
}
