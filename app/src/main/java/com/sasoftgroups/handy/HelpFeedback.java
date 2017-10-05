package com.sasoftgroups.handy;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.hsalf.smilerating.SmileRating;

import java.util.HashMap;
import java.util.Map;

public class HelpFeedback extends AppCompatActivity {

    SmileRating sr;
    String id, helpid, sender;
    EditText feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_feedback);
        sr = (SmileRating) findViewById(R.id.rateImage);
        feedback = (EditText) findViewById(R.id.edtGiveFeedBack);
    }

    //region Onclick Give Feedback Button
    public void onClick_GiveFeedBack(View view) {

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setTitle("Please Wait a Second..!");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        id = sharedPref.getString(config.CurrentUserID, "");
        helpid = sharedPref.getString(config.HELPID, "");
        sender = sharedPref.getString(config.Sender, "");

        final int ss = sr.getRating();
        final String userFeddback = feedback.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.add_Review,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDoalog.dismiss();
                        Toast.makeText(HelpFeedback.this, "Success", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDoalog.dismiss();
                        Toast.makeText(HelpFeedback.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("str_helpId", helpid);
                params.put("str_sender", id);
                params.put("str_reciver", sender);
                params.put("str_rate", Integer.toString(ss));
                params.put("str_feedback", userFeddback);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //endregion
}
