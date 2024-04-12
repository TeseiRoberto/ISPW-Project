package com.rt.ispwproject.model;

import java.time.LocalDate;
import java.time.Period;

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
            throw new IllegalArgumentException("A start date must be specified");

        if(this.endDate != null && date.isAfter(this.endDate))
            throw new IllegalArgumentException("The start date cannot be after the end date");

        this.startDate = date;
    }

    public void setEndDate(LocalDate date) throws IllegalArgumentException
    {
        if(date == null)
            throw new IllegalArgumentException("An end date must be specified");

        if(this.startDate != null && date.isBefore(this.startDate))
            throw new IllegalArgumentException("The End date cannot be before the start date");

        this.endDate = date;
    }

    public LocalDate getStartDate()     { return this.startDate; }
    public LocalDate getEndDate()       { return this.endDate; }

    // Returns the number of nights between the start and the end date.
    public int getNightsBetween()
    {
        return Period.between(this.startDate, this.endDate).getDays();
    }


    // Returns true if the start date and the end date of this instance are the same of the given instance
    public boolean isEqual(DateRange other)
    {
        return this.startDate.isEqual(other.startDate) && this.endDate.isEqual(other.endDate);
    }
}
