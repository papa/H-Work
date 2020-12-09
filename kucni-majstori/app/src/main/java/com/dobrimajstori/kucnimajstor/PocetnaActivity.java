package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PocetnaActivity extends AppCompatActivity {

    Fragment selectedFragment = null;
    private FirebaseAuth firebaseAuth;
    private Boolean exit = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = PosaoFragment.newInstance(1);
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = ItemTwoFragment.newInstance();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = MojNalogFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna);


        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){

            Intent PokreniActivity=new Intent(PocetnaActivity.this, LoginProba.class);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(PokreniActivity);
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PosaoFragment.newInstance(1));
        transaction.commit();
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
}
