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

public class PregovoriPoslodavacRecyclerViewAdapter extends RecyclerView.Adapter<PregovoriPoslodavacRecyclerViewAdapter.ViewHolder>
{

    private final ArrayList<Bid> mValues;
    int budz;
    int[] mp;

    public PregovoriPoslodavacRecyclerViewAdapter(ArrayList<Bid> items) {
        mValues = items;
    }

    @Override
    public PregovoriPoslodavacRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pregovori_poslodavac_kartica, parent, false);
        return new PregovoriPoslodavacRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PregovoriPoslodavacRecyclerViewAdapter.ViewHolder holder, final int position) {


        Pocetna.baza.getReference("SviPoslovi").child(mValues.get(position).getIdposla()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                budz=dataSnapshot.getValue(Posao.class).getBudzet();

                holder.naslovPosla.setText(dataSnapshot.getValue(Posao.class).getNaslovposla());
                //holder.opis.setText(dataSnapshot.getValue(Posao.class).getOpis());
                holder.budzet.setText(Integer.toString(budz));
                holder.ponudaMajstora.setText(Integer.toString(mValues.get(position).getNovac()));
                if(mValues.get(position).isSkola())
                    holder.ponudaLevo.setText("Praksa");
                else
                    holder.ponudaLevo.setText(mValues.get(position).getNovac() +" RSD");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Pocetna.baza.getReference("Korisnici").child(mValues.get(position).getIdmajstora()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Korisnik majstor=dataSnapshot.getValue(Korisnik.class);

                holder.imeMajstora.setText(majstor.getIme()+" "+majstor.getPrezime());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.mView.setTag(position);

        holder.mItem = mValues.get(position);

        DatabaseReference data=FirebaseDatabase.getInstance().getReference("Korisnici").child(mValues.get(position).getIdmajstora()).child("UriSlike");

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

        Pocetna.baza.getReference("Ugovori").child(Pocetna.currentFirebaseUser.getUid() + mValues.get(position).getIdmajstora() + mValues.get(position).getIdposla()).addValueEventListener(new ValueEventListener() {
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
                    //holder.ugovorInfo.setTextColor();
                }
                else
                {
                    holder.ugovorInfo.setText("Poslat");
                    holder.ugovorInfo.setTextColor(Color.GREEN);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(mValues.get(position).isSkola())
        {
            holder.verifikovano.setVisibility(View.VISIBLE);
            holder.ponudaLevo.setText("Praksa");
        }


        //holder.budzet.setText(mValues.get(position).getNovac());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final View mView;
        public final TextView naslovPosla;
        //public final TextView opis;
        public Bid mItem;
        public final TextView budzet;
        public final TextView ponudaMajstora;
        public final  TextView ponudaLevo;
        public final TextView imeMajstora;
        public final ImageView profilna;
        public final TextView ugovorInfo;
        public final ImageView verifikovano;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            profilna= view.findViewById(R.id.content_avatar);
            naslovPosla = view.findViewById(R.id.title_from_address);
            //opis = (TextView) view.findViewById(R.id.sub_textmajstor);
            budzet= view.findViewById(R.id.title_requests_count);
            ponudaMajstora = view.findViewById(R.id.title_pledge);
            ponudaLevo= view.findViewById(R.id.title_price);
            imeMajstora= view.findViewById(R.id.content_name_view);
            ugovorInfo= view.findViewById(R.id.title_weight);
            verifikovano= view.findViewById(R.id.verifikovanoPonude);
            view.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + naslovPosla.getText() + "'";
        }

        @Override
        public void onClick(View view) {

            try {


                int pos = (int) view.getTag();


                Pocetna.prenosBida = mValues.get(pos);

                MessageActivity.drugiId = mValues.get(pos).getIdmajstora();

                Intent i = new Intent(view.getContext(), MessageActivity.class);
                view.getContext().startActivity(i);
            }
            catch (Exception e)
            {
                Toast.makeText(view.getContext(),"Posao je završen",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
