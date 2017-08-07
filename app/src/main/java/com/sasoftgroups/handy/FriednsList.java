package com.sasoftgroups.handy;

//region Import Library's

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
//endregion

public class FriednsList extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    //region Variable Declaration
    ListView listView, FriendRequestlistView, allFriendsListView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //endregion

    //region On Create Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friedns_list);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("My Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Requests"));
        tabLayout.addTab(tabLayout.newTab().setText("Handy Users"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

        allFriendsListView = (ListView) findViewById(R.id.listview_allFriends);
        getAllFriends();

    }
    //endregion

    //region Tab Click Event Handler
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            allFriendsListView = (ListView) findViewById(R.id.listview_allFriends);
            getAllFriends();

        }
        if (tab.getPosition() == 1) {
            FriendRequestlistView = (ListView) findViewById(R.id.listVFrendRequest);
            GetAllFriendRequests();
        }
        if (tab.getPosition() == 2) {
            listView = (ListView) findViewById(R.id.listv_usersList);
            GetAllUsers();
        }
        viewPager.setCurrentItem(tab.getPosition());
    }
    //endregion

    //region Get All Friends
    private void getAllFriends() {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String id = sharedPref.getString(config.CurrentUserID, "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ALL_FRIENDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getAllFriendsJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FriednsList.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", id);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getAllFriendsJSON(String json) {
        allFriendsListView = (ListView) findViewById(R.id.listview_allFriends);
        usersJason pj = new usersJason(json);
        pj.parseJSON();
        GetAllFriends_ListViewData cl = new GetAllFriends_ListViewData(this, usersJason.ids, usersJason.names, usersJason.emails);
        allFriendsListView.setAdapter(cl);
    }
    //endregion

    //region NotUsing This Tab Selecters
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    //endregion

    //region Get All Users To List View
    private void GetAllUsers() {
        StringRequest stringRequest = new StringRequest(config.ALL_USERLIST_LINK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AllUsersJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FriednsList.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void AllUsersJSON(String json) {
        listView = (ListView) findViewById(R.id.listv_usersList);
        usersJason pj = new usersJason(json);
        pj.parseJSON();
        HandyUsersCustomListView cl = new HandyUsersCustomListView(this, usersJason.ids, usersJason.names, usersJason.emails);
        listView.setAdapter(cl);
    }
    //endregion

    //region Get All Friends Requests to List View
    private void GetAllFriendRequests() {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String id = sharedPref.getString(config.CurrentUserID, "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.ALL_FRIENDSREQUEST_LINK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showFriendrequestJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FriednsList.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", id);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showFriendrequestJSON(String json) {
        FriendRequestlistView = (ListView) findViewById(R.id.listVFrendRequest);
        usersJason pj = new usersJason(json);
        pj.parseJSON();
        FriendRequestListViewData cl = new FriendRequestListViewData(this, usersJason.ids, usersJason.names, usersJason.emails);
        FriendRequestlistView.setAdapter(cl);
    }
    //endregion

    //region On Click Send Friend Request

    public void onClick_SendFRequest(View view) throws JSONException {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String id = sharedPref.getString(config.CurrentUserID, "");
        int position = 0;
        position = listView.getPositionForView((LinearLayout) view.getParent());
        Object items = listView.getItemAtPosition(position);
        final String uid = items.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SEND_FRIEND_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(FriednsList.this, "Friend Request Sent", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FriednsList.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String email = sharedPref.getString(config.EMAIL_SHARED_PREF, "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", id);
                params.put("senderID", uid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //endregion

    //region Commented Code
/*
    public ListView getListView() {
        return listView;
    }


    private void SendFRequest(final String uid) throws JSONException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SEND_FRIEND_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(FriednsList.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FriednsList.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("userID", config.CurrentUserID);
                params.put("senderID",uid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
*/
//endregion

    //region Check Friend - Not using yet
    public boolean CheckFriend(final String CurrentUID, final String FriendUID) {
        final boolean[] boolFrind = {false};
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == "1") {
                            boolFrind[0] = true;
                        } else {
                            boolFrind[0] = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("currentUID", CurrentUID);
                params.put("friendID", FriendUID);

                //returning parameter
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        return boolFrind[0];
    }
    //endregion

    //region On Click Accept Friend Request
    public void onClick_AcceptFriendRequest(View view) {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String id = sharedPref.getString(config.CurrentUserID, "");
        int position = 0;
        position = FriendRequestlistView.getPositionForView((LinearLayout) view.getParent());
        Object items = FriendRequestlistView.getItemAtPosition(position);

        final String uid = items.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.ACCEPT_FIREND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(FriednsList.this, "Friend Request Accepted", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FriednsList.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String email = sharedPref.getString(config.EMAIL_SHARED_PREF, "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("currentUser", id);
                params.put("FriendID", uid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //endregion
}
