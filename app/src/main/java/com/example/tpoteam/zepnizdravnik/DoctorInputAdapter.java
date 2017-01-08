package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Luka Loboda on 08-Jan-17.
 */

public class DoctorInputAdapter extends ArrayAdapter<Zdravnik> {

    public DoctorInputAdapter(Context context, ArrayList<Zdravnik> doctors) {
        super(context, 0, doctors);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Zdravnik z = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_doctor, parent, false);
        }

        TextView displayIme = (TextView) convertView.findViewById(R.id.ime);
        TextView displayPriimek = (TextView) convertView.findViewById(R.id.priimek);

        displayIme.setText(z.getIme());
        displayPriimek.setText(z.getPriimek());

        return convertView;
    }
}
