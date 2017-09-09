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

public class GetAllHelpRequestData extends ArrayAdapter<String> {
    private String[] hid;
    private String[] senderID;
    private String[] topic;
    private String[] dis;
    private String[] helptype;
    private Activity context;

    public GetAllHelpRequestData(Activity context, String[] hid, String[] senderID, String[] topic, String[] dis, String[] helptype) {
        super(context, R.layout.all_helps_listview, hid);
        this.context = context;
        this.hid = hid;
        this.senderID = senderID;
        this.topic = topic;
        this.dis = dis;
        this.helptype = helptype;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.all_helps_listview, null, true);

        TextView HID = (TextView) listViewItem.findViewById(R.id.tv_Helps_hid);
        TextView TOPIC = (TextView) listViewItem.findViewById(R.id.tv_Helps_Topic);
        TextView DIS = (TextView) listViewItem.findViewById(R.id.tv_Helps_Dis);

        HID.setText(hid[position]);
        TOPIC.setText(topic[position]);
        DIS.setText(dis[position]);

        return listViewItem;
    }


}
