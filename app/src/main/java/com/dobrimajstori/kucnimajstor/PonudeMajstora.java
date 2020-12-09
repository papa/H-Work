package com.dobrimajstori.kucnimajstor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PonudeMajstora extends Fragment {


        private static final String ARG_COLUMN_COUNT = "1";

        private int columnCount = 1;

        ArrayList<Posao> listaPoslova;
        PonudeMajstoraAdapter adapter;
        ArrayList<Bid> prijavljeniP;
        ImageView background;

        ListView theListView;


    Calendar datumU1;
    Calendar datumU2;

        private Bid b;


        public PonudeMajstora() {

        }


        @SuppressWarnings("unused")
        public static PonudeMajstora newInstance(int columnCount) {
                PonudeMajstora fragment = new PonudeMajstora();
                Bundle args = new Bundle();
                args.putInt(ARG_COLUMN_COUNT, columnCount);
                fragment.setArguments(args);
                return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                if (getArguments() != null) {
                        columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
                }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
                final View view = inflater.inflate(R.layout.fragment_ponude_majstora, container, false);

                listaPoslova=new ArrayList<Posao>();
                prijavljeniP=new ArrayList<Bid>();
                adapter =new PonudeMajstoraAdapter(getContext(),prijavljeniP);
                background= view.findViewById(R.id.background);

                String idkorisnika = Pocetna.currentFirebaseUser.getUid();

                //Uzima iz baze sve bidove i stavlja u prijavljeniP;
                final DatabaseReference ponudeMajstora=FirebaseDatabase.getInstance().getReference("Korisnici").child(idkorisnika).child("PonudeMajstora");


                //Proverava da li je posao vec ugovoren
                Pocetna.baza.getReference("SviPoslovi").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot posloviDataSnapshot) {


                    ponudeMajstora.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ponudeDataSnapshot) {


                            for(DataSnapshot bidPodIdMajstora:ponudeDataSnapshot.getChildren()){
                                for(DataSnapshot bidPodIdPosla:bidPodIdMajstora.getChildren())
                                {
                                    b =bidPodIdPosla.getValue(Bid.class);

                                    if(!posloviDataSnapshot.child(b.getIdposla()).getValue(Posao.class).isUgovoren())
                                        prijavljeniP.add( b);



                                }

                            }

                            if(prijavljeniP !=null)
                                if(prijavljeniP.size()==0)
                                {
                                    background.setImageResource(R.drawable.background_nodata_ponude_majstora);
                                }
                                else
                                Collections.sort(prijavljeniP, new Comparator<Bid>() {
                                    @Override
                                    public int compare(Bid u1, Bid u2) {


                                        if (u1.isSkola() && !u2.isSkola())
                                            return -1;
                                        else if (!u1.isSkola() && u2.isSkola())
                                            return 1;
                                        else {

                                            datumU1 = Calendar.getInstance();
                                            datumU1.set(u1.getDatumBida().getGodina(), u1.getDatumBida().getMesec(), u1.getDatumBida().getDan(), u1.getDatumBida().getSati(), u1.getDatumBida().getMinuti());

                                            Calendar datumU2 = Calendar.getInstance();
                                            datumU2.set(u2.getDatumBida().getGodina(), u2.getDatumBida().getMesec(), u2.getDatumBida().getDan(), u2.getDatumBida().getSati(), u2.getDatumBida().getMinuti());

                                            return datumU1.compareTo(datumU2);
                                        }



                                    }
                                });
                            else
                                prijavljeniP =new ArrayList<Bid>();
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });







                return view;
        }


        public interface OnListFragmentInteractionListener {

                void onListFragmentInteraction(Posao item);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);


            // get our list view
            theListView = view.findViewById(R.id.ponudeMajstoraLista);

            // set elements to adapter
            theListView.setAdapter(adapter);

            // set on click event listener to list view
            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    // toggle clicked ugovor_majstor_kartica state
                    ((FoldingCell) view).toggle(false);
                    // register in adapter that state for selected ugovor_majstor_kartica is toggled
                    adapter.registerToggle(pos);
                }
            });







            // TextView poruka =(TextView)getView().findViewById(R.id.porukaNemaMojihPoslova);

                //ovde uvek vidi da je prihvaceniBidovi prazno iako nije ne znam zasto

        /*if(prihvaceniBidovi.isEmpty())
            poruka.setVisibility(View.VISIBLE);
        else
            poruka.setVisibility(View.GONE);
*/


        }

}
