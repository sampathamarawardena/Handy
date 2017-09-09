package com.sasoftgroups.handy;

/**
 * Created by ArunaAmarawardena on 7/11/2017.
 */

public class config {
    //URL to our login.php file
    public static final String LOGIN_URL = "http://sasoftgroups.com/handy/login.php";

    public static final String REGISTER_URL = "http://sasoftgroups.com/handy/register.php";

    public static final String SEND_FRIEND_REQUEST_URL = "http://sasoftgroups.com/handy/sendFrequest.php";

    public static final String ALL_USERLIST_LINK = "http://sasoftgroups.com/handy/userslist.php";

    public static final String ALL_FRIENDSREQUEST_LINK = "http://sasoftgroups.com/handy/allFriendRequests.php";

    public static final String GET_USER_DETAILS = "http://sasoftgroups.com/handy/getUserDetails.php";

    public static final String ACCEPT_FIREND = "http://sasoftgroups.com/handy/acceptFRequest.php";

    public static final String GET_ALL_FRIENDS = "http://sasoftgroups.com/handy/getFriendsList.php";

    public static final String UPDATE_REGISTERED_USER = "http://sasoftgroups.com/handy/registerUpdate.php";


    //User Profile Details
    public static final String CurrentUserID = "id";
    public static final String CurrentUserName = "name";


    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public static final String LOGIN_FAIL = "failure";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
