package kutum.kelime.kelimekutum.Adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kutum.kelime.kelimekutum.MainActivity;
import kutum.kelime.kelimekutum.MainScreen;
import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.R;

/**
 * Created by deniz on 22.2.2018.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.MyViewHolder> {

    Context context;
    private static List<Word> wordList;
    private TextToSpeech t1;
    public WordAdapter(MainActivity mainActivity, List<Word> wordList, TextToSpeech t1) {
        this.context=mainActivity;
        this.wordList=wordList;
        this.t1=t1;
    }

    public WordAdapter(Context context, List<Word> wordList, TextToSpeech t1) {
        this.context=context;
        this.wordList=wordList;
        this.t1=t1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_word,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvWord.setText(wordList.get(position).getWord());
        holder.tvMeaning1.setText("1."+wordList.get(position).getMeaning1());
            holder.tvMeaning2.setText("2."+wordList.get(position).getMeaning2());

            holder.tvMeaning3.setText("3."+wordList.get(position).getMeaning3());

            holder.tvMeaning4.setText("4."+wordList.get(position).getMeaning4());

            holder.tvMeaning5.setText("5."+wordList.get(position).getMeaning5());

        Log.e("gürkan", Integer.toString(position));
        holder.tvVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(wordList.get(position).getWord(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvWord,tvVoice ;
        private TextView tvMeaning1,tvMeaning2,tvMeaning3,tvMeaning4,tvMeaning5;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvWord=itemView.findViewById(R.id.tvWord);
            tvVoice=itemView.findViewById(R.id.tvVoice);
            tvMeaning1=itemView.findViewById(R.id.tvMeaning1);
            tvMeaning2=itemView.findViewById(R.id.tvMeaning2);
            tvMeaning3=itemView.findViewById(R.id.tvMeaning3);
            tvMeaning4=itemView.findViewById(R.id.tvMeaning4);
            tvMeaning5=itemView.findViewById(R.id.tvMeaning5);
        }
    }
}
