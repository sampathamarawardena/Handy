package com.sasoftgroups.handy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class HandRequest extends AppCompatActivity {

    EditText topic, description;
    String keywordsSearch = "";
    String UTopic = "";
    Spinner category;
    ToggleButton type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_request);
        topic = (EditText) findViewById(R.id.edt_handRequest_Topic);
        description = (EditText) findViewById(R.id.edt_handRequest_Description);
        category = (Spinner) findViewById(R.id.edt_handRequest_Spinner);
        type = (ToggleButton) findViewById(R.id.tb_handRequest_Type);
        loadSpinner();
    }

    public void loadSpinner() {

    }

    public void GenerateKeyword() {
        UTopic = topic.getText().toString();
        int count = 0;
        if (!UTopic.isEmpty()) {
            String[] splitStr = UTopic.trim().split("\\s+");
            String[] keyws = getResources().getStringArray(R.array.auto_keyword);
            String[][] arr = new String[splitStr.length + 1][splitStr.length + 1];
            arr[0][0] = "Test";
            arr[0][1] = "0";
            for (int i = 0; i < keyws.length; i++) {
                for (int a = 0; a < splitStr.length; a++) {
                    if (keyws[i].toLowerCase().equals(splitStr[a].toLowerCase())) {
                        boolean add = false;
                        for (int b = 0; b < arr.length; b++) {
                            if (arr[b][0] != null) {
                                if (arr[b][0].toLowerCase().equals(splitStr[a].toLowerCase())) {
                                    int tot = Integer.parseInt(arr[b][1]) + 1;
                                    arr[b][1] = "" + tot;
                                    add = true;
                                }
                            }
                        }
                        if (!add) {
                            for (int b = 0; b < arr.length; b++) {
                                if (arr[b][0] == null) {
                                    arr[b][0] = splitStr[a];
                                    arr[b][1] = "1";
                                    b = arr.length;
                                }
                            }
                            count += 1;
                        }
                    }
                }
            }
            if (count >= 5) {
                for (int c = 0; c < arr.length; c++) {
                    for (int j = 0; j < arr.length; j++) {
                        if (arr[j][0] != null && arr[j + 1][0] != null) {
                            if (Integer.parseInt(arr[j][1]) < Integer.parseInt(arr[j + 1][1])) {
                                String temp = arr[j][1];
                                String temp2 = arr[j][0];
                                arr[j][1] = arr[j + 1][1];
                                arr[j][0] = arr[j + 1][0];
                                arr[j + 1][1] = temp;
                                arr[j + 1][0] = temp2;
                            }
                        }
                    }
                }
                for (int i = 0; i < 5; i++) {
                    keywordsSearch = keywordsSearch + "," + arr[i][0];
                }
            } else {
                for (int i = 0; i < count; i++) {
                    keywordsSearch = keywordsSearch + "," + arr[i][0];
                }
            }
            AddToDB();
        } else {
            System.out.println("Error");
        }
    }

    public void AddToDB() {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String id = sharedPref.getString(config.CurrentUserID, "");
        final String STRdescription = description.getText().toString();
//        final String hcategory = category.getSelectedItem().toString();
        final String ty = type.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PUT_HELP_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(HandRequest.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HandRequest.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("str_sender", id);
                params.put("str_helpType", ty);
                params.put("str_topic", UTopic);
                params.put("str_discription", STRdescription);
                params.put("str_keywords", keywordsSearch);
                params.put("str_catagory", "test");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void onClickSubmithRequest(View view) {
        GenerateKeyword();
    }
}
