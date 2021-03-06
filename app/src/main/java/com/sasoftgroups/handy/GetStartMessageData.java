package com.sasoftgroups.handy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by ArunaAmarawardena on 7/18/2017.
 */

public class GetStartMessageData extends ArrayAdapter<String> {
    private String[] bothIDs;
    private String[] hid;
    private String[] senderID;
    private String[] message;
    private String[] datetime;
    private String[] topic;
    private String[] senderName;
    private Activity context;

    public GetStartMessageData(Activity context, String[] hid, String[] senderID, String[] senderName, String[] message, String[] datetime, String[] topic, String[] bothIDs) {
        super(context, R.layout.all_start_message_listview, bothIDs);
        this.context = context;
        this.hid = hid;
        this.senderID = senderID;
        this.senderName = senderName;
        this.message = message;
        this.datetime = datetime;
        this.topic = topic;
        this.bothIDs = bothIDs;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.all_start_message_listview, null, true);

        TextView TOPIC = (TextView) listViewItem.findViewById(R.id.tv_Message_Topic);
        TextView Sender = (TextView) listViewItem.findViewById(R.id.tv_Message_Sender);
        TextView HID = (TextView) listViewItem.findViewById(R.id.tv_Message_hid);

        String se = hid[position] + "" + senderID[position];
        TOPIC.setText(topic[position]);
        Sender.setText(senderName[position]);
        HID.setText(se);


        return listViewItem;
    }
}
