package kutum.kelime.kelimekutum.select_word_practice;

import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.List;

import kutum.kelime.kelimekutum.Model.Word;
import kutum.kelime.kelimekutum.R;
import kutum.kelime.kelimekutum.WordList;

public class SelectWordPracticeAdapter extends RecyclerView.Adapter<SelectWordPracticeAdapter.MyViewHolder> {

    private Context context ;
    private List<Word> listWords ;
    private List<Word> selectedWords;
    private static int counter =0;
    OnGetCounter onGetCounter ;
    public SelectWordPracticeAdapter(Context context, List<Word> wordList, SelectWordList selectWordList) {
        this.context = context ;
        this.listWords = wordList ;
        onGetCounter = selectWordList ;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        selectedWords = new ArrayList<>();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_word_design,null);
        return new MyViewHolder(view);
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        SelectWordPracticeAdapter.counter = counter;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvWord.setText(listWords.get(position).getWord());

        if(selectedWords.contains(listWords.get(position))) {
            holder.cardWord.setCardBackgroundColor(Color.GREEN);
        } else {
            holder.cardWord.setCardBackgroundColor(Color.parseColor("#f4cbcb"));
        }
        holder.cardWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedWords.contains(listWords.get(position)) && selectedWords.size() > 0) {
                    holder.cardWord.setCardBackgroundColor(Color.parseColor("#f4cbcb"));
                    selectedWords.remove(listWords.get(position));
                    counter--;
                   onGetCounter.getCounter(counter);
                } else {
                    counter++;
                    holder.cardWord.setCardBackgroundColor(Color.GREEN);
                    selectedWords.add(listWords.get(position));
                    onGetCounter.getCounter(counter);
                }
            }
        });
    }

    private void setNegativeToastMessage(String message) {
        StyleableToast st=new StyleableToast(context,message, 1000);
        st.setBackgroundColor(Color.parseColor("#ff0000"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    private void setPositiveToastMessage(String message) {
        StyleableToast st=new StyleableToast(context,message, 1000);
        st.setBackgroundColor(Color.parseColor("#0000ff"));
        st.setTextColor(Color.WHITE);
        st.setCornerRadius(2);
        st.show();
    }

    @Override
    public int getItemCount() {
        return listWords.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord ;
        CardView cardWord;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardWord = itemView.findViewById(R.id.cardWord);
            tvWord = itemView.findViewById(R.id.tvWord);
        }
    }
}
