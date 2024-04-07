package com.rt.ispwproject.cmdview;


public class LoginViewCmd extends BaseView {

    private String username;
    private String password;


    // Implements the login form
    public void showLoginForm()
    {
        showScreenTitle(BaseView.LOGIN_SCREEN_NAME);

        print("Username: ");
        username = getStringFromUser();

        print("Password: ");
        password = getStringFromUser();
    }


    // Getters
    public String getUsername()     { return this.username; }
    public String getPassword()     { return this.password; }
}
