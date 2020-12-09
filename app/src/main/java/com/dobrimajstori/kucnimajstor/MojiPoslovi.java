package com.dobrimajstori.kucnimajstor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MojiPoslovi extends Fragment {


    private static final String ARG_COLUMN_COUNT = "1";

    private int columnCount = 1;

    ArrayList<String> mojiP;
    ArrayList<Posao> listaPoslova;

    MojiPosloviAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView theListView;
    ImageView background;




    public MojiPoslovi() {

    }


    @SuppressWarnings("unused")
    public static MojiPoslovi newInstance(int columnCount) {
        MojiPoslovi fragment = new MojiPoslovi();
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
        final View view = inflater.inflate(R.layout.fragment_moji_poslovi, container, false);

        background= view.findViewById(R.id.background_moji_poslovi);



        listaPoslova=new ArrayList<Posao>();

        adapter =new MojiPosloviAdapter(getContext(),listaPoslova);

        String idkorisnika = Pocetna.currentFirebaseUser.getUid();

        DatabaseReference prijavljeniposloviRef=FirebaseDatabase.getInstance().getReference("Korisnici").child(idkorisnika).child("postavljeniposlovi");
        mojiP =new ArrayList<String>();

        prijavljeniposloviRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    mojiP.add(dataSnapshot1.getValue(String.class));

                if(mojiP.size()==0)
                    background.setImageResource(R.drawable.background_nodata_moji_poslovi);

                if(mojiP !=null)
                    Collections.reverse(mojiP);
                else
                    mojiP =new ArrayList<String>();

                for(String mojPosao : mojiP)
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


        return view;
    }


    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Posao item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // get our list view
        theListView = view.findViewById(R.id.mojiPosloviKartica);

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



       FloatingActionButton fab =getView().findViewById(R.id.fabDodajPosao);

      // TextView poruka =(TextView)getView().findViewById(R.id.porukaNemaMojihPoslova);

        //ovde uvek vidi da je prihvaceniBidovi prazno iako nije ne znam zasto

        /*if(prihvaceniBidovi.isEmpty())
            poruka.setVisibility(View.VISIBLE);
        else
            poruka.setVisibility(View.GONE);
*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PokreniActivity=new Intent(getContext(), PostavljanjePoslovaActivity.class);
                startActivity(PokreniActivity);
            }
        });

    }

}
