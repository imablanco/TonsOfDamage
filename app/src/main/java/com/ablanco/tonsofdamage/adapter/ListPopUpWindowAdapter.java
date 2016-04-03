package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class ListPopUpWindowAdapter extends ArrayAdapter<Integer> {

    public ListPopUpWindowAdapter(Context context, List<Integer> actions) {
        super(context, android.R.layout.simple_list_item_1, actions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        ((TextView)convertView).setText(getContext().getString(getItem(position)));

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position);
    }
}
