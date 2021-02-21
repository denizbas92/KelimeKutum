package kutum.kelime.kelimekutum.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kutum.kelime.kelimekutum.InterfacePackage.WordDao;
import kutum.kelime.kelimekutum.Model.Word;

/**
 * Created by deniz on 22.2.2018.
 */


@Database(entities = {Word.class},version = 1,exportSchema = false)
public abstract class WordApp extends RoomDatabase {
    public abstract WordDao wordDao();
}

