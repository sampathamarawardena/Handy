package com.sasoftgroups.handy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class GiveHand extends AppCompatActivity {

    ListView helpsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_hand);
        helpsListView = (ListView) findViewById(R.id.listView_Helps);
        getAllHelpRequests();
    }

    //region Get All Help Requests
    private void getAllHelpRequests() {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String id = sharedPref.getString(config.CurrentUserID, "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_HELP_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getAllHelpsJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GiveHand.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void getAllHelpsJSON(String json) {
        //allFriendsListView = (ListView) findViewById(R.id.listview_allFriends);
        helpRequestJSON pj = new helpRequestJSON(json);
        pj.parseJSON();
        GetAllHelpRequestData cl = new GetAllHelpRequestData(this, helpRequestJSON.hid, helpRequestJSON.senderID, helpRequestJSON.topic, helpRequestJSON.dis, helpRequestJSON.helptype);
        helpsListView.setAdapter(cl);
    }
    //endregion-
}
