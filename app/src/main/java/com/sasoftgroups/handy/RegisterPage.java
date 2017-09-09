package com.sasoftgroups.handy;

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

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    EditText name, uname, email, phone, password;
    String str_name, str_uname, str_email, str_phone, str_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        name = (EditText) findViewById(R.id.edtRegName);
        uname = (EditText) findViewById(R.id.edtRegUname);
        email = (EditText) findViewById(R.id.edtRegEmail);
        phone = (EditText) findViewById(R.id.edtRegPhone);
        password = (EditText) findViewById(R.id.edtRegPassword);

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
        str_password = password.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.isEmpty()) {
                            Toast.makeText(RegisterPage.this, "There is an error please check your internet connection", Toast.LENGTH_LONG).show();
                        } else {
                            if (response.equals("email")) {
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
                                Toast.makeText(RegisterPage.this, "Please Try again later there is any server error", Toast.LENGTH_LONG).show();
                            } else {
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
    }
}
