package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Thinkpad on 13. 12. 2016.
 */

public class ZdravnikiExpandableAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private ArrayList<Zdravnik> zdravniki;


    public ZdravnikiExpandableAdapter(Context con, ArrayList<Zdravnik> zdr){
        this._context = con;
        this.zdravniki = zdr;
    }

    @Override
    public int getGroupCount() {
        return zdravniki.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return zdravniki.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return zdravniki.get(groupPosition);
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
        TextView naziv = (TextView)convertView.findViewById(R.id.tvNazivDoktorja);
        TextView ime = (TextView)convertView.findViewById(R.id.tvImeDoktorja);
        TextView priimek = (TextView)convertView.findViewById(R.id.tvPriimekDoktorja);
        naziv.setText(zdravniki.get(groupPosition).getNaziv());
        ime.setText(zdravniki.get(groupPosition).getIme());
        priimek.setText(zdravniki.get(groupPosition).getPriimek());

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView tw = null;
        if(convertView == null){
            LayoutInflater li = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.selected_doctor_item, null);
        }

        ((TextView)convertView.findViewById(R.id.tvZdravstveniDom)).setText(zdravniki.get(groupPosition).getIme_doma());
        ((TextView)convertView.findViewById(R.id.tvMailZdravnika)).setText(zdravniki.get(groupPosition).getMail_zdravnika());
        ((TextView)convertView.findViewById(R.id.tvTelefonZdravnika)).setText(zdravniki.get(groupPosition).getTelefon_zdravnika());


        //urnik
        ((TextView)convertView.findViewById(R.id.tvPonedeljek)).setText(zdravniki.get(groupPosition).getUrnik().get(0).getDelovnik());
        ((TextView)convertView.findViewById(R.id.tvTorek)).setText(zdravniki.get(groupPosition).getUrnik().get(1).getDelovnik());
        ((TextView)convertView.findViewById(R.id.tvSreda)).setText(zdravniki.get(groupPosition).getUrnik().get(2).getDelovnik());
        ((TextView)convertView.findViewById(R.id.tvCetrtek)).setText(zdravniki.get(groupPosition).getUrnik().get(3).getDelovnik());
        ((TextView)convertView.findViewById(R.id.tvPetek)).setText(zdravniki.get(groupPosition).getUrnik().get(4).getDelovnik());
        ((TextView)convertView.findViewById(R.id.tvSobota)).setText(zdravniki.get(groupPosition).getUrnik().get(5).getDelovnik());
        ((TextView)convertView.findViewById(R.id.tvNedelja)).setText(zdravniki.get(groupPosition).getUrnik().get(6).getDelovnik());



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
