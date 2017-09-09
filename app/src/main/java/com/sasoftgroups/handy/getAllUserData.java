package com.sasoftgroups.handy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MultiAutoCompleteTextView;

public class getAllUserData extends AppCompatActivity {
    public MultiAutoCompleteTextView mvactv_KeywordRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_last_step);
/*
        mvactv_KeywordRegister = (MultiAutoCompleteTextView) findViewById(R.id.mvactv_KeywordRegister);

        String [] keyws = getResources().getStringArray(R.array.auto_keyword);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.select_dialog_item , keyws);
        mvactv_KeywordRegister.setThreshold(1);
        mvactv_KeywordRegister.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mvactv_KeywordRegister.setAdapter(arrayAdapter);*/
    }
}
