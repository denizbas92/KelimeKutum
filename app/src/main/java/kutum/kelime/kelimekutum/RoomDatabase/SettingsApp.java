package kutum.kelime.kelimekutum.RoomDatabase;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.annotation.VisibleForTesting;

import kutum.kelime.kelimekutum.InterfacePackage.SettingsDao;
import kutum.kelime.kelimekutum.Model.SettingsPost;

/**
 * Created by deniz on 26.2.2018.
 */

@Database(entities = {SettingsPost.class},version = 1,exportSchema = false)
public abstract class SettingsApp extends RoomDatabase {
    public abstract SettingsDao settingsDao();
}
