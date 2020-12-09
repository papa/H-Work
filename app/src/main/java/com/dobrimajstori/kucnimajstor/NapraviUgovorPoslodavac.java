package com.dobrimajstori.kucnimajstor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NapraviUgovorPoslodavac extends AppCompatActivity
{
    String nazivPosla;
    String imeMajstora;
    String prezimeMajstora;
    TextView cenaPosla;

    Button napraviUgovor;
    DatabaseReference ugovori;
    String datum;
    String vreme;
    String cenaPoslaStr;

    String idug;
    String idposla;
    String poslodavacId;
    String majstorId;
    int cena;
    double lat;
    double lon;
    Posao posao;

    Button setDatum;
    Button setVreme;
    TextView tekstUgovora;

    String tekstUgovoraStr;

    Datum datumDolaska;

    String lokacija;

    Korisnik majstor;

    Ugovor ugovor;

    Button oceniMajstora;

    private boolean ugovorPostoji;


    Button izbrisiUgovorButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_napravi_ugovor);

        loadViews();

        datum="*izaberite datum*";
        vreme="*izaberite vreme*";


        cenaPoslaStr=String.valueOf(Pocetna.prenosBida.getNovac());
        lokacija="izaberite vašu lokaciju";
        if(Pocetna.currentKorisnik.getLon()!=0 && Pocetna.currentKorisnik.getLat()!=0)
            lokacija=Pocetna.currentKorisnik.getLokacija();

        datumDolaska=new Datum();

        idug=Pocetna.currentFirebaseUser.getUid() + MessageActivity.drugiId + Pocetna.prenosBida.getIdposla();


        Pocetna.baza.getReference("Ugovori").child(idug).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ugovor=dataSnapshot.getValue(Ugovor.class);

                if(ugovor==null)
                {

                    Pocetna.baza.getReference("SviPoslovi").child(Pocetna.prenosBida.getIdposla()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            posao=dataSnapshot.getValue(Posao.class);
                            nazivPosla=posao.getNaslovposla();
                            if(posao.isUgovoren())
                            {
                                napraviUgovor.setText("Ugovor potpisan sa drugim majstorom");
                                napraviUgovor.setEnabled(false);
                                setDatum.setEnabled(false);
                                setVreme.setEnabled(false);
                                cenaPosla.setEnabled(false);
                            }
                            else {

                                String naslovDugma=izbrisiUgovorButton.getText().toString();
                                if(naslovDugma.equals("Povuci zahtev"))
                                {
                                    Toast.makeText(getApplicationContext(),"Ugovor je obrisan!",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                ugovorPostoji=false;

                            }

                            cenaPosla.setText(String.valueOf(Pocetna.prenosBida.getNovac()));


                            try
                            {

                                Pocetna.baza.getReference("Korisnici").child(Pocetna.prenosBida.getIdmajstora()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        majstor =dataSnapshot.getValue(Korisnik.class);
                                        imeMajstora= majstor.getIme();
                                        prezimeMajstora= majstor.getPrezime();

                                        posteviTekstUgovora();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            catch (Exception e){
                                startActivity(new Intent(NapraviUgovorPoslodavac.this,Pocetna.class));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else {


                    Pocetna.baza.getReference("SviPoslovi").child(Pocetna.prenosBida.getIdposla()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            posao=dataSnapshot.getValue(Posao.class);
                            nazivPosla=posao.getNaslovposla();



                            try
                            {

                                Pocetna.baza.getReference("Korisnici").child(Pocetna.prenosBida.getIdmajstora()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        majstor =dataSnapshot.getValue(Korisnik.class);
                                        imeMajstora= majstor.getIme();
                                        prezimeMajstora= majstor.getPrezime();

                                        posteviTekstUgovora();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            catch (Exception e){
                                startActivity(new Intent(NapraviUgovorPoslodavac.this,Pocetna.class));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    ugovorPostoji = true;



                    Calendar vremeUgovora = Calendar.getInstance();
                    vremeUgovora.set(ugovor.getDandolaska().getGodina(), ugovor.getDandolaska().getMesec(), ugovor.getDandolaska().getDan(),ugovor.getDandolaska().getSati(),ugovor.getDandolaska().getMinuti(),0);

                    Calendar trenutnoCalendar = Calendar.getInstance();

                    if (vremeUgovora.before(trenutnoCalendar)) {

                        //Ugovor je isteko
                        tekstUgovora.setText(ugovor.getPoruka());
                        tekstUgovoraStr = ugovor.getPoruka();

                        Calendar popuni = Calendar.getInstance();
                        String myFormat = "dd/MM/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        popuni.set(ugovor.getDandolaska().getGodina(), ugovor.getDandolaska().getMesec(), ugovor.getDandolaska().getDan(), ugovor.getDandolaska().getSati(), ugovor.getDandolaska().getMinuti());
                        datum = sdf.format(popuni.getTime());
                        setDatum.setText(datum);

                        setVreme.setText(String.format("%02d:%02d", ugovor.getDandolaska().getSati(), ugovor.getDandolaska().getMinuti()));
                        vreme = String.format("%02d:%02d", ugovor.getDandolaska().getSati(), ugovor.getDandolaska().getMinuti());

                        cenaPosla.setText(String.valueOf(ugovor.getCena()));
                        cenaPoslaStr = String.valueOf(ugovor.getCena());



                        if (ugovor.isPotpisan()) {


                            setDatum.setEnabled(false);
                            setVreme.setEnabled(false);
                            cenaPosla.setEnabled(false);
                            izbrisiUgovorButton.setVisibility(View.GONE);
                            napraviUgovor.setVisibility(View.GONE);

                            oceniMajstora.setVisibility(View.VISIBLE);

                        } else {

                            napraviUgovor.setText("Izmeni Ugovor");
                            napraviUgovor.setVisibility(View.VISIBLE);

                            izbrisiUgovorButton.setVisibility(View.VISIBLE);
                            izbrisiUgovorButton.setTag("Izbrisi");

                        }
                    } else {


                        //Ako je ugovor poslat

                        izbrisiUgovorButton.setVisibility(View.VISIBLE);
                        izbrisiUgovorButton.setTag("Izbrisi");
                        //ugovor = dataSnapshot.child(Pocetna.currentFirebaseUser.getUid() + MessageActivity.drugiId + Pocetna.prenosBida.getIdposla()).getValue(Ugovor.class);

                        //Popunjava stranicu ugovora

                        tekstUgovora.setText(ugovor.getPoruka());
                        tekstUgovoraStr = ugovor.getPoruka();

                        Calendar popuni = Calendar.getInstance();
                        String myFormat = "dd/MM/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        popuni.set(ugovor.getDandolaska().getGodina(), ugovor.getDandolaska().getMesec(), ugovor.getDandolaska().getDan(), ugovor.getDandolaska().getSati(), ugovor.getDandolaska().getMinuti());
                        datum = sdf.format(popuni.getTime());
                        setDatum.setText(datum);

                        setVreme.setText(String.format("%02d:%02d", ugovor.getDandolaska().getSati(), ugovor.getDandolaska().getMinuti()));
                        vreme = String.format("%02d:%02d", ugovor.getDandolaska().getSati(), ugovor.getDandolaska().getMinuti());

                        cenaPosla.setText(String.valueOf(ugovor.getCena()));
                        cenaPoslaStr = String.valueOf(ugovor.getCena());

                        if (ugovor.isPotpisan()) {
                            //Ako je ugovor vec potpisan
                            napraviUgovor.setText("Ugovor Potpisan");
                            napraviUgovor.setVisibility(View.GONE);
                            napraviUgovor.setEnabled(false);
                            setDatum.setEnabled(false);
                            setVreme.setEnabled(false);
                            cenaPosla.setEnabled(false);
                            if (ugovor.getRaskidPoslodavac()) {
                                izbrisiUgovorButton.setText("Povuci zahtev");
                                izbrisiUgovorButton.setTag("Raskini");
                            } else {
                                if (ugovor.getRaskidMajstor()) {
                                    izbrisiUgovorButton.setText("Raskini ugovor");
                                    izbrisiUgovorButton.setTag("Raskini");
                                } else {
                                    izbrisiUgovorButton.setText("Zahtev za raskid");
                                    izbrisiUgovorButton.setTag("Raskini");
                                }

                            }


                        } else {
                            napraviUgovor.setText("Izmeni Ugovor");
                        }

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        postaviListener();

    }

    private void loadViews()
    {
       // nazivPosla=(TextView)findViewById(R.id.nazivposla);
       //imeMajstora=(TextView)findViewById(R.id.imemajstora);
        cenaPosla= findViewById(R.id.cenaPoalaUgovor);
        napraviUgovor= findViewById(R.id.napraviugovor);
        //datum=(TextView)findViewById(R.id.datumUgovorPravljenje);
        //vreme=(TextView)findViewById(R.id.vremeUgovorPravljenje);
        setDatum= findViewById(R.id.setDatum);
        setVreme= findViewById(R.id.setVreme);
        tekstUgovora= findViewById(R.id.teksUgovora);

        izbrisiUgovorButton= findViewById(R.id.izbrisiRaskini);
        oceniMajstora= findViewById(R.id.oceniMajstoraNapraviUgovor);

        ugovori=FirebaseDatabase.getInstance().getReference("Korisnici");
    }

    private void postaviListener()
    {
        izbrisiUgovorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!ugovorPostoji)
                {
                    Toast.makeText(getApplicationContext(),"Ugovor je obrisan!",Toast.LENGTH_LONG).show();
                    finish();
                }
                if(view.getTag().equals("Izbrisi")){



                    final PrettyDialog dialogfilter= new PrettyDialog(NapraviUgovorPoslodavac.this);
                    dialogfilter
                            .setTitle("Izbriši ugovor!")
                            .setMessage("Majstor još uvvek nije prihvatio ugovor, da li želite da ga izbrišete?")
                            .setIcon(R.drawable.pdlg_icon_info)
                            .setIconTint(R.color.colorPrimary)
                            .addButton(
                                    "IZBRIŠI",     // button text
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {

                                            Pocetna.baza.getReference("Ugovori").child(idug).removeValue();
                                            izbrisiUgovorButton.setVisibility(View.GONE);
                                            napraviUgovor.setText("Pošalji ugovor");
                                            Toast.makeText(getApplicationContext(),"Ugovor uspešno izbrisan!",Toast.LENGTH_LONG).show();
                                            finish();


                                            dialogfilter.dismiss();

                                        }
                                    }  // button background color

                            )
                            .show();

                }
                else
                {
                    if(ugovor.getRaskidPoslodavac())
                    {


                        final PrettyDialog dialogfilter= new PrettyDialog(NapraviUgovorPoslodavac.this);
                        dialogfilter
                                .setTitle("Izbriši zahtev!")
                                .setMessage("Majstor još uvek nije prihvatio zahtev za raskid da li želite da ga povučete?")
                                .setIcon(R.drawable.pdlg_icon_info)
                                .setIconTint(R.color.colorPrimary)
                                .addButton(
                                        "IZBRIŠI ZAHTEV",     // button text
                                        R.color.pdlg_color_white,  // button text color
                                        R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                            @Override
                                            public void onClick() {


                                                Pocetna.baza.getReference("Ugovori").child(idug).child("raskidPoslodavac").setValue(false);
                                                izbrisiUgovorButton.setText("Zahtev za raskid");
                                                izbrisiUgovorButton.setTag("Raskini");
                                                Toast.makeText(NapraviUgovorPoslodavac.this,"Zahtev za raskid uspešno izbrisan!",Toast.LENGTH_LONG);


                                                dialogfilter.dismiss();

                                            }
                                        }  // button background color

                                )
                                .show();
                    }
                    else
                    //trazi raskid


                    if(ugovor.getRaskidMajstor())//ako je i majstor trazio raskid odmah brise
                    {
                        final PrettyDialog dialogfilter= new PrettyDialog(NapraviUgovorPoslodavac.this);
                        dialogfilter
                                .setTitle("Raskini ugovor!")
                                .setMessage("Majstor je već podneo zahtev za raskid, da li želite da raskinete ugovor?")
                                .setIcon(R.drawable.pdlg_icon_info)
                                .setIconTint(R.color.colorPrimary)
                                .addButton(
                                        "RASKINI UGOVOR",     // button text
                                        R.color.pdlg_color_white,  // button text color
                                        R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                            @Override
                                            public void onClick() {


                                                Pocetna.baza.getReference("SviPoslovi").child(ugovor.getPosao()).child("ugovoren").setValue(false);
                                                Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("PotpisaniUgovoriPoslodavac").child(ugovor.getIdugovora()).removeValue();
                                                Pocetna.baza.getReference("Korisnici").child(ugovor.getMajstor()).child("PotpisaniUgovoriMajstor").child(ugovor.getIdugovora()).removeValue();
                                                Pocetna.baza.getReference("Ugovori").child(ugovor.getIdugovora()).removeValue();
                                                Toast.makeText(getApplicationContext(),"Ugovor raskinut!",Toast.LENGTH_LONG);

                                              /*  Intent PokreniActivity=new Intent(NapraviUgovorPoslodavac.this, Pocetna.class);
                                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(PokreniActivity);*/
                                              finish();
                                                dialogfilter.dismiss();

                                            }
                                        }  // button background color

                                )
                                .show();



                    }
                    else{

                        final PrettyDialog dialogfilter= new PrettyDialog(NapraviUgovorPoslodavac.this);
                        dialogfilter
                                .setTitle("Zahtev za raskid!")
                                .setMessage("Da li želite da pošaljete zahtev za raskid ugovora?")
                                .setIcon(R.drawable.pdlg_icon_info)
                                .setIconTint(R.color.colorPrimary)
                                .addButton(
                                        "POŠALJI ZAHTEV",     // button text
                                        R.color.pdlg_color_white,  // button text color
                                        R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                            @Override
                                            public void onClick() {


                                                Pocetna.baza.getReference("Ugovori").child(idug).child("raskidPoslodavac").setValue(true);
                                                Toast.makeText(NapraviUgovorPoslodavac.this,"Zahtev za raskid uspesno poslat!",Toast.LENGTH_LONG);
                                                izbrisiUgovorButton.setText("Izbriši zahtev za raskid");

                                                dialogfilter.dismiss();

                                            }
                                        }  // button background color

                                )
                                .show();

                    }


                }



            }
        });

        cenaPosla.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                cenaPoslaStr=cenaPosla.getText().toString();
                posteviTekstUgovora();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        napraviUgovor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                 if(uzmiPodatke()) {

                     String idug=poslodavacId+majstorId+idposla;

                     Ugovor u = new Ugovor(idug, idposla, poslodavacId, majstorId, cena,datumDolaska, Pocetna.currentKorisnik.getLat(), Pocetna.currentKorisnik.getLon(),tekstUgovoraStr,Pocetna.currentKorisnik.getLokacija());


                     Pocetna.baza.getReference("Ugovori").child(idug).setValue(u);



                     Toast.makeText(NapraviUgovorPoslodavac.this,"Ugovor poslat!",Toast.LENGTH_SHORT).show();
                     //Intent PokreniActivity = new Intent(NapraviUgovorPoslodavac.this, Pocetna.class);
                     //startActivity(PokreniActivity);



                 }

            }
        });
        setDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Calendar calendarDate = Calendar.getInstance();


                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        calendarDate.set(Calendar.YEAR, year);
                        calendarDate.set(Calendar.MONTH, monthOfYear);
                        calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        datumDolaska.setGodina(year);
                        datumDolaska.setDan(dayOfMonth);
                        datumDolaska.setMesec(monthOfYear);




                        String myFormat = "dd/MM/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        datum=sdf.format(calendarDate.getTime());
                        setDatum.setText(datum);



                        posteviTekstUgovora();

                    }

                };

                new DatePickerDialog(NapraviUgovorPoslodavac.this, date, calendarDate
                        .get(Calendar.YEAR),calendarDate.get(Calendar.MONTH),
                        calendarDate.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        setVreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar mcurrentTime = Calendar.getInstance();

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NapraviUgovorPoslodavac.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mcurrentTime.set(Calendar.HOUR_OF_DAY,selectedHour);
                        mcurrentTime.set(Calendar.MINUTE,selectedMinute);
                        setVreme.setText( String.format("%02d:%02d", selectedHour, selectedMinute));
                        vreme=String.format("%02d:%02d", selectedHour, selectedMinute);


                        datumDolaska.setSati(selectedHour);
                        datumDolaska.setMinuti(selectedMinute);


                        posteviTekstUgovora();
                    }
                },mcurrentTime.get(Calendar.HOUR_OF_DAY), mcurrentTime.get(Calendar.MINUTE), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });


        oceniMajstora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                OceniMajstora.prenosUgovora=ugovor;
                startActivity(new Intent(getApplicationContext(), OceniMajstora.class));

            }
        });

    }

    private boolean uzmiPodatke()
    {
        idposla=Pocetna.prenosBida.getIdposla();
        poslodavacId =Pocetna.currentFirebaseUser.getUid();
        majstorId =MessageActivity.drugiId;
        if(datum.equals("*izaberite datum*"))
        {
            Toast.makeText(NapraviUgovorPoslodavac.this,"Morate izabrati datum posla klikom na dugme!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(vreme.equals("*izaberite vreme*"))
        {
            Toast.makeText(NapraviUgovorPoslodavac.this,"Morate izabrati vreme posla klikom na dugme!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(cenaPoslaStr.equals("*izaberite cenu*"))
        {
            Toast.makeText(NapraviUgovorPoslodavac.this,"Morate izabrati cenu posla!",Toast.LENGTH_SHORT).show();
            return false;
        }
        cena=Integer.valueOf(cenaPoslaStr);

        Calendar trenutnoCalendar = Calendar.getInstance();
        Calendar odabranDatum=Calendar.getInstance();
        odabranDatum.set(datumDolaska.getGodina(),datumDolaska.getMesec(),datumDolaska.getDan(),datumDolaska.getSati(),datumDolaska.getMinuti());

        if(odabranDatum.before(trenutnoCalendar))
        {
            Toast.makeText(NapraviUgovorPoslodavac.this,"Izaberite datum u predstojećem periodu!",Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;

    }
    private void posteviTekstUgovora()
    {

        tekstUgovoraStr="Ja, majstor, "+imeMajstora+" "+prezimeMajstora+" sklapam ugovor sa poslodavcem "+Pocetna.currentKorisnik.getIme()+" "+Pocetna.currentKorisnik.getPrezime()+" o poslu \""+nazivPosla+"\".  Prihvatam da dodjem na lokaciju "+lokacija+", dana: "+datum+" u vreme: "+vreme+", i radim na poslu po dogovorenoj ceni od "+cenaPoslaStr+" din." +
                " Nakon obavljenog posla poslodavac "+Pocetna.currentKorisnik.getIme()+" me može oceniti i dati komentar na moj rad, koji će biti vidljiv drugim poslodavcima.";
        tekstUgovora.setText(tekstUgovoraStr);
    }
    protected void onResume()
    {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.none);
    }
    protected void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.none, R.anim.slide_out_down);
    }

}
