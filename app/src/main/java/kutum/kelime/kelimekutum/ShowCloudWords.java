package kutum.kelime.kelimekutum;

import android.content.Intent;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kutum.kelime.kelimekutum.Adapter.FirebaseWordAdapter;
import kutum.kelime.kelimekutum.Constants.NodeName;
import kutum.kelime.kelimekutum.Model.Word;

public class ShowCloudWords extends AppCompatActivity {

    private TextToSpeech t1;
    private AdView adView;
    private RecyclerView recWords;
    private LinearLayoutManager linearLayoutManager;

    private List<Word> wordList;
    private static String userName;
    private FirebaseWordAdapter firebaseWordAdapter;

    private DatabaseReference databaseReference;

    public ShowCloudWords() {
    }

    public ShowCloudWords(String userName) {
        this.userName=userName;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShowCloudWords.this,MainScreen.class));
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cloud_words);
        init();

        MobileAds.initialize(this, String.valueOf(R.string.appID));
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        t1=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        getWords();
    }

    private void init() {
        adView=findViewById(R.id.adView);
        recWords=findViewById(R.id.recWords);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recWords.setLayoutManager(linearLayoutManager);
        wordList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    private void getWords() {
        databaseReference
                .child(NodeName.USERS)
                .child(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(NodeName.WORDS)){
                            databaseReference
                                    .child(NodeName.USERS)
                                    .child(userName)
                                    .child(NodeName.WORDS)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                databaseReference
                                                        .child(NodeName.USERS)
                                                        .child(userName)
                                                        .child(NodeName.WORDS)
                                                        .child(snapshot.getKey().toString())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Word word=dataSnapshot.getValue(Word.class);
                                                                wordList.add(word);
                                                                firebaseWordAdapter=new FirebaseWordAdapter(ShowCloudWords.this,userName,wordList,t1);
                                                                recWords.setAdapter(firebaseWordAdapter);
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }else{
                            setNegativeToastMessage("Kayıt Bulunamadı");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setNegativeToastMessage(String message) {
        StyleableToast st=new StyleableToast(ShowCloudWords.this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#ff0000"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void setPositiveToastMessage(String message) {
        StyleableToast st=new StyleableToast(ShowCloudWords.this,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#0000ff"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }
}
