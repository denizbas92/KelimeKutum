package kutum.kelime.kelimekutum.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by deniz on 8.3.2018.
 */
@Entity
public class User {



    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "userName")
    private String userName;
    @ColumnInfo(name = "password")
    private String password;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
