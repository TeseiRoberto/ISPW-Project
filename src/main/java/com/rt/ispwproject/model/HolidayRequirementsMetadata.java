package com.rt.ispwproject.model;

import java.time.LocalDate;

public class HolidayRequirementsMetadata {

    private int         holidayId;
    private String      ownerUsername;
    private int         ownerId;
    private LocalDate   dateOfPost;
    private int         numOfViews;
    private boolean     satisfied;


    public HolidayRequirementsMetadata(int holidayId, String ownerUsername, int ownerId, LocalDate dateOfPost, int numOfViews)
    {
        this.holidayId = holidayId;
        this.ownerUsername = ownerUsername;
        this.ownerId = ownerId;
        this.dateOfPost = dateOfPost;
        this.numOfViews = numOfViews;
        this.satisfied = false;
    }


    public HolidayRequirementsMetadata(String ownerUsername, int ownerId, LocalDate dateOfPost)
    {
        this.holidayId = 0;
        this.ownerUsername = ownerUsername;
        this.ownerId = ownerId;
        this.dateOfPost = dateOfPost;
        this.numOfViews = 0;
        this.satisfied = false;
    }


    // Setters
    public void setHolidayId(int id)                { this.holidayId = id; }
    public void setOwnerUsername(String username)   { this.ownerUsername = username; }
    public void setOwnerId(int id)                  { this.ownerId = id; }
    public void setDateOfPost(LocalDate date)       { this.dateOfPost = date; }
    public void setNumOfViews(int numOfViews)       { this.numOfViews = numOfViews; }
    public void setSatisfied(boolean isSatisfied)   { this.satisfied = isSatisfied; }


    // Getters
    public int getHolidayId()               { return this.holidayId; }
    public String getOwnerUsername()        { return this.ownerUsername; }
    public int getOwnerId()                 { return this.ownerId; }
    public LocalDate getDateOfPost()        { return this.dateOfPost; }
    public int getNumOfViews()              { return this.numOfViews; }
    public boolean getSatisfied()           { return this.satisfied; }

}
