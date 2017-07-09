package com.sasoftgroups.handy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginPage extends AppCompatActivity {

    EditText UsernameEt;
    EditText PasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        UsernameEt = (EditText) findViewById(R.id.txtEmail);
        PasswordEt = (EditText) findViewById(R.id.txtPass);
    }

    public void btnLogin(View view) {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);

      /*  Intent login = new Intent(LoginPage.this, HomePage.class);
        startActivity(login);*/
    }

    public void btnSingUp(View view) {
        Intent register = new Intent(LoginPage.this, RegisterPage.class);
        startActivity(register);
    }
}
