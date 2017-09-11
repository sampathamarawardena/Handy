package com.sasoftgroups.handy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

public class Messenger extends AppCompatActivity {
    ListView StartMessageListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        StartMessageListView = (ListView) findViewById(R.id.listView_StartMessage);

        GetStartMessage();

        final Handler handler = new Handler();
        final int delay = 8000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                GetStartMessage();
                Toast.makeText(Messenger.this, "Hi", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void GetStartMessage() {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String id = sharedPref.getString(config.CurrentUserID, "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_START_MESSAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getmessageJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Messenger.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void getmessageJSON(String json) {
        JSON_StartMessage pj = new JSON_StartMessage(json);
        pj.parseJSON();
        GetStartMessageData cl = new GetStartMessageData(this, JSON_StartMessage.hid, JSON_StartMessage.senderID, JSON_StartMessage.message, JSON_StartMessage.datetime, JSON_StartMessage.topic);
        StartMessageListView.setAdapter(cl);
    }

    public void onClick_ViewMessage(View view) throws JSONException {
        int position = 0;
        position = StartMessageListView.getPositionForView((LinearLayout) view.getParent());
        Object items = StartMessageListView.getItemAtPosition(position);

        final String tv_Helps_hid = items.toString();

        Toast.makeText(Messenger.this, tv_Helps_hid, Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editors = sharedPref.edit();
        editors.putString(config.HELPID, tv_Helps_hid.toString());
        editors.commit();

        Intent chat = new Intent(Messenger.this, oneChat.class);
        startActivity(chat);
    }
}
