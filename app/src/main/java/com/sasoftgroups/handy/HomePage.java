package com.sasoftgroups.handy;

import android.app.AlertDialog;
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
import android.widget.Button;
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

public class HomePage extends AppCompatActivity {
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        messegButtonImage();
        notificationButtonImage();
        getUserDetails();
    }

    private void killActivity() {
        finish();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Handy")
                .setMessage("Are you sure you want to close Handy?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();
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

    //region Methods
    public void getUserDetails() {
        if (isNetworkAvailable(this) == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_USER_DETAILS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = sharedPref.edit();
                            editors.putString(config.CurrentUserID, response);
                            editors.commit();
                            //LoadCategory();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(HomePage.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String email = sharedPref.getString(config.EMAIL_SHARED_PREF, "");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
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
    //endregion

    //region Home Button Click

    public void btnNeedHand(View view) {
        Intent needHand = new Intent(HomePage.this, HandRequest.class);
        startActivity(needHand);
    }

    public void onClickGiveHand(View view) {
        Intent giveHand = new Intent(HomePage.this, GiveHand.class);
        startActivity(giveHand);
    }

    public void messegButtonImage() {

        Button mesg = (Button) findViewById(R.id.btnMessages);
        int message = 5;

        if (message == 1) {
            mesg.setBackgroundResource(R.drawable.message1);
        } else if (message == 2) {
            mesg.setBackgroundResource(R.drawable.message2);
        } else if (message == 3) {
            mesg.setBackgroundResource(R.drawable.message3);
        } else if (message == 4) {
            mesg.setBackgroundResource(R.drawable.message4);
        } else if (message == 5) {
            mesg.setBackgroundResource(R.drawable.message5);
        } else if (message == 6) {
            mesg.setBackgroundResource(R.drawable.message6);
        } else if (message == 7) {
            mesg.setBackgroundResource(R.drawable.message7);
        } else if (message == 8) {
            mesg.setBackgroundResource(R.drawable.message8);
        } else if (message == 9) {
            mesg.setBackgroundResource(R.drawable.message9);
        } else if (message == 10) {
            mesg.setBackgroundResource(R.drawable.message10);
        } else if (message > 10) {
            mesg.setBackgroundResource(R.drawable.message10plus);
        } else {
            mesg.setBackgroundResource(R.drawable.messages);
        }

    }

    public void notificationButtonImage() {
        Button notification = (Button) findViewById(R.id.btnNotification);
        int notifi = 50;

        if (notifi == 1) {
            notification.setBackgroundResource(R.drawable.notificaton1);
        } else if (notifi == 2) {
            notification.setBackgroundResource(R.drawable.notificaton2);
        } else if (notifi == 3) {
            notification.setBackgroundResource(R.drawable.notificaton3);
        } else if (notifi == 4) {
            notification.setBackgroundResource(R.drawable.notificaton4);
        } else if (notifi == 5) {
            notification.setBackgroundResource(R.drawable.notificaton5);
        } else if (notifi == 6) {
            notification.setBackgroundResource(R.drawable.notificaton6);
        } else if (notifi == 7) {
            notification.setBackgroundResource(R.drawable.notificaton7);
        } else if (notifi == 8) {
            notification.setBackgroundResource(R.drawable.notificaton8);
        } else if (notifi == 9) {
            notification.setBackgroundResource(R.drawable.notificaton9);
        } else if (notifi == 10) {
            notification.setBackgroundResource(R.drawable.notificaton10);
        } else if (notifi > 10) {
            notification.setBackgroundResource(R.drawable.notificaton10plus);
        } else {
            notification.setBackgroundResource(R.drawable.notificaton);
        }
    }

    public void clickMyProfile(View view) {
        Intent myProfile = new Intent(HomePage.this, MyProfile.class);
        startActivity(myProfile);
    }

    public void clickFriendsList(View view) {
        Intent FriendsList = new Intent(HomePage.this, FriednsList.class);
        startActivity(FriendsList);
    }

    public void btn_Logout(View view) {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {


                SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(config.LOGGEDIN_SHARED_PREF, false);
                editor.putString(config.EMAIL_SHARED_PREF, "");
                editor.commit();

                Intent intent = new Intent(HomePage.this, LoginPage.class);
                startActivity(intent);

            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void onclick_Messages(View view) {
        Intent Message = new Intent(HomePage.this, Messenger.class);
        startActivity(Message);
    }
    //endregion

}
