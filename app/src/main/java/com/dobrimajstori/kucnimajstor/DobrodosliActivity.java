package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DobrodosliActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private Button buttonSignUp;



    //Userovi podaci
    private String ImeIPrezime;
    private String phoneNumber;

    //firebase
    private FirebaseAuth firebaseAuth;

    //Objekat korisnik
    Korisnik k;


    /*
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {

        //proverava da li je user ulogovan i uzima podatke
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            final FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {


                //Dodavanje korisnika u bazu ako se prvi put loguje

                FirebaseDatabase.getInstance().getReference("Korisnici").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.hasChild(user.getUid()))
                        {
                            //Dodavanje korisnika u bazu

                            String[] str_array = user.getDisplayName().split(" ");
                            String ime = str_array[0];
                            String prezime = str_array[1];

                            //Todo logovanje...
                            //k = new Korisnik(user.getUid(), ime,prezime, "Nema mail preko facea, pitaj Marka", 0.0, 0.0, new ArrayList<String>(), "Opis", "0691045244", "Lokacija", 0.0, 0.0, new ArrayList<String>(), true, new ArrayList<String>(),0,new ArrayList<Bid>(),new ArrayList<Bid>(),new  ArrayList<Bid>(),0);

                            FirebaseDatabase.getInstance().getReference("Korisnici").child(user.getUid()).setValue(k);

                            //Otvaranje stranice za biranje vrste naloga strane bez mogucnosti povratka

                            Intent PokreniActivity=new Intent(DobrodosliActivity.this, IzaberiActivity.class);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(PokreniActivity);
                        }
                        else
                        {
                            //Otvaranje pocetne strane bez mogucnosti povratka

                            Intent PokreniActivity=new Intent(DobrodosliActivity.this, IzaberiActivity.class);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(PokreniActivity);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                ImeIPrezime = firebaseAuth.getCurrentUser().getDisplayName();
                phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
                Uri photo = firebaseAuth.getCurrentUser().getPhotoUrl();


            }

        }


    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dobrodosli);

        ///////////dugmici za signIn i signUp aktivitije
        buttonSignIn= findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DobrodosliActivity.this,LoginProba.class));
            }
        });

        buttonSignUp= findViewById(R.id.buttonSignup);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DobrodosliActivity.this,Register.class));
            }
        });
        //////////////

        firebaseAuth = FirebaseAuth.getInstance();

        ////////////

        //TODO:Izbrisi ovo google i face svuda

    }

    @Override
    public void onStart() {
        super.onStart();

       // firebaseAuth.addAuthStateListener(mAuthListener);

        Intent PokreniActivity=new Intent(DobrodosliActivity.this, IzaberiActivity.class);
        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null)
            startActivity(PokreniActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



    }



    @Override
    public void onClick(View view) {

    }
}
