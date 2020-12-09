package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dobrimajstori.kucnimajstor.Notifications.Data;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;
import java.util.Calendar;

public class InfoUgovor extends AppCompatActivity implements RatingDialogListener {


    static Ugovor prenosUgovora;
    private Button oceniMajstoraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ugovor);


            TextView tekstUgovora= findViewById(R.id.tekstUgovoraMajstor);

            tekstUgovora.setText(prenosUgovora.getPoruka());

            oceniMajstoraButton= findViewById(R.id.oceniMajstora);

            if(!Pocetna.isMajstor)
                oceniMajstoraButton.setVisibility(View.VISIBLE);

            //oceniMajstoraButton.setVisibility(View.GONE);

            Calendar trenutoCalendar=Calendar.getInstance();
            //Datum trenutniDatum=new Datum(trenutoCalendar.get(Calendar.YEAR),trenutoCalendar.get(Calendar.MONTH),trenutoCalendar.get(Calendar.DAY_OF_MONTH),trenutoCalendar.get(Calendar.HOUR),trenutoCalendar.get(Calendar.MINUTE));

            Calendar datumIstekaUgovora=Calendar.getInstance();
            Ugovor ugovor=prenosUgovora;
            datumIstekaUgovora.set(ugovor.getDandolaska().getGodina(),ugovor.getDandolaska().getMesec(),ugovor.getDandolaska().getDan(),ugovor.getDandolaska().getSati(),ugovor.getDandolaska().getMinuti(),0);


            if(datumIstekaUgovora.after(trenutoCalendar))
            {
                oceniMajstoraButton.setEnabled(false);
                oceniMajstoraButton.setText("Ocenite majstora nakon što istekne ugovor");
            }




            oceniMajstoraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    OceniMajstora.prenosUgovora=prenosUgovora;
                    startActivity(new Intent(InfoUgovor.this,OceniMajstora.class));

                }
            });


    }


    @Override
    public void onPositiveButtonClicked(final int rate, String comment) {

        //Brisemo potpisane ugovore

        final String idPosla=prenosUgovora.getPosao();

        String idugovora=Pocetna.currentFirebaseUser.getUid()+prenosUgovora.getMajstor()+idPosla;

        Pocetna.baza.getReference("Korisnici").child(prenosUgovora.getMajstor()).child("PotpisaniUgovoriMajstor").child(idugovora).removeValue();
        Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("PotpisaniUgovoriPoslodavac").child(idugovora).removeValue();
        Pocetna.baza.getReference("Ugovori").child(idugovora).removeValue();


        Pocetna.baza.getReference("Korisnici").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sviKorisniciData) {

                    //Dodavanje prosecne ocene

                    Korisnik majstorKorisnik=sviKorisniciData.child(prenosUgovora.getMajstor()).getValue(Korisnik.class);
                    double prosecnaocena=majstorKorisnik.getProsecnaocena();
                    int brojposlova=majstorKorisnik.getBrojposlova();

                    double ukupnaocena=prosecnaocena*brojposlova;

                    ukupnaocena+=rate;
                    brojposlova++;

                    Pocetna.baza.getReference("Korisnici").child(prenosUgovora.getMajstor()).child("prosecnaocena").setValue(ukupnaocena/brojposlova);
                    Pocetna.baza.getReference("Korisnici").child(prenosUgovora.getMajstor()).child("brojposlova").setValue(brojposlova);


                    //Prolazenje kroz sve korisnike brise bidovanep i pregovore za majstora

                    for(DataSnapshot korisnikData: sviKorisniciData.getChildren())
                    {

                            Korisnik korisnik=korisnikData.getValue(Korisnik.class);


                            //Brisem bidovane svuda
                            if (korisnikData.child("bidovaniposlovi").hasChild(idPosla))
                                Pocetna.baza.getReference("Korisnici").child(korisnik.getId()).child("bidovaniposlovi").child(idPosla).removeValue();

                            //Brisem pregovore za majstore
                            if(korisnikData.child("pregovoriMajstor").hasChild(idPosla))
                                Pocetna.baza.getReference("Korisnici").child(korisnik.getId()).child("pregovoriMajstor").child(idPosla).removeValue();




                    }

                    //Brisem ponudu majstora

                    for(DataSnapshot idMajstoraPonuda: sviKorisniciData.child(Pocetna.currentFirebaseUser.getUid()).child("PonudeMajstora").getChildren())
                        if(idMajstoraPonuda.hasChild(idPosla))
                            Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("PonudeMajstora").child(idMajstoraPonuda.child(idPosla).getValue(Bid.class).getIdmajstora()).child(idPosla).removeValue();


                    //Brisemo pregovore poslodavca za taj poso

                    for(DataSnapshot snapshot : sviKorisniciData.child(Pocetna.currentFirebaseUser.getUid()).child("pregovoriPoslodavac").getChildren())
                    {
                        if(snapshot.hasChild(idPosla))
                        {
                            Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("pregovoriPoslodavac").child(snapshot.child(idPosla).getValue(Bid.class).getIdmajstora()).child(idPosla).removeValue();
                        }
                    }


                    //Brisem postavljeni poso za tog poslodavca
                    Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("postavljeniposlovi").child(idPosla).removeValue();

                    //Brisem ocenjen posao iz baze
                    Pocetna.baza.getReference("SviPoslovi").child(idPosla).removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }


}
