package com.sasoftgroups.handy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ArunaAmarawardena on 7/18/2017.
 */

public class JSON_StartMessage {
    public static final String JSON_ARRAY = "result";

    public static final String KEY_HID = "hid";
    public static final String KEY_SENDER_ID = "senderID";
    public static final String KEY_MESSEAGE = "message";
    public static final String KEY_DIS = "datetime";
    public static final String KEY_TOPIC = "topic";


    public static String[] hid;
    public static String[] senderID;
    public static String[] message;
    public static String[] datetime;
    public static String[] topic;

    private JSONArray SMessage = null;

    private String json;

    public JSON_StartMessage(String json) {
        this.json = json;
    }

    protected void parseJSON() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            SMessage = jsonObject.getJSONArray(JSON_ARRAY);

            hid = new String[SMessage.length()];
            senderID = new String[SMessage.length()];
            message = new String[SMessage.length()];
            topic = new String[SMessage.length()];
            datetime = new String[SMessage.length()];

            for (int i = 0; i < SMessage.length(); i++) {
                JSONObject jo = SMessage.getJSONObject(i);
                hid[i] = jo.getString(KEY_HID);
                senderID[i] = jo.getString(KEY_SENDER_ID);
                message[i] = jo.getString(KEY_MESSEAGE);
                datetime[i] = jo.getString(KEY_DIS);
                topic[i] = jo.getString(KEY_TOPIC);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
