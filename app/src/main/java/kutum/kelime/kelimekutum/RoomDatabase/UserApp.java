package kutum.kelime.kelimekutum.RoomDatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import kutum.kelime.kelimekutum.InterfacePackage.UserDao;
import kutum.kelime.kelimekutum.Model.User;

/**
 * Created by deniz on 8.3.2018.
 */

@Database(entities = {User.class},version = 1,exportSchema = false)
public abstract class UserApp extends RoomDatabase {
    public abstract UserDao userDao();
}

