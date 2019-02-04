package kutum.kelime.kelimekutum;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import kutum.kelime.kelimekutum.Adapter.FalseAnswerAdapter;
import kutum.kelime.kelimekutum.Adapter.TrueAnswersAdapter;
import kutum.kelime.kelimekutum.Model.TrueFalseAnswerPost;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrueAnswerFragment extends Fragment {

    @BindView(R.id.recTrueAnswers)
    RecyclerView recTrueAnswers;
    private LinearLayoutManager linearLayoutManager;
    private TrueAnswersAdapter trueAnswersAdapter;
    private static List<String> listTrueAnswer;
    private List<TrueFalseAnswerPost> trueFalseAnswerPostList;
    private TrueFalseAnswerPost trueFalseAnswerPost;

    private TextToSpeech t1;
    ScoreActivity scoreActivity;
    private List<Word> wordList;

    public TrueAnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_true_answer, container, false);
        ButterKnife.bind(this, view);

        linearLayoutManager=new LinearLayoutManager(container.getContext(),LinearLayoutManager.VERTICAL,false);
        recTrueAnswers.setLayoutManager(linearLayoutManager);

        trueFalseAnswerPost=new TrueFalseAnswerPost();
        scoreActivity=new ScoreActivity();
        listTrueAnswer=scoreActivity.getListTrueAnswer();
        trueFalseAnswerPostList=new ArrayList<>();
        t1=new TextToSpeech(container.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        getWord();
        getWordsAndAnswers(container,listTrueAnswer,t1);

        return view;
    }

    private void getWordsAndAnswers(ViewGroup container, List<String> listTrueAnswer, TextToSpeech t1) {
        for(int i=0 ; i<wordList.size() ; i++){
            for(int j=0 ; j<listTrueAnswer.size() ; j++){
                if(wordList.get(i).getWord().equalsIgnoreCase(listTrueAnswer.get(j))){
                    trueFalseAnswerPost=new TrueFalseAnswerPost(wordList.get(i).getWord(),
                                                                wordList.get(i).getMeaning1(),
                                                                wordList.get(i).getMeaning2(),
                                                                wordList.get(i).getMeaning3(),
                                                                wordList.get(i).getMeaning4(),
                                                                wordList.get(i).getMeaning5());
                    trueFalseAnswerPostList.add(trueFalseAnswerPost);
                }
            }
            if(i==wordList.size()-1){
                trueAnswersAdapter=new TrueAnswersAdapter(container.getContext(),trueFalseAnswerPostList,t1);
                recTrueAnswers.setAdapter(trueAnswersAdapter);
            }
        }
    }

    private void getWord() {
        WordApp wordApp= Room.databaseBuilder(getContext(),WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
    }

}
