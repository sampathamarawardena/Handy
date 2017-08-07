package com.sasoftgroups.handy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

        //sign_in_register = (Button) findViewById(R.id.btn_Login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(config.LOGGEDIN_SHARED_PREF, false);
        //If we will get true
        if (loggedIn) {
            //We will start the Profile Activity
            Intent intent = new Intent(LoginPage.this, HomePage.class);
            startActivity(intent);
        }
    }


    private void login() {
        //Getting values from edit texts
        final String email = UsernameEt.getText().toString().trim();
        final String password = PasswordEt.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN_URL,
                new Response.Listener<String>() {
                    private Activity activity;

                    public Activity getActivity() {
                        return activity;
                    }

                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if (response.equalsIgnoreCase(config.LOGIN_FAIL)) {
                            Toast.makeText(LoginPage.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        } else {
                            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = sharedPref.edit();
                            editors.putBoolean(config.LOGGEDIN_SHARED_PREF, true);
                            editors.putString(config.EMAIL_SHARED_PREF, email);
                            editors.putString("Score", "15");
                            editors.commit();

                            //Starting profile activity
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
                //Adding parameters to request
                params.put("email", email);
                params.put("password", password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void btn_Login(View view) {
        login();
    }

    public void btnSingUp(View view) {
        Intent Register = new Intent(LoginPage.this, RegisterPage.class);
        startActivity(Register);
    }
}
