package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Thinkpad on 19. 12. 2016.
 */

public class MyRecyclerViewZdravnikiAdapter extends RecyclerView.Adapter<MyRecyclerViewZdravnikiAdapter.ViewHolder>  implements Serializable{
    private ArrayList<Zdravnik> zdravniki;
    private transient Context mContext;
    private boolean inSearch;
    private int expandedPosition = -1;
    private ArrayList<Zdravnik> lokalniZdravniki;

    public static class ViewHolder extends RecyclerView.ViewHolder{

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
        public FloatingActionButton remove;


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
            remove = (FloatingActionButton)itemView.findViewById(R.id.fabRemove);

        }
    }





    public MyRecyclerViewZdravnikiAdapter(Context c , ArrayList<Zdravnik> zdr, boolean inSearch) {
        zdravniki = zdr;
        mContext = c;
        this.inSearch = inSearch;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View doc_item = inflater.inflate(R.layout.doctor_item_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(doc_item);
        lokalniZdravniki = getDoctors();
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


        if(!inSearch){
            holder.remove.show();
        }else{
            holder.remove.hide();
        }


        for(Zdravnik zd : lokalniZdravniki){
            if(zd.getIme().equals(zdravniki.get(position).getIme()) && zd.getPriimek().equals(zdravniki.get(position).getPriimek())){
                //if(!inSearch)
                    holder.remove.show();
            }
        }




        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inSearch){
                    //Zdravnik temp = new Zdravnik(zdravniki.get(position).getIme(), zdravniki.get(position).getPriimek(), zdravniki.get(position).getIme_doma(), zdravniki.get(position).getMail_zdravnika(), zdravniki.get(position).getTelefon_zdravnika(), zdravniki.get(position).getNaziv(), zdravniki.get(position).getID_urnika());
                    Zdravnik temp = zdravniki.get(position);
                    temp.setLocal(true);
                    writeObjectToFile(temp);
                    holder.remove.show();
                    //System.out.println(getDoctors().toString());
                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inSearch){
                    Zdravnik temp = zdravniki.get(position);
                    Boolean t = removeObjectFromFile(temp);

                    zdravniki.remove(temp);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, zdravniki.size());

                    Log.e("stanje remove: ", t.toString());
                }else{
                    Zdravnik temp = zdravniki.get(position);
                    removeObjectFromFile(temp);
                    holder.remove.hide();
                }
            }
        });
    }

    private boolean removeObjectFromFile(Zdravnik o){
        ArrayList<Zdravnik> dosedaj = new ArrayList<Zdravnik>();
        dosedaj = getDoctors();
        for(Zdravnik z:dosedaj){
            if(z.getIme().equals(o.getIme()) && z.getPriimek().equals(o.getPriimek()) && z.getIme_doma().equals(o.getIme_doma())
                    && z.getNaziv().equals(o.getNaziv())){
                dosedaj.remove(z);
                break;
            }
        }

        try {
            FileOutputStream fos = mContext.openFileOutput(MainActivity.fileNameWithDoctors, mContext.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(dosedaj);
            objectOutputStream.close();
            fos.close();
        } catch (IOException e) {
            return false;
        }
        return true;



    }

    private boolean writeObjectToFile(Zdravnik o){
        ArrayList<Zdravnik> dosedaj = new ArrayList<Zdravnik>();
        dosedaj = getDoctors();
        boolean obstaja = false;
        for(Zdravnik z:dosedaj){
            if(z.getIme().equals(o.getIme()) && z.getPriimek().equals(o.getPriimek()) && z.getIme_doma().equals(o.getIme_doma())
                    && z.getNaziv().equals(o.getNaziv())){
                obstaja=true;
                break;
            }
        }
        if(!obstaja){
            dosedaj.add(o);
        }

        try {
            FileOutputStream fos = mContext.openFileOutput(MainActivity.fileNameWithDoctors, mContext.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(dosedaj);
            objectOutputStream.close();
            fos.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private ArrayList<Zdravnik> getDoctors()
    {

        File file = new File(mContext.getFilesDir() + "/" + MainActivity.fileNameWithDoctors);
        ArrayList<Zdravnik> notri = new ArrayList<Zdravnik>();
        if(file.exists()) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = mContext.openFileInput(MainActivity.fileNameWithDoctors);
                ois = new ObjectInputStream(fis);
                notri = (ArrayList<Zdravnik>) ois.readObject();
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
        return zdravniki.size();
    }
}
