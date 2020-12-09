package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SkolaNeodobrena extends AppCompatActivity {

    Korisnik skola;
    Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skola_ne_odobrena);

        logOut= findViewById(R.id.logOutVerifikacija);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseAuth.getInstance().signOut();

                Intent PokreniActivity=new Intent(getApplicationContext(), DobrodosliActivity.class);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(PokreniActivity);
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                skola=dataSnapshot.getValue(Korisnik.class);

                if(skola.isSkolaOdobren()){

                    Intent PokreniActivity=new Intent(SkolaNeodobrena.this, Pocetna.class);
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
    }
}
