package com.sasoftgroups.handy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginPage extends AppCompatActivity {
    private EditText UsernameEt;
    private EditText PasswordEt;
    private boolean loggedIn = false;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Handy")
                .setMessage("Are you sure you want to close Handy?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        UsernameEt = (EditText) findViewById(R.id.txtEmail);
        PasswordEt = (EditText) findViewById(R.id.txtPass);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable(this) == true) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            loggedIn = sharedPreferences.getBoolean(config.LOGGEDIN_SHARED_PREF, false);
            if (loggedIn) {
                Intent intent = new Intent(LoginPage.this, HomePage.class);
                startActivity(intent);
            }
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

    private void login() {
        //Getting values from edit texts
        final String email = UsernameEt.getText().toString().trim();
        final String password = PasswordEt.getText().toString().trim();
        if (!email.isEmpty() || !password.isEmpty()) {
            if (isNetworkAvailable(this) == true) {
                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(this);
                progressDoalog.setMessage("Loading....");
                progressDoalog.setTitle("Please Wait a Second..!");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equalsIgnoreCase(config.LOGIN_FAIL)) {
                                    progressDoalog.dismiss();
                                    Toast.makeText(LoginPage.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                                } else {
                                    SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editors = sharedPref.edit();
                                    editors.putBoolean(config.LOGGEDIN_SHARED_PREF, true);
                                    editors.putString(config.EMAIL_SHARED_PREF, email);
                                    editors.commit();
                                    progressDoalog.dismiss();
                                    Intent intent = new Intent(LoginPage.this, HomePage.class);
                                    startActivity(intent);
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
                        params.put("email", email);
                        params.put("password", password);
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
        } else {
            Toast.makeText(LoginPage.this, "Invalid username or password", Toast.LENGTH_LONG).show();
        }

    }

    public void btn_Login(View view) {
        Button b = (Button) findViewById(R.id.btn_Login);
        b.setEnabled(false);
        login();
        b.setEnabled(true);
    }

    public void btnSingUp(View view) {
        Intent Register = new Intent(LoginPage.this, RegisterPage.class);
        startActivity(Register);
    }
}
