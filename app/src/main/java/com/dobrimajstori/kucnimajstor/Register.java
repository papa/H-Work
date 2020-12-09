package com.dobrimajstori.kucnimajstor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import lib.kingja.switchbutton.SwitchMultiButton;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextIme;
    private EditText editTextPrezime;
    private EditText editTextMejl;
    private EditText editTextSifra;
    private EditText editTextBrTel;
    private TextView imaNalog;

    private SwitchMultiButton skolaKorisnik;

    private Button buttonSignup;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference korisnici;

    private TextInputLayout prezimeTextInput;
    private TextInputLayout imeTextInput;
    String id;

    Korisnik k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();



        editTextIme = findViewById(R.id.editTextIme);
        editTextPrezime = findViewById(R.id.editTextPrezime);
        editTextMejl = findViewById(R.id.editTextMejl);
        editTextSifra = findViewById(R.id.editTextSifra);
        editTextBrTel= findViewById(R.id.editTextBrTelefina);
        imaNalog= findViewById(R.id.imaNalog);
        skolaKorisnik= findViewById(R.id.majstorPoslodavacSwitch);
        prezimeTextInput= findViewById(R.id.textInputPrezime);
        imeTextInput= findViewById(R.id.inputTextIme);

        buttonSignup = findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        buttonSignup.setOnClickListener(this);

        korisnici=FirebaseDatabase.getInstance().getReference("Korisnici");

        imaNalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent PokreniActivity=new Intent(Register.this, LoginProba.class);
                startActivity(PokreniActivity);

            }
        });

        skolaKorisnik.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {

                if(position==1)
                {
                    final PrettyDialog dialogfilter= new PrettyDialog(Register.this);
                    dialogfilter
                            .setTitle("Školski nalog!")
                            .setMessage("Izabrali ste školski nalog sa posebnim privilegijama, za aktivaciju naloga potrebno je da administracija potvrdi legitimnost naloga kada bude kontaktirana u roku od jednog radnog dana.")
                            .setIcon(R.drawable.pdlg_icon_info)
                            .setIconTint(R.color.colorPrimary)
                            .addButton(
                                    "U REDU",     // button text
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {


                                            dialogfilter.dismiss();
                                        }
                                    }  // button background color

                            )
                            .show();

                    prezimeTextInput.setHint("Grad");
                    imeTextInput.setHint("Naziv škole");
                }
                else
                {
                    imeTextInput.setHint("Ime");
                    prezimeTextInput.setVisibility(View.VISIBLE);
                    prezimeTextInput.setHint("Prezime");
                    //editTextPrezime.setHint("Prezime");
                    editTextPrezime.setVisibility(View.VISIBLE);

                }
            }
        });


    }

    private void registerUser(){

        //getting email and password from edit texts
        final String email = editTextMejl.getText().toString().trim();
        final String password  = editTextSifra.getText().toString().trim();
        final String ime=editTextIme.getText().toString().trim();
        final String prezime=editTextPrezime.getText().toString().trim();
        final String brojTelefona=editTextBrTel.getText().toString().trim();

        if(TextUtils.isEmpty(ime))
        {
            Toast.makeText(this,"Unesite ime",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(prezime))
        {
            if(skolaKorisnik.getSelectedTab()==0)
                Toast.makeText(this,"Unesite prezime",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this,"Unesite grad",Toast.LENGTH_LONG).show();

            return;
        }

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Unesite email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Unesite šifru",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length()<=6)
        {
            Toast.makeText(this,"Šifra mora da sadrži 6 ili više karaktera",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(brojTelefona))
        {
            Toast.makeText(this,"Unesite broj telefona",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registracija, molimo sačekajte...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here

                            try
                            {
                                id=firebaseAuth.getCurrentUser().getUid();
                            }
                            catch (NullPointerException e)
                            {
                                id=korisnici.push().getKey();
                                e.printStackTrace();
                            }

                            if(skolaKorisnik.getSelectedTab()==0)
                                k=new Korisnik(id,ime,prezime,email,"Opis",brojTelefona,"Lokacija",0.0,0.0,true,0,false);
                            else
                                k=new Korisnik(id,ime,prezime,email,"Opis",brojTelefona,"Lokacija",0.0,0.0,true,0,true);


                            korisnici.child(id).setValue(k);

                            Toast.makeText(Register.this,"Registrovanje uspelo!",Toast.LENGTH_LONG).show();

                            if(skolaKorisnik.getSelectedTab()==0)
                            {
                                Intent PokreniActivity=new Intent(Register.this, IzaberiActivity.class);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(PokreniActivity);

                            }
                            else
                            {
                                Intent PokreniActivity=new Intent(Register.this, Pocetna.class);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(PokreniActivity);
                            }

                        }
                        else {
                            //display some message here
                            Toast.makeText(Register.this,"Greška prilikom registrovanja!",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });



    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

    }
}
