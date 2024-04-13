package com.rt.ispwproject.cmdview;

import com.rt.ispwproject.beans.Announcement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

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
    public void printList(String[] strings, boolean numbered)
    {
        if(numbered)
            for(int i = 0; i < strings.length; i++)
                print(i + 1 + "] " + strings[i] + "\n");
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

        print(separatorLine.toString());
        print(titleLine.toString());
        print(separatorLine.toString());
    }


    // Displays some info for each announcement in the given list
    public void showAnnouncementsList(List<Announcement> announcements)
    {
        for(int i = 0; i < announcements.size(); i++)
        {
            printf("%d] %s - %s - %s - %s\n", i + 1,
                    announcements.get(i).getDestination(),
                    announcements.get(i).getHolidayDuration().getDepartureDate().toString(),
                    announcements.get(i).getHolidayDuration().getReturnDate().toString(),
                    announcements.get(i).getAvailableBudgetAsStr()
            );
        }
    }


    // Displays all the details of the given announcement
    // @numbered: if true then a number is displayed before each field of the announcement
    public void showAnnouncementDetails(Announcement a, boolean numberedFields)
    {
        // TODO: Add implementation
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
        int val = Integer.MAX_VALUE;

        do {
            try {
                val = Integer.parseInt(getStringFromUser());
            } catch(NumberFormatException e)
            {
                showErrorDialog("Please insert a number between " + minValue + " and " + maxValue);
            }
        } while(val < minValue || val > maxValue);
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
                showErrorDialog("Please insert a date (i.e 2000-12-24)");
            }

        } while(result == null);
        return result;
    }


    // Displays the given strings as a numbered list and returns the int associated to the string chosen by the user
    public int getChoiceFromUser(String[] possibilities)
    {
        printList(possibilities, true);
        print("What do you want to do? ==> ");
        return getIntFromUser(1, possibilities.length);
    }

}
