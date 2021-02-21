package kutum.kelime.kelimekutum.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by deniz on 4.3.2018.
 */

@Entity
public class SeeMeaningPost {



    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "seeMeaningTestQuantity")
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
