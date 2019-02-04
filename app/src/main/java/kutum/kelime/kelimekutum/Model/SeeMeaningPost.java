package kutum.kelime.kelimekutum.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by deniz on 4.3.2018.
 */

@Entity
public class SeeMeaningPost {



    @PrimaryKey (autoGenerate = true)
    public int id;

    @ColumnInfo (name = "seeMeaningTestQuantity")
    private String seeMeaningTestQuantity;

    public SeeMeaningPost() {
    }

    public SeeMeaningPost(String seeMeaningTestQuantity) {
        this.seeMeaningTestQuantity = seeMeaningTestQuantity;
    }

    public String getSeeMeaningTestQuantity() {
        return seeMeaningTestQuantity;
    }

    public void setSeeMeaningTestQuantity(String seeMeaningTestQuantity) {
        this.seeMeaningTestQuantity = seeMeaningTestQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
