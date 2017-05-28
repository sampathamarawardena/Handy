package com.sasoftgroups.handy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void btnLogin(View view) {
        Intent login = new Intent(LoginPage.this, HomePage.class);
        startActivity(login);
    }

    public void btnSingUp(View view) {
        Intent register = new Intent(LoginPage.this, RegisterPage.class);
        startActivity(register);
    }
}
