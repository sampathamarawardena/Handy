package com.sasoftgroups.handy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
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

public class Messenger extends AppCompatActivity {
    ListView StartMessageListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        StartMessageListView = (ListView) findViewById(R.id.listView_StartMessage);

        GetStartMessage();

    }

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

    public void GetStartMessage() {
        if (isNetworkAvailable(this) == true) {
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

    private void getmessageJSON(String json) {
        JSON_StartMessage pj = new JSON_StartMessage(json);
        pj.parseJSON();
        GetStartMessageData cl = new GetStartMessageData(this, JSON_StartMessage.hid, JSON_StartMessage.senderID, JSON_StartMessage.senderName, JSON_StartMessage.message, JSON_StartMessage.datetime, JSON_StartMessage.topic, JSON_StartMessage.bothIDs);
        StartMessageListView.setAdapter(cl);
    }

    public void onClick_ViewMessage(View view) throws JSONException {
        if (isNetworkAvailable(this) == true) {
            int position = 0;
            position = StartMessageListView.getPositionForView((LinearLayout) view.getParent());
            String h = (String) StartMessageListView.getItemAtPosition(position);
            String[] data = h.split(",");

            Toast.makeText(Messenger.this, data[0], Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editors = sharedPref.edit();
            editors.putString(config.HELPID, data[0]);
            editors.putString(config.Sender, data[1]);
            editors.commit();


            final String id = sharedPref.getString(config.CHELPOD, "");
            final String sid = sharedPref.getString(config.Sender, "");

            Toast.makeText(Messenger.this, id + "_" + sid, Toast.LENGTH_SHORT).show();
            Intent chat = new Intent(Messenger.this, oneChat.class);
            startActivity(chat);
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
}
