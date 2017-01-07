package com.example.tpoteam.zepnizdravnik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.util.ArrayList;
import java.util.List;


import static android.content.ContentValues.TAG;

public class DatabaseTestActivity extends AppCompatActivity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);

        //rocno dodajanje zdravnikov
        Zdravnik nov = new Zdravnik("Miha","Novak");
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.addZdravnik(nov);

        List<Zdravnik> zdravniki = dbHelper.getAllZdravniki();
        for (Zdravnik doc : zdravniki) {
            Log.d(TAG, "V bazi je zdravnik z imenom " + doc.getIme() + " " + doc.getPriimek()+ "\n");
        }

        //izpis vnosov v bazi Zdravniki
        lv = (ListView) findViewById(R.id.lvDbEntries);
        ArrayAdapter adapter = new ArrayAdapter<Zdravnik>(this,R.layout.database_listview,zdravniki);
        lv.setAdapter(adapter);





    }
}
