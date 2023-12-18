package com.example.d308_mobile_applications.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "vacations")
public class Vacation {

    // step 2 in instructions

    @PrimaryKey(autoGenerate = true)
    private int vacationID;

    private String vacationTitle;

    private String hotel;

    private Date vacationStartDate;

    private Date vacationEndDate;
    public Vacation(int vacationID, String vacationTitle, String hotel, Date vacationStartDate, Date vacationEndDate) {
        this.vacationID = vacationID;
        this.vacationTitle = vacationTitle;
        this.hotel = hotel;
        this.vacationStartDate = vacationStartDate;
        this.vacationEndDate = vacationEndDate;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public Date getVacationStartDate() {
        return vacationStartDate;
    }

    public void setVacationStartDate(Date vacationStartDate) {
        this.vacationStartDate = vacationStartDate;
    }

    public Date getVacationEndDate() {
        return vacationEndDate;
    }

    public void setVacationEndDate(Date vacationEndDate) {
        this.vacationEndDate = vacationEndDate;
    }
}
