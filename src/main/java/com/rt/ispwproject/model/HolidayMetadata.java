package com.rt.ispwproject.model;

import java.time.LocalDate;

public class HolidayMetadata {

    private int holidayId;
    private String ownerUsername;
    private LocalDate dateOfPost;
    private int numOfViews;
    private boolean satisfied;


    public HolidayMetadata(int holidayId, String ownerUsername, LocalDate dateOfPost, int numOfViews)
    {
        this.holidayId = holidayId;
        this.ownerUsername = ownerUsername;
        this.dateOfPost = dateOfPost;
        this.numOfViews = numOfViews;
        this.satisfied = false;
    }


    // Setters
    public void setHolidayId(int id)                { this.holidayId = id; }
    public void setOwnerUsername(String username)   { this.ownerUsername = username; }
    public void setDateOfPost(LocalDate date)       { this.dateOfPost = date; }
    public void setNumOfViews(int numOfViews)       { this.numOfViews = numOfViews; }
    public void setSatisfied(boolean isSatisfied)   { this.satisfied = isSatisfied; }


    // Getters
    public int getHolidayId()               { return this.holidayId; }
    public String getOwnerUsername()        { return this.ownerUsername; }
    public LocalDate getDateOfPost()        { return this.dateOfPost; }
    public int getNumOfViews()              { return this.numOfViews; }
    public boolean getSatisfied()           { return this.satisfied; }

}
