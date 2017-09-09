package com.sasoftgroups.handy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ArunaAmarawardena on 7/18/2017.
 */

public class helpRequestJSON {
    public static final String JSON_ARRAY = "result";
    public static final String KEY_HID = "hid";
    public static final String KEY_SENDER_ID = "senderID";
    public static final String KEY_TOPIC = "topic";
    public static final String KEY_DIS = "discription";
    public static final String KEY_HELP_TYPE = "HelpType";

    public static String[] hid;
    public static String[] senderID;
    public static String[] topic;
    public static String[] dis;
    public static String[] helptype;
    private JSONArray helps = null;

    private String json;

    public helpRequestJSON(String json) {
        this.json = json;
    }

    protected void parseJSON() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            helps = jsonObject.getJSONArray(JSON_ARRAY);

            hid = new String[helps.length()];
            senderID = new String[helps.length()];
            topic = new String[helps.length()];
            dis = new String[helps.length()];
            helptype = new String[helps.length()];

            for (int i = 0; i < helps.length(); i++) {
                JSONObject jo = helps.getJSONObject(i);
                hid[i] = jo.getString(KEY_HID);
                senderID[i] = jo.getString(KEY_SENDER_ID);
                topic[i] = jo.getString(KEY_TOPIC);
                dis[i] = jo.getString(KEY_DIS);
                helptype[i] = jo.getString(KEY_HELP_TYPE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
