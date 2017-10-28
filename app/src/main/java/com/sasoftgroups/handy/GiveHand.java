package com.sasoftgroups.handy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class GiveHand extends AppCompatActivity {

    ListView helpsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_hand);
        helpsListView = (ListView) findViewById(R.id.listView_Helps);
        getAllHelpRequests();
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

    //region Get All Help Requests
    private void getAllHelpRequests() {
        if (isNetworkAvailable(this) == true) {
            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(this);
            progressDoalog.setMessage("Loading....");
            progressDoalog.setTitle("Please Wait a Second..!");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            final String id = sharedPref.getString(config.CurrentUserID, "");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_HELP_REQUEST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            getAllHelpsJSON(response);
                            progressDoalog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDoalog.dismiss();
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
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void getAllHelpsJSON(String json) {
        helpRequestJSON pj = new helpRequestJSON(json);
        pj.parseJSON();
        GetAllHelpRequestData cl = new GetAllHelpRequestData(this, helpRequestJSON.hid, helpRequestJSON.senderID, helpRequestJSON.topic, helpRequestJSON.dis, helpRequestJSON.helptype);
        helpsListView.setAdapter(cl);
    }
    //endregion-

    //region Accept Hand Request
    public void onClick_GiveHand(View view) throws JSONException {
        if (isNetworkAvailable(this) == true) {
            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(this);
            progressDoalog.setMessage("Loading....");
            progressDoalog.setTitle("Please Wait a Second..!");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            int position = 0;
            position = helpsListView.getPositionForView((LinearLayout) view.getParent());
            Object items = helpsListView.getItemAtPosition(position);

            final String tv_Helps_hid = items.toString();
            Toast.makeText(GiveHand.this, tv_Helps_hid, Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            final String id = sharedPref.getString(config.CurrentUserID, "");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.ACCEPT_HELP_REQUEST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDoalog.dismiss();
                            Toast.makeText(GiveHand.this, "Accepted", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDoalog.dismiss();
                            Toast.makeText(GiveHand.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("helpID", tv_Helps_hid);
                    params.put("helperID", id);
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
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    //endregion
}
