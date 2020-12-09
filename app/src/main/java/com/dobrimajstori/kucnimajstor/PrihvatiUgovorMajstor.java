package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class PrihvatiUgovorMajstor extends AppCompatActivity {

    static Ugovor prenosUgovora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prihvati_ugovor_majstor);




        TextView tekstUgovora= findViewById(R.id.tekstUgovoraMajstor);

        tekstUgovora.setText(prenosUgovora.getPoruka());

        final Button prihvatiUgovor= findViewById(R.id.prihvatiugovor);
        final Button raskiniButton= findViewById(R.id.raskiniMajstor);




        FirebaseDatabase.getInstance().getReference("Ugovori").child(prenosUgovora.getIdugovora()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                prenosUgovora=dataSnapshot.getValue(Ugovor.class);
                if(prenosUgovora==null)
                {
                    Toast.makeText(getApplicationContext(),"Ugovor je izbrisan!",Toast.LENGTH_LONG).show();

                    finish();
                }
                else
                {

                    Calendar vremeUgovora = Calendar.getInstance();
                    vremeUgovora.set(prenosUgovora.getDandolaska().getGodina(), prenosUgovora.getDandolaska().getMesec(), prenosUgovora.getDandolaska().getDan(),prenosUgovora.getDandolaska().getSati(),prenosUgovora.getDandolaska().getMinuti(),0);

                    Calendar trenutnoCalendar = Calendar.getInstance();

                    if (vremeUgovora.before(trenutnoCalendar))
                    {
                        raskiniButton.setVisibility(View.GONE);

                        if(!prenosUgovora.isPotpisan())
                        {
                            prihvatiUgovor.setVisibility(View.VISIBLE);
                            prihvatiUgovor.setEnabled(false);
                            prihvatiUgovor.setText("Ugovor je istekao!");
                        }
                        else
                            prihvatiUgovor.setVisibility(View.GONE);
                    }
                    else {

                        prihvatiUgovor.setText("Prihvati ugovor!");
                        prihvatiUgovor.setEnabled(true);

                        if (prenosUgovora.isPotpisan()) {

                            //prihvatiUgovor.setText("Ugovor Prihvacen");
                            //prihvatiUgovor.setEnabled(false);
                            prihvatiUgovor.setVisibility(View.GONE);
                            raskiniButton.setVisibility(View.VISIBLE);
                            if (prenosUgovora.getRaskidMajstor())
                                raskiniButton.setText("Izbriši zahtev");
                            else if (prenosUgovora.getRaskidPoslodavac())
                                raskiniButton.setText("Raskini ugovor");
                            else
                                raskiniButton.setText("Zahtev za raskid");
                        }

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





/*
        if(prenosUgovora.isPotpisan())
        {
            //prihvatiUgovor.setText("Ugovor Prihvacen");
            //prihvatiUgovor.setEnabled(false);
            prihvatiUgovor.setVisibility(View.GONE);
            raskiniButton.setVisibility(View.VISIBLE);
            if(prenosUgovora.getRaskidMajstor())
                raskiniButton.setText("Izbriši zahtev");
            else
            if(prenosUgovora.getRaskidPoslodavac())
                raskiniButton.setText("Raskini ugovor");
            else
                raskiniButton.setText("Zzahtev za raskid");
        }
*/

        raskiniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(prenosUgovora.getRaskidMajstor())
                {

                    final PrettyDialog dialogfilter= new PrettyDialog(PrihvatiUgovorMajstor.this);
                    dialogfilter
                            .setTitle("Izbriši zahtev")
                            .setMessage("Već ste podneli zahtev za raskid ali ga poslodavac još nije prihvatio, da li želite da povučete zahtev za raskid?")
                            .setIcon(R.drawable.pdlg_icon_info)
                            .setIconTint(R.color.colorPrimary)
                            .addButton(
                                    "IZBRIŠI ZAHTEV",     // button text
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {


                                            Pocetna.baza.getReference("Ugovori").child(prenosUgovora.getIdugovora()).child("raskidMajstor").setValue(false);
                                            Toast.makeText(getApplicationContext(),"Zahtev za raskid uspešno izbrisan",Toast.LENGTH_LONG).show();
                                            raskiniButton.setText("Zahtev za raskid");

                                            dialogfilter.dismiss();

                                        }
                                    }  // button background color

                            )
                            .show();

                }
                else
                if(prenosUgovora.getRaskidPoslodavac())
                {
                    final PrettyDialog dialogfilter= new PrettyDialog(PrihvatiUgovorMajstor.this);
                    dialogfilter
                            .setTitle("Raskinite ugovor!")
                            .setMessage("Poslodavac je već podneo zahtev za raskid ugovora, da li želite da raskinete ugovor?")
                            .setIcon(R.drawable.pdlg_icon_info)
                            .setIconTint(R.color.colorPrimary)
                            .addButton(
                                    "RASKINI",     // button text
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {

                                            Pocetna.baza.getReference("SviPoslovi").child(prenosUgovora.getPosao()).child("ugovoren").setValue(false);
                                            Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("PotpisaniUgovoriMajstor").child(prenosUgovora.getIdugovora()).removeValue();
                                            Pocetna.baza.getReference("Korisnici").child(prenosUgovora.getPoslodavac()).child("PotpisaniUgovoriPoslodavac").child(prenosUgovora.getIdugovora()).removeValue();
                                            Pocetna.baza.getReference("Ugovori").child(prenosUgovora.getIdugovora()).removeValue();
                                            Toast.makeText(getApplicationContext(),"Ugovor uspešno raskinut",Toast.LENGTH_LONG).show();

                                            finish();

                                            dialogfilter.dismiss();

                                        }
                                    }  // button background color

                            )
                            .show();



                }
                else
                {

                    final PrettyDialog dialogfilter= new PrettyDialog(PrihvatiUgovorMajstor.this);

                    dialogfilter
                            .setTitle("Zahtev za raskid")
                            .setMessage("Da li želite da pošaljete poslodavcu zahtev za raskid ugovora?")
                            .setIcon(R.drawable.pdlg_icon_info)
                            .setIconTint(R.color.colorPrimary)
                            .addButton(
                                    "POŠALJI ZAHTEV",     // button text
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.colorPrimary, new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {


                                            Pocetna.baza.getReference("Ugovori").child(prenosUgovora.getIdugovora()).child("raskidMajstor").setValue(true);
                                            Toast.makeText(getApplicationContext(),"Zahtev za raskid uspešno poslat",Toast.LENGTH_LONG).show();
                                            raskiniButton.setText("Izbriši zahtev");

                                            dialogfilter.dismiss();
                                        }
                                    }  // button background color

                            )
                            .show();
                }



            }
        });

        prihvatiUgovor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PrettyDialog dialogfilter= new PrettyDialog(PrihvatiUgovorMajstor.this);

                dialogfilter
                        .setTitle("Prihvati ugovor!")
                        .setMessage("Nakon prihvatanja ugovora poslodavac će moći da Vas oceni kada prođe veme predviđeno za obavljanje posla, da li želite da prihvatite ugovor?")
                        .setIcon(R.drawable.pdlg_icon_info)
                        .setIconTint(R.color.colorPrimary)
                        .addButton(
                                "PRIHVATI UGOVOR",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.colorPrimary, new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {



                                        Pocetna.baza.getReference("Ugovori").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                if (dataSnapshot.hasChild(prenosUgovora.getPoslodavac() + Pocetna.currentFirebaseUser.getUid()+ Pocetna.prenosBida.getIdposla()))
                                                {

                                                    prenosUgovora.setPotpisan(true);
                                                    Pocetna.baza.getReference("Ugovori").child(prenosUgovora.getIdugovora()).child("potpisan").setValue(true);
                                                    Pocetna.baza.getReference("SviPoslovi").child(prenosUgovora.getPosao()).child("ugovoren").setValue(true);
                                                    Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("PotpisaniUgovoriMajstor").child(prenosUgovora.getIdugovora()).setValue(prenosUgovora);
                                                    Pocetna.baza.getReference("Korisnici").child(MessageActivity.drugiId).child("PotpisaniUgovoriPoslodavac").child(prenosUgovora.getIdugovora()).setValue(prenosUgovora);



                                                    prihvatiUgovor.setText("Ugovor Prihvacen");
                                                    prihvatiUgovor.setEnabled(false);
                                                    raskiniButton.setVisibility(View.VISIBLE);
                                                    if(prenosUgovora.getRaskidPoslodavac())
                                                        raskiniButton.setText("Raskini ugovor");
                                                    else
                                                        raskiniButton.setText("Zahtev za raskid");

                                                    Toast.makeText(getApplicationContext(),"Ugovor prihvćen!",Toast.LENGTH_LONG).show();
                                                    finish();


                                                }
                                                else
                                                {
                                                    prihvatiUgovor.setText("Ugovor obrisan u medjuvremenu :(");
                                                    prihvatiUgovor.setEnabled(false);
                                                    Toast.makeText(getApplicationContext(),"Ugovor obrisan u medjuvremenu :(",Toast.LENGTH_LONG).show();
                                                    finish();
                                                }



                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        dialogfilter.dismiss();
                                    }
                                }  // button background color

                        )
                        .show();




                /*
                Intent PokreniActivity=new Intent(PrihvatiUgovorMajstor.this, Pocetna.class);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(PokreniActivity);
                */
            }
        });





    }
}
