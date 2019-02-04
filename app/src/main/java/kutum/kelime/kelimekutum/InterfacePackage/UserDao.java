package kutum.kelime.kelimekutum.InterfacePackage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import kutum.kelime.kelimekutum.Model.User;

/**
 * Created by deniz on 8.3.2018.
 */
@Dao
public interface UserDao {
    @Query("select * from User")
    List<User> getUserInfo();

    @Insert
    void insertUser(User... users);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
