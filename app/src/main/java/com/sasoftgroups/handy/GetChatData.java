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

public class GetChatData extends ArrayAdapter<String> {
    private String[] bothIDs;
    private String[] hid;
    private String[] senderID;
    private String[] message;
    private String[] datetime;
    private String[] topic;
    private String[] senderName;
    private Activity context;

    public GetChatData(Activity context, String[] hid, String[] senderID, String[] senderName, String[] message, String[] datetime, String[] topic) {
        super(context, R.layout.chatlog, hid);
        this.context = context;
        this.hid = hid;
        this.senderID = senderID;
        this.senderName = senderName;
        this.message = message;
        this.datetime = datetime;
        this.topic = topic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View ChatLogItems = inflater.inflate(R.layout.chatlog, null, true);

        TextView TOPIC = (TextView) ChatLogItems.findViewById(R.id.tv_Message);
        TextView Sender = (TextView) ChatLogItems.findViewById(R.id.tv_sender);
        TextView HID = (TextView) ChatLogItems.findViewById(R.id.tv_MessageTime);

        TOPIC.setText(message[position]);
        Sender.setText(senderName[position]);
        HID.setText(datetime[position]);

        return ChatLogItems;
    }
}
