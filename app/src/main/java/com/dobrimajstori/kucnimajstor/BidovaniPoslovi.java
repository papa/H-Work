package com.dobrimajstori.kucnimajstor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BidovaniPoslovi extends Fragment {


    private static final String ARG_COLUMN_COUNT = "1";

    private int columnCount = 1;

    ArrayList<Posao> listaPoslova;
    BidovaniPosloviRecyclerViewAdapter adapter;
    ArrayList<String> bidovaniP;

    private OnListFragmentInteractionListener mListener;


    public BidovaniPoslovi() {

    }


    @SuppressWarnings("unused")
    public static BidovaniPoslovi newInstance(int columnCount) {
        BidovaniPoslovi fragment = new BidovaniPoslovi();
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
        final View view = inflater.inflate(R.layout.fragment_bidovani_poslovi, container, false);

        listaPoslova=new ArrayList<Posao>();

        adapter =new BidovaniPosloviRecyclerViewAdapter(listaPoslova);

        String idkorisnika = Pocetna.currentFirebaseUser.getUid();


        //Uzima iz baze id svih poslova za koje se korisnik prijavio i stavlja u bidovaniP;
        DatabaseReference prijavljeniposloviRef=FirebaseDatabase.getInstance().getReference("Korisnici").child(idkorisnika).child("bidovaniposlovi");
        bidovaniP =new ArrayList<String>();

        prijavljeniposloviRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    bidovaniP.add(dataSnapshot1.getValue(String.class));

                // GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                // bidovaniP =dataSnapshot.getValue(t);

                if(bidovaniP !=null)
                    Collections.reverse(bidovaniP);
                else
                    bidovaniP =new ArrayList<String>();

                for(String mojPosao : bidovaniP)
                {

                    //Toast.makeText(view.getContext(),mojPosao,Toast.LENGTH_LONG).show();

                    Pocetna.baza.getReference("SviPoslovi").child(mojPosao).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            listaPoslova.add(dataSnapshot.getValue(Posao.class));

                            adapter.notifyDataSetChanged();

                            //Toast.makeText(getContext(),dataSnapshot.getValue(Posao.class).getNaslovposla(),Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     /*
        prijavljeniposloviRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    bidovaniP.add(dataSnapshot1.getValue(String.class));

               // GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
               // bidovaniP =dataSnapshot.getValue(t);

                if(bidovaniP !=null)
                    Collections.reverse(bidovaniP);
                else
                    bidovaniP =new ArrayList<String>();

                for(String mojPosao : bidovaniP)
                {

                    //Toast.makeText(view.getContext(),mojPosao,Toast.LENGTH_LONG).show();

                    Pocetna.baza.getReference("SviPoslovi").child(mojPosao).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            listaPoslova.add(dataSnapshot.getValue(Posao.class));

                            adapter.notifyDataSetChanged();

                            //Toast.makeText(getContext(),dataSnapshot.getValue(Posao.class).getNaslovposla(),Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/


        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //  + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Posao item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Postavljanje adaptera
        Context context = view.getContext();
        RecyclerView recyclerView = getView().findViewById(R.id.listaBidovanihPoslova);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        // TextView poruka =(TextView)getView().findViewById(R.id.porukaNemaMojihPoslova);

        //ovde uvek vidi da je prihvaceniBidovi prazno iako nije ne znam zasto

        /*if(prihvaceniBidovi.isEmpty())
            poruka.setVisibility(View.VISIBLE);
        else
            poruka.setVisibility(View.GONE);
*/


    }

}
