package com.dobrimajstori.kucnimajstor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dobrimajstori.kucnimajstor.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PregovoriMajstor extends Fragment {


    private static final String ARG_COLUMN_COUNT = "1";

    private int columnCount = 1;

    ArrayList<Posao> listaPoslova;
    PregovoriMajstoraRecyclerViewAdapter adapter;
    ArrayList<Bid> prihvaceniBidovi;
    ImageView background;

    private OnListFragmentInteractionListener mListener;


    public PregovoriMajstor() {

    }


    @SuppressWarnings("unused")
    public static PregovoriMajstor newInstance(int columnCount) {
        PregovoriMajstor fragment = new PregovoriMajstor();
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
        final View view = inflater.inflate(R.layout.fragment_pregovori_majstor, container, false);

        listaPoslova=new ArrayList<Posao>();
        prihvaceniBidovi =new ArrayList<Bid>();
        adapter =new PregovoriMajstoraRecyclerViewAdapter(prihvaceniBidovi);
        background= view.findViewById(R.id.background);

        String idkorisnika = Pocetna.currentFirebaseUser.getUid();


        //Uzima iz baze sve bidove koje je poslodavac prihvatio i stavlja ih u prihvaceniBidovi;
        final DatabaseReference prihvacenePonude=FirebaseDatabase.getInstance().getReference("Korisnici").child(idkorisnika).child("pregovoriMajstor");
        prihvacenePonude.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot prijave:dataSnapshot.getChildren()){
                    prihvaceniBidovi.add(prijave.getValue(Bid.class));
                }
                if(prihvaceniBidovi.size()==0)
                    background.setImageResource(R.drawable.background_nodata_pregovori);
                else {
                    Collections.reverse(prihvaceniBidovi);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    public void updateToken(String token)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(Pocetna.currentFirebaseUser.getUid()).setValue(token1);
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
        RecyclerView recyclerView = getView().findViewById(R.id.listaPregovoriMajstor);
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
