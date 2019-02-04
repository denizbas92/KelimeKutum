package kutum.kelime.kelimekutum.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import kutum.kelime.kelimekutum.InterfacePackage.SettingSeeMeaningDao;
import kutum.kelime.kelimekutum.Model.SeeMeaningPost;

/**
 * Created by deniz on 4.3.2018.
 */

@Database(entities = {SeeMeaningPost.class},version = 1,exportSchema = false)
public abstract class SettingSeeMeaningApp extends RoomDatabase {
    public abstract SettingSeeMeaningDao settingSeeMeaningDao();
}
