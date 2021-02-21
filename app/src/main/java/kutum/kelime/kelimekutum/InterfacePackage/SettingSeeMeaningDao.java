package kutum.kelime.kelimekutum.InterfacePackage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kutum.kelime.kelimekutum.Model.SeeMeaningPost;

/**
 * Created by deniz on 4.3.2018.
 */

@Dao
public interface SettingSeeMeaningDao {
    @Query("select * from SeeMeaningPost")
    List<SeeMeaningPost> getSeeMeaningSetting();

    @Insert
    void insertSettings(SeeMeaningPost... seeMeaningPosts);

    @Update
    void updateSettings(SeeMeaningPost seeMeaningPost);

    @Delete
    void deleteSettings(SeeMeaningPost seeMeaningPost);
}
