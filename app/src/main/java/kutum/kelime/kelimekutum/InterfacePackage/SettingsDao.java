package kutum.kelime.kelimekutum.InterfacePackage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kutum.kelime.kelimekutum.Model.SettingsPost;

/**
 * Created by deniz on 26.2.2018.
 */

@Dao
public interface SettingsDao {
    @Query("select * from SettingsPost")
    List<SettingsPost> getSettings();

    @Insert
    void insertSettings(SettingsPost... settingsPosts);

    @Update
    void updateSettings(SettingsPost settingsPost);

    @Delete
    void deleteSettings(SettingsPost settingsPost);
}
