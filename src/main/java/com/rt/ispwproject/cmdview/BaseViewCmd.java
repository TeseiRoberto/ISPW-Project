package com.rt.ispwproject.cmdview;

import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Offer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;


// This class defines some common behaviour for the command line view classes
public abstract class BaseViewCmd {

    protected static final String       LOGIN_SCREEN_NAME = "My Holiday";

    // Titles of the screens used by the simple user
    protected static final String       MY_ANNOUNCEMENTS_SCREEN_NAME = "My announcements";
    protected static final String       CREATE_ANNOUNCEMENT_SCREEN_NAME = "Create announcement";
    protected static final String       ANNOUNCEMENT_DETAILS_SCREEN_NAME = "Announcement details";
    protected static final String       REQUEST_CHANGES_SCREEN_NAME = "Request changes";

    // Titles of the screens used by the travel agency
    protected static final String       SEARCH_ANNOUNCEMENTS_SCREEN_NAME = "Search announcements";
    protected static final String       MAKE_OFFER_SCREEN_NAME = "Make offer";
    protected static final String       MAKE_COUNTEROFFER_SCREEN_NAME = "Make counteroffer";
    protected static final String       MY_OFFERS_SCREEN_NAME = "My offers";
    protected static final String       OFFER_DETAILS_SCREEN_NAME = "Offer details";

    // Useful ansi codes
    private static final String         SET_WHITE_TEXT = "\033[97m";
    private static final String         SET_GREEN_TEXT = "\033[92m";
    private static final String         SET_YELLOW_TEXT = "\033[93m";
    private static final String         SET_RED_TEXT = "\033[91m";

    // Generic messages used to communicate with the user
    public static final String          INVALID_OPTION_MSG = "Selected option is not valid";
    public static final String          UNKNOWN_STRING = "unknown";

    // Map used to associate user input to the announcement fields
    public static final Map<Integer, String> ANNOUNCEMENT_FIELDS = Map.ofEntries(
            Map.entry(1, "holiday description"),
            Map.entry(2, "destination"),
            Map.entry(3, "available budget"),
            Map.entry(4, "departure date"),
            Map.entry(5, "return date"),
            Map.entry(6, "accommodation type"),
            Map.entry(7, "accommodation quality"),
            Map.entry(8, "number of rooms"),
            Map.entry(9, "transport type"),
            Map.entry(10, "transport quality"),
            Map.entry(11, "departure location"),
            Map.entry(12, "number of travelers")
    );

    // Map used to associate user input to the offer fields
    public static final Map<Integer, String> OFFER_FIELDS = Map.ofEntries(
            Map.entry(1, "destination"),
            Map.entry(2, "departure date"),
            Map.entry(3, "return date"),
            Map.entry(4, "price"),
            Map.entry(5, "accommodation type"),
            Map.entry(6, "accommodation name"),
            Map.entry(7, "accommodation quality"),
            Map.entry(8, "accommodation address"),
            Map.entry(9, "number of rooms"),
            Map.entry(10, "transport type"),
            Map.entry(11, "transport company name"),
            Map.entry(12, "transport quality"),
            Map.entry(13, "departure location"),
            Map.entry(14, "number of travelers")
    );

