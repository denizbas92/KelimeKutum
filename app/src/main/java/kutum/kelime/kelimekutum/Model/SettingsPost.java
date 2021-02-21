package kutum.kelime.kelimekutum.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by deniz on 26.2.2018.
 */
@Entity
public class SettingsPost {



    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "trueFalse")
    private String trueFalse;

    @ColumnInfo(name = "multipleChoice")
    private String multipleChoice;

    @ColumnInfo(name = "listenSelectMeaning")
    private String listenSelectMeaning;

    @ColumnInfo(name = "listenSelectWord")
    private String listenSelectWord;

    @ColumnInfo(name = "listenWriteWord")
    private String listenWriteWord;

    public SettingsPost() {
    }

    public SettingsPost(String trueFalse, String multipleChoice, String listenSelectMeaning, String listenSelectWord, String listenWriteWord ) {
        this.trueFalse = trueFalse;
        this.multipleChoice = multipleChoice;
        this.listenSelectMeaning = listenSelectMeaning;
        this.listenSelectWord = listenSelectWord;
        this.listenWriteWord = listenWriteWord;
    }

    public String getTrueFalse() {
        return trueFalse;
    }

    public void setTrueFalse(String trueFalse) {
        this.trueFalse = trueFalse;
    }

    public String getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(String multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public String getListenSelectMeaning() {
        return listenSelectMeaning;
    }

    public void setListenSelectMeaning(String listenSelectMeaning) {
        this.listenSelectMeaning = listenSelectMeaning;
    }

    public String getListenSelectWord() {
        return listenSelectWord;
    }

    public void setListenSelectWord(String listenSelectWord) {
        this.listenSelectWord = listenSelectWord;
    }

    public String getListenWriteWord() {
        return listenWriteWord;
    }

    public void setListenWriteWord(String listenWriteWord) {
        this.listenWriteWord = listenWriteWord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
