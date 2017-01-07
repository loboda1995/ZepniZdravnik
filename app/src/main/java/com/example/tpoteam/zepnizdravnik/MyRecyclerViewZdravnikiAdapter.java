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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.Context.MODE_APPEND;

/**
 * Created by Thinkpad on 19. 12. 2016.
 */

public class MyRecyclerViewZdravnikiAdapter extends RecyclerView.Adapter<MyRecyclerViewZdravnikiAdapter.ViewHolder> {
    private ArrayList<Zdravnik> zdravniki;
    private Context mContext;
    private int expandedPosition = -1;

    private String fileName = "lokalniZdravniki";


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView naziv;
        public TextView ime;
        public TextView priimek;
        public TextView nazivDoma;
        public TextView mailZdravnika;
        public TextView telZdravnika;
        public TextView pon;
        public TextView tor;
        public TextView sre;
        public TextView cet;
        public TextView pet;
        public TextView sob;
        public TextView ned;
        public LinearLayout details;
        public FloatingActionButton add;


        public ViewHolder(View itemView) {
            super(itemView);

            naziv  = (TextView)itemView.findViewById(R.id.tvNazivDoktorja);
            ime = (TextView)itemView.findViewById(R.id.tvImeDoktorja);
            priimek = (TextView)itemView.findViewById(R.id.tvPriimekDoktorja);
            nazivDoma= (TextView)itemView.findViewById(R.id.tvZdravstveniDom);
            mailZdravnika = (TextView)itemView.findViewById(R.id.tvMailZdravnika);
            telZdravnika = (TextView)itemView.findViewById(R.id.tvTelefonZdravnika);
            pon = (TextView)itemView.findViewById(R.id.tvPonedeljek);
            tor = (TextView)itemView.findViewById(R.id.tvTorek);
            sre = (TextView)itemView.findViewById(R.id.tvSreda);
            cet = (TextView)itemView.findViewById(R.id.tvCetrtek);
            pet = (TextView)itemView.findViewById(R.id.tvPetek);
            sob = (TextView)itemView.findViewById(R.id.tvSobota);
            ned = (TextView)itemView.findViewById(R.id.tvNedelja);
            details = (LinearLayout)itemView.findViewById(R.id.docInfo);
            add = (FloatingActionButton) itemView.findViewById(R.id.fab);

        }
    }





    public MyRecyclerViewZdravnikiAdapter(Context c , ArrayList<Zdravnik> zdr) {
        zdravniki = zdr;
        mContext = c;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View doc_item = inflater.inflate(R.layout.doctor_item_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(doc_item);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Zdravnik z = zdravniki.get(position);
        holder.ime.setText(z.getIme());
        holder.priimek.setText(z.getPriimek());
        holder.naziv.setText(z.getNaziv());

        holder.nazivDoma.setText(zdravniki.get(position).getIme_doma());
        holder.mailZdravnika.setText(zdravniki.get(position).getMail_zdravnika());
        final int pos = position;
        holder.mailZdravnika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {zdravniki.get(pos).getMail_zdravnika() });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Naročilo na zdravniški pregled");
                intent.putExtra(Intent.EXTRA_TEXT, "Pozdravljeni,\n");
                if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(Intent.createChooser(intent, ""));
                }

            }
        });

        holder.telZdravnika.setText(zdravniki.get(position).getTelefon_zdravnika());

        //urnik
        holder.pon.setText(zdravniki.get(position).getUrnik().get(0).getDelovnik());
        holder.tor.setText(zdravniki.get(position).getUrnik().get(1).getDelovnik());
        holder.sre.setText(zdravniki.get(position).getUrnik().get(2).getDelovnik());
        holder.cet.setText(zdravniki.get(position).getUrnik().get(3).getDelovnik());
        holder.pet.setText(zdravniki.get(position).getUrnik().get(4).getDelovnik());
        holder.sob.setText(zdravniki.get(position).getUrnik().get(5).getDelovnik());
        holder.ned.setText(zdravniki.get(position).getUrnik().get(6).getDelovnik());

        if (position == expandedPosition) {
            holder.details.setVisibility(View.VISIBLE);
        } else {
            holder.details.setVisibility(View.GONE);
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vnos = holder.ime.getText().toString()+ ":" + holder.priimek.getText().toString();
                if(doesExist(vnos)){
                    save(holder.ime.getText().toString(), holder.priimek.getText().toString());
                }
            }
        });


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

    private boolean doesExist(String text){
        boolean rez = true;
        String line = "";
        FileInputStream fis;
        try {
            try{
                fis = mContext.openFileInput(fileName);
            }catch (FileNotFoundException e){
                FileOutputStream fos = mContext.openFileOutput(fileName, MODE_APPEND);
                fos.write("".getBytes("UTF-8"));
                fos.close();
                fis = mContext.openFileInput(fileName);
            }

            final InputStreamReader isr = new InputStreamReader(fis);
            final BufferedReader br = new BufferedReader(isr);

            while((line = br.readLine()) != null){
                if(line.equals(text)) {
                    return false;
                }
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {}
        return rez;
    }

    private void save(String ime, String priimek) {
        try{
            FileOutputStream fos = mContext.openFileOutput(fileName, MODE_APPEND);
            String zdravnik_ime_priimek = ime + ":" + priimek + "\n";
            fos.write((zdravnik_ime_priimek).getBytes("UTF-8"));
            fos.close();
        } catch (IOException e) {}
    };

    @Override
    public int getItemCount() {
        return zdravniki.size();
    }
}
