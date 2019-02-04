package kutum.kelime.kelimekutum.InterfacePackage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
