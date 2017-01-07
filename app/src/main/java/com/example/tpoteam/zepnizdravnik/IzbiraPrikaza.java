package com.example.tpoteam.zepnizdravnik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Žiga on 12.12.2016.
 */


public class IzbiraPrikaza extends AppCompatActivity {
    private boolean izbran = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_display);

        final RadioButton zd = (RadioButton) findViewById(R.id.radioButton);
        final RadioButton zdravnik = (RadioButton) findViewById(R.id.radioButton2);


        Button prikazi_izbrano  = (Button) findViewById(R.id.prikaz);
        TextView opis = (TextView) findViewById(R.id.TVOpis);


        opis.setText("Prikazala se vam bodo imena zdravstvenih domov urejenih po abecednem vrstnem redu. S klikom nanje dobite podrobnejše informacije.");

        RadioGroup groupIzbira = (RadioGroup) findViewById(R.id.IzbiraGroup);
        groupIzbira.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == zd.getId()){
                    izbran = false;
                    TextView opis = (TextView) findViewById(R.id.TVOpis);
                    opis.setText("Prikazala se vam bodo imena zdravstvenih domov urejenih po abecednem vrstnem redu. S klikom nanje dobite podrobnejše informacije.");

                }else if(checkedId == zdravnik.getId()){
                    izbran = true;
                    TextView opis = (TextView) findViewById(R.id.TVOpis);
                    opis.setText("Prikazala se vam bodo imena zdravnikov urejenih po abecednem vrstnem redu. S klikom nanje dobite podrobnejše informacije.");

                }
            }
        });

        prikazi_izbrano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent baza_prikaz = new Intent(IzbiraPrikaza.this, ActivityJsonTest.class);
                baza_prikaz.putExtra("Izbira", getIzber());
                startActivity(baza_prikaz);
                finish();
            }
        });
    }

    private boolean getIzber(){
        return izbran;
    }
}
