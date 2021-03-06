package com.sasoftgroups.handy;

//region Import Library's

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.w("INTERNET:", String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.w("INTERNET:", "connected!");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //region Tab Click Event Handler
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            // allFriendsListView = (ListView) findViewById(R.id.listview_allFriends);
            getAllFriends();

        }
        if (tab.getPosition() == 1) {
            // FriendRequestlistView = (ListView) findViewById(R.id.listVFrendRequest);
            GetAllFriendRequests();
        }
        if (tab.getPosition() == 2) {
            // listView = (ListView) findViewById(R.id.listv_usersList);
            GetAllUsers();
        }
        viewPager.setCurrentItem(tab.getPosition());
    }
    //endregion

    //region Get All Friends
    private void getAllFriends() {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setTitle("Please Wait a Second..!");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String id = sharedPref.getString(config.CurrentUserID, "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ALL_FRIENDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getAllFriendsJSON(response);
                        progressDoalog.dismiss();
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
    //endregion-

    //region useless Tab Selectors
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    //endregion

    //region Get All Users To List View
    private void GetAllUsers() {
        if (isNetworkAvailable(this) == true) {
            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(this);
            progressDoalog.setMessage("Loading....");
            progressDoalog.setTitle("Please Wait a Second..!");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            final String id = sharedPref.getString(config.CurrentUserID, "");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.ALL_USERLIST_LINK,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            AllUsersJSON(response);
                            progressDoalog.dismiss();
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
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("INFORMATION")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
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
        if (isNetworkAvailable(this) == true) {
            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(this);
            progressDoalog.setMessage("Loading....");
            progressDoalog.setTitle("Please Wait a Second..!");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            final String id = sharedPref.getString(config.CurrentUserID, "");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.ALL_FRIENDSREQUEST_LINK,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showFriendrequestJSON(response);
                            progressDoalog.dismiss();
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
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("INFORMATION")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

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
        if (isNetworkAvailable(this) == true) {

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(this);
            progressDoalog.setMessage("Friend Request Sending....");
            progressDoalog.setTitle("Please Wait a Second..!");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

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
                        progressDoalog.dismiss();
                        Toast.makeText(FriednsList.this, "Friend Request Sent", Toast.LENGTH_LONG).show();
                        GetAllUsers();
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
    } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("INFORMATION")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    //endregion

    //region Check Friend - Not using yet
    /*
    public boolean CheckFriend(final String CurrentUID, final String FriendUID) {
        if (isNetworkAvailable(this) == true) {
            final boolean[] boolFrind = {false};
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            boolFrind[0] = response == "1";
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
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("INFORMATION")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return false;
    } */
    //endregion

    //region On Click Accept Friend Request
    public void onClick_AcceptFriendRequest(View view) {
        if (isNetworkAvailable(this) == true) {
            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Its loading....");
            progressDoalog.setTitle("ProgressDialog bar example");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // show it
            progressDoalog.show();

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
                            GetAllFriendRequests();
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
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("INFORMATION")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    //endregion
}
