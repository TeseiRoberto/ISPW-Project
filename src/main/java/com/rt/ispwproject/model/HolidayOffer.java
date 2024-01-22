package com.rt.ispwproject.model;

public class HolidayOffer extends Holiday {

    public HolidayOffer(HolidayMetadata metadata, DateRange duration, int price, Accommodation accommodation, Transport transport)
    {
        super(metadata, transport.getArrivalLocation(), price, duration, accommodation, transport);
    }


    // Setters
    public void setPrice(int price)     { this.price = price; }


    // Getters
    public int getPrice()               { return this.price; }

}
