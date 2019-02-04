package kutum.kelime.kelimekutum.FragmentsAdapter;


import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import kutum.kelime.kelimekutum.Adapter.WordAdapter;
import kutum.kelime.kelimekutum.Constants.NodeName;
import kutum.kelime.kelimekutum.InternetService.ConnectionDetector;
import kutum.kelime.kelimekutum.ListenWriteWord;
import kutum.kelime.kelimekutum.MainScreen;
import kutum.kelime.kelimekutum.Model.SeeMeaningPost;
import kutum.kelime.kelimekutum.Model.User;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.R;
import kutum.kelime.kelimekutum.RemoteConfigPackage.UpdateHelper;
import kutum.kelime.kelimekutum.RoomDatabase.SettingSeeMeaningApp;
import kutum.kelime.kelimekutum.RoomDatabase.UserApp;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;
import kutum.kelime.kelimekutum.ShowCloudWords;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private HomeFragment homeFragment;
    private ProgressDialog progressDialog;
    private TextView tvWordNumber,tvFirebaseWordNumber,tvShowFirebaseWords;
    private TextToSpeech t1;
    private Button btnDownload;
    private Button btnUploadFirebase;

    private RecyclerView recWords;
    private GridLayoutManager gridLayoutManager;
    private WordAdapter wordAdapter;

    private List<Word> wordList;
    private static List<Word> fwordList;
    private List<SeeMeaningPost> seeMeaningPostList;

    private DatabaseReference databaseReference;
    private List<User> userList;
    private static int counterWord=0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
  //      Log.e("homeFragment","girdi");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recWords=view.findViewById(R.id.recWords);
        homeFragment=new HomeFragment() ;
        tvWordNumber=view.findViewById(R.id.tvWordNumber);
        tvFirebaseWordNumber=view.findViewById(R.id.tvFirebaseWordNumber);
        tvShowFirebaseWords=view.findViewById(R.id.tvShowFirebaseWords);
        btnUploadFirebase=view.findViewById(R.id.btnUploadFirebase);
        btnDownload=view.findViewById(R.id.btnDownload);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        progressDialog=new ProgressDialog(container.getContext());
        fwordList=new ArrayList<>();
        t1=new TextToSpeech(container.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        isNewSettings();
        checkFirstTime();
        getWords();
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionDetector connectionDetector= new ConnectionDetector(getContext());
                if(!connectionDetector.isConnection()){
                    setNegativeToastMessage("İnternet Bağlantınızı Kontrol Ediniz");
                }else{
                    downloadWord();
                }
            }
        });
        btnUploadFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionDetector connectionDetector= new ConnectionDetector(getContext());
                if(!connectionDetector.isConnection()){
                    setNegativeToastMessage("İnternet Bağlantınızı Kontrol Ediniz");
                }else{
                    uploadWord();
                }
            }
        });

        tvShowFirebaseWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionDetector connectionDetector= new ConnectionDetector(getContext());
                if(!connectionDetector.isConnection()){
                    setNegativeToastMessage("İnternet Bağlantınızı Kontrol Ediniz");
                }else{
                    if(userList.size()>0){
                        new ShowCloudWords(userList.get(0).getUserName());
                        startActivity(new Intent(getContext(),ShowCloudWords.class));
                    }else{
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        View view1= LayoutInflater.from(getContext()).inflate(R.layout.layout_message,null);
                        TextView tvMessage=view1.findViewById(R.id.tvMessage);
                        tvMessage.setText("Ayarlar bölümünden kullanıcı bilgisi kaydı yapınız. Eğer kaydınız var ise " +
                                " kullanıcı adı ve şifrenizi yazıp kaydetmeniz yeterlidir.");
                        builder.setView(view1);

                        final AlertDialog dialog=builder.create();
                        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                        dialog.show();
                    }
                }
            }
        });

        return view;
    }

    private void uploadWord() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=LayoutInflater.from(getContext()).inflate(R.layout.layout_user_register,null);
        final EditText etUserName=view.findViewById(R.id.etUserName);
        final EditText etPassword=view.findViewById(R.id.etPassword);
        etPassword.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
        Button btnExit=view.findViewById(R.id.btnExit);
        Button btnSave=view.findViewById(R.id.btnSave);
        btnSave.setText("YÜKLE");
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
                if(TextUtils.isEmpty(etUserName.getText().toString())){
                    setNegativeToastMessage("Kullanıcı Adı Giriniz");
                    return;
                }
                if(TextUtils.isEmpty(etPassword.getText().toString())){
                    setNegativeToastMessage("Şifre Giriniz");
                    return;
                }
                progressDialog.setMessage("Yükleniyor...");
                progressDialog.show();
                databaseReference
                        .child(NodeName.USERS)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(etUserName.getText().toString())){
                                    databaseReference
                                            .child(NodeName.USERS)
                                            .child(etUserName.getText().toString())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(final DataSnapshot dataUser) {
                                                    databaseReference
                                                            .child(NodeName.USERS)
                                                            .child(etUserName.getText().toString())
                                                            .child(NodeName.USER_INFO)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    User user=dataSnapshot.getValue(User.class);
                                                                    String password=user.getPassword();
                                                                    if(password.equals(etPassword.getText().toString())){
                                                                            if(wordList.size()>=100){
                                                                                for(int i=0 ; i<wordList.size() ; i++){
                                                                                    databaseReference
                                                                                            .child(NodeName.USERS)
                                                                                            .child(etUserName.getText().toString())
                                                                                            .child(NodeName.WORDS)
                                                                                            .child(wordList.get(i).getWord())
                                                                                            .setValue(wordList.get(i));
                                                                                }
                                                                                dialog.dismiss();
                                                                                setPositiveToastMessage("Yükleme Tamamlandı");
                                                                                new Handler().postDelayed(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        progressDialog.dismiss();
                                                                                        ((MainScreen)getActivity()).refresh();
                                                                                    }
                                                                                },1000);
                                                                            }else{
                                                                                progressDialog.dismiss();
                                                                                setNegativeToastMessage("En az 100 kelime olduğunda dışa aktar yapabilirsiniz.");
                                                                            }
                                                                    }else{
                                                                        progressDialog.dismiss();
                                                                        setNegativeToastMessage("Yanlış Şifre");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }else{
                                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                    View view=LayoutInflater.from(getContext()).inflate(R.layout.layout_message,null);
                                    TextView tvMessage=view.findViewById(R.id.tvMessage);
                                    tvMessage.setText("Geçersiz Kullanıcı Adı \n"+
                                            "Eğer kullanıcı hesabınız yok ise ayarlar kısmından oluşturabilirsiniz.");
                                    builder.setView(view);

                                    final AlertDialog dialog=builder.create();
                                    dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    private void downloadWord() {
        checkWord();
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=LayoutInflater.from(getContext()).inflate(R.layout.layout_download_word_dialog,null);
        final EditText etUserName=view.findViewById(R.id.etUserName);
        Button btnExit=view.findViewById(R.id.btnExit);
        Button btnDownload=view.findViewById(R.id.btnDownload);
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

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etUserName.getText().toString())){
                    setNegativeToastMessage("Kullanıcı Adı Giriniz");
                    return;
                }
                progressDialog.setMessage("İndiriliyor...");
                progressDialog.show();
                databaseReference
                        .child(NodeName.USERS)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(etUserName.getText().toString())){
                                    databaseReference
                                            .child(NodeName.USERS)
                                            .child(etUserName.getText().toString())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(NodeName.WORDS)){
                                                        databaseReference
                                                                .child(NodeName.USERS)
                                                                .child(etUserName.getText().toString())
                                                                .child(NodeName.WORDS)
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(final DataSnapshot dataWord) {
                                                                        for(DataSnapshot snapWord:dataWord.getChildren()){
                                                                            for(int i=0 ; i<wordList.size() ; i++){
                                                                                if(wordList.get(i).getWord().equals(snapWord.getKey().toString())){
                                                                                    break;
                                                                                }else{
                                                                                    if(i==wordList.size()-1){
                                                                                        databaseReference
                                                                                                .child(NodeName.USERS)
                                                                                                .child(etUserName.getText().toString())
                                                                                                .child(NodeName.WORDS)
                                                                                                .child(snapWord.getKey().toString())
                                                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                        Word word=dataSnapshot.getValue(Word.class);
                                                                                                        WordApp wordApp= Room.databaseBuilder(getContext(),WordApp.class,"Words")
                                                                                                                .allowMainThreadQueries()
                                                                                                                .build();
                                                                                                        Word newWord=new Word();
                                                                                                        newWord.setWord(word.getWord());
                                                                                                        newWord.setMeaning1(word.getMeaning1());
                                                                                                        newWord.setMeaning2(word.getMeaning2());
                                                                                                        newWord.setMeaning3(word.getMeaning3());
                                                                                                        newWord.setMeaning4(word.getMeaning4());
                                                                                                        newWord.setMeaning5(word.getMeaning5());
                                                                                                        wordApp.wordDao().insertWord(newWord);
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        dialog.dismiss();
                                                                        setPositiveToastMessage("İndirme İşlemi Tamamlandı");
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                progressDialog.dismiss();
                                                                                ((MainScreen)getActivity()).refresh();
                                                                            }
                                                                        },1500);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });
                                                    }else{
                                                        progressDialog.dismiss();
                                                        setNegativeToastMessage("Kayıt Bulunamadı");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }else{
                                    progressDialog.dismiss();
                                    setNegativeToastMessage("Geçersiz Kullanıcı Adı");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    private void getWords() {
        final DisplayMetrics metrics= new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth=metrics.widthPixels;
        int screenHeight=metrics.heightPixels;
        double wi=(double)screenWidth/(double)metrics.xdpi;
        double hi=(double)screenHeight/(double)metrics.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        final double screenInches = Math.sqrt(x+y);

        if (screenInches<5){
            gridLayoutManager=new GridLayoutManager(getContext(),1);
        }else if(screenInches>5 && screenInches<7){
            gridLayoutManager=new GridLayoutManager(getContext(),2);
        }else{
            gridLayoutManager=new GridLayoutManager(getContext(),3);
        }
        recWords.setLayoutManager(gridLayoutManager);
        checkWord();
        if(wordList.size()>0){
            wordAdapter=new WordAdapter(getContext(),wordList,t1);
            recWords.setAdapter(wordAdapter);
        }

    }

    private void checkWord() {
        WordApp wordApp= Room.databaseBuilder(getContext(),WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
        tvWordNumber.setText("Toplam Kelime Sayısı : "+Integer.toString(wordList.size()));

        UserApp userApp= Room.databaseBuilder(getContext(),UserApp.class,"UserInfo")
                .allowMainThreadQueries()
                .build();
        userList=userApp.userDao().getUserInfo();
        if(userList.size()>0){
            databaseReference
                    .child(NodeName.USERS)
                    .child(userList.get(0).getUserName())
                    .child(NodeName.WORDS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tvFirebaseWordNumber.setText(getString(R.string.word_number_firebase) + String.valueOf(dataSnapshot.getChildrenCount()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }else{
            tvFirebaseWordNumber.setText(getString(R.string.word_number_firebase) + "0");
        }

        if(wordList.size()>0){
            try{
                Comparator<Word> com=new ComImp();
                Collections.sort(wordList,com);
            }catch (Exception e){

            }
        }
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
        WordApp wordApp= Room.databaseBuilder(getContext(),WordApp.class,"Words")
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void isNewSettings() {
        SettingSeeMeaningApp settingSeeMeaning= Room.databaseBuilder(getContext(),SettingSeeMeaningApp.class,"SeeMeaning")
                .allowMainThreadQueries()
                .build();
        seeMeaningPostList=settingSeeMeaning.settingSeeMeaningDao().getSeeMeaningSetting();
        if(seeMeaningPostList.size()==0){
            SeeMeaningPost seeMeaningPost=new SeeMeaningPost("20");
            settingSeeMeaning.settingSeeMeaningDao().insertSettings(seeMeaningPost);
        }
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
