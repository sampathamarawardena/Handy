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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    EditText name, uname, email, phone, fpassword, confPassword;
    String str_name, str_uname, str_email, str_phone, str_password, str_CPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        name = (EditText) findViewById(R.id.edtRegName);
        uname = (EditText) findViewById(R.id.edtRegUname);
        email = (EditText) findViewById(R.id.edtRegEmail);
        phone = (EditText) findViewById(R.id.edtRegPhone);
        fpassword = (EditText) findViewById(R.id.edtRegPassword);
        confPassword = (EditText) findViewById(R.id.edtRegCPass);

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

    public void btnLogin(View view) {
        Intent login = new Intent(RegisterPage.this, LoginPage.class);
        startActivity(login);
    }

    public void btnCreateAccount(View view) throws JSONException {
        RegisterUser();
    }

    private void RegisterUser() throws JSONException {
        str_name = name.getText().toString();
        str_uname = uname.getText().toString();
        str_email = email.getText().toString();
        str_phone = phone.getText().toString();
        str_password = fpassword.getText().toString();
        str_CPassword = confPassword.getText().toString();

        if (str_uname.isEmpty() || str_name.isEmpty() || str_email.isEmpty() || str_phone.isEmpty() || str_password.isEmpty() || str_CPassword.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Please fill all the Firelds.", Toast.LENGTH_LONG).show();
        } else {
            if (str_password.equals(str_CPassword)) {
                if (isNetworkAvailable(this) == true) {
                    final ProgressDialog progressDoalog;
                    progressDoalog = new ProgressDialog(this);
                    progressDoalog.setMessage("Loading....");
                    progressDoalog.setTitle("Please Wait a Second..!");
                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDoalog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, config.REGISTER_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.isEmpty()) {
                                        Toast.makeText(RegisterPage.this, "There is an error please check your internet connection", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (response.equals("email")) {
                                            progressDoalog.dismiss();
                                            new AlertDialog.Builder(RegisterPage.this)
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .setTitle("Alredy Have Account")
                                                    .setMessage("This email address is already registered")
                                                    .setPositiveButton("Login to Handy", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent login = new Intent(RegisterPage.this, RegisterPartTwo.class);
                                                            startActivity(login);
                                                        }
                                                    })
                                                    .setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent login = new Intent(RegisterPage.this, LoginPage.class);
                                                            startActivity(login);
                                                        }
                                                    }).show();
                                        } else if (response.equals("fail")) {
                                            progressDoalog.dismiss();
                                            Toast.makeText(RegisterPage.this, "Please Try again later there is any server error", Toast.LENGTH_LONG).show();
                                        } else {
                                            progressDoalog.dismiss();
                                            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editors = sharedPref.edit();
                                            editors.putString(config.CurrentUserID, response);
                                            editors.commit();

                                            Toast.makeText(RegisterPage.this, "You are successfully Registered to Handy", Toast.LENGTH_LONG).show();
                                            Toast.makeText(RegisterPage.this, "We need few more details", Toast.LENGTH_LONG).show();

                                            Intent reg = new Intent(RegisterPage.this, RegisterPartTwo.class);
                                            startActivity(reg);
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDoalog.dismiss();
                                    Toast.makeText(RegisterPage.this, error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("str_name", str_name);
                            params.put("str_uname", str_uname);
                            params.put("str_email", str_email);
                            params.put("str_phone", str_phone);
                            params.put("str_password", str_password);
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
                Toast.makeText(RegisterPage.this, "Password and Confirm Password Dose not match.", Toast.LENGTH_LONG).show();
            }
        }
    }


}
