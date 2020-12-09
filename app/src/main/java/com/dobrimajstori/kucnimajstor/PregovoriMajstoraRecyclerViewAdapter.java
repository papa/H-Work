package com.dobrimajstori.kucnimajstor;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PregovoriMajstoraRecyclerViewAdapter extends RecyclerView.Adapter<PregovoriMajstoraRecyclerViewAdapter.ViewHolder>
{

    private final ArrayList<Bid> mValues;
    int budz;
    int[] mp;

    public PregovoriMajstoraRecyclerViewAdapter(ArrayList<Bid> items) {
        mValues = items;
    }

    @Override
    public PregovoriMajstoraRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pregovori_majstor_kartica, parent, false);
        return new PregovoriMajstoraRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PregovoriMajstoraRecyclerViewAdapter.ViewHolder holder, final int position) {


        final DatabaseReference data=FirebaseDatabase.getInstance().getReference("Korisnici").child(mValues.get(position).getIdPoslodavca()).child("UriSlike");

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String uri=dataSnapshot.getValue(String.class);
                    Picasso.get().load(uri).into(holder.profilna);
                }
                else
                {
                    holder.profilna.setImageResource(R.drawable.ic_korisnik);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Pocetna.baza.getReference("SviPoslovi").child(mValues.get(position).getIdposla()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                budz=dataSnapshot.getValue(Posao.class).getBudzet();

                holder.naslov.setText(dataSnapshot.getValue(Posao.class).getNaslovposla());
                holder.budzet.setText(Integer.toString(budz));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.ponudaMajstora.setText(Integer.toString(mValues.get(position).getNovac()));
        holder.mojaPonudaLevo.setText(mValues.get(position).getNovac() +" RSD");


        Pocetna.baza.getReference("Korisnici").child(mValues.get(position).getIdPoslodavca()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Korisnik poslodavac=dataSnapshot.getValue(Korisnik.class);

                holder.imePoslodavca.setText(poslodavac.getIme()+" "+poslodavac.getPrezime());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Pocetna.baza.getReference("Ugovori").child(mValues.get(position).getIdPoslodavca() + Pocetna.currentFirebaseUser.getUid()+ mValues.get(position).getIdposla()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Ugovor ugovor=dataSnapshot.getValue(Ugovor.class);
                if(ugovor==null)
                {
                    holder.ugovorInfo.setText("Nema");
                }
                else
                    if(ugovor.isPotpisan())
                    {
                        holder.ugovorInfo.setText("Prihvaćen");
                        holder.ugovorInfo.setTextColor(Color.GREEN);
                        //holder.ugovorInfo.setTextColor();
                    }
                    else
                    {
                        holder.ugovorInfo.setText("Poslat");
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.mView.setTag(position);

        holder.mItem = mValues.get(position);

        //holder.budzet.setText(mValues.get(position).getNovac());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final View mView;
        public final TextView naslov;
        public final TextView imePoslodavca;
        public Bid mItem;
        public final TextView budzet;
        public final TextView ponudaMajstora;
        public final TextView mojaPonudaLevo;
        public final ImageView profilna;
        public final TextView ugovorInfo;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            profilna= view.findViewById(R.id.content_avatar);
            naslov = view.findViewById(R.id.title_from_address);
            imePoslodavca = view.findViewById(R.id.content_name_view);
            budzet= view.findViewById(R.id.title_requests_count);
            ponudaMajstora = view.findViewById(R.id.title_pledge);
            mojaPonudaLevo= view.findViewById(R.id.title_price);
            ugovorInfo= view.findViewById(R.id.title_weight);
            view.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + naslov.getText() + "'";
        }

        @Override
        public void onClick(final View view) {

            final int pos=(int)view.getTag();


            Pocetna.prenosBida=mValues.get(pos);

            Pocetna.baza.getReference("SviPoslovi").child(mValues.get(pos).getIdposla()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {


                        MessageActivity.drugiId = dataSnapshot.getValue(Posao.class).getIdposlodavca();

                        Intent i = new Intent(view.getContext(), MessageActivity.class);
                        view.getContext().startActivity(i);

                    }
                    catch (Exception e){
                        Toast.makeText(view.getContext(),"Posao je završen!",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });






        }
    }
}
