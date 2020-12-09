package com.dobrimajstori.kucnimajstor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Pocetna extends AppCompatActivity {

    Fragment selectedFragment = null;
    private FirebaseAuth firebaseAuth;
    private Boolean exit = false;
    static FirebaseUser currentFirebaseUser;
    static FirebaseDatabase baza;
    static DatabaseReference currentUserReference;
    static boolean isMajstor;
    private BottomNavigationView navigation;
    private ProgressDialog progressDialog;
    static Korisnik currentKorisnik;
    int PLACE_PICKER_REQUEST = 2;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            selectedFragment = null;

            switch (item.getItemId()) {
                case 1:
                    selectedFragment = SviPoslovi.newInstance(1);
                    break;
                case 2:
                    selectedFragment = PregovoriMajstor.newInstance(1);
                    break;

                case 3:
                    selectedFragment=UgovoriMajstor.newInstance(1);
                    break;
                case 4:
                case 9:
                    selectedFragment = MojNalogFragment.newInstance();
                    break;
                case 5:
                    selectedFragment = MojiPoslovi.newInstance(1);
                    break;
                case 6:
                    selectedFragment = PonudeMajstora.newInstance(1);
                    break;
                case 7:
                    selectedFragment = PregovoriPoslodavac.newInstance(1);
                    break;
                case 8:
                    selectedFragment=UgovoriPoslodavac.newInstance(1);
                    break;

                default:
                    selectedFragment = (isMajstor) ? SviPoslovi.newInstance(1) : MojiPoslovi.newInstance(1);

            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };


    static LocationManager locationManager;
    static Location userLocation;


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {
                        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null)
                        {
                            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            userLocation=lastKnownLocation;
                        }
                        else
                        {
                           /* if(!trazenjeLokacie) {
                                progressdialog.setMessage("Trazenje lokacije...");
                                progressdialog.show();
                                trazenjeLokacie = true;
                            }*/
                        }
                        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 50, locationListener);

                    }

                }

            }

        }

    }

    static Posao prenosPosla;
    static Bid prenosBida;
    static String uri;

    private void ucitajUri()
    {
        DatabaseReference uriSlike=FirebaseDatabase.getInstance().getReference("Korisnici").child(currentFirebaseUser.getUid()).child("UriSlike");

        uriSlike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    uri=dataSnapshot.getValue(String.class);
                }
                else
                {
                    uri="prazno";
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna);

        baza = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = firebaseAuth.getCurrentUser();

        navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

