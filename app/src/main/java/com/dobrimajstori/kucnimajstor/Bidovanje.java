package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Calendar;

public class Bidovanje extends AppCompatActivity {

    private EditText predstavljanje;
    private EditText bidovanje;
    private Button posaljip;
    private String pred;
    private int novac;
    private DatabaseReference bidoviRef;
    private Bid b;
    private String idbid;
    private String imePrezime;
    private DatabaseReference bidovaniPosloviRef;
    private DatabaseReference ponudeMajstoraRef;
    private Datum datumBidovanja;
    private CardView ponudaKartica;

    private ArrayList<String> posaobidovi;
    private DatabaseReference bidoviKodPoslovaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidovanje);

        predstavljanje= findViewById(R.id.predstavljanje);
        bidovanje= findViewById(R.id.bid);
        posaljip= findViewById(R.id.posaljiponudu);
        ponudaKartica= findViewById(R.id.ponudaKartica);

        if(Pocetna.currentKorisnik.isSkola())
        {
            ponudaKartica.setVisibility(View.GONE);
            predstavljanje.setHint("Predstavite svoju školu, svoje učenike, njihove veštine i iskustva kako bi poslodavac znao da li su oni odgovarajući za njegov posao...");
        }

        bidoviRef = FirebaseDatabase.getInstance().getReference("Bidovi");
        bidovaniPosloviRef =FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("bidovaniposlovi");
        ponudeMajstoraRef =FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.prenosPosla.getIdposlodavca()).child("PonudeMajstora");

        bidoviKodPoslovaRef =FirebaseDatabase.getInstance().getReference("SviPoslovi").child(Pocetna.prenosPosla.getIdposla()).child("Bidovi");

        posaobidovi=new ArrayList<>();



        posaljip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Pocetna.currentKorisnik.isSkola())
                    novac=0;
                else
                    novac=Integer.parseInt(bidovanje.getText().toString());
                
               pred=predstavljanje.getText().toString();

               if(pred.equals(""))
               {
                   Toast.makeText(Bidovanje.this,"Unesite nesto o sebi u polju za predstavljanje",Toast.LENGTH_LONG).show();
               }
               else
               {

                   FirebaseDatabase.getInstance().getReference("SviPoslovi").child(Pocetna.prenosPosla.getIdposla()).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                           if(dataSnapshot.getValue(Posao.class)!=null && !dataSnapshot.getValue(Posao.class).isUgovoren())
                           {

                               Calendar trenutoCalendar=Calendar.getInstance();

                               Datum trenutniDatum=new Datum(trenutoCalendar.get(Calendar.YEAR),trenutoCalendar.get(Calendar.MONTH),trenutoCalendar.get(Calendar.DAY_OF_MONTH),trenutoCalendar.get(Calendar.HOUR),trenutoCalendar.get(Calendar.MINUTE));


                               //Postavljanje bida kod odgovarajuceg posla pod id majstora(korisnika)koji je bidovao
                               b=new Bid(novac,Pocetna.prenosPosla.getIdposla(),pred,Pocetna.currentFirebaseUser.getUid(),trenutniDatum,Pocetna.prenosPosla.getIdposlodavca(),Pocetna.currentKorisnik.isSkola());
                               bidoviKodPoslovaRef.child(Pocetna.currentFirebaseUser.getUid()).setValue(b);

                               //Dodavanje id posla kod korisnika(majstora) koji je bidovao
                               bidovaniPosloviRef.child(Pocetna.prenosPosla.getIdposla()).setValue(Pocetna.prenosPosla.getIdposla());

                               //Dodavanje id bida kod korisnika(poslodavca) ciji je posao bidovan kao ponudu majstora
                               ponudeMajstoraRef.child(b.getIdmajstora()).child(b.getIdposla()).setValue(b);


                               Toast.makeText(Bidovanje.this,"Prijavljivanje uspelo",Toast.LENGTH_LONG).show();


                               startActivity(new Intent(Bidovanje.this,Pocetna.class));


                           }
                           else{

                               Toast.makeText(Bidovanje.this,"Prijavljivanje neuspelo, posao više nije u ponudi",Toast.LENGTH_LONG).show();
                               startActivity(new Intent(Bidovanje.this,Pocetna.class));
                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

               }

            }
        });

    }
}
