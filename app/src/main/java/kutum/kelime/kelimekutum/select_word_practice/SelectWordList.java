package kutum.kelime.kelimekutum.select_word_practice;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kutum.kelime.kelimekutum.MainActivity;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.R;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;

public class SelectWordList extends AppCompatActivity implements OnGetCounter {

    private TextView tvCounter;
    private EditText etSearch ;
    private RecyclerView recList ;
    private SelectWordPracticeAdapter selectWordPracticeAdapter ;
    private List<Word> wordList ;
    LinearLayoutManager mLayoutManager ;
    private static int counter = 0;

    public SelectWordList() {

    }
    public SelectWordList(int counter) {
        this.counter = counter ;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SelectWordList.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_word_list);

        init();
        getWords();
        showWords();
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        SelectWordList.counter = counter;
    }

    private void init() {
        tvCounter = findViewById(R.id.tvCounter);
        etSearch = findViewById(R.id.etSearch);
        recList = findViewById(R.id.recList);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recList.setLayoutManager(mLayoutManager);

        wordList = new ArrayList<>();
    }

    private void getWords() {
        WordApp wordApp= Room.databaseBuilder(SelectWordList.this,WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
        try{
            Comparator<Word> com=new ComImp();
            Collections.sort(wordList,com);
        }catch (Exception e){

        }
    }

    private void showWords() {
        selectWordPracticeAdapter = new SelectWordPracticeAdapter(SelectWordList.this,wordList,this);
        recList.setAdapter(selectWordPracticeAdapter);
    }

    @Override
    public void getCounter(int counter) {
        tvCounter = findViewById(R.id.tvCounter);
        tvCounter.setText(String.valueOf(counter));
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