// Internet provera dostupnosti
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Pocetna.this)
                    .setMessage("Internet nije dostupan")
                    .setPositiveButton("Ukljucite internet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            finish();
                            Pocetna.this.startActivity(myIntent);


                        }
                    });

            dialog.show();
        }
        else
        if (currentFirebaseUser == null) {

            Intent PokreniActivity = new Intent(Pocetna.this, DobrodosliActivity.class);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(PokreniActivity);
        } else {
                // Dozvola za lokaciju

            ucitajUri();


            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                if (Build.VERSION.SDK_INT < 23) {

                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null)
                    {
                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        userLocation=lastKnownLocation;
                    }
                    /*else {
                        if(!trazenjeLokacie) {
                            progressdialog.setMessage("Trazenje lokacije...");
                            progressdialog.show();
                            trazenjeLokacie = true;
                        }
                    }
                    */
                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 50, locationListener);

                } else {

                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                       // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


                    } else {
                        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null)
                        {
                            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            userLocation=lastKnownLocation;
                        }
                        /*else {
                            if(!progressdialog.isShowing()) {
                                progressdialog.setMessage("Trazenje lokacije...");
                                progressdialog.show();
                                trazenjeLokacie = true;
                            }
                        }
*/
                        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 50, locationListener);



                    }


                }




                currentUserReference = baza.getReference("Korisnici").child(currentFirebaseUser.getUid());

                currentUserReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // progressDialog = new ProgressDialog(Pocetna.this);

                        // progressDialog.setMessage("Ucitavanje...");
                        // progressDialog.show();
                        // navigation.getMenu().clear();

                        currentKorisnik = dataSnapshot.getValue(Korisnik.class);

                        if(currentKorisnik.isSkola())
                        {
                            if(!currentKorisnik.isSkolaOdobren())
                            {
                                Intent PokreniActivity=new Intent(Pocetna.this, SkolaNeodobrena.class);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(PokreniActivity);

                            }
                            else
                            {
                                isMajstor = true;


                                navigation.getMenu().removeGroup(R.id.grupa_poslodavac);

                                navigation.getMenu().add(R.id.grupa_majstor, 1, 1, "Poslovi").setIcon(R.drawable.ic_home_black_24dp);
                                //navigation.getMenu().add(R.id.grupa_majstor, 2, 2, "Bidovani Poslovi").setIcon(R.drawable.ic_notifications_black_24dp);
                                navigation.getMenu().add(R.id.grupa_majstor, 2, 2, "Pregovori").setIcon(R.drawable.ic_forum_black_24dp);
                                navigation.getMenu().add(R.id.grupa_majstor, 3, 3, "Ugovori").setIcon(R.drawable.contract);
                                navigation.getMenu().add(R.id.grupa_majstor, 4, 4, "Moj Nalog").setIcon(R.drawable.ic_dashboard_black_24dp);
                                navigation.getMenu().setGroupCheckable(R.id.grupa_majstor, true, true);
                                navigation.getMenu().setGroupVisible(R.id.grupa_majstor, true);

                                if(currentKorisnik.getOblastRadaMajstor()==0)
                                {

                                    //TODO: dodati upit dijalog za opseg rada za majstora
                                /*final Dialog d = new Dialog(Pocetna.this);
                                d.setTitle("Izaberite opseg rada");
                                d.setContentView(R.layout.opseg_rada_picker);
                                Button b1 = (Button) d.findViewById(R.id.button1);
                                Button b2 = (Button) d.findViewById(R.id.button2);
                                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                                np.setMaxValue(100); // max value 100
                                np.setMinValue(1);   // min value 0
                                np.setWrapSelectorWheel(false);
                                //np.setOnValueChangedListener(this);
                                b1.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(Pocetna.this,String.valueOf(np.getValue()),Toast.LENGTH_LONG); //set the value to textview
                                        d.dismiss();
                                    }
                                });
                                b2.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        d.dismiss(); // dismiss the dialog
                                    }
                                });
                                d.show();*/
                                }
                            }



                        }
                        else
                        if (currentKorisnik.isMajstor()) {


                            //Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_LONG).show();
                            isMajstor = true;


                            navigation.getMenu().removeGroup(R.id.grupa_poslodavac);

                            navigation.getMenu().add(R.id.grupa_majstor, 1, 1, "Poslovi").setIcon(R.drawable.ic_home_black_24dp);
                            //navigation.getMenu().add(R.id.grupa_majstor, 2, 2, "Bidovani Poslovi").setIcon(R.drawable.ic_notifications_black_24dp);
                            navigation.getMenu().add(R.id.grupa_majstor, 2, 2, "Pregovori").setIcon(R.drawable.ic_forum_black_24dp);
                            navigation.getMenu().add(R.id.grupa_majstor, 3, 3, "Ugovori").setIcon(R.drawable.contract);
                            navigation.getMenu().add(R.id.grupa_majstor, 4, 4, "Moj Nalog").setIcon(R.drawable.ic_dashboard_black_24dp);
                            navigation.getMenu().setGroupCheckable(R.id.grupa_majstor, true, true);
                            navigation.getMenu().setGroupVisible(R.id.grupa_majstor, true);

                            if(currentKorisnik.getOblastRadaMajstor()==0)
                            {

                                //TODO: dodati upit dijalog za opseg rada za majstora
                                /*final Dialog d = new Dialog(Pocetna.this);
                                d.setTitle("Izaberite opseg rada");
                                d.setContentView(R.layout.opseg_rada_picker);
                                Button b1 = (Button) d.findViewById(R.id.button1);
                                Button b2 = (Button) d.findViewById(R.id.button2);
                                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                                np.setMaxValue(100); // max value 100
                                np.setMinValue(1);   // min value 0
                                np.setWrapSelectorWheel(false);
                                //np.setOnValueChangedListener(this);
                                b1.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(Pocetna.this,String.valueOf(np.getValue()),Toast.LENGTH_LONG); //set the value to textview
                                        d.dismiss();
                                    }
                                });
                                b2.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        d.dismiss(); // dismiss the dialog
                                    }
                                });
                                d.show();*/
                            }


                        } else {
                            //Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                            isMajstor = false;

                            navigation.getMenu().removeGroup(R.id.grupa_majstor);

                            navigation.getMenu().add(R.id.grupa_poslodavac, 5, 1, "Moji Poslovi").setIcon(R.drawable.ic_home_black_24dp);
                            navigation.getMenu().add(R.id.grupa_poslodavac, 6, 2, "Ponude Majstora").setIcon(R.drawable.ic_notifications_black_24dp);
                            navigation.getMenu().add(R.id.grupa_poslodavac, 7, 3, "Pregovori").setIcon(R.drawable.ic_forum_black_24dp);
                            navigation.getMenu().add(R.id.grupa_poslodavac, 8, 4,"Ugovori").setIcon(R.drawable.contract);
                            navigation.getMenu().add(R.id.grupa_poslodavac, 9, 5, "Moj Nalog").setIcon(R.drawable.ic_dashboard_black_24dp);
                            navigation.getMenu().setGroupCheckable(R.id.grupa_poslodavac, true, true);
                            navigation.getMenu().setGroupVisible(R.id.grupa_poslodavac, true);



                        }

                        selectedFragment = null;
                        Intent iin= getIntent();
                        Bundle b = iin.getExtras();
                        if(b!=null)
                        {
                            String j =(String) b.get("fragment");

                            if(j!=null && !j.equals("")) {


                                switch (j) {
                                    case "sviposlovi":
                                        selectedFragment = SviPoslovi.newInstance(1);
                                        navigation.getMenu().findItem(1).setChecked(true);
                                        break;
                                    case "pregovorimajstor":
                                        selectedFragment = PregovoriMajstor.newInstance(1);
                                        navigation.getMenu().findItem(2).setChecked(true);
                                        break;

                                    case "ugovorimajstor":
                                        selectedFragment = UgovoriMajstor.newInstance(1);
                                        navigation.getMenu().findItem(3).setChecked(true);
                                        break;
                                    case "mojnalog":
                                        selectedFragment = MojNalogFragment.newInstance();
                                        navigation.getMenu().findItem(4).setChecked(true);
                                        break;
                                    case "mojiposlovi":
                                        selectedFragment = MojiPoslovi.newInstance(1);
                                        navigation.getMenu().findItem(5).setChecked(true);
                                        break;
                                    case "ponudemajstora":
                                        selectedFragment = PonudeMajstora.newInstance(1);
                                        navigation.getMenu().findItem(6).setChecked(true);
                                        break;
                                    case "pregovoriposlodavac":
                                        selectedFragment = PregovoriPoslodavac.newInstance(1);
                                        navigation.getMenu().findItem(7).setChecked(true);

                                        break;
                                    case "ugovoriposlodavac":
                                        selectedFragment = UgovoriPoslodavac.newInstance(1);
                                        navigation.getMenu().findItem(8).setChecked(true);
                                        break;

                                    default:
                                        selectedFragment = (isMajstor) ? SviPoslovi.newInstance(1) : MojiPoslovi.newInstance(1);
                                }

                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, selectedFragment);
                                transaction.commit();
                            }
                            else
                            {
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, (isMajstor) ? SviPoslovi.newInstance(1) : MojiPoslovi.newInstance(1));
                                transaction.commit();

                            }

                        }
                        else{

                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, (isMajstor) ? SviPoslovi.newInstance(1) : MojiPoslovi.newInstance(1));
                            transaction.commit();
                        }




                        //Trazi likaciju
                        if (currentKorisnik.getLat() == 0 && currentKorisnik.getLon() == 0) {

                            final PrettyDialog dialogLokacija= new PrettyDialog(Pocetna.this);
                            dialogLokacija
                                    .setTitle("Lokacija")
                                    .setMessage("Zbog funkcionalnosti aplikacije potrebno je da unesete lokaciju Vašeg mesta stanovanja, ona neće biti zloupotrebljena i biće vidljiva samo određenim korisnicima sa kojim sklopite ugovor!")
                                    .setIcon(R.drawable.pdlg_icon_info)
                                    .setIconTint(R.color.pdlg_color_green)
                                    .addButton(
                                            "OK",     // button text
                                            R.color.pdlg_color_white,  // button text color
                                            R.color.pdlg_color_green,new PrettyDialogCallback() {  // button OnClick listener
                                                @Override
                                                public void onClick() {

                                                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                                                    try {
                                                        startActivityForResult(builder.build(Pocetna.this), PLACE_PICKER_REQUEST);
                                                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                                                        e.printStackTrace();
                                                    }

                                                    dialogLokacija.dismiss();
                                                }
                                            }  // button background color

                                    ).setCanceledOnTouchOutside(false);
                            dialogLokacija.setCancelable(false);

                            dialogLokacija.show();
                            //startActivity(new Intent(Pocetna.this, IzaberiLokacijuMaps.class));


                        }

                        //progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Pritisnite Back opet ako zelite da izadjete!",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentFirebaseUser == null) {

            Intent PokreniActivity = new Intent(Pocetna.this, DobrodosliActivity.class);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(PokreniActivity);
        }
        else
        {
            currentUserReference = baza.getReference("Korisnici").child(currentFirebaseUser.getUid());

            currentUserReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentKorisnik = dataSnapshot.getValue(Korisnik.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Izabrana adresa: %s", place.getAddress());
                baza.getReference("Korisnici").child(currentFirebaseUser.getUid()).child("lokacija").setValue(place.getAddress());
                baza.getReference("Korisnici").child(currentFirebaseUser.getUid()).child("lat").setValue(place.getLatLng().latitude);
                baza.getReference("Korisnici").child(currentFirebaseUser.getUid()).child("lon").setValue(place.getLatLng().longitude);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Morate izabrati lokaciju Vašeg stanovanja!",Toast.LENGTH_LONG).show();
                //Trazi likaciju
                if (currentKorisnik.getLat() == 0 && currentKorisnik.getLon() == 0) {

                    final PrettyDialog dialogLokacija= new PrettyDialog(Pocetna.this);
                    dialogLokacija
                            .setTitle("Lokacija")
                            .setMessage("Zbog funkcionalnosti aplikacije potrebno je da unesete lokaciju Vašeg mesta stanovanja, ona neće biti zloupotrebljena i neće biti vidljiva svima sve dok ne sklopite ugovor sa određenim majstorom!")
                            .setIcon(R.drawable.pdlg_icon_info)
                            .setIconTint(R.color.pdlg_color_green)
                            .addButton(
                                    "OK",     // button text
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.pdlg_color_green,new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {

                                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                                            try {
                                                startActivityForResult(builder.build(Pocetna.this), PLACE_PICKER_REQUEST);
                                            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                                                e.printStackTrace();
                                            }

                                            dialogLokacija.dismiss();
                                        }
                                    }  // button background color

                            ).setCanceledOnTouchOutside(false);
                    dialogLokacija.setCancelable(false);

                    dialogLokacija.show();
                    //startActivity(new Intent(Pocetna.this, IzaberiLokacijuMaps.class));


                }

            }


        }
    }
}
