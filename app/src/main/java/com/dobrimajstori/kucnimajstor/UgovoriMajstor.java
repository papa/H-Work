package com.dobrimajstori.kucnimajstor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import static com.facebook.FacebookSdk.getApplicationContext;


public class UgovoriMajstor extends Fragment {


    private static final String ARG_COLUMN_COUNT = "1";

    private int columnCount = 1;

    ArrayList<Ugovor> listaUgovora;
    //UgovoriMajstorRecyclerViewAdapter adapter;

    ListView theListView;
    ImageView background;

    UgovoriMajstorAdapter adapter;
    //private OnListFragmentInteractionListener mListener;


    public UgovoriMajstor () {

    }


    @SuppressWarnings("unused")
    public static UgovoriMajstor newInstance(int columnCount) {
        UgovoriMajstor fragment = new UgovoriMajstor();
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



        final View view = inflater.inflate(R.layout.fragment_ugovori_majstor, container, false);


        background= view.findViewById(R.id.background);


        // prepare elements to display
        listaUgovora=new ArrayList<Ugovor>();

        adapter = new UgovoriMajstorAdapter(getContext(), listaUgovora);


        String idkorisnika = Pocetna.currentFirebaseUser.getUid();

        DatabaseReference ugovoriMajstoraRef=FirebaseDatabase.getInstance().getReference("Korisnici").child(idkorisnika).child("PotpisaniUgovoriMajstor");

        ugovoriMajstoraRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    listaUgovora.add(dataSnapshot1.getValue(Ugovor.class));

                    //Toast.makeText(view.getContext(),dataSnapshot1.getValue(Ugovor.class).getPosao(),Toast.LENGTH_SHORT).show();
                }

                if(listaUgovora !=null)
                    if(listaUgovora.size()==0)
                        background.setImageResource(R.drawable.background_nodata_ugovori);
                    else
                    Collections.sort(listaUgovora, new Comparator<Ugovor>() {
                        @Override
                        public int compare(Ugovor u1, Ugovor u2) {


                            Calendar datumU1 =Calendar.getInstance();
                            datumU1.set(u1.getDandolaska().getGodina(),u1.getDandolaska().getMesec(),u1.getDandolaska().getDan(),u1.getDandolaska().getSati(),u1.getDandolaska().getMinuti());

                            Calendar datumU2=Calendar.getInstance();
                            datumU2.set(u2.getDandolaska().getGodina(),u2.getDandolaska().getMesec(),u2.getDandolaska().getDan(),u2.getDandolaska().getSati(),u2.getDandolaska().getMinuti());

                            return datumU1.compareTo(datumU2);
                        }
                    });
                else
                    listaUgovora =new ArrayList<Ugovor>();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
           // mListener = (OnListFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //  + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Ugovor item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // get our list view
        theListView = view.findViewById(R.id.mainListView);

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




        /*
        //Postavljanje adaptera
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.listaUgovoraRecyclerMajstor);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        */
    }

}
