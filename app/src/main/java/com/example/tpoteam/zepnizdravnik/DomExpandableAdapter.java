package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Žiga on 3.1.2017.
 */

public class DomExpandableAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<Dom> domovi;


    public DomExpandableAdapter(Context con, ArrayList<Dom> zdrDom){
        this._context = con;
        this.domovi = zdrDom;
    }

    @Override
    public int getGroupCount() {
        return domovi.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return domovi.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return domovi.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //return null;
        if(convertView == null){
            LayoutInflater linflate = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = linflate.inflate(R.layout.doctor_item,null);
        }
        TextView kraj = (TextView)convertView.findViewById(R.id.tvKrajDoma);
        TextView naslov = (TextView)convertView.findViewById(R.id.tvNaslovDoma);
        TextView mail = (TextView)convertView.findViewById(R.id.tvMailDoma);
        TextView telefon = (TextView)convertView.findViewById(R.id.tvTelefonDoma);
        kraj.setText(domovi.get(groupPosition).getKraj());
        naslov.setText(domovi.get(groupPosition).getNaslov());
        mail.setText(domovi.get(groupPosition).getMail());
        telefon.setText(domovi.get(groupPosition).getTelefon());

        return convertView;

    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final int pos;
        pos = groupPosition;
        TextView tw = null;
        if(convertView == null){
            LayoutInflater li = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.selected_house_item, null);
        }

        ((TextView)convertView.findViewById(R.id.tvNazivDoma)).setText(domovi.get(groupPosition).getIme());
        TextView mail = (TextView)convertView.findViewById(R.id.tvMailDoma);
        mail.setText(domovi.get(groupPosition).getMail());
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {domovi.get(pos).getMail() });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Potrebujem informacije");
                intent.putExtra(Intent.EXTRA_TEXT, "Pozdravljeni,\n");
                if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(Intent.createChooser(intent, ""));
                }
            }
        });

        ((TextView)convertView.findViewById(R.id.tvTelefonDoma)).setText(domovi.get(groupPosition).getTelefon());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
