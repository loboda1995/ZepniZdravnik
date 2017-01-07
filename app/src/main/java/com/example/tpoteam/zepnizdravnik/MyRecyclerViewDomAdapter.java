package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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


        public ViewHolder(View itemView) {
            super(itemView);

            ime = (TextView)itemView.findViewById(R.id.tvImeDoma);
            naziv = (TextView)itemView.findViewById(R.id.tvNazivDoma);
            mailDoma= (TextView)itemView.findViewById(R.id.tvMailDoma);
            telDoma = (TextView)itemView.findViewById(R.id.tvTelefonDoma);
            krajDoma = (TextView)itemView.findViewById(R.id.tvKrajDoma);
            naslovDoma = (TextView)itemView.findViewById(R.id.tvNaslovDoma);
            details = (LinearLayout)itemView.findViewById(R.id.docInfo);
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
        holder.krajDoma.setText(d.getNaslov());
        holder.naslovDoma.setText(d.getKraj());
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
    }
    @Override
    public int getItemCount() {
        return domovi.size();
    }


}
