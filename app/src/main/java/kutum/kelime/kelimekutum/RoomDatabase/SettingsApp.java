package kutum.kelime.kelimekutum.RoomDatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.VisibleForTesting;

import kutum.kelime.kelimekutum.InterfacePackage.SettingsDao;
import kutum.kelime.kelimekutum.Model.SettingsPost;

/**
 * Created by deniz on 26.2.2018.
 */

@Database(entities = {SettingsPost.class},version = 1,exportSchema = false)
public abstract class SettingsApp extends RoomDatabase {
    public abstract SettingsDao settingsDao();
}
