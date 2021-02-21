package kutum.kelime.kelimekutum.InterfacePackage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