    private static final BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );


    public abstract void showScreenTitle();


    // ========================[ Simple output methods ]========================

    // Prints the given string to system out
    public void print(String str)
    {
        System.out.print(str);
    }


    // Prints the given formatted string to system out
    public void printf(String fmt, Object ... obj)
    {
        System.out.printf(fmt, obj);
    }


    // Prints the given strings as a list
    // @numbered: if true then a number is displayed before each element of the list
    public void printList(List<String> strings, boolean numbered)
    {
        if(numbered)
            for(int i = 0; i < strings.size(); i++)
                print(i + 1 + "] " + strings.get(i) + "\n");
        else
            for (String string : strings)
                print(string + "\n");
    }


    // ========================[ Complex output methods ]========================


    // Displays the given error message
    public void showErrorDialog(String msg)
    {
        print(SET_RED_TEXT + "[ERROR] " + msg + "\n" + SET_WHITE_TEXT);
    }


    // Displays the given info message
    public void showInfoDialog(String msg)
    {
        print(SET_GREEN_TEXT + "[ERROR] " + msg + "\n" + SET_WHITE_TEXT);
    }


    // Displays the given message and waits for the user to enter (Y/y) or (N/n)
    // Returns true if user inserted Y/y, false if N/n was inserted
    public boolean showConfirmDialog(String msg)
    {
        int res = -1;
        print(msg + ' ');

        while(res != 0 && res != 1)
        {
            print("Enter Y for yes, N for no: ");
            String in = getStringFromUser();

            if(!in.isBlank())
            {
                in = in.toLowerCase();
                res = switch(in.charAt(0))
                {
                    case 'y' -> 1;
                    case 'n' -> 0;
                    default -> -1;
                };
            }
        }

        return res != 0;
    }


    // Displays the given string in between 2 lines made with the character *
    public void printTitle(String title)
    {
        int len = title.length() + 10;
        String sep = "*";

        // Build the separator line
        StringBuilder separatorLine = new StringBuilder();
        separatorLine.append(sep.repeat(len));
        separatorLine.append('\n');

        // Build the title line
        StringBuilder titleLine = new StringBuilder();
        titleLine.append(" ".repeat(len));

        int j = (len / 2) - (title.length() / 2);
        for(int i = 0; i < title.length(); i++)
        {
            titleLine.setCharAt(j, title.charAt(i));
            j++;
        }

        titleLine.setCharAt(0, sep.charAt(0));
        titleLine.setCharAt(len - 1, sep.charAt(0));
        titleLine.append('\n');

        print(SET_YELLOW_TEXT);
        print(separatorLine.toString());
        print(titleLine.toString());
        print(separatorLine.toString());
        print(SET_WHITE_TEXT);
    }


    // Displays the given string in a line like "=====[ subtitle ]===="
    public void printSubtitle(String subtitle)
    {
        printf("%s=======[ %s ]=======%s\n", SET_YELLOW_TEXT, subtitle, SET_WHITE_TEXT);
    }


    // Displays some info for each announcement in the given list
    public void showAnnouncementsList(List<Announcement> announcements, boolean showOwnerUsername)
    {
        for(int i = 0; i < announcements.size(); i++)
        {
            Announcement a = announcements.get(i);
            printf("%d] %s - %s - %s - %s - %d views ", i + 1,
                    a.getDestination(),
                    a.getHolidayDuration().getDepartureDate().toString(),
                    a.getHolidayDuration().getReturnDate().toString(),
                    a.getAvailableBudgetAsStr(),
                    a.getNumOfViews()
            );

            if(showOwnerUsername)
                printf("posted by: %s\n", a.getOwnerUsername());
            else
                print("\n");
        }
    }


    // Displays all the details of the given announcement
    // @numbered: if true then a number is displayed before each field of the announcement
    public void showAnnouncementDetails(Announcement a,  boolean showMetadata, boolean numberedFields)
    {
        if(showMetadata)
            printf("Announcement posted on: %s by: %s\n", a.getDateOfPost() == null ? UNKNOWN_STRING : a.getDateOfPost().toString(),
                    a.getOwnerUsername() == null ? UNKNOWN_STRING : a.getOwnerUsername());

        List<String> fields = List.of(
                ANNOUNCEMENT_FIELDS.get(1) + ": " + a.getHolidayDescription(),
                ANNOUNCEMENT_FIELDS.get(2) + ": " + ( a.getDestination().isBlank() ? UNKNOWN_STRING : a.getDestination() ),
                ANNOUNCEMENT_FIELDS.get(3) + ": " + a.getAvailableBudgetAsStr(),
                ANNOUNCEMENT_FIELDS.get(4) + ": " + ( a.getHolidayDuration().getDepartureDate() == null ? UNKNOWN_STRING : a.getHolidayDuration().getDepartureDate().toString() ),
                ANNOUNCEMENT_FIELDS.get(5) + ": " + ( a.getHolidayDuration().getReturnDate() == null ? UNKNOWN_STRING : a.getHolidayDuration().getReturnDate().toString() ),
                ANNOUNCEMENT_FIELDS.get(6) + ": " + ( a.getAccommodationRequirements().getType().isBlank() ? UNKNOWN_STRING : a.getAccommodationRequirements().getType() ),
                ANNOUNCEMENT_FIELDS.get(7) + ": " + ( a.getAccommodationRequirements().getQuality() == 0 ? UNKNOWN_STRING : Integer.toString( a.getAccommodationRequirements().getQuality()  )),
                ANNOUNCEMENT_FIELDS.get(8) + ": " + ( a.getAccommodationRequirements().getNumOfRooms() == 0 ? UNKNOWN_STRING : Integer.toString( a.getAccommodationRequirements().getNumOfRooms() ) ),
                ANNOUNCEMENT_FIELDS.get(9) + ": " + ( a.getTransportRequirements().getType().isBlank() ? UNKNOWN_STRING : a.getTransportRequirements().getType() ),
                ANNOUNCEMENT_FIELDS.get(10) + ": " + ( a.getTransportRequirements().getQuality() == 0 ? UNKNOWN_STRING : Integer.toString( a.getTransportRequirements().getQuality() ) ),
                ANNOUNCEMENT_FIELDS.get(11) + ": " + ( a.getTransportRequirements().getDepartureLocation().isBlank() ? UNKNOWN_STRING : a.getTransportRequirements().getDepartureLocation() ),
                ANNOUNCEMENT_FIELDS.get(12) + ": " + ( a.getTransportRequirements().getNumOfTravelers() == 0 ? UNKNOWN_STRING : Integer.toString( a.getTransportRequirements().getNumOfTravelers() ) )
        );

        printList(fields, numberedFields);
    }


    // Displays some info for each offer in the given list
    public void showOffersList(List<Offer> offers)
    {
        for(int i = 0; i < offers.size(); i++)
        {
            Offer o = offers.get(i);
            printf("%d] %s - %s - %s - %s", i + 1,
                    o.getDestination(),
                    o.getHolidayDuration().getDepartureDate().toString(),
                    o.getHolidayDuration().getReturnDate().toString(),
                    o.getPriceAsStr()
            );
        }
    }


    // Displays all the details of the given offer
    // @numbered: if true then a number is displayed before each field of the offer
    public void showOfferDetails(Offer o, boolean showMetadata, boolean numberedFields)
    {
        if(showMetadata)
            printf("Offer made by: %s\n", o.getBidderUsername() == null ? UNKNOWN_STRING : o.getBidderUsername());

        List<String> fields = List.of(
                OFFER_FIELDS.get(1) + ": " + ( o.getDestination().isBlank() ? UNKNOWN_STRING : o.getDestination() ),
                OFFER_FIELDS.get(2) + ": " + ( o.getHolidayDuration().getDepartureDate() == null ? UNKNOWN_STRING : o.getHolidayDuration().getDepartureDate().toString() ),
                OFFER_FIELDS.get(3) + ": " + ( o.getHolidayDuration().getReturnDate() == null ? UNKNOWN_STRING : o.getHolidayDuration().getReturnDate().toString() ),
                OFFER_FIELDS.get(4) + ": " + o.getPriceAsStr(),
                OFFER_FIELDS.get(5) + ": " + ( o.getAccommodationOffer().getType().isBlank() ? UNKNOWN_STRING : o.getAccommodationOffer().getType() ),
                OFFER_FIELDS.get(6) + ": " + ( o.getAccommodationOffer().getName().isBlank() ? UNKNOWN_STRING : o.getAccommodationOffer().getName() ),
                OFFER_FIELDS.get(7) + ": " + ( o.getAccommodationOffer().getQuality() == 0 ? UNKNOWN_STRING : Integer.toString( o.getAccommodationOffer().getQuality()  )),
                OFFER_FIELDS.get(8) + ": " + ( o.getAccommodationOffer().getAddress().isBlank() ? UNKNOWN_STRING : o.getAccommodationOffer().getAddress() ),
                OFFER_FIELDS.get(9) + ": " + ( o.getAccommodationOffer().getNumOfRooms() == 0 ? UNKNOWN_STRING : Integer.toString( o.getAccommodationOffer().getNumOfRooms() ) ),
                OFFER_FIELDS.get(10) + ": " + ( o.getTransportOffer().getType().isBlank() ? UNKNOWN_STRING : o.getTransportOffer().getType() ),
                OFFER_FIELDS.get(11) + ": " + ( o.getTransportOffer().getCompanyName().isBlank() ? UNKNOWN_STRING : o.getTransportOffer().getCompanyName() ),
                OFFER_FIELDS.get(12) + ": " + ( o.getTransportOffer().getQuality() == 0 ? UNKNOWN_STRING : Integer.toString( o.getTransportOffer().getQuality() ) ),
                OFFER_FIELDS.get(13) + ": " + ( o.getTransportOffer().getDepartureLocation().isBlank() ? UNKNOWN_STRING : o.getTransportOffer().getDepartureLocation() ),
                OFFER_FIELDS.get(14) + ": " + ( o.getTransportOffer().getNumOfTravelers() == 0 ? UNKNOWN_STRING : Integer.toString( o.getTransportOffer().getNumOfTravelers() ) )
        );

        printList(fields, numberedFields);
        printf("\nOffer status: %s\n", o.getOfferStatus());
    }


    // ========================[ Simple input methods ]========================

    // Reads a string from system in and returns it
    public String getStringFromUser()
    {
        String res = "";
        try {
            res = reader.readLine();

        } catch(IOException e)
        {
            showErrorDialog("Cannot get input from user, program is terminating...");
            exit(0);
        }

        return res;
    }


    // Reads an int from system in and returns it
    public int getIntFromUser(int minValue, int maxValue)
    {
        boolean repeatRead;
        int val = Integer.MAX_VALUE;

        do {
            repeatRead = false;
            try {
                val = Integer.parseInt(getStringFromUser());
                if(val < minValue || val > maxValue)
                    throw new NumberFormatException();

            } catch(NumberFormatException e)
            {
                showErrorDialog("Please insert a number between " + minValue + " and " + maxValue);
                repeatRead = true;
            }
        } while(repeatRead);

        return val;
    }


    // Reads a date from system in and returns it
    public LocalDate getDateFromUser()
    {
        LocalDate result = null;
        do {
            String dateAsStr = getStringFromUser();
            try {
                result = LocalDate.parse(dateAsStr);
            } catch(DateTimeParseException e)
            {
                showErrorDialog("Please insert a date (i.e 2000-12-24, year-month-day)");
            }

        } while(result == null);
        return result;
    }


    // Displays the given strings as a numbered list and returns the int associated to the string chosen by the user
    public int getChoiceFromUser(List<String> possibilities)
    {
        printSubtitle("Available options");
        printList(possibilities, true);
        print("What do you want to do? ");
        return getIntFromUser(1, possibilities.size());
    }

}
