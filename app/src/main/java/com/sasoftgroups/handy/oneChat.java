package com.sasoftgroups.handy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
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

public class oneChat extends AppCompatActivity {

    ListView chatLog;
    EditText message;

    String id, helpid, sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_chat);

        //Toolbar mt = (Toolbar) findViewById(R.id.)

        chatLog = (ListView) findViewById(R.id.listView_MessageList);
        message = (EditText) findViewById(R.id.edt_Message);

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        id = sharedPref.getString(config.CurrentUserID, "");
        helpid = sharedPref.getString(config.HELPID, "");
        sender = sharedPref.getString(config.Sender, "");

        GETCHAT();

        final Handler handler = new Handler();
        final int delay = 8000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                GETCHAT();
                //Toast.makeText(Messenger.this, "Hi", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, delay);
            }
        }, delay);

        int firstVisiblePosition = chatLog.getFirstVisiblePosition();
        View view = chatLog.getChildAt(0);
        int distFromTop = (view == null) ? 0 : view.getTop();
        chatLog.setSelectionFromTop(firstVisiblePosition, distFromTop);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatmenu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chatmenu_Complete:
                Toast.makeText(oneChat.this, "Complete", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.chatmenu_Report:
                Toast.makeText(oneChat.this, "Report", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void GETCHAT() {
        if (isNetworkAvailable(this) == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_FULL_CHAT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            getmessageJSON(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(oneChat.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("uid", id);
                    params.put("sender", sender);
                    params.put("hid", helpid);
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
    }

    private void getmessageJSON(String json) {
        JSON_ChatMessage pj = new JSON_ChatMessage(json);
        pj.parseJSON();
        GetChatData c2 = new GetChatData(this, JSON_ChatMessage.hid, JSON_ChatMessage.senderID, JSON_ChatMessage.senderName, JSON_ChatMessage.message, JSON_ChatMessage.datetime, JSON_ChatMessage.topic);
        chatLog.setAdapter(c2);
    }

    public void onClickSendMessage(View view) {
        final String mm = message.getText().toString();

        if (!mm.isEmpty()) {
            if (isNetworkAvailable(this) == true) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SEND_MESSAGE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(oneChat.this, "Message", Toast.LENGTH_SHORT).show();
                                GETCHAT();
                                message.setText("");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(oneChat.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("str_helpid", helpid);
                        params.put("str_sender", id);
                        params.put("str_reciver", sender);
                        params.put("str_message", mm);
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
            Toast.makeText(oneChat.this, "Please Type Message to Send.", Toast.LENGTH_LONG).show();
        }
    }

}
