package com.sasoftgroups.handy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        messegButtonImage();
        notificationButtonImage();

    }

    public void btnNeedHand(View view) {
        Intent needHand = new Intent(HomePage.this, HandRequest.class);
        startActivity(needHand);
    }

    public void messegButtonImage(){

        Button mesg = (Button) findViewById(R.id.btnMessages);
        int message = 5;

        if (message == 1){
            mesg.setBackgroundResource(R.drawable.message1);
        }
        else if (message == 2){
            mesg.setBackgroundResource(R.drawable.message2);
        }
        else if (message == 3){
            mesg.setBackgroundResource(R.drawable.message3);
        }
        else if (message == 4){
            mesg.setBackgroundResource(R.drawable.message4);
        }
        else if (message == 5){
            mesg.setBackgroundResource(R.drawable.message5);
        }
        else if (message == 6){
            mesg.setBackgroundResource(R.drawable.message6);
        }
        else if (message == 7){
            mesg.setBackgroundResource(R.drawable.message7);
        }
        else if (message == 8){
            mesg.setBackgroundResource(R.drawable.message8);
        }
        else if (message == 9){
            mesg.setBackgroundResource(R.drawable.message9);
        }
        else if (message == 10){
            mesg.setBackgroundResource(R.drawable.message10);
        }
        else if (message > 10){
            mesg.setBackgroundResource(R.drawable.message10plus);
        }
        else {
            mesg.setBackgroundResource(R.drawable.messages);
        }

    }

    public void notificationButtonImage(){
        Button notification = (Button)findViewById(R.id.btnNotification);
        int notifi = 50;

        if (notifi == 1){
            notification.setBackgroundResource(R.drawable.notificaton1);
        }
        else if (notifi == 2){
            notification.setBackgroundResource(R.drawable.notificaton2);
        }
        else if (notifi == 3){
            notification.setBackgroundResource(R.drawable.notificaton3);
        }
        else if (notifi == 4){
            notification.setBackgroundResource(R.drawable.notificaton4);
        }
        else if (notifi == 5){
            notification.setBackgroundResource(R.drawable.notificaton5);
        }
        else if (notifi == 6){
            notification.setBackgroundResource(R.drawable.notificaton6);
        }
        else if (notifi == 7){
            notification.setBackgroundResource(R.drawable.notificaton7);
        }
        else if (notifi == 8){
            notification.setBackgroundResource(R.drawable.notificaton8);
        }
        else if (notifi == 9){
            notification.setBackgroundResource(R.drawable.notificaton9);
        }
        else if (notifi == 10){
            notification.setBackgroundResource(R.drawable.notificaton10);
        }
        else if (notifi > 10 ){
            notification.setBackgroundResource(R.drawable.notificaton10plus);
        }
        else {
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
}
