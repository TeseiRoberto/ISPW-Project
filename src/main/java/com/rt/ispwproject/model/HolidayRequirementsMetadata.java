package com.rt.ispwproject.model;

import java.time.LocalDate;

public class HolidayRequirementsMetadata {

    private final int           requirementsId;
    private final Profile       requirementsOwner;
    private final LocalDate     dateOfPost;
    private int                 numOfViews;
    private int                 numOfOffersReceived;


    public HolidayRequirementsMetadata(Profile requirementsOwner, LocalDate dateOfPost) throws IllegalArgumentException
    {
        this(0, requirementsOwner, dateOfPost, 0, 0);
    }


    public HolidayRequirementsMetadata(int requirementsId, Profile requirementsOwner, LocalDate dateOfPost, int numOfOffersReceived, int numOfViews) throws IllegalArgumentException
    {
        if(requirementsOwner == null)
            throw new IllegalArgumentException("Holiday requirements owner must be specified");

        if(dateOfPost == null)
            throw new IllegalArgumentException("Date of post must be specified");

        this.requirementsId = requirementsId;
        this.requirementsOwner = requirementsOwner;
        this.dateOfPost = dateOfPost;
        this.numOfViews = numOfViews;
        this.numOfOffersReceived = numOfOffersReceived;
    }


    // Getters
    public int getRequirementsId()          { return this.requirementsId; }
    public Profile getRequirementsOwner()   { return this.requirementsOwner; }
    public LocalDate getDateOfPost()        { return this.dateOfPost; }
    public int getNumOfViews()              { return this.numOfViews; }
    public int getNumOfOffersReceived()     { return this.numOfOffersReceived; }

}
