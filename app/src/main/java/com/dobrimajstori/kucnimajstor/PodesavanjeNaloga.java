package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import lib.kingja.switchbutton.SwitchMultiButton;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class PodesavanjeNaloga extends AppCompatActivity {

    CircleImageView profilna;
    EditText editIme;
    EditText editPrezime;
    EditText editMail;
    EditText editPass;
    EditText editTel;
    ImageView update;
    SwitchMultiButton majstorPoslodavac;
    TextView lokacija;
    ImageView promeniLokaciju;
    ImageView cancle;
    Place izabranaLokacija;
    Bitmap bitmap;
    boolean majstor;
    private final int PICK_IMAGE_REQUEST = 71;
    int PLACE_PICKER_REQUEST = 2;
    private Uri filePath;
    TextView prezimeTextInfoGrad;
    TextView infopromenanaloga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podesavanje_naloga);

        deklarisiSve();

        //Postavljanje trenutnih vrednosti
        editIme.setText(Pocetna.currentKorisnik.getIme());
        editPrezime.setText(Pocetna.currentKorisnik.getPrezime());
        editMail.setText(Pocetna.currentKorisnik.getMail());
        editTel.setText(Pocetna.currentKorisnik.getBrtel());
        lokacija.setText(Pocetna.currentKorisnik.getLokacija());

        if (Pocetna.currentKorisnik.isSkola()) {
            majstorPoslodavac.setVisibility(View.GONE);
            prezimeTextInfoGrad.setText("Grad");
            editPrezime.setHint("Unestie grad škole");
            infopromenanaloga.setVisibility(View.GONE);
        }
        else
        if(Pocetna.currentKorisnik.isMajstor())
            majstorPoslodavac.setSelectedTab(0);
        else
            majstorPoslodavac.setSelectedTab(1);


        ucitajSliku();

        postaviListener();
    }

    private void ucitajSliku()
    {
        profilna= findViewById(R.id.slikakoris);

        if(Pocetna.uri.equals("prazno"))
        {
            profilna.setImageResource(R.drawable.ic_korisnik);
        }
        else
        {
            Picasso.get().load(Pocetna.uri).into(profilna);
        }
    }

    private void deklarisiSve()
    {
        editIme= findViewById(R.id.editname);
        editPrezime= findViewById(R.id.editprezime);
        editMail= findViewById(R.id.editmail);
        editPass= findViewById(R.id.editpass);
        editTel= findViewById(R.id.editbrtelefona);
        update= findViewById(R.id.update);
        majstorPoslodavac= findViewById(R.id.majstorPoslodavacSwitch);
        lokacija= findViewById(R.id.lokacijaPodesavanja);
        promeniLokaciju= findViewById(R.id.promeniLokacijuPodesavanja);
        cancle= findViewById(R.id.cancel);
        prezimeTextInfoGrad= findViewById(R.id.prezimePodesavanjaTextinfo);
        infopromenanaloga= findViewById(R.id.infopromenanaloga);

    }

    private void izaberiSliku()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Izaberite sliku"),PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filePath=data.getData();
            try
            {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                bitmap=Bitmap.createScaledBitmap(bitmap,160,160,true);
                profilna.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Izabrana adresa: %s", place.getAddress());
                izabranaLokacija=place;
                lokacija.setText(place.getAddress());

                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
            //Toast.makeText(getApplicationContext(),"Greška prilikom učitavanja slike",Toast.LENGTH_LONG).show();
    }

    private void postaviListener()
    {
        profilna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                izaberiSliku();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final PrettyDialog dialogfilter= new PrettyDialog(PodesavanjeNaloga.this);
                dialogfilter
                        .setTitle("Promeni profil!")
                        .setMessage("Da li želite da sačuvate podešavanja naloga?")
                        .setIcon(R.drawable.pdlg_icon_info)
                        .setIconTint(R.color.colorPrimary)
                        .addButton(
                                "SAČUVAJ",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {


                                        updateAcc();

                                        dialogfilter.dismiss();
                                    }
                                }  // button background color

                        )
                        .addButton(
                                "OTKAŽI",
                                R.color.pdlg_color_white,  // button text color
                                R.color.colorPrimary,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        dialogfilter.dismiss();
                                    }
                                }
                        )
                        .show();



            }
        });

        majstorPoslodavac.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                majstor = position == 0;
            }
        });

        promeniLokaciju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(PodesavanjeNaloga.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                Intent PokreniActivity=(new Intent(PodesavanjeNaloga.this, Pocetna.class));
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(PokreniActivity);


            }
        });

    }


    private void uploadSliku()
    {
        if(filePath!=null) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference("SlikeKorisnika/" + Pocetna.currentFirebaseUser.getUid());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Pocetna.uri = uri.toString();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid());
                            databaseReference.child("UriSlike").setValue(uri.toString());

                        }
                    });


                }
            });
        }
    }

    private void updateAcc()
    {
        uploadSliku();

        if(!editIme.getText().toString().trim().isEmpty())
        {
            FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("ime").setValue(editIme.getText().toString().trim());
        }

        if(!editPrezime.getText().toString().trim().isEmpty())
        {
            FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("prezime").setValue(editPrezime.getText().toString().trim());
        }

        if(!editMail.getText().toString().trim().isEmpty())
        {
            Pocetna.currentFirebaseUser.updateEmail(editMail.getText().toString().trim());
            FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("mail").setValue(editMail.getText().toString().trim());

        }

        if(!editTel.getText().toString().trim().isEmpty())
        {
            FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("brtel").setValue(editTel.getText().toString().trim());
        }

        if(majstor)
        {
            FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("majstor").setValue(true);
        }
        else
        {
            FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("majstor").setValue(false);
        }

        if(izabranaLokacija!=null)
        {
            Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("lokacija").setValue(izabranaLokacija.getAddress());
            Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("lat").setValue(izabranaLokacija.getLatLng().latitude);
            Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("lon").setValue(izabranaLokacija.getLatLng().longitude);
        }

        if(!editPass.getText().toString().trim().isEmpty())
        {
            if(editPass.getText().toString().trim().length()<6)
                Toast.makeText(getApplicationContext(),"Šifra mora da ima 6 ili više karaktera!",Toast.LENGTH_LONG).show();
            else
            {


                Pocetna.currentFirebaseUser.updatePassword(editPass.getText().toString().trim());

                Toast.makeText(PodesavanjeNaloga.this,"Vaš profil je uspešno podešen!",Toast.LENGTH_LONG).show();

                Intent PokreniActivity=(new Intent(PodesavanjeNaloga.this, Pocetna.class));
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(PokreniActivity);
            }
        }
        else
        {
            Toast.makeText(PodesavanjeNaloga.this,"Vaš profil je uspešno podešen!",Toast.LENGTH_LONG).show();

            Intent PokreniActivity=(new Intent(PodesavanjeNaloga.this, Pocetna.class));
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(PokreniActivity);

        }




    }



}
