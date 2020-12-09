package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InfoPosaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_posao);


        Button prijaviSeButton= (Button) findViewById(R.id.prijaviSe);

        prijaviSeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(InfoPosaoActivity.this,Bidovanje.class);
                startActivity(i);
            }
        });
    }
}
