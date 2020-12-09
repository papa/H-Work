/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.dobrimajstori.kucnimajstor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MojNalogFragment extends Fragment {

    public static MojNalogFragment newInstance()
    {
        MojNalogFragment fragment = new MojNalogFragment();
        return fragment;
    }

    //Userovi podaci
    private String ImeIPrezime;
    private String phoneNumber;
    private TextView imeiPrezime;

    //firebase
    private FirebaseAuth firebaseAuth;
    static StorageReference mStorage;
    static FirebaseUser user;

    private TextView imeiprezime;
    private TextView poslovi;
    private TextView ocena;
    private TextView mail;
    private TextView brojtel;
    private TextView lokacija;
    private DatabaseReference korisnikRef;
    static Korisnik currentKorisnik;
    private CircleImageView sliketina;
    private static final int GALLERY_INTENT = 2;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {

        //proverava da li je user ulogovan i uzima podatke
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            if(firebaseAuth.getCurrentUser() != null)
            {

                ImeIPrezime = firebaseAuth.getCurrentUser().getDisplayName();
                phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
                Uri photo = firebaseAuth.getCurrentUser().getPhotoUrl();
            }

        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moj_nalog, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        imeiPrezime= getView().findViewById(R.id.imeiprezime);
        poslovi= getView().findViewById(R.id.poslovi);
        ocena= getView().findViewById(R.id.ocena);
        mail= getView().findViewById(R.id.mail);
        brojtel= getView().findViewById(R.id.telefon);
        lokacija= getView().findViewById(R.id.lokacijaMojnalog);

        //Toast.makeText(view.getContext(),userid,Toast.LENGTH_LONG).show();
        korisnikRef =FirebaseDatabase.getInstance().getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid());

        korisnikRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentKorisnik =dataSnapshot.getValue(Korisnik.class);


                imeiPrezime.setText(currentKorisnik.getIme()+" "+ currentKorisnik.getPrezime());
                poslovi.setText(Integer.toString(currentKorisnik.getBrojposlova()));
                //ocena.setText(Double.toString(currentKorisnik.getProsecnaocena()));
                ocena.setText(String.format("%.1f", currentKorisnik.getProsecnaocena()));
                mail.setText(currentKorisnik.getMail());
                brojtel.setText(currentKorisnik.getBrtel());
                lokacija.setText(currentKorisnik.getLokacija());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sliketina= getView().findViewById(R.id.profilnaslika);

       if(Pocetna.uri.equals("prazno"))
       {
           sliketina.setImageResource(R.drawable.ic_korisnik);
       }
       else
       {
           Picasso.get().load(Pocetna.uri).into(sliketina);
       }




       Button promeniVrstuNaloga= getView().findViewById(R.id.buttonPromeniPristup);
        promeniVrstuNaloga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PokreniActivity=new Intent(getContext(), PodesavanjeNaloga.class);/*
             PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);*/
                startActivity(PokreniActivity);
            }
        });

        Button logOut= getView().findViewById(R.id.buttonLogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                    Intent PokreniActivity=new Intent(getContext(), DobrodosliActivity.class);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(PokreniActivity);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            if (checkPermissionREAD_EXTERNAL_STORAGE(getActivity())) {
                Uri uri = data.getData();
                Log.d("SLIKA", "onActivityResult: " + uri);
                final ImageView circle = getView().findViewById(R.id.profilnaslika);


//            byte[] slikaBytes = null;
//            try {
//                Bitmap bitmap = Picasso.with(HomeActivity.this).load(uri).fit().centerCrop().resize(200, 200).onlyScaleDown().get();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                slikaBytes = baos.toByteArray();
//
//            }
//            catch(Exception e) {
//                Log.d("SLIKA", "Greska", e);
//            }

                Log.d("SLIKA", "onActivityResult: 2");
                StorageReference filepath = mStorage.child("Photos").child(user.getUid());

//            filepath.putBytes(slikaBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();

                        RequestOptions myOptions = new RequestOptions()
                                .override(100, 100);

                        Log.d("SLIKA 2", "onActivityResult: " + downloadUri);
                        //Picasso.with(getActivity()).load(String.valueOf(downloadUri)).fit().centerCrop().into(circle);
                        Glide.with(getActivity()).load(downloadUri).apply(RequestOptions.circleCropTransform()).apply(myOptions).into(circle);

                    }


                });
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(getActivity(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
}
