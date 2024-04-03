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


    public String getStringFromUser() throws IOException
    {
        return reader.readLine();
    }


    // Prints the given error message
    public void displayErrorDialog(String msg)
    {
        print("[ERROR] " + msg + "\n");
    }


    // Prints the given message and waits for the user to enter (Y/y) or (N/n)
    // Returns true if user inserted Y/y, false otherwise
    public boolean displayConfirmDialog(String msg) throws IOException
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
