package com.dobrimajstori.kucnimajstor;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class InfoMajstorKomentariAdapter  extends RecyclerView.Adapter<InfoMajstorKomentariAdapter.ViewHolder>
{

    private final ArrayList<Komentar> mValues;


    public InfoMajstorKomentariAdapter(ArrayList<Komentar> items) {
        mValues = items;
    }

    @Override
    public InfoMajstorKomentariAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.komentar_info_majstor_kartica, parent, false);



        return new InfoMajstorKomentariAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InfoMajstorKomentariAdapter.ViewHolder holder, final int position) {


        Komentar komentar =mValues.get(position);



        String urislike=komentar.getUriSlika();

        if(urislike==null)
        {
            holder.slika.setImageResource(R.drawable.ic_korisnik);
        }
        else
        {
            Picasso.get().load(urislike).into(holder.slika);
        }

        holder.mView.setTag(position);

        holder.ime.setText(komentar.getIme());
        holder.prezime.setText(komentar.getPrezime());
        holder.komentar.setText(komentar.getKomentar());
        holder.ocena.setRating(komentar.getOcena());


        Calendar popuni =Calendar.getInstance();
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        popuni.set(komentar.getDatumKomentara().getGodina(),komentar.getDatumKomentara().getMesec(),komentar.getDatumKomentara().getDan(),komentar.getDatumKomentara().getSati(),komentar.getDatumKomentara().getMinuti());
        String datum=sdf.format(popuni.getTime());
        holder.datum.setText(datum);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final View mView;
        public Komentar mItem;
        public final TextView ime;
        public final TextView prezime;
        public final TextView komentar;
        public final TextView datum;
        public final ImageView slika;
        public final MaterialRatingBar ocena;


        public ViewHolder(View view) {

            super(view);
            mView = view;
            ime = view.findViewById(R.id.imeKomentar);
            prezime = view.findViewById(R.id.prezimeKomentar);
            ocena= view.findViewById(R.id.ocenaKomentar);
            komentar= view.findViewById(R.id.komentarText);
            datum= view.findViewById(R.id.datumKomentar);
            slika= view.findViewById(R.id.slikaKomentar);
            view.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + ime.getText() + "'";
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
