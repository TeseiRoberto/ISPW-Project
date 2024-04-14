package com.rt.ispwproject.beans;

import java.time.LocalDate;
import java.time.Period;

public class Duration {

    private LocalDate departureDate;
    private LocalDate returnDate;


    public Duration()
    {
        this.departureDate = null;
        this.returnDate = null;
    }


    public Duration(LocalDate start, LocalDate end) throws IllegalArgumentException
    {
        setDepartureDate(start);
        setReturnDate(end);
    }


    // Setters
    public void setDepartureDate(LocalDate date) throws IllegalArgumentException
    {
        if(date == null)
            throw new IllegalArgumentException("Departure date cannot be empty!");

        if(returnDate != null && date.isAfter(this.returnDate))
            throw new IllegalArgumentException("Departure date cannot be after the return date!");

        this.departureDate = date;
    }

    public void setReturnDate(LocalDate date) throws IllegalArgumentException
    {
        if(date == null)
            throw new IllegalArgumentException("Return date cannot be empty!");

        if(departureDate != null && date.isBefore(departureDate))
            throw new IllegalArgumentException("Return date cannot be before the departure date!");

        this.returnDate = date;
    }

    // Getters
    public LocalDate getDepartureDate()         { return this.departureDate; }
    public LocalDate getReturnDate()            { return this.returnDate; }


    // Returns the number of days between the departure and return dates
    public int getDurationInDays()
    {
        if(departureDate == null || returnDate == null)
            return 0;

        return Period.between(departureDate, returnDate).getDays();
    }
}
