package com.rt.ispwproject.cmdview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


// This class defines some common behaviour for the command line view classes
public class BaseView {

    private static final BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );


    // Prints the given string to system out
    public void print(String str)
    {
        System.out.print(str);
    }


    public String getStringFromUser()
    {
        String input = "";
        try {
            input = reader.readLine();
        } catch(IOException e)
        {
            displayErrorDialog("Failed to read input string from user");
        }

        return input;
    }


    // Prints the given error message
    public void displayErrorDialog(String msg)
    {
        print("[ERROR] " + msg + "\n");
    }


    // Prints the given message and waits for the user to enter (Y/y) or (N/n)
    // Returns true if user inserted Y/y, false otherwise
    public boolean displayConfirmDialog(String msg)
    {
        boolean res = false;
        print(msg + " Enter Y for yes, N for no\n==> ");

        while(true)
        {
            String in = getStringFromUser();

            if(!in.isBlank())
            {
                in = in.toLowerCase();
                if(in.charAt(0) == 'y')
                {
                    res = true;
                    break;
                } else if(in.charAt(0) == 'n')
                {
                    break;
                }
            }

            print("Please insert Y (for yes) or N (for no)\n==> ");
        }

        return res;
    }

}
