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
    private String[] hid;
    private String[] senderID;
    private String[] message;
    private String[] datetime;
    private String[] topic;
    private Activity context;

    public GetStartMessageData(Activity context, String[] hid, String[] senderID, String[] message, String[] datetime, String[] topic) {
        super(context, R.layout.all_helps_listview, hid);
        this.context = context;
        this.hid = hid;
        this.senderID = senderID;
        this.message = message;
        this.datetime = datetime;
        this.topic = topic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.all_start_message_listview, null, true);

        TextView TOPIC = (TextView) listViewItem.findViewById(R.id.tv_Message_Topic);
        TextView Sender = (TextView) listViewItem.findViewById(R.id.tv_Message_Sender);
        TextView HID = (TextView) listViewItem.findViewById(R.id.tv_Message_hid);

        HID.setText(hid[position]);
        TOPIC.setText(topic[position]);
        Sender.setText(senderID[position]);

        return listViewItem;
    }


}
