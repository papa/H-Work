package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dobrimajstori.kucnimajstor.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class IzaberiActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private DatabaseReference korisnik;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izaberi);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        korisnik=FirebaseDatabase.getInstance().getReference("Korisnici").child(currentUser.getUid());


        Button izaberimajstor = findViewById(R.id.izaberim);
        Button izaberiposlodavac = findViewById(R.id.izaberip);

        izaberimajstor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final PrettyDialog dialogfilter= new PrettyDialog(IzaberiActivity.this);
                dialogfilter
                        .setTitle("Dobrodošao majsotre!")
                        .setMessage("Izabrali ste da budete prijavljeni kao majstor, kasnije ovo možete promeniti u podešavanjima naloga.")
                        .setIcon(R.drawable.pdlg_icon_info)
                        .setIconTint(R.color.colorPrimary)
                        .addButton(
                                "U REDU",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {

                                        //Pocetna.isMajstor=true;
                                        korisnik.child("majstor").setValue(true);
                                        Intent PokreniActivity=new Intent(IzaberiActivity.this, Pocetna.class);
                                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(PokreniActivity);

                                        dialogfilter.dismiss();
                                    }
                                }  // button background color

                        )
                        .show();





            }
        });

        izaberiposlodavac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PrettyDialog dialogfilter= new PrettyDialog(IzaberiActivity.this);
                dialogfilter
                        .setTitle("Dobrodošao poslodavče!")
                        .setMessage("Izabrali ste da budete prijavljeni kao poslodavac, kasnije ovo možete promeniti u podešavanjima naloga.")
                        .setIcon(R.drawable.pdlg_icon_info)
                        .setIconTint(R.color.colorPrimary)
                        .addButton(
                                "U REDU",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {

                                        //Pocetna.isMajstor=false;
                                        korisnik.child("majstor").setValue(false);
                                        Intent PokreniActivity=new Intent(IzaberiActivity.this, Pocetna.class);
                                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(PokreniActivity);

                                        dialogfilter.dismiss();
                                    }
                                }  // button background color

                        )
                        .show();




            }
        });

    }

    /*static public void updateToken(String token)
    {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(user.getUid()).setValue(token1);
    }*/
}
