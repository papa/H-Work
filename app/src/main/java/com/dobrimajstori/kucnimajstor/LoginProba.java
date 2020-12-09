package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
///////
////////////////
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class LoginProba extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextEmail;
    private EditText editTextPassword;

    private Button buttonSignIn;

    private String id;
    private String ime;
    private String prezime;
    private String phoneNumber;
    private String email;
    private DatabaseReference korisnici;
    private TextView zabSifru;
    private TextView nemaNalog;

    Korisnik k;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


            if(firebaseAuth.getCurrentUser() != null){

                /*
                //slika korisnika
                Uri photoUrl = firebaseAuth.getCurrentUser().getPhotoUrl();
                id=firebaseAuth.getCurrentUser().getUid();
                ime=firebaseAuth.getCurrentUser().getDisplayName();
                prezime="";
                email=firebaseAuth.getCurrentUser().getEmail();
                phoneNumber=firebaseAuth.getCurrentUser().getPhoneNumber();

                ArrayList<String> kateg=new ArrayList<>();
                kateg.add("Popravka kompjutera");

                ArrayList<String> prijpos=new ArrayList<>();
                prijpos.add("Prijavljeni poslovi");

               // k=new Korisnik(id,ime,prezime,email,0.0,0.0,kateg,"Opis",phoneNumber,"Lokacija",0.0,0.0,prijpos,true);


                korisnici.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         if(!dataSnapshot.hasChild(id))
                         {
                             ArrayList<String> kateg=new ArrayList<>();
                             kateg.add("Popravka kompjutera");

                             ArrayList<String> prijpos=new ArrayList<>();
                             prijpos.add("Prijavljeni poslovi");

                            // k=new Korisnik(id,ime,prezime,email,0.0,0.0,kateg,"Opis",phoneNumber,"Lokacija",0.0,0.0,prijpos,true);
                             korisnici.child(id).setValue(k);
                         }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                    /*ArrayList<String> kat = new ArrayList<>();
                    m = new Majstor(ime, prezime, id, email, 0.0, kat, "", 0.0, phoneNumber);
                         majstori.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 if(!dataSnapshot.hasChild(id)) {
                                     ArrayList<String> kat = new ArrayList<>();
                                     m = new Majstor(ime, prezime, id, email, 0.0, kat, "", 0.0, phoneNumber);
                                     majstori.child(id).setValue(m);
                                 }
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });

                    poslodavci.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.hasChild(id)) {
                                p = new Poslodavac(ime, prezime, email, id, "lokacija", 0.0, 0.0, phoneNumber);
                                poslodavci.child(id).setValue(p);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/




                Intent PokreniActivity=new Intent(LoginProba.this, Pocetna.class);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(PokreniActivity);

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_proba);

        firebaseAuth = FirebaseAuth.getInstance();

        korisnici=FirebaseDatabase.getInstance().getReference("Korisnici");

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        zabSifru= findViewById(R.id.zabSifru);
        nemaNalog= findViewById(R.id.nemaNalog);

        buttonSignIn = findViewById(R.id.buttonSignIn);

        buttonSignIn.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });

        zabSifru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final FirebaseAuth auth = FirebaseAuth.getInstance();
                final String emailAddress = editTextEmail.getText().toString();
                if(emailAddress.equals(""))
                    Toast.makeText(getApplicationContext(),"Unesite Email za resetovanje šifre",Toast.LENGTH_LONG).show();
                else{


                    final PrettyDialog dialogfilter= new PrettyDialog(LoginProba.this);
                    dialogfilter
                            .setTitle("Resetuj šifru")
                            .setMessage("Da li želite da pošaljemo mail za resetovanje šifre na "+ emailAddress)
                            .setIcon(R.drawable.pdlg_icon_info)
                            .setIconTint(R.color.colorPrimary)
                            .addButton(
                                    "DA",     // button text
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {



                                            auth.sendPasswordResetEmail(emailAddress)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(),"Email uspešno poslat na "+emailAddress,Toast.LENGTH_LONG).show();
                                                            }
                                                            else
                                                                Toast.makeText(getApplicationContext(),"Ne postoji ni jedan nalog pod ovom Email adresom!",Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                            dialogfilter.dismiss();
                                        }
                                    }  // button background color

                            )
                            .show();



                }



            }
        });

        nemaNalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent PokreniActivity=new Intent(LoginProba.this, Register.class);
                startActivity(PokreniActivity);

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

       firebaseAuth.addAuthStateListener(mAuthListener);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null)
            startActivity(new Intent(LoginProba.this,Pocetna.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void startSignIn()
    {
        String email = editTextEmail.getText().toString();
        String sifra = editTextPassword.getText().toString();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(sifra)){

            Toast.makeText(LoginProba.this, "Molimo Vas popunite polja!", Toast.LENGTH_LONG).show();

        } else {


            firebaseAuth.signInWithEmailAndPassword(email, sifra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginProba.this, "Greška prilikom prijavljivanja", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override
    public void onClick(View view) {

    }
}
