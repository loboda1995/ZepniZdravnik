package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Žiga on 3.1.2017.
 */

public class MyRecyclerViewDomAdapter extends RecyclerView.Adapter<MyRecyclerViewDomAdapter.ViewHolder>{
    private ArrayList<Dom> domovi;
    private Context mContext;
    private int expandedPosition = -1;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ime;
        public TextView mailDoma;
        public TextView telDoma;
        public TextView krajDoma;
        public TextView naslovDoma;
        public LinearLayout details;
        public TextView naziv;
        public FloatingActionButton add;


        public ViewHolder(View itemView) {
            super(itemView);

            ime = (TextView)itemView.findViewById(R.id.tvImeDoma);
            naziv = (TextView)itemView.findViewById(R.id.tvNazivDoma);
            mailDoma= (TextView)itemView.findViewById(R.id.tvMailDoma);
            telDoma = (TextView)itemView.findViewById(R.id.tvTelefonDoma);
            krajDoma = (TextView)itemView.findViewById(R.id.tvKrajDoma);
            naslovDoma = (TextView)itemView.findViewById(R.id.tvNaslovDoma);
            details = (LinearLayout)itemView.findViewById(R.id.docInfo);
            add = (FloatingActionButton) itemView.findViewById(R.id.fab);
        }
    }

    public MyRecyclerViewDomAdapter(Context c , ArrayList<Dom> zdr) {
        domovi = zdr;
        mContext = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View doc_item = inflater.inflate(R.layout.house_item_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(doc_item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Dom d = domovi.get(position);
        holder.ime.setText(d.getIme());

        holder.mailDoma.setText(d.getMail());
        holder.krajDoma.setText(d.getPosta());
        holder.naslovDoma.setText(d.getNaslov());
        holder.naziv.setText("Zdravstveni dom");

        final int pos = position;
        holder.mailDoma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {domovi.get(pos).getMail() });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Informacije");
                intent.putExtra(Intent.EXTRA_TEXT, "Pozdravljeni,\n");
                if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(Intent.createChooser(intent, ""));
                }

            }
        });

        holder.telDoma.setText(domovi.get(position).getTelefon());

        if (position == expandedPosition) {
            holder.details.setVisibility(View.VISIBLE);
        } else {
            holder.details.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedPosition >= 0) {
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }
                if(position == expandedPosition){
                    notifyItemChanged(-1);
                    expandedPosition = -1;
                }else{ expandedPosition = position;
                    notifyItemChanged(expandedPosition);}


            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Zdravnik temp = new Zdravnik(zdravniki.get(position).getIme(), zdravniki.get(position).getPriimek(), zdravniki.get(position).getIme_doma(), zdravniki.get(position).getMail_zdravnika(), zdravniki.get(position).getTelefon_zdravnika(), zdravniki.get(position).getNaziv(), zdravniki.get(position).getID_urnika());
                Dom temp = domovi.get(position);

                writeObjectToFile(temp);
                System.out.println(getDoctors().toString());
            }
        });
    }


    private boolean writeObjectToFile(Dom o){
        ArrayList<Dom> dosedaj = new ArrayList<Dom>();
        dosedaj = getDoctors();
        boolean obstaja = false;
        for(Dom z:dosedaj){
            if(z.getIme().equals(o.getIme()) ){
                obstaja=true;
                break;
            }
        }
        if(!obstaja){
            dosedaj.add(o);
        }

        try {
            FileOutputStream fos = mContext.openFileOutput(MainActivity.fileNameWithHouse, mContext.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(dosedaj);
            objectOutputStream.close();
            fos.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private ArrayList<Dom> getDoctors()
    {

        File file = new File(mContext.getFilesDir() + "/" + MainActivity.fileNameWithHouse);
        ArrayList<Dom> notri = new ArrayList<Dom>();
        if(file.exists()) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = mContext.openFileInput(MainActivity.fileNameWithHouse);
                ois = new ObjectInputStream(fis);
                notri = (ArrayList<Dom>) ois.readObject();
                ois.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notri;
    }
    @Override
    public int getItemCount() {
        return domovi.size();
    }


}
