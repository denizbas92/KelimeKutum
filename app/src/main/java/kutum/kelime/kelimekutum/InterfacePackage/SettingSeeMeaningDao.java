package kutum.kelime.kelimekutum.InterfacePackage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
