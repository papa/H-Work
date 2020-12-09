package com.dobrimajstori.kucnimajstor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.validator.ChipifyingNachoValidator;

public class PostavljanjePoslovaActivity extends AppCompatActivity {


    private static String[] SUGGESTIONS = new String[]{"Popravljanje Kompjutera", "Popravljanje Računara", "Stolarija", "Vodovodne instalacije", "Kanalizacija",
            "Telefoni", "Održavanje kuće", "Krojač", "Šnajder","Sitne Popravke","Popravka Kola","Popravka Automobila","Popravka Displeja","Popravka Televizora",
            "Popravka Frižidera","Popravka Šporeta","Popravka Zvučnika","Popravka Bojlera","Popravka Grejnih Tela","Keramika","Kupatilo","Hitno","Građevina","Električar","Struja"};

    NachoTextView mNachoTextView;

    EditText naslovpos;
    EditText opispos;
    EditText bid;
    int novac;
    Button postavipos;
    String np;
    String op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavljanje_poslova);


        naslovpos=(EditText)findViewById(R.id.naslovposla);
        opispos=(EditText)findViewById(R.id.opisposla);
        bid=(EditText)findViewById(R.id.bid);
        postavipos=(Button)findViewById(R.id.postaviposao);

        mNachoTextView=(NachoTextView)findViewById(R.id.nacho_text_view);
        setupChipTextView(mNachoTextView);

        postavipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(proveri())
                {
                    novac=Integer.parseInt(bid.getText().toString());
                }

            }
        });

    }

    private void setupChipTextView(final NachoTextView nachoTextView) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, SUGGESTIONS);
        nachoTextView.setAdapter(adapter);
        nachoTextView.enableEditChipOnTouch(true, true);
    }

    public boolean proveri()
    {
        np=naslovpos.getText().toString();
        if(TextUtils.isEmpty(np))
        {
            Toast.makeText(PostavljanjePoslovaActivity.this,"Unesite naslov posla",Toast.LENGTH_LONG).show();
            return false;
        }

        op=naslovpos.getText().toString();
        if(TextUtils.isEmpty(op))
        {
            Toast.makeText(PostavljanjePoslovaActivity.this,"Unesite opis posla",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }
}
