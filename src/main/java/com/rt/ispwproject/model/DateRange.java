package com.rt.ispwproject.model;

import java.time.LocalDate;

public class DateRange {

    private LocalDate startDate;
    private LocalDate endDate;


    public DateRange(LocalDate start, LocalDate end) throws IllegalArgumentException
    {
        setStartDate(start);
        setEndDate(end);
    }


    public void setStartDate(LocalDate date) throws IllegalArgumentException
    {
        if(date == null)
            throw new IllegalArgumentException("Start date cannot be empty!");

        if(this.endDate != null && date.isAfter(this.endDate))
            throw new IllegalArgumentException("Start date cannot be after the end date!");

        this.startDate = date;
    }

    public void setEndDate(LocalDate date) throws IllegalArgumentException
    {
        if(date == null)
            throw new IllegalArgumentException("End date cannot be empty!");

        if(this.startDate != null && date.isBefore(this.startDate))
            throw new IllegalArgumentException("End date cannot be before the start date!");

        this.endDate = date;
    }

    public LocalDate getStartDate()     { return this.startDate; }
    public LocalDate getEndDate()       { return this.endDate; }
}
