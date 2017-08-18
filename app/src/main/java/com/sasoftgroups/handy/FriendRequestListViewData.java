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

public class FriendRequestListViewData extends ArrayAdapter<String> {
    private String[] ids;
    private String[] names;
    private String[] emails;
    private Activity context;

    public FriendRequestListViewData(Activity context, String[] ids, String[] names, String[] emails) {
        super(context, R.layout.friend_requests_listview, ids);
        this.context = context;
        this.ids = ids;
        this.names = names;
        this.emails = emails;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.friend_requests_listview, null, true);
        TextView textViewId = (TextView) listViewItem.findViewById(R.id.lblUserID);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.lblfriendName);

        textViewId.setText(ids[position]);
        textViewName.setText(names[position]);

        return listViewItem;
    }


}
