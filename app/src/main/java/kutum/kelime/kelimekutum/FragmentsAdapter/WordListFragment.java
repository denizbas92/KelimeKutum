package kutum.kelime.kelimekutum.FragmentsAdapter;


import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.R;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;
import kutum.kelime.kelimekutum.WordList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordListFragment extends Fragment {

    private TextToSpeech t1;
    private Toolbar toolbar;
    private MaterialSearchView searchView ;
    private ListView listView ;
    private static List<Word> wordList;
    private String [] listSource;

    private static int index;
    ArrayAdapter adapterFisrt ;

    public WordListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_word_list, container, false);
        setHasOptionsMenu(true);
        searchView=(MaterialSearchView)view.findViewById(R.id.searchView);
        listView=(ListView)view.findViewById(R.id.listView);
        toolbar=(Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Kelimeler");

        t1=new TextToSpeech(container.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        materialSearch();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(int j=0 ; j<wordList.size() ; j++){
                    if(wordList.get(j).getWord().equalsIgnoreCase(listView.getItemAtPosition(i).toString())){
                        index=j;
                        break;
                    }
                }
                showWord(index);
            }
        });

        return view;
    }

    private void showWord(final int index) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_show_word,null);
        TextView tvWord=view.findViewById(R.id.tvWord);
        TextView tvVoice=view.findViewById(R.id.tvVoice);
        TextView tvMeaning1=view.findViewById(R.id.tvMeaning1);
        TextView tvMeaning2=view.findViewById(R.id.tvMeaning2);
        TextView tvMeaning3=view.findViewById(R.id.tvMeaning3);
        TextView tvMeaning4=view.findViewById(R.id.tvMeaning4);
        TextView tvMeaning5=view.findViewById(R.id.tvMeaning5);
        Button btnDelete=view.findViewById(R.id.btnDelete);
        Button btnUpdate=view.findViewById(R.id.btnUpdate);

        tvWord.setText(wordList.get(index).getWord());
        tvMeaning1.setText("1."+wordList.get(index).getMeaning1());

        if(wordList.get(index).getMeaning2().equalsIgnoreCase("")==false){
            tvMeaning2.setText("2."+wordList.get(index).getMeaning2());
        }
        if(wordList.get(index).getMeaning3().equalsIgnoreCase("")==false){
            tvMeaning3.setText("3."+wordList.get(index).getMeaning3());
        }
        if(wordList.get(index).getMeaning4().equalsIgnoreCase("")==false){
            tvMeaning4.setText("4."+wordList.get(index).getMeaning4());
        }
        if(wordList.get(index).getMeaning5().equalsIgnoreCase("")==false){
            tvMeaning5.setText("5."+wordList.get(index).getMeaning5());
        }

        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();

        tvVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(wordList.get(index).getWord(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWord(index,dialog);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWord(index,dialog);
            }
        });
    }

    private void updateWord(final int index, final AlertDialog mainDialog) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_add_word,null);
        Button btnSave=view.findViewById(R.id.btnSave);
        Button btnCancel=view.findViewById(R.id.btnCancel);
        final EditText etWord=view.findViewById(R.id.etWord);
        final EditText etMeaning1=view.findViewById(R.id.etMeaning1);
        final EditText etMeaning2=view.findViewById(R.id.etMeaning2);
        final EditText etMeaning3=view.findViewById(R.id.etMeaning3);
        final EditText etMeaning4=view.findViewById(R.id.etMeaning4);
        final EditText etMeaning5=view.findViewById(R.id.etMeaning5);

        final String oldWord=wordList.get(index).getWord();
        etWord.setText(wordList.get(index).getWord());
        etMeaning1.setText(wordList.get(index).getMeaning1());
        etMeaning2.setText(wordList.get(index).getMeaning2());
        etMeaning3.setText(wordList.get(index).getMeaning3());
        etMeaning4.setText(wordList.get(index).getMeaning4());
        etMeaning5.setText(wordList.get(index).getMeaning5());

        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etWord.getText().toString())){
                    setNegativeToastMessage("Kelimeyi giriniz...");
                    return;
                }
                if(TextUtils.isEmpty(etMeaning1.getText().toString())){
                    setNegativeToastMessage("1.Anlamı giriniz...");
                    return;
                }

                WordApp wordApp= Room.databaseBuilder(getContext(),WordApp.class,"Words")
                        .allowMainThreadQueries()
                        .build();
                checkWord();
                final String word=etWord.getText().toString().trim();
                final String meaning1=etMeaning1.getText().toString().trim();
                final String meaning2=etMeaning2.getText().toString().trim();
                final String meaning3=etMeaning3.getText().toString().trim();
                final String meaning4=etMeaning4.getText().toString().trim();
                final String meaning5=etMeaning5.getText().toString().trim();
                Word objWord=new Word();
                objWord.setWord(word);

                for(int i=0 ; i<wordList.size() ; i++){
                    if(word.equalsIgnoreCase(oldWord)==false){
                        if(wordList.get(i).getWord().equalsIgnoreCase(word)){
                            setNegativeToastMessage("Bu kelime önceden eklenmiş.");
                            break;
                        }else{
                            if(wordList.size()==i+1){
                                wordList.get(index).setWord(word);
                                wordList.get(index).setMeaning1(meaning1);
                                wordList.get(index).setMeaning2(meaning2);
                                wordList.get(index).setMeaning3(meaning3);
                                wordList.get(index).setMeaning4(meaning4);
                                wordList.get(index).setMeaning5(meaning5);
                                wordApp.wordDao().updateWord(wordList.get(index));
                                setPositiveToastMessage("Güncelleme Tamamlandı");
                                dialog.dismiss();
                                mainDialog.dismiss();
                                wordList.clear();
                                materialSearch();
                                break;
                            }
                        }
                    }else{
                        wordList.get(index).setWord(word);
                        wordList.get(index).setMeaning1(meaning1);
                        wordList.get(index).setMeaning2(meaning2);
                        wordList.get(index).setMeaning3(meaning3);
                        wordList.get(index).setMeaning4(meaning4);
                        wordList.get(index).setMeaning5(meaning5);
                        wordApp.wordDao().updateWord(wordList.get(index));
                        setPositiveToastMessage("Güncelleme Tamamlandı");
                        dialog.dismiss();
                        mainDialog.dismiss();
                        wordList.clear();
                        materialSearch();
                        break;
                    }
                }
            }
        });
    }

    private void deleteWord(final int index, final AlertDialog mainDialog) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_confirm_dialog,null);
        TextView tvTitle=view.findViewById(R.id.tvTitle);
        Button btnCancel=view.findViewById(R.id.btnCancel);
        Button btnConfirm=view.findViewById(R.id.btnConfirm);
        tvTitle.setText(wordList.get(index).getWord() +" silinsin mi ?");
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordApp wordApp= Room.databaseBuilder(getContext(),WordApp.class,"Words")
                        .allowMainThreadQueries()
                        .build();
                wordApp.wordDao().deleteUser(wordList.get(index));
                setPositiveToastMessage("Kelime Silindi");
                //       startActivity(getIntent());
                wordList.clear();
                try{
                    materialSearch();
                }catch (Exception e){

                }
                dialog.dismiss();
                mainDialog.dismiss();
            }
        });
    }

    private void materialSearch() {
        checkWord();
        listSource=new String[wordList.size()];
        for(int i=0 ; i<wordList.size() ; i++){
            listSource[i]=wordList.get(i).getWord();
        }

        for(int i=0;i<listSource.length;i++){
            if(!listSource[i].equals(listSource[i].toLowerCase()))
                System.out.println("It contains uppercase char");
            listSource[i] = listSource[i].toLowerCase();
        }
        adapterFisrt=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,listSource);
        listView.setAdapter(adapterFisrt);


        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                ArrayAdapter adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,listSource);
                listView.setAdapter(adapter);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText !=null && !newText.isEmpty()){
                    List<String> listFound=new ArrayList<String>();
                    for(String item:listSource){
                        if(item.contains(newText))
                            listFound.add(item);
                    }

                    ArrayAdapter adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,listFound);
                    listView.setAdapter(adapter);
                }else {
                    // if search text is null
                    // return default
                    ArrayAdapter adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,listSource);
                    listView.setAdapter(adapter);
                }
                return true ;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        searchView.setMenuItem(menuItem);
        super.onCreateOptionsMenu(menu, inflater);
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

    private void checkWord() {
        WordApp wordApp= Room.databaseBuilder(getContext(),WordApp.class,"Words")
                .allowMainThreadQueries()
                .build();
        wordList=wordApp.wordDao().getAllWord();
        try{
            Comparator<Word> com=new ComImp();
            Collections.sort(wordList,com);
        }catch (Exception e){

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
