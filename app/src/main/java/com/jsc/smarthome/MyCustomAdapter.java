/*
 * Copyright (c) 2017. Jeneral Samopal Company
 * Design and Programming by Alex Dovby
 */

package com.jsc.smarthome;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter {
    private ArrayList<String> mListItems;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public MyCustomAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        mListItems = arrayList;

        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return mListItems.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {
        return null;
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {
        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.list_item, null);
        }

        //get the string item from the position "position" from array list to put it on the TextView

        String stringItem = mListItems.get(position);
        // int bgColor = R.drawable.item_exo_bg;
        // int textColor = R.color.colorPrimary;
        int iconID = R.drawable.ic_menu_test_measurement;


        if (stringItem != null) {
            TextView itemDate = (TextView) view.findViewById(R.id.list_item_text_date);
            TextView itemTime = (TextView) view.findViewById(R.id.list_item_text_time);
            TextView itemDelta = (TextView) view.findViewById(R.id.list_item_text_delta);
            TextView itemValue = (TextView) view.findViewById(R.id.list_item_text_value);

            // set icon -----------------------------------------
            ImageView itemIcon = (ImageView) view.findViewById(R.id.list_item_image_view);
            itemIcon.setImageResource(iconID);

            if (itemDate != null) {
                JSONObject obj = new JSONObject();
                try {
                    obj = new JSONObject(stringItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                itemDate.setText(obj.optString("date"));
               itemTime.setText(obj.optString("time"));
               String str_tmp = "Time: " + obj.optString("time");
               itemTime.setText(str_tmp);
               str_tmp = obj.optString("value") + "°C";
               itemValue.setText(str_tmp);
                itemIcon.setColorFilter(getIconColor(obj.optString("attribute")));
                itemDelta.setText(obj.optString("delta"));
                if (obj.optBoolean("warmer")) {
                    itemDelta.setTextColor(ContextCompat.getColor(context, R.color.item_text_warmer));
                }
                // view.setBackgroundResource(bgColor);
                // itemName.setBackgroundColor(ContextCompat.getColor(context, bgColor));
            }
        }
        // this method must return the view corresponding to the data at the specified position.
        return view;
    }

    private Integer getIconColor(String attribute) {
        // System.out.println("attribute : |" + attribute + "|");
        switch (attribute) {
            case "hot":
                return Color.argb(255, 255, 0, 33);
            case "warm":
                return Color.argb(255, 255, 204, 0);
            default:
                return Color.argb(255, 0, 204, 255);
        }
    }
}