package com.rt.ispwproject.beans;

import java.time.LocalDate;

public class Duration {

    private LocalDate departureDate;
    private LocalDate returnDate;


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
}
