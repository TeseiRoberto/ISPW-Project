package com.rt.ispwproject.cmdview;


public class LoginViewCmd extends BaseViewCmd {


    public void showScreenTitle()
    {
        printTitle(BaseViewCmd.LOGIN_SCREEN_NAME);
    }


    // Asks the username to be used for the login
    public String getUsername()
    {
        print("Username: ");
        return getStringFromUser();
    }

    // Asks the password to be used for the login
    public String getPassword()
    {
        print("Password: ");
        return getStringFromUser();
    }


}
