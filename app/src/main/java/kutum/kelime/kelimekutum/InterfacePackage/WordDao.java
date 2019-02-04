package kutum.kelime.kelimekutum.InterfacePackage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import kutum.kelime.kelimekutum.Model.Word;

@Dao
public interface WordDao {
    @Query("select * from Word")
    List<Word> getAllWord();

    @Insert
    void insertWord(Word... words);

    @Update
    void updateWord(Word word);

    @Delete
    void deleteUser(Word word);
}
