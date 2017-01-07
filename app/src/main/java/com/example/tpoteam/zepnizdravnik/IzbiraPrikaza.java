package com.example.tpoteam.zepnizdravnik;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Žiga on 12.12.2016.
 */


public class IzbiraPrikaza extends AppCompatActivity {

    JSONArray jsonZdravniki, jsonDomovi;
    ArrayList<Zdravnik> listaZdravnikov = new ArrayList<>();
    ArrayList<Dom> listaDomov = new ArrayList<>();
    private ProgressWheel pw;

    private LinearLayout master;
    private LinearLayout search;
    private LinearLayout searchDomovi;
    private LinearLayout searchZdravniki;

    private RadioGroup searchTypeGroup;
    private EditText inputDomIme;
    private EditText inputDomPosta;
    private EditText inputDomKraj;
    private EditText inputImeZdrav;
    private EditText inputPriimekZdrav;
    private EditText inputImeDomZdrav;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        getAllFields();

    }

    private void  getAllFields(){
        master = (LinearLayout) findViewById(R.id.masterField);
        master.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(search.getVisibility() == View.GONE){
                    search.setVisibility(View.VISIBLE);
                    return;
                }
                if(search.getVisibility() == View.VISIBLE){
                    search.setVisibility(View.GONE);
                    return;
                }
            }
        });
        search = (LinearLayout) findViewById(R.id.search);
        searchDomovi = (LinearLayout) findViewById(R.id.searchDomovi);
        searchZdravniki = (LinearLayout) findViewById(R.id.searchZdravniki);

        searchTypeGroup = (RadioGroup) findViewById(R.id.searchTypeGroup);
        searchTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioDomovi){
                    searchDomovi.setVisibility(View.VISIBLE);
                    searchZdravniki.setVisibility(View.GONE);
                }else if(checkedId == R.id.radioZdravniki){
                    searchDomovi.setVisibility(View.GONE);
                    searchZdravniki.setVisibility(View.VISIBLE);
                }
            }
        });

        inputDomIme = (EditText) findViewById(R.id.imeDoma);
        inputDomPosta = (EditText) findViewById(R.id.postaDoma);
        inputDomKraj = (EditText) findViewById(R.id.krajDoma);

        inputImeZdrav = (EditText) findViewById(R.id.imeZdravnika);
        inputPriimekZdrav = (EditText) findViewById(R.id.priimekZdravnika);
        inputImeDomZdrav = (EditText) findViewById(R.id.imeDomaZdravnika);

        searchButton = (Button) findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(search.getVisibility() == View.GONE){
                    search.setVisibility(View.VISIBLE);
                    return;
                }
                pw.setVisibility(View.VISIBLE);
                pw.spin();
                listaDomov.clear();
                listaZdravnikov.clear();
                if(searchDomovi.getVisibility(
                ) == View.VISIBLE){
                    prikazZdravstvenihDomov();
                }else{
                    prikazZdravnikov();
                }
            }
        });

        pw = (ProgressWheel)findViewById(R.id.progress_wheel);
    }

    private void prikazZdravstvenihDomov(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://zepnizdravnik.azurewebsites.net/index.php/homeInfoJSON")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    jsonDomovi = new JSONArray(responseData);

                    for(int i=0; i<jsonDomovi.length(); i++) {
                        JSONObject zdrDom = (JSONObject) jsonDomovi.get(i);

                        Dom d = new Dom(zdrDom.getString("Ime_doma"), zdrDom.getString("Naslov"), zdrDom.getString("Kraj"), zdrDom.getString("Mail_dom"), zdrDom.getString("Postna_stevilka"), zdrDom.getString("Telefon_dom"));

                        listaDomov.add(d);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                IzbiraPrikaza.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            pw.stopSpinning();
                            pw.setVisibility(View.GONE);
                            search.setVisibility(View.GONE);

                            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

                            RecyclerView.Adapter mAdapter = new MyRecyclerViewDomAdapter(IzbiraPrikaza.this, listaDomov);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(IzbiraPrikaza.this));
                            mRecyclerView.setAdapter(mAdapter);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

    private void prikazZdravnikov(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                //spremeniti na naslov, ki bo na azure
                .url("http://zepnizdravnik.azurewebsites.net/index.php/doctorInfoJSON")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }


            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    //Log.e("vrne", responseData.toString());
                    jsonZdravniki = new JSONArray(responseData);

                    for(int i=0; i<jsonZdravniki.length(); i++) {
                        JSONObject zdr = (JSONObject) jsonZdravniki.get(i);


                        Zdravnik t = new Zdravnik(zdr.getString("Ime"), zdr.getString("Priimek"), zdr.getString("Ime_doma"),
                                zdr.getString("Mail_zdravnik"),
                                zdr.getString("Telefon_zdravnik"),
                                zdr.getString("Naziv"),
                                zdr.getString("ID_urnika"));

                        ArrayList<Dan> dneviUrnik = new ArrayList<>();
                        String[] dnevi = {"Ponedeljek", "Torek", "Sreda", "Cetrtek", "Petek", "Sobota", "Nedelja"};
                        for (int j = 0; j < dnevi.length; j++) {
                            dneviUrnik.add(new Dan(dnevi[j], zdr.getString(dnevi[j])));
                        }
                        t.setUrnik(dneviUrnik);
                        listaZdravnikov.add(t);



                    }

                    IzbiraPrikaza.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                pw.stopSpinning();
                                pw.setVisibility(View.GONE);
                                search.setVisibility(View.GONE);


                                RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

                                RecyclerView.Adapter mAdapter = new MyRecyclerViewZdravnikiAdapter(IzbiraPrikaza.this,listaZdravnikov);
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(IzbiraPrikaza.this));
                                mRecyclerView.setAdapter(mAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
