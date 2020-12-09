package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PostavljanjePoslovaActivity extends AppCompatActivity
{


    static String[] kategorije = new String[]{"Popravljanje Kompjutera", "Popravljanje Računara", "Stolarija", "Vodovodne instalacije", "Kanalizacija",
            "Telefoni", "Održavanje kuće", "Krojač", "Šnajder","Sitne Popravke","Popravka Kola","Popravka Automobila","Popravka Displeja","Popravka Televizora",
            "Popravka Frižidera","Popravka Šporeta","Popravka Zvučnika","Popravka Bojlera","Popravka Grejnih Tela","Keramika","Kupatilo","Hitno","Građevina","Električar","Struja"};

    NachoTextView mNachoTextView;

    private EditText naslovpos;
    private EditText opispos;
    private int novac;
    private Button postavipos;
    private String np;
    private String op;
    private DatabaseReference poslovi;
    private String idkorisnika;
    private Posao p;
    private String idposla;
    private DatabaseReference postavljeniposlovi;
    private ArrayList<String> posp;
    private TextView budzet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavljanje_poslova);


        naslovpos= findViewById(R.id.naslovposla);
        opispos= findViewById(R.id.opisposla);
        budzet=(EditText)findViewById(R.id.budzet);
        postavipos= findViewById(R.id.postaviposao);
        posp=new ArrayList<String>();



        idkorisnika= Pocetna.currentFirebaseUser.getUid();
        poslovi= FirebaseDatabase.getInstance().getReference("SviPoslovi");
        postavljeniposlovi=FirebaseDatabase.getInstance().getReference("Korisnici").child(idkorisnika).child("postavljeniposlovi");

        /*postavljeniposlovi.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String str=dataSnapshot.getValue(String.class);
                prihvaceniBidovi.add(str);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        // Prepravljeno ovo gore bolje je i radi

       /* postavljeniposlovi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                posp=dataSnapshot.getValue(t);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        mNachoTextView= findViewById(R.id.nacho_text_view);
        setupChipTextView(mNachoTextView);

        postavipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(proveri())
                {
                    novac=Integer.parseInt(budzet.getText().toString());
                    np=naslovpos.getText().toString();
                    op=opispos.getText().toString();

                    idposla=poslovi.push().getKey();

                    List<String> kategorije=mNachoTextView.getChipValues();

                    ArrayList<String> bidovi=new ArrayList<>();
                    bidovi.add("Bid");

                    Calendar trenutoCalendar=Calendar.getInstance();

                    Datum trenutniDatum=new Datum(trenutoCalendar.get(Calendar.YEAR),trenutoCalendar.get(Calendar.MONTH),trenutoCalendar.get(Calendar.DAY_OF_MONTH),trenutoCalendar.get(Calendar.HOUR),trenutoCalendar.get(Calendar.MINUTE));



                    p=new Posao(idposla,np,op,idkorisnika,kategorije,bidovi,novac,trenutniDatum,Pocetna.currentKorisnik.getLat(),Pocetna.currentKorisnik.getLon(),Pocetna.currentKorisnik.getLokacija());

                    poslovi.child(idposla).setValue(p);


                    Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("postavljeniposlovi").child(idposla).setValue(idposla);



                    Intent PokreniActivity=new Intent(getApplicationContext(), Pocetna.class);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(PokreniActivity);

                }

            }
        });

    }

    private void setupChipTextView(final NachoTextView nachoTextView) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, kategorije);
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
        List<String> kategorije=mNachoTextView.getChipValues();
        if(kategorije.isEmpty()) {
            Toast.makeText(PostavljanjePoslovaActivity.this, "Unesite kategoriju posla", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
}
