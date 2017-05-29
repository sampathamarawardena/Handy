package com.sasoftgroups.handy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }

    public void btnLogin(View view) {
        Intent login = new Intent(RegisterPage.this, LoginPage.class);
        startActivity(login);
    }

    public void btnCreateAccount(View view) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Complete")
                .setMessage("Your Account is successfully created. Please check email and follow the steps.  Thanks")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(RegisterPage.this, LoginPage.class);
                        startActivity(login);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }
}
