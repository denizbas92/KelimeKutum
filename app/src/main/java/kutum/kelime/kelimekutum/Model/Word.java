package kutum.kelime.kelimekutum.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by deniz on 22.2.2018.
 */
@Entity
public class Word {



    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "meaning1")
    private String meaning1;

    @ColumnInfo(name = "meaning2")
    private String meaning2;

    @ColumnInfo(name = "meaning3")
    private String meaning3;

    @ColumnInfo(name = "meaning4")
    private String meaning4;

    @ColumnInfo(name = "meaning5")
    private String meaning5;

    public Word() {
    }

    public Word(String word, String meaning1, String meaning2, String meaning3, String meaning4, String meaning5) {
        this.word = word;
        this.meaning1 = meaning1;
        this.meaning2 = meaning2;
        this.meaning3 = meaning3;
        this.meaning4 = meaning4;
        this.meaning5 = meaning5;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning1() {
        return meaning1;
    }

    public void setMeaning1(String meaning1) {
        this.meaning1 = meaning1;
    }

    public String getMeaning2() {
        return meaning2;
    }

    public void setMeaning2(String meaning2) {
        this.meaning2 = meaning2;
    }

    public String getMeaning3() {
        return meaning3;
    }

    public void setMeaning3(String meaning3) {
        this.meaning3 = meaning3;
    }

    public String getMeaning4() {
        return meaning4;
    }

    public void setMeaning4(String meaning4) {
        this.meaning4 = meaning4;
    }

    public String getMeaning5() {
        return meaning5;
    }

    public void setMeaning5(String meaning5) {
        this.meaning5 = meaning5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
