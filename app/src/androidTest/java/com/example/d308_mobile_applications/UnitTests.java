package com.example.d308_mobile_applications;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.d308_mobile_applications.dao.ExcursionDAO;
import com.example.d308_mobile_applications.dao.VacationDAO;
import com.example.d308_mobile_applications.database.DateConverter;
import com.example.d308_mobile_applications.database.VacationDatabaseBuilder;
import com.example.d308_mobile_applications.entities.Excursion;
import com.example.d308_mobile_applications.entities.Vacation;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UnitTests {
    private VacationDatabaseBuilder db;
    private VacationDAO vacationDAO;
    private ExcursionDAO excursionDAO;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, VacationDatabaseBuilder.class)
                .allowMainThreadQueries()
                .build();
        vacationDAO = db.vacationDAO();
        excursionDAO = db.excursionDAO();
    }

    @After
    public void closeDB() {
        db.close();
    }

    @Test
    public void testVacationDetails() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 1);
        Date startDate = calendar.getTime();
        calendar.set(2023, Calendar.JANUARY, 15);
        Date endDate = calendar.getTime();

        Vacation vacation = new Vacation(0, "Italy", "Generator Hostel", startDate, endDate);
        vacationDAO.insert(vacation);
        List<Vacation> vacations = vacationDAO.getAllVacations();

        assertFalse("Vacation list is populated", vacations.isEmpty());
        assertEquals("Vacation ID match", 1, vacations.get(0).getVacationID());
        assertEquals("Vacation title match", "Italy", vacations.get(0).getVacationTitle());
        assertEquals("Vacation hotel match", "Generator Hostel", vacations.get(0).getHotel());
        assertEquals("Vacation start date match", startDate, vacations.get(0).getVacationStartDate());
        assertEquals("Vacation end date match", endDate, vacations.get(0).getVacationEndDate());
    }

    @Test
    public void testExcursionDetails() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 8);
        Date date = calendar.getTime();

        Excursion excursion = new Excursion(0,"Colosseum", date, 0);
        excursionDAO.insert(excursion);
        List<Excursion> excursions = excursionDAO.getAllExcursions();

        assertFalse("Excursion list is populated", excursions.isEmpty());
        assertEquals("Excursion ID match", 1, excursions.get(0).getExcursionID());
        assertEquals("Excursion name match", "Colosseum", excursions.get(0).getExcursionName());
        assertEquals("Excursion vacation ID match", 0, excursions.get(0).getVacationID());
    }

    @Test
    public void testDeleteVacation(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 1);
        Date startDate = calendar.getTime();
        calendar.set(2023, Calendar.JANUARY, 15);
        Date endDate = calendar.getTime();

        Vacation vacation = new Vacation(0, "Italy", "Generator Hostel", startDate, endDate);
        vacationDAO.insert(vacation);
        List<Vacation> vacations = vacationDAO.getAllVacations();

        vacation.setVacationID(vacations.get(0).getVacationID());
        vacationDAO.delete(vacation);
        vacations = vacationDAO.getAllVacations();

        assertTrue("Vacation list is empty", vacations.isEmpty());
    }

    @Test
    public void testDeleteExcursion(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 8);
        Date date = calendar.getTime();

        Excursion excursion = new Excursion(0,"Colosseum", date, 0);
        excursionDAO.insert(excursion);
        List<Excursion> excursions = excursionDAO.getAllExcursions();

        excursion.setExcursionID(excursions.get(0).getExcursionID());
        excursionDAO.delete(excursion);
        excursions = excursionDAO.getAllExcursions();

        assertTrue("Excursion list is empty", excursions.isEmpty());
    }

    @Test
    public void testToDate(){
        long timestamp = 1672531200000L;

        Date correctDate = new Date(timestamp);
        Date convertedDate = DateConverter.toDate(timestamp);

        assertEquals("Dates should match", convertedDate, correctDate);
    }

    @Test
    public void testToTimestamp(){
        Date date = new Date(1672531200000L);

        Long correctTimestamp= 1672531200000L;
        Long convertedTimestamp = DateConverter.toTimestamp(date);

        assertEquals("Dates should match", convertedTimestamp, correctTimestamp);
    }

    @Test
    public void testUpdateVacation() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 1);
        Date startDate = calendar.getTime();
        calendar.set(2023, Calendar.JANUARY, 15);
        Date endDate = calendar.getTime();

        Vacation vacation = new Vacation(0, "Italy", "Generator Hostel", startDate, endDate);
        vacationDAO.insert(vacation);

        Vacation updatedVacation = vacationDAO.getAllVacations().get(0);
        updatedVacation.setVacationTitle("Spain");
        updatedVacation.setHotel("La Casa");

        vacationDAO.update(updatedVacation);
        Vacation endstateVacation = vacationDAO.getAllVacations().get(0);

        assertEquals("Vacation title match", "Spain", endstateVacation.getVacationTitle());
        assertEquals("Hotel name match", "La Casa", endstateVacation.getHotel());
    }

    @Test
    public void testUpdateExcursion() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 8);
        Date date = calendar.getTime();

        Excursion excursion = new Excursion(0, "Colosseum", date, 0);
        excursionDAO.insert(excursion);

        Excursion updatedExcursion = excursionDAO.getAllExcursions().get(0);
        updatedExcursion.setExcursionName("Spanish Steps");

        excursionDAO.update(updatedExcursion);
        Excursion endstateExcursion = excursionDAO.getAllExcursions().get(0);

        assertEquals("Excursion name match", "Spanish Steps", endstateExcursion.getExcursionName());
    }

    @Test
    public void testSearchExcursions() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 8);
        Date date = calendar.getTime();

        Excursion excursion1 = new Excursion(0, "Colosseum", date, 0);
        Excursion excursion2 = new Excursion(0, "Statue of David", date, 0);
        Excursion excursion3 = new Excursion(0, "Pompeii", date, 0);
        excursionDAO.insert(excursion1);
        excursionDAO.insert(excursion2);
        excursionDAO.insert(excursion3);

        List<Excursion> excursionSearchResults = excursionDAO.searchExcursions("David", 0);

        assertEquals("Size should be one", 1, excursionSearchResults.size());
        assertEquals("Excursion name match", "Statue of David", excursionSearchResults.get(0).getExcursionName());
    }

    @Test
    public void testSearchVacations() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 1);
        Date startDate = calendar.getTime();
        calendar.set(2023, Calendar.JANUARY, 15);
        Date endDate = calendar.getTime();

        Vacation vacation1 = new Vacation(0, "Trip to Italy", "Generator Hostel", startDate, endDate);
        Vacation vacation2 = new Vacation(0, "Trip to France", "Paris Hilton", startDate, endDate);
        Vacation vacation3 = new Vacation(0, "Trip to Spain", "La Casa", startDate, endDate);
        vacationDAO.insert(vacation1);
        vacationDAO.insert(vacation2);
        vacationDAO.insert(vacation3);

        List<Vacation> vacationSearchResults = vacationDAO.searchVacations("to France");

        assertEquals("Size should be one", 1, vacationSearchResults.size());
        assertEquals("Vacation title match", "Trip to France", vacationSearchResults.get(0).getVacationTitle());
    }
}