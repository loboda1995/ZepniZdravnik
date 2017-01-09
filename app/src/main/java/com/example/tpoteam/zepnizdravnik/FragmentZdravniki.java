package com.example.tpoteam.zepnizdravnik;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentZdravniki extends Fragment {
    ArrayList<Zdravnik> shranjeniZdravniki = new ArrayList<>();
    Context c;
    RecyclerView rc;

    public FragmentZdravniki() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        shranjeniZdravniki = getDoctors();
        return inflater.inflate(R.layout.fragment_zdravniki, container, false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shranjeniZdravniki = new ArrayList<>();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rc = (RecyclerView)view.findViewById(R.id.my_recycler_view);
        RecyclerView.Adapter mAdapter = new MyRecyclerViewZdravnikiAdapter(c.getApplicationContext(), shranjeniZdravniki);
        rc.setLayoutManager(new LinearLayoutManager(c.getApplicationContext()));
        rc.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        c = context;
    }

    private ArrayList<Zdravnik> getDoctors()
    {

        ArrayList<Zdravnik> notri = new ArrayList<Zdravnik>();
        try{
            File file = new File(c.getApplicationContext().getFilesDir() + "/" + MainActivity.fileNameWithDoctors);

            if(file.exists()) {
                FileInputStream fis = null;
                ObjectInputStream ois = null;
                try {
                    fis = c.openFileInput(MainActivity.fileNameWithDoctors);
                    ois = new ObjectInputStream(fis);
                    notri = (ArrayList<Zdravnik>) ois.readObject();
                    ois.close();
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return notri;
    }

}
