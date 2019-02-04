package kutum.kelime.kelimekutum.FragmentsAdapter;


import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import kutum.kelime.kelimekutum.MainActivity;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.R;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;
import kutum.kelime.kelimekutum.StartPractice;
import kutum.kelime.kelimekutum.select_word_practice.SelectWordList;
import kutum.kelime.kelimekutum.select_word_practice.SelectWordPracticeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeFragment extends Fragment {

    private Button btnTrueFalse;
    private Button btnMultipleChoice;
    private Button btnListenMeaning;
    private Button btnListenWord;
    private Button btnListenWrite;
    private Button btnSeeMeaningWriteWord;
    private Button btnSelectWord;
    private List<Word> wordList;

    public PracticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_practice, container, false);

        btnTrueFalse=view.findViewById(R.id.btnTrueFalse);
        btnMultipleChoice=view.findViewById(R.id.btnMultipleChoice);
        btnListenMeaning=view.findViewById(R.id.btnListenMeaning);
        btnListenWord=view.findViewById(R.id.btnListenWord);
        btnListenWrite=view.findViewById(R.id.btnListenWrite);
        btnSeeMeaningWriteWord=view.findViewById(R.id.btnSeeMeaningWriteWord);
        btnSelectWord = view.findViewById(R.id.btnSelectWord);
        selectOption(container);
        return view;
    }

    private void selectOption(final ViewGroup container) {
        btnTrueFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(interStitialAd.isLoaded()){
                    interStitialAd.show();
                }else{
                    new StartPractice(1);
                    startActivity(new Intent(MainActivity.this, StartPractice.class));
                    finish();
                }*/
                new StartPractice(1);
                startActivity(new Intent(container.getContext(), StartPractice.class));
            }
        });

        btnMultipleChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(2);
                startActivity(new Intent(container.getContext(), StartPractice.class));
            }
        });

        btnListenMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(3);
                startActivity(new Intent(container.getContext(), StartPractice.class));
            }
        });

        btnListenWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(4);
                startActivity(new Intent(container.getContext(), StartPractice.class));
            }
        });

        btnListenWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(5);
                startActivity(new Intent(container.getContext(), StartPractice.class));
            }
        });

        btnSeeMeaningWriteWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartPractice(6);
                startActivity(new Intent(container.getContext(), StartPractice.class));
            }
        });

        btnSelectWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(container.getContext(), SelectWordList.class));
            }
        });
    }
}
