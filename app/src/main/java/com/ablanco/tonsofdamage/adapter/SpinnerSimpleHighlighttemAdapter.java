package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ablanco.tonsofdamage.R;

import java.util.List;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
public abstract class SpinnerSimpleHighlighttemAdapter<T> extends ArrayAdapter<T> {

    public SpinnerSimpleHighlighttemAdapter(Context context, List<T> objects) {
        super(context, R.layout.simple_spinner_item, objects);
    }

    protected abstract boolean shouldHighLightItem(int position);

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_item, parent, false);
        }

        if(shouldHighLightItem(position)){
            ((TextView)convertView).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }else {
            ((TextView)convertView).setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
        }

        ((TextView)convertView).setText(getItem(position).toString());

        return convertView;
    }
}
