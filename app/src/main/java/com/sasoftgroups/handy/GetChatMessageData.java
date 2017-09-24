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

public class GetChatMessageData extends ArrayAdapter<String> {
    private String[] hid;
    private String[] senderID;
    private String[] message;
    private String[] datetime;
    private String[] topic;
    private Activity context;

    public GetChatMessageData(Activity context, String[] hid, String[] senderID, String[] message, String[] datetime, String[] topic) {
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
        View listViewItem = inflater.inflate(R.layout.chatlog, null, true);

        TextView Meesage = (TextView) listViewItem.findViewById(R.id.tv_Message);
        TextView Sender = (TextView) listViewItem.findViewById(R.id.tv_sender);
        TextView Time = (TextView) listViewItem.findViewById(R.id.tv_MessageTime);

        Meesage.setText(message[position]);
        Sender.setText(senderID[position]);
        Time.setText(datetime[position]);

        return listViewItem;
    }


}
