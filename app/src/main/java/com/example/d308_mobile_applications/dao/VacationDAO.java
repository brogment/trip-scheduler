package com.example.d308_mobile_applications.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308_mobile_applications.entities.Vacation;
import java.util.List;

@Dao
public interface VacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM VACATIONS ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM VACATIONS WHERE vacationID = :vacationId")
    Vacation getVacationByID(int vacationId);

    @Query("SELECT * FROM VACATIONS WHERE vacationTitle LIKE '%' || :query || '%'")
    List<Vacation> searchVacations(String query);

}
