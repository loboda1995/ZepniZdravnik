package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Luka Loboda on 09-Jan-17.
 */

public class AutocompleteAdapter extends ArrayAdapter<String> {


    public AutocompleteAdapter(Context context, ArrayList<String> inst) {
        super(context, 0, inst);

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String s = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_autocomplete, parent, false);
        }

        TextView display = (TextView) convertView.findViewById(R.id.polje);

        display.setText(s);

        return convertView;
    }
}
