package kutum.kelime.kelimekutum.Adapter;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import java.util.List;

import kutum.kelime.kelimekutum.Constants.NodeName;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.R;
import kutum.kelime.kelimekutum.RoomDatabase.UserApp;
import kutum.kelime.kelimekutum.RoomDatabase.WordApp;
import kutum.kelime.kelimekutum.ShowCloudWords;
import kutum.kelime.kelimekutum.WordList;

/**
 * Created by deniz on 11.3.2018.
 */

public class FirebaseWordAdapter extends RecyclerView.Adapter<FirebaseWordAdapter.MyViewHolder> {

    Context context;
    private List<Word> wordList;
    private TextToSpeech t1;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private static String userName;

    public FirebaseWordAdapter(ShowCloudWords showCloudWords, String userName,List<Word> wordList, TextToSpeech t1) {
        this.context=showCloudWords;
        this.wordList=wordList;
        this.t1=t1;
        this.userName=userName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_firebase_word,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvWord.setText(wordList.get(position).getWord());
        holder.tvWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWord(position);
            }
        });
    }

    private void showWord(final int index) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.layout_show_word,null);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.layout_add_word,null);
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
                                Word word1=new Word();
                                word1.setWord(word);
                                word1.setId(wordList.get(index).getId());
                                word1.setMeaning1(meaning1);
                                word1.setMeaning2(meaning2);
                                word1.setMeaning3(meaning3);
                                word1.setMeaning4(meaning4);
                                word1.setMeaning5(meaning5);
                                databaseReference.child(NodeName.USERS).child(userName).child(NodeName.WORDS).child(oldWord).setValue(word1);

                                final DatabaseReference dbFrom=databaseReference.child(NodeName.USERS).child(userName).child(NodeName.WORDS).child(oldWord);
                                final DatabaseReference dbTo=databaseReference.child(NodeName.USERS).child(userName).child(NodeName.WORDS).child(word);

                                setPositiveToastMessage("Güncelleme Tamamlandı");
                                dialog.dismiss();
                                mainDialog.dismiss();

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
                                                            context.startActivity(new Intent(context,ShowCloudWords.class));
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
                                break;
                            }
                        }
                    }else{
                        Word word1=new Word();
                        word1.setWord(word);
                        word1.setId(wordList.get(index).getId());
                        word1.setMeaning1(meaning1);
                        word1.setMeaning2(meaning2);
                        word1.setMeaning3(meaning3);
                        word1.setMeaning4(meaning4);
                        word1.setMeaning5(meaning5);
                        databaseReference.child(NodeName.USERS).child(userName).child(NodeName.WORDS).child(word).setValue(word1);
                        setPositiveToastMessage("Güncelleme Tamamlandı");
                        dialog.dismiss();
                        mainDialog.dismiss();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                context.startActivity(new Intent(context,ShowCloudWords.class));
                            }
                        },1000);
                        break;
                    }
                }
            }
        });
    }

    private void deleteWord(final int index, final AlertDialog mainDialog) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.layout_confirm_dialog,null);
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
                databaseReference.child(NodeName.USERS).child(userName).child(NodeName.WORDS).child(wordList.get(index).getWord()).removeValue();
                setPositiveToastMessage("Kelime Silindi");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.startActivity(new Intent(context,ShowCloudWords.class));
                    }
                },1000);
                dialog.dismiss();
                mainDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    private void setNegativeToastMessage(String message) {
        StyleableToast st=new StyleableToast(context,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#ff0000"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void setPositiveToastMessage(String message) {
        StyleableToast st=new StyleableToast(context,message, Toast.LENGTH_SHORT);
        st.setBackgroundColor(Color.parseColor("#0000ff"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvWord;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvWord=itemView.findViewById(R.id.tvWord);
        }
    }
}
