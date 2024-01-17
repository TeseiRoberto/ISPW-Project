package com.rt.ispwproject.beans;

import java.time.LocalDate;

public class Duration {

    private LocalDate startDate;
    private LocalDate endDate;


    public Duration(LocalDate start, LocalDate end) throws IllegalArgumentException
    {
        setStartDate(start);
        setEndDate(end);
    }


    // Setters
    public void setStartDate(LocalDate date) throws IllegalArgumentException
    {
        if(date == null)
            throw new IllegalArgumentException("Start date cannot be empty!");

        if(endDate != null && date.isAfter(this.endDate))
            throw new IllegalArgumentException("Start date cannot be after the end date!");

        this.startDate = date;
    }

    public void setEndDate(LocalDate date) throws IllegalArgumentException
    {
        if(date == null)
            throw new IllegalArgumentException("End date cannot be empty!");

        if(startDate != null && date.isBefore(startDate))
            throw new IllegalArgumentException("End date cannot be before the start date!");

        this.endDate = date;
    }

    // Getters
    public LocalDate getStartDate()             { return this.startDate; }
    public LocalDate getEndDate()               { return this.endDate; }
}
