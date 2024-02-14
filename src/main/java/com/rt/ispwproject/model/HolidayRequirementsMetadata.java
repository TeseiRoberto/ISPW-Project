package com.rt.ispwproject.model;

import java.time.LocalDate;

public class HolidayRequirementsMetadata {

    private final int           holidayId;
    private final Profile       requirementsOwner;
    private final LocalDate     dateOfPost;
    private int                 numOfViews;
    private boolean             satisfied;


    public HolidayRequirementsMetadata(int holidayId, Profile requirementsOwner, LocalDate dateOfPost, int numOfViews)
    {
        this.holidayId = holidayId;
        this.requirementsOwner = requirementsOwner;
        this.dateOfPost = dateOfPost;
        this.numOfViews = numOfViews;
        this.satisfied = false;
    }

    public HolidayRequirementsMetadata(Profile requirementsOwner, LocalDate dateOfPost)
    {
        this.holidayId = -1;
        this.requirementsOwner = requirementsOwner;
        this.dateOfPost = dateOfPost;
        this.numOfViews = 0;
        this.satisfied = false;
    }


    // Setters
    public void setNumOfViews(int numOfViews)       { this.numOfViews = numOfViews; }
    public void setSatisfied(boolean isSatisfied)   { this.satisfied = isSatisfied; }


    // Getters
    public int getHolidayId()               { return this.holidayId; }
    public Profile getOwner()               {return this.requirementsOwner; }
    public LocalDate getDateOfPost()        { return this.dateOfPost; }
    public int getNumOfViews()              { return this.numOfViews; }
    public boolean getSatisfied()           { return this.satisfied; }

}
