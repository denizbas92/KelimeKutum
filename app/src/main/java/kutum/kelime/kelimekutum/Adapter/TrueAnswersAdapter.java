package kutum.kelime.kelimekutum.Adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kutum.kelime.kelimekutum.Model.TrueFalseAnswerPost;
import kutum.kelime.kelimekutum.R;

/**
 * Created by deniz on 5.3.2018.
 */

public class TrueAnswersAdapter extends RecyclerView.Adapter<TrueAnswersAdapter.MyViewHolder> {

    Context context;
    private static List<TrueFalseAnswerPost> trueFalseAnswerPostList;
    private TextToSpeech t1;

    public TrueAnswersAdapter(Context context, List<TrueFalseAnswerPost> trueFalseAnswerPostList, TextToSpeech t1) {
        this.context=context;
        this.trueFalseAnswerPostList=trueFalseAnswerPostList;
        this.t1=t1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_true_false_answers,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvWord.setText(trueFalseAnswerPostList.get(position).getWord());
        holder.tvMeaning1.setText("1."+trueFalseAnswerPostList.get(position).getMeaning1());
        holder.tvMeaning2.setText("2."+trueFalseAnswerPostList.get(position).getMeaning2());

        holder.tvMeaning3.setText("3."+trueFalseAnswerPostList.get(position).getMeaning3());

        holder.tvMeaning4.setText("4."+trueFalseAnswerPostList.get(position).getMeaning4());

        holder.tvMeaning5.setText("5."+trueFalseAnswerPostList.get(position).getMeaning5());

        Log.e("gürkan", Integer.toString(position));
        holder.tvVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(trueFalseAnswerPostList.get(position).getWord(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trueFalseAnswerPostList.size();
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
