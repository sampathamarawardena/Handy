package com.sasoftgroups.handy;

/**
 * Created by ArunaAmarawardena on 7/11/2017.
 */

public class config {
    //URL to our login.php file
    public static final String LOGIN_URL = "http://sasoftgroups.com/handy/login.php";

    public static final String REGISTER_URL = "http://sasoftgroups.com/handy/register.php";

    public static final String SEND_FRIEND_REQUEST_URL ="http://sasoftgroups.com/handy/sendFrequest.php";

    public static final String ALL_USERLIST_LINK = "http://sasoftgroups.com/handy/userslist.php";

    public static final String ALL_FRIENDSREQUEST_LINK = "http://sasoftgroups.com/handy/allFriendRequests.php";



    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    public static final String LOGIN_FAIL = "failure";

    public static final String CurrentUserID = "id";


    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
