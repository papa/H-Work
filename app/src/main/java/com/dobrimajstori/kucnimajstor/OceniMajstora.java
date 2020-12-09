package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class OceniMajstora extends AppCompatActivity implements RatingDialogListener {

     static Ugovor prenosUgovora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oceni_majstora);


        showDialog();




    }

    @Override
    public void onPositiveButtonClicked(final int rate, final String comment) {

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


                Calendar trenutoCalendar=Calendar.getInstance();

                Datum trenutniDatum=new Datum(trenutoCalendar.get(Calendar.YEAR),trenutoCalendar.get(Calendar.MONTH),trenutoCalendar.get(Calendar.DAY_OF_MONTH),trenutoCalendar.get(Calendar.HOUR),trenutoCalendar.get(Calendar.MINUTE));

                Komentar komentar=new Komentar(Pocetna.currentKorisnik.getIme(),Pocetna.currentKorisnik.getPrezime(),comment,rate,Pocetna.currentKorisnik.getUriSlike(),trenutniDatum);

                ArrayList<Komentar> komentariPoslodavca=majstorKorisnik.getKomentariPoslodavca();

                if(komentariPoslodavca==null)
                    komentariPoslodavca=new ArrayList<Komentar>();
                komentariPoslodavca.add(komentar);


                Pocetna.baza.getReference("Korisnici").child(prenosUgovora.getMajstor()).child("prosecnaocena").setValue(ukupnaocena/brojposlova);
                Pocetna.baza.getReference("Korisnici").child(prenosUgovora.getMajstor()).child("brojposlova").setValue(brojposlova);
                Pocetna.baza.getReference("Korisnici").child(prenosUgovora.getMajstor()).child("komentariPoslodavca").setValue(komentariPoslodavca);


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


               Intent PokreniActivity=new Intent(OceniMajstora.this, Pocetna.class);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //PokreniActivity.putExtra("fragment","ugovoriposlodavac");
                startActivity(PokreniActivity);
              // finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onNegativeButtonClicked() {

        finish();
    }

    @Override
    public void onNeutralButtonClicked() {

        finish();
    }

    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Pošalji")
                .setNegativeButtonText("Otkaži")
                .setNeutralButtonText("Kasnije")
                .setNoteDescriptions(Arrays.asList("Veoma loše", "Nije dobro", "Dobro", "Vrlo dobro", "Odlično !!!"))
                .setDefaultRating(2)
                .setTitle("Ocenite majstora")
                .setDescription("Molimo Vas izaberite jednu od zvezdica i ocenite majstora")
                .setCommentInputEnabled(true)
                .setDefaultComment("Zadovoljan sam radom majstora !")
                .setStarColor(R.color.orangeApp)
                .setNoteDescriptionTextColor(R.color.orangeApp)
                .setTitleTextColor(R.color.chip_default_text_color)
                .setDescriptionTextColor(R.color.colorSecondaryTextDefaultMaterialLight)
                .setHint("Unesite Vaš komentar ovde ...")
                .setHintTextColor(R.color.chip_default_text_color)
                .setCommentTextColor(R.color.chip_default_text_color)
                .setCommentBackgroundColor(R.color.pdlg_color_white)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(OceniMajstora.this)//                                .setTargetFragment(this, TAG) // only if listener is implemented by fragment
                .show();
    }
}
