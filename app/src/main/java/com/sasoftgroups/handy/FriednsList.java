package com.sasoftgroups.handy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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


public class FriednsList extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    ListView listView, FriendRequestlistView;

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getPosition() == 2){
            listView = (ListView) findViewById(R.id.listv_usersList);
            sendRequest();
        }
        else if(tab.getPosition() == 1){
            FriendRequestlistView = (ListView) findViewById(R.id.listVFrendRequest);
            FriendsRequestList();
        }
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

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

    }

    private void sendRequest() {
    StringRequest stringRequest = new StringRequest(config.ALL_USERLIST_LINK,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showJSON(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FriednsList.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

    RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json){
        usersJason pj = new usersJason(json);
        pj.parseJSON();
        HandyUsersCustomListView cl = new HandyUsersCustomListView(this, usersJason.ids,usersJason.names,usersJason.emails);
        listView.setAdapter(cl);
    }


    private void FriendsRequestList() {
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
                        Toast.makeText(FriednsList.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("uid", "12");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showFriendrequestJSON(String json){
        usersJason pj = new usersJason(json);
        pj.parseJSON();
        HandyUsersCustomListView cl = new HandyUsersCustomListView(this, usersJason.ids,usersJason.names,usersJason.emails);
        FriendRequestlistView.setAdapter(cl);
    }

    public void onClick_SendFRequest(View view) throws JSONException {

        if(CheckFriend("7","12") == true){
            String uid = null;
            int position = 0;
            position = getListView().getPositionForView((LinearLayout) view.getParent());

            Object items = FriendRequestlistView.getItemAtPosition(position);
            uid = items.toString();

            Toast.makeText(FriednsList.this,"Friend Request Accepted",Toast.LENGTH_LONG).show();
        } else {
            String uid = null;
            int position = 0;
            position = getListView().getPositionForView((LinearLayout) view.getParent());

            Object items = listView.getItemAtPosition(position);
            uid = items.toString();

            //SendFRequest(uid);
            position = 0;
            Toast.makeText(FriednsList.this,"Friend Request Send",Toast.LENGTH_LONG).show();
        }
    }


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

    public boolean CheckFriend(final String CurrentUID, final String FriendUID){
        final boolean[] boolFrind = {false};
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response == "1"){
                            boolFrind[0] = true;
                        }
                        else {
                            boolFrind[0] = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
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
}
