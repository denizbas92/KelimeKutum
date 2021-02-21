package kutum.kelime.kelimekutum.InterfacePackage;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
