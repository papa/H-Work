package com.dobrimajstori.kucnimajstor;

import android.content.Context;
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
import android.widget.ImageView;

import com.dobrimajstori.kucnimajstor.Notifications.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PregovoriPoslodavac extends Fragment {


    private static final String ARG_COLUMN_COUNT = "1";

    private int columnCount = 1;

    ArrayList<Posao> listaPoslova;
    PregovoriPoslodavacRecyclerViewAdapter adapter;
    ArrayList<Bid> prijavljeniP;
    ImageView background;

    private OnListFragmentInteractionListener mListener;


    public PregovoriPoslodavac() {

    }


    @SuppressWarnings("unused")
    public static PregovoriPoslodavac newInstance(int columnCount) {
        PregovoriPoslodavac fragment = new PregovoriPoslodavac();
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
        final View view = inflater.inflate(R.layout.fragment_pregovori_poslodavac, container, false);

        listaPoslova=new ArrayList<Posao>();
        prijavljeniP=new ArrayList<Bid>();
        adapter =new PregovoriPoslodavacRecyclerViewAdapter(prijavljeniP);
        background= view.findViewById(R.id.background);

        String idkorisnika = Pocetna.currentFirebaseUser.getUid();


        //Uzima iz baze dve bidove koje je poslodavac odobrio;
        DatabaseReference prijavljeniposlovi=FirebaseDatabase.getInstance().getReference("Korisnici").child(idkorisnika).child("pregovoriPoslodavac");
        prijavljeniposlovi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot bidPodIdMajstora:dataSnapshot.getChildren())
                {
                    for(DataSnapshot bidPodIdPosla:bidPodIdMajstora.getChildren())
                        prijavljeniP.add(bidPodIdPosla.getValue(Bid.class));
                }
                if(prijavljeniP.size()==0)
                    background.setImageResource(R.drawable.background_nodata_pregovori);
                Collections.reverse(prijavljeniP);
                Collections.sort(prijavljeniP, new Comparator<Bid>() {
                    @Override
                    public int compare(Bid u1, Bid u2) {


                        if (u1.isSkola() && !u2.isSkola())
                            return -1;
                        else if (!u1.isSkola() && u2.isSkola())
                            return 1;
                        else {
                            return -1;

                            /*datumU1 = Calendar.getInstance();
                            datumU1.set(u1.getDatumBida().getGodina(), u1.getDatumBida().getMesec(), u1.getDatumBida().getDan(), u1.getDatumBida().getSati(), u1.getDatumBida().getMinuti());

                            Calendar datumU2 = Calendar.getInstance();
                            datumU2.set(u2.getDatumBida().getGodina(), u2.getDatumBida().getMesec(), u2.getDatumBida().getDan(), u2.getDatumBida().getSati(), u2.getDatumBida().getMinuti());

                            return datumU1.compareTo(datumU2);*/
                        }



                    }
                });
                adapter.notifyDataSetChanged();
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
        RecyclerView recyclerView = getView().findViewById(R.id.listaPregovoriPoslodavac);
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
