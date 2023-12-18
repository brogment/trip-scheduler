package com.example.d308_mobile_applications.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.d308_mobile_applications.dao.ExcursionDAO;
import com.example.d308_mobile_applications.dao.VacationDAO;
import com.example.d308_mobile_applications.entities.Excursion;
import com.example.d308_mobile_applications.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 9, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (VacationDatabaseBuilder.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context
                            .getApplicationContext()
                            ,VacationDatabaseBuilder.class, "MyVacationDatabase.db" )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
