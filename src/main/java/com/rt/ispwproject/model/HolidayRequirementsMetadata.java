package com.rt.ispwproject.model;

import java.time.LocalDate;

public class HolidayRequirementsMetadata {

    private final int           requirementsId;
    private final Profile       requirementsOwner;
    private final LocalDate     dateOfPost;
    private int                 numOfViews;
    private int                 numOfOffersReceived;
    private boolean             isSatisfied;


    public HolidayRequirementsMetadata(int requirementsId, Profile requirementsOwner, LocalDate dateOfPost, int numOfOffersReceived, int numOfViews)
    {
        this.requirementsId = requirementsId;
        this.requirementsOwner = requirementsOwner;
        this.dateOfPost = dateOfPost;
        this.numOfViews = numOfViews;
        this.numOfOffersReceived = numOfOffersReceived;
        this.isSatisfied = false;
    }

    public HolidayRequirementsMetadata(Profile requirementsOwner, LocalDate dateOfPost)
    {
        this.requirementsId = -1;
        this.requirementsOwner = requirementsOwner;
        this.dateOfPost = dateOfPost;
        this.numOfViews = 0;
        this.numOfOffersReceived = 0;
        this.isSatisfied = false;
    }


    // Setters
    public void setNumOfViews(int num)              { this.numOfViews = num; }
    public void setNumOfOffersReceived(int num)     { this.numOfOffersReceived = num; }
    public void setSatisfied(boolean isSatisfied)   { this.isSatisfied = isSatisfied; }


    // Getters
    public int getRequirementsId()          { return this.requirementsId; }
    public Profile getRequirementsOwner()   { return this.requirementsOwner; }
    public LocalDate getDateOfPost()        { return this.dateOfPost; }
    public int getNumOfViews()              { return this.numOfViews; }
    public int getNumOfOffersReceived()     { return this.numOfOffersReceived; }
    public boolean isSatisfied()            { return this.isSatisfied; }

}
