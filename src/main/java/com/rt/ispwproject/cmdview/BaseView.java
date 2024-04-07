package com.rt.ispwproject.cmdview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;


// This class defines some common behaviour for the command line view classes
public class BaseView {

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

    private static final BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );


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


    // Prints the given strings as a numerated list
    public void printNumeratedList(String[] strings)
    {
        for(int i = 0; i < strings.length; i++)
            print(i + 1 + "] " + strings[i] + "\n");
    }


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


    public int getIntFromUser(int minValue, int maxValue)
    {
        int val = Integer.MAX_VALUE;

        do {
            print("Insert a number between " + minValue + " and " + maxValue + ": ");
            try {
                val = Integer.parseInt(getStringFromUser());
            } catch(NumberFormatException e)
            {
                showErrorDialog("Please insert a number.");
            }
        } while(val < minValue && val > maxValue);
        return val;
    }


    // Prints the given string in between 2 lines made with the character *
    public void showScreenTitle(String title)
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


    // Displays the given possibilities and returns the one chosen by the user
    public int showMenu(String[] possibilities)
    {
        print("What do you want to do?\n");
        printNumeratedList(possibilities);
        return getIntFromUser(1, possibilities.length);
    }


    // Displays the given error message
    public void showErrorDialog(String msg)
    {
        print("[ERROR] " + msg + "\n");
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

}
