package com.dobrimajstori.kucnimajstor;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BidovaniPosloviRecyclerViewAdapter  extends RecyclerView.Adapter<BidovaniPosloviRecyclerViewAdapter.ViewHolder>
{

    private final ArrayList<Posao> mValues;


    public BidovaniPosloviRecyclerViewAdapter(ArrayList<Posao> items) {
        mValues = items;
    }

    @Override
    public BidovaniPosloviRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bidovani_poslovi_kartica, parent, false);



        return new BidovaniPosloviRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BidovaniPosloviRecyclerViewAdapter.ViewHolder holder, final int position) {


        final int[] mp = new int[1];
        final int budz;


        holder.mView.setTag(position);

        budz=mValues.get(position).getBudzet();

        holder.mItem = mValues.get(position);
        holder.naslov.setText(mValues.get(position).getNaslovposla());
        holder.opis.setText(mValues.get(position).getOpis());
        holder.budzet.setText("Budzet: " + budz +" din.");


        Pocetna.baza.getReference("SviPoslovi").child(mValues.get(position).getIdposla()).child("Bidovi").child(Pocetna.currentFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bid b=dataSnapshot.getValue(Bid.class);
                mp[0] =b.getNovac();
                holder.mojaPonuda.setText("Moja ponuda: "+ mp[0] +" din.");
                if(mp[0]>budz)
                {
                    holder.mojaPonuda.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       /* holder.mView.setTag(position);

        budz=mValues.get(position).getBudzet();

        holder.mItem = mValues.get(position);
        holder.naslov.setText(mValues.get(position).getNaslovposla());
        holder.opis.setText(mValues.get(position).getOpis());
        holder.budzet.setText("Budzet: " +Integer.toString(budz)+" din.");*/

        /*if(mp[0]>budz)
        {
            holder.mojaPonuda.setTextColor(Color.RED);
        }*/

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final View mView;
        public final TextView naslov;
        public final TextView opis;
        public Posao mItem;
        public final TextView budzet;
        public final TextView mojaPonuda;


        public ViewHolder(View view) {

            super(view);
            mView = view;
            naslov = view.findViewById(R.id.primary_textbid);
            opis = view.findViewById(R.id.sub_textbid);
            budzet= view.findViewById(R.id.budzetbid);
            mojaPonuda= view.findViewById(R.id.mojaponuda);
            view.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + opis.getText() + "'";
        }

        @Override
        public void onClick(View view) {

            int pos=(int)view.getTag();


            // Pocetna.prenosPosla=mValues.get(pos);


            // Intent i= new Intent(view.getContext(),InfoPosaoActivity.class);
            // view.getContext().startActivity(i);

        }
    }
}
