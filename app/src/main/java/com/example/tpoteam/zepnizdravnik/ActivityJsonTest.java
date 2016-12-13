package com.example.tpoteam.zepnizdravnik;

import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityJsonTest extends AppCompatActivity {
    JSONArray jsonZdravniki;
    ArrayList<Zdravnik> listaZdravnikov = new ArrayList<>();
    ArrayAdapter adapter = new ArrayAdapter<Zdravnik>(this,R.layout.database_listview,listaZdravnikov);
    boolean razparsano = false;
    boolean crash = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_test);

        Bundle b = this.getIntent().getExtras();
        if(b.getBoolean("Izbira")){
            //prikazZdravstvenihDomov();
        }
        else{
            prikazZdravnikov();
        }
    }

/*

    private void prikazZdravstvenihDomov(){
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
                        JSONObject zdrDom = (JSONObject) jsonZdravniki.get(i);


                        Dom t = new Zdravnik(zdrDom.getString("Ime"), zdrDom.getString("Priimek"), zdrDom.getString("Ime_doma"),
                                zdrDom.getString("Mail_zdravnik"),
                                zdrDom.getString("Telefon_zdravnik"),
                                zdrDom.getString("Naziv"),
                                zdrDom.getString("ID_urnika"));

                        ArrayList<Dan> dneviUrnik = new ArrayList<>();
                        String[] dnevi = {"Ponedeljek", "Torek", "Sreda", "Cetrtek", "Petek", "Sobota", "Nedelja"};
                        for (int j = 0; j < dnevi.length; j++) {
                            dneviUrnik.add(new Dan(dnevi[j], zdrDom.getString(dnevi[j])));
                        }
                        t.setUrnik(dneviUrnik);
                        listaZdravnikov.add(t);



                    }
                    //Log.e("doctor", listaZdravnikov.get(0).ime);
                    razparsano = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    crash = true;
                }
            }


        });
        while(!razparsano && !crash){}
        ListView lv = (ListView) findViewById(R.id.lvJson);
        adapter.setDropDownViewResource(R.layout.activity_database_test);
        lv.setAdapter(adapter);
    }

*/







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
                    //Log.e("doctor", listaZdravnikov.get(0).ime);
                    razparsano = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    crash = true;
                }
            }


        });
        while(!razparsano && !crash){}
        ListView lv = (ListView) findViewById(R.id.lvJson);
        adapter.setDropDownViewResource(R.layout.activity_database_test);
        lv.setAdapter(adapter);
    }
}
