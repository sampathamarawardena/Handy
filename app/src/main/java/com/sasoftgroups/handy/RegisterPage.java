package com.sasoftgroups.handy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterPage extends AppCompatActivity {

    EditText name, uname, email, phone, password;
    String str_name, str_uname, str_email, str_phone, str_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        name = (EditText) findViewById(R.id.edtName);
        uname = (EditText) findViewById(R.id.edtUsername);
        email = (EditText) findViewById(R.id.edtEmail);
        phone = (EditText) findViewById(R.id.edtPhone);
        password = (EditText) findViewById(R.id.edtPassword);
    }

    public void btnLogin(View view) {
        Intent login = new Intent(RegisterPage.this, LoginPage.class);
        startActivity(login);
    }

    public void btnCreateAccount(View view) {
        str_name = name.getText().toString();
        str_uname = uname.getText().toString();
        str_email = email.getText().toString();
        str_phone = phone.getText().toString();
        str_password = password.getText().toString();
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_name, str_uname, str_email, str_phone, str_password);

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
