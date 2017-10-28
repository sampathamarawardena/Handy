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
import android.widget.EditText;
import android.widget.TextView;
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

public class MyProfile extends AppCompatActivity {
    TextView Helps, rate, TXNAME;
    EditText name, Email, Keys, Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Helps = (TextView) findViewById(R.id.txt_Helps);
        rate = (TextView) findViewById(R.id.txt_FeedbackCounts);
        name = (EditText) findViewById(R.id.edt_Name);
        Email = (EditText) findViewById(R.id.edt_EMAIL);
        Phone = (EditText) findViewById(R.id.edtPhone);
        Keys = (EditText) findViewById(R.id.edtKeys);
        TXNAME = (TextView) findViewById(R.id.txtName);

        GetMyProfileData();
    }

    //region Network Connection Check
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
    //endregion

    //region Get User Profile Data
    public void GetMyProfileData() {
        if (isNetworkAvailable(this)) {
            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(this);
            progressDoalog.setMessage("Loading....");
            progressDoalog.setTitle("Please Wait a Second..!");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();

            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            final String id = sharedPref.getString(config.CurrentUserID, "");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_PROFILE_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            SetDataToView(response);
                            progressDoalog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MyProfile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            progressDoalog.dismiss();
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

    public void SetDataToView(String data) {
        String[] splited = data.split("%");

        name.setText(splited[1].trim());
        TXNAME.setText(splited[1].trim());
        Email.setText(splited[2].trim());
        Phone.setText(splited[3].trim());
        Keys.setText(splited[4].trim());
        rate.setText(splited[5].trim());
        Helps.setText(splited[6].trim());

    }

    //endregion

    public void UpdateMyProfileData() {
        final String Name = name.getText().toString();
        final String email = Email.getText().toString();
        final String phone = Phone.getText().toString();
        final String keys = Keys.getText().toString();


        if (!Name.equals("") && !email.equals("") && !phone.equals("") && !keys.equals("")) {
            if (isNetworkAvailable(this)) {
                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(this);
                progressDoalog.setMessage("Loading....");
                progressDoalog.setTitle("Please Wait a Second..!");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();

                SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                final String id = sharedPref.getString(config.CurrentUserID, "");

                StringRequest stringRequests = new StringRequest(Request.Method.POST, config.UPDATE_MYProfile,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDoalog.setTitle("Getting Profile Data...");
                                GetMyProfileData();
                                progressDoalog.dismiss();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MyProfile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                progressDoalog.dismiss();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", id);
                        params.put("Name", Name);
                        params.put("email", email);
                        params.put("phone", phone);
                        params.put("keys", keys);
                        return params;
                    }

                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequests);

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
        } else {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("INFORMATION")
                    .setMessage("Some Fields are missing. Please recheck the details.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    public void onClickSaveChanges(View view) {
        UpdateMyProfileData();
    }
}
