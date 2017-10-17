package com.sasoftgroups.handy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
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

public class RegisterPartTwo extends AppCompatActivity {
    static final int DIALOG_ID = 0;
    EditText dob;
    MultiAutoCompleteTextView keyword;
    int year, month, day;
    String bdate;
    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            bdate = "" + year + "-" + (month + 1) + "-" + day;
            dob.setText(bdate);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_last_step);
        keyword = (MultiAutoCompleteTextView) findViewById(R.id.edt_Keywords);
        dob = (EditText) findViewById(R.id.edt_DOB);
        showDialogOnText();
        keys();

    }

    //region Show Date Picker when Click The Edit Text
    public void showDialogOnText() {
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePicker, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        } else {
            return null;
        }
    }
    //endregion

    public void keys() {
        String[] keyws = getResources().getStringArray(R.array.auto_keyword);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, keyws);
        keyword.setThreshold(1);
        keyword.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        keyword.setAdapter(arrayAdapter);
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

    public void onClickRegAdd(View view) {
        final String keys = keyword.getText().toString();
        final String birth = dob.getText().toString();

        if (!keys.isEmpty() || !birth.isEmpty()) {
            if (isNetworkAvailable(this) == true) {
                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(this);
                progressDoalog.setMessage("Loading....");
                progressDoalog.setTitle("Please Wait a Second..!");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.UPDATE_REGISTERED_USER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("success")) {
                                    progressDoalog.dismiss();
                                    new AlertDialog.Builder(RegisterPartTwo.this)
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .setTitle("Welcome to Handy")
                                            .setMessage("You are successfully Registered to Handy")
                                            .setPositiveButton("Login to Handy", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent login = new Intent(RegisterPartTwo.this, LoginPage.class);
                                                    startActivity(login);
                                                }
                                            }).show();
                                } else if (response.equals("fail")) {
                                    progressDoalog.dismiss();
                                    Toast.makeText(RegisterPartTwo.this, "Something going wrong", Toast.LENGTH_LONG).show();
                                } else {
                                    progressDoalog.dismiss();
                                    Toast.makeText(RegisterPartTwo.this, response, Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RegisterPartTwo.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        final String id = sharedPref.getString(config.CurrentUserID, "");

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("str_id", id);
                        params.put("str_keys", keys);
                        params.put("str_dob", bdate);
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
        } else {
            Toast.makeText(RegisterPartTwo.this, "Some Field are Empty. Please re-check", Toast.LENGTH_LONG).show();
        }
    }
}
