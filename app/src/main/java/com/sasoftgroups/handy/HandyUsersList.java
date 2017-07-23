package com.sasoftgroups.handy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HandyUsersList extends AppCompatActivity {

    public static final String JSON_URL = "http://sasoftgroups.com/handy/userslist.php";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handy_users_list);
        listView = (ListView) findViewById(R.id.HandyUsersListView);
        sendRequest();

    }

    private void sendRequest(){

        StringRequest stringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HandyUsersList.this,error.getMessage(),Toast.LENGTH_LONG).show();
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

    public void onClick_SendFRequest(View view){


        //Button btnChild = (Button)vwParentRow.getChildAt(1);


        Toast.makeText(HandyUsersList.this, "Test", Toast.LENGTH_LONG).show();
    }


}
