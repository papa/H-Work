package com.dobrimajstori.kucnimajstor;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.adroitandroid.chipcloud.FlowLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SviPoslovi extends Fragment {


    private static final String ARG_COLUMN_COUNT = "1";

    private int mColumnCount = 1;

    ArrayList<Posao> listaPoslova;
    ArrayList<Posao> filtriranaListaPoslova;
    SviPosloviAdapter adapter;

    TextView izabranifilteriText;

    List<String> izabraniFilteriList;
    HashSet<String> izabraniFilterSet;
    ArrayList<Integer> prethodnoSelektovaniFilteri;
    TextView poruka;
    Button ukloniFiltereButton;

    ChipCloud filteriChipCloud;
    ImageView backImage;
    Button filterButton;



    ListView theListView;

    public SviPoslovi() {
    }



    @SuppressWarnings("unused")
    public static SviPoslovi newInstance(int columnCount) {
        SviPoslovi fragment = new SviPoslovi();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_svi_poslovi, container, false);


        backImage= view.findViewById(R.id.backImageSviPoslovi);
        filterButton= view.findViewById(R.id.filterButton);
        prethodnoSelektovaniFilteri=new ArrayList<Integer>();

        listaPoslova=new ArrayList<Posao>();
        filtriranaListaPoslova=new ArrayList<Posao>();

        adapter =new SviPosloviAdapter(getContext(),filtriranaListaPoslova);

        Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("bidovaniposlovi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataBidovaniPoslovi) {


                Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("pregovoriMajstor").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataPregovoriMajstor) {



                        Pocetna.baza.getReference("SviPoslovi").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSviPoslovi) {
                                for (DataSnapshot dataPosao : dataSviPoslovi.getChildren()) {

                                    final Posao p = dataPosao.getValue(Posao.class);

                                    if (!p.getIdposlodavca().equals(Pocetna.currentFirebaseUser.getUid()))//Proverava da li je to njegov posao
                                        if (!dataBidovaniPoslovi.hasChild(p.getIdposla()))//Proverava da li je bidovao taj posao
                                            if (!dataPregovoriMajstor.hasChild(p.getIdposla()))//Proverava da li je taj posao u pregovorima
                                                if (!p.isUgovoren())//Proverava da li je posao ugovoren
                                                    listaPoslova.add(p);//Ako nije nista od proverenog dodaje ga za prikazivanje


                                }
                                if (listaPoslova.size() == 0) {


                                    filterButton.setVisibility(View.GONE);

                                    backImage.setImageResource(R.drawable.background_nodata);
                                }
                                else
                                {
                                    Collections.reverse(listaPoslova);
                                    filtriranaListaPoslova.addAll(listaPoslova);

                                    adapter.notifyDataSetChanged();
                                }

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


        //izabranifilteriText=(TextView) view.findViewById(R.id.izabranifilteriText);
        poruka= view.findViewById(R.id.porukaSviPoslovi);
        ukloniFiltereButton= view.findViewById(R.id.ukloniFiltere);
        filteriChipCloud= view.findViewById(R.id.filteri_chip_cloud);
        //filteriChipGroup=(ChipGroup)view.findViewById(R.id.filterChipGroup);



        // get our list view
        theListView = view.findViewById(R.id.listaSviPoslovi);

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


      /*  if(filtriranaListaPoslova.size()==0){
            poruka.setText("Trenutno nema poslova!!!");
            poruka.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            poruka.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
*/


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                HashSet<String> kategorije=new HashSet<String>();

               ArrayList<MultiSelectModel> listaKategorija= new ArrayList<MultiSelectModel>();

                for(int i=0;i<listaPoslova.size();i++)
                    kategorije.addAll(listaPoslova.get(i).getKategorije());


                int i=0;
                for(String k:kategorije)
                    listaKategorija.add(new MultiSelectModel(i++,k));



                izabraniFilteriList=new ArrayList<String>();


                MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                        .title("Izaberite filter") //setting title for dialog
                        .titleSize(25)
                        .positiveText("Potvrdi")
                        .negativeText("Otkaži")
                        .setMinSelectionLimit(0) //you can set minimum checkbox selection limit (Optional)
                        .setMaxSelectionLimit(Arrays.asList(PostavljanjePoslovaActivity.kategorije).size()) //you can set maximum checkbox selection limit (Optional)
                        .preSelectIDsList(prethodnoSelektovaniFilteri) //List of ids that you need to be selected
                        .multiSelectList(listaKategorija) // the multi select model list with ids and name
                        .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                            @Override
                            public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                                //will return list of selected IDS

                                prethodnoSelektovaniFilteri=new ArrayList<Integer>(selectedIds);

                                izabraniFilterSet=new HashSet<String>();
                                filteriChipCloud.removeAllViews();
                                izabraniFilteriList=new ArrayList<String >();

                                filtriranaListaPoslova.clear();
                                for (int i = 0; i < selectedIds.size(); i++) {



                                        izabraniFilteriList.add(selectedNames.get(i)) ;
                                        izabraniFilterSet.add(selectedNames.get(i));




                            /*
                            Toast.makeText(MainActivity.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
                                    "Selected Names : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();

*/
                                 }
                                if(izabraniFilterSet.size()>0){

                                    //ukloniFiltereButton.setVisibility(View.VISIBLE);

                                    boolean naso;
                                    //izabranifilteriText.setText(izabraniFilteri);

                                    //Postavljanje chips filtera

                                    final String [] listaFilteraStr=izabraniFilteriList.toArray(new String[0]);


                                    new ChipCloud.Configure()
                                            .chipCloud(filteriChipCloud)
                                            .selectedColor(Color.parseColor("#ff00cc"))
                                            .selectedFontColor(Color.parseColor("#ffffff"))
                                            .deselectedColor(Color.parseColor("#e1e1e1"))
                                            .deselectedFontColor(Color.parseColor("#333333"))
                                            .selectTransitionMS(500)
                                            .deselectTransitionMS(250)
                                            .labels(listaFilteraStr)
                                            .mode(ChipCloud.Mode.MULTI)
                                            .allCaps(true)
                                            .gravity(ChipCloud.Gravity.LEFT)
                                            .textSize(getResources().getDimensionPixelSize(R.dimen.default_textsize))
                                            .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                                            .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))//.typeface(Typeface.createFromAsset(getContext().getAssets(), "RobotoSlab-Regular.ttf"))
                                            .chipListener(new ChipListener() {
                                                @Override
                                                public void chipSelected(int index) {
                                                    if(filteriChipCloud.getChildCount()==1) {
                                                        //Uklanjanje poslednjeg filtera
                                                        filteriChipCloud.removeAllViews();
                                                        filtriranaListaPoslova.clear();
                                                        filtriranaListaPoslova.addAll(listaPoslova);
                                                        adapter.notifyDataSetChanged();
                                                        prethodnoSelektovaniFilteri=new ArrayList<Integer>();
                                                        izabraniFilterSet.clear();


                                                    }
                                                    else
                                                    try{
                                                        //Uklanjanje odredjenog filtera

                                                        filteriChipCloud.removeViewAt(index);
                                                        prethodnoSelektovaniFilteri.remove(index);
                                                        izabraniFilterSet.remove(izabraniFilteriList.get(index));
                                                        filtriranaListaPoslova.clear();

                                                        for (int i = 0; i < listaPoslova.size(); i++) {
                                                            boolean naso = false;
                                                            for (String k : listaPoslova.get(i).getKategorije()) {
                                                                if (izabraniFilterSet.contains(k))
                                                                    naso = true;
                                                            }
                                                            if (naso)
                                                                filtriranaListaPoslova.add(listaPoslova.get(i));
                                                        }
                                                        adapter.notifyDataSetChanged();



                                                    }
                                                    catch (Exception e){

                                                    }
                                                }
                                                @Override
                                                public void chipDeselected(int index) {
                                                    //...
                                                }
                                            })
                                            .build();





                                    for (int i = 0; i < listaPoslova.size(); i++) {
                                        naso = false;
                                        for (String k : listaPoslova.get(i).getKategorije()) {
                                            if (izabraniFilterSet.contains(k))
                                                naso = true;
                                        }
                                        if (naso)
                                            filtriranaListaPoslova.add(listaPoslova.get(i));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    ResetujFilter();

                                }



                             /*   if(filtriranaListaPoslova.size()==0)
                                {
                                   final PrettyDialog dialogfilter= new PrettyDialog(getContext());
                                            dialogfilter
                                            .setTitle("Filter")
                                            .setMessage("Nema poslova koji obuhvataju izabrane kategorije!")
                                            .setIcon(R.drawable.pdlg_icon_info)
                                            .setIconTint(R.color.pdlg_color_green)
                                            .addButton(
                                                    "OK",     // button text
                                                    R.color.pdlg_color_white,  // button text color
                                                    R.color.pdlg_color_green,new PrettyDialogCallback() {  // button OnClick listener
                                                        @Override
                                                        public void onClick() {

                                                            dialogfilter.dismiss();
                                                        }
                                                    }  // button background color

                                            )
                                            .show();



                                            //resetovanje uklanjanje filtera



                                    filtriranaListaPoslova.clear();
                                    for(int i=0;i<listaPoslova.size();i++)
                                    {
                                        filtriranaListaPoslova.add(listaPoslova.get(i));
                                    }
                                    adapter.notifyDataSetChanged();
                                    izabranifilteriText.setText("Izaberite filter...");
                                    prethodnoSelektovaniFilteri=new ArrayList<Integer>();
                                    if(filtriranaListaPoslova.size()>0)
                                    {
                                        poruka.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }


                                }

*/




                    }

                    @Override
                    public void onCancel() {
                       // Log.d(TAG,"Dialog cancelled");
                    }


                });

        multiSelectDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "multiSelectDialog");

            }
        });




        ukloniFiltereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResetujFilter();

            }
        });




    }
    public void ResetujFilter()
    {
        filtriranaListaPoslova.clear();
        filtriranaListaPoslova.addAll(listaPoslova);
        adapter.notifyDataSetChanged();
        filteriChipCloud.removeAllViews();

        //izabranifilteriText.setText("Izaberite filter...");
        //filteriChipGroup.removeAllViews();
        prethodnoSelektovaniFilteri=new ArrayList<Integer>();
        if(filtriranaListaPoslova.size()>0)
        {
            poruka.setVisibility(View.GONE);
            theListView.setVisibility(View.VISIBLE);
            ukloniFiltereButton.setVisibility(View.GONE);
        }

    }
}
