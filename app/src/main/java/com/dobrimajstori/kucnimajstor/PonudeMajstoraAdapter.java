package com.dobrimajstori.kucnimajstor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class PonudeMajstoraAdapter extends ArrayAdapter<Bid> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private Posao posao;
    private Korisnik poslodavac;
    private String idchat;

    public PonudeMajstoraAdapter(Context context, List<Bid> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        final Bid bid = getItem(position);
        // if ugovor_majstor_kartica is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.ponude_majstora_cell, parent, false);
            // binding view parts to view holder
            viewHolder.profilna=cell.findViewById(R.id.content_avatar);
            viewHolder.price = cell.findViewById(R.id.title_price);
            viewHolder.time = cell.findViewById(R.id.title_time_label);
            viewHolder.date = cell.findViewById(R.id.title_date_label);
            viewHolder.fromAddress = cell.findViewById(R.id.title_from_address);
            viewHolder.toAddress = cell.findViewById(R.id.title_to_address);
            viewHolder.requestsCount = cell.findViewById(R.id.title_requests_count);
            viewHolder.pledgePrice = cell.findViewById(R.id.title_pledge);
            viewHolder.contentRequestBtn = cell.findViewById(R.id.content_request_btn);
            viewHolder.imePoslodavca=cell.findViewById(R.id.content_name_view);
            viewHolder.josBrDana=cell.findViewById(R.id.title_weight);
            viewHolder.ulicaiBr=cell.findViewById(R.id.content_from_address_1);
            //viewHolder.gradDrzava=cell.findViewById(R.id.content_from_address_2);
            viewHolder.datumContent=cell.findViewById(R.id.content_delivery_time);
            viewHolder.vremeContent=cell.findViewById(R.id.content_deadline_time);
            viewHolder.poslodavacNalog=cell.findViewById(R.id.poslodavacNalog);
            viewHolder.poslodavacAdresa=cell.findViewById(R.id.poslodavacAdresa);
            viewHolder.poslodavacDatumEvent=cell.findViewById(R.id.poslodavacDatumEvent);
            viewHolder.cenaContent=cell.findViewById(R.id.head_image_center_text);
            viewHolder.daniContent=cell.findViewById(R.id.head_image_right_text);
            viewHolder.udaljenostContent=cell.findViewById(R.id.head_image_left_text);
            //viewHolder.zahhteviContent=cell.findViewById(R.id.zahteviSviposloviContent);
            viewHolder.imePoslaContent=cell.findViewById(R.id.imePoslaContent);
            viewHolder.opisPoslaContent=cell.findViewById(R.id.opisPoslaContent);
            viewHolder.odbijPonudu=cell.findViewById(R.id.odbijPonuduContent);
            viewHolder.majstorNalog=cell.findViewById(R.id.majstorNalog);
            viewHolder.levoTitleBojaSkola=cell.findViewById(R.id.levoTitleBoja);
            viewHolder.verifikacijaSkola=cell.findViewById(R.id.verifikovanoPonude);
            //viewHolder.kategorijeChip=cell.findViewById(R.id.chipgroup);

            cell.setTag(viewHolder);
        } else {
            // for existing ugovor_majstor_kartica set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == bid)
            return cell;




        Pocetna.baza.getReference("SviPoslovi").child(bid.getIdposla()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                posao=dataSnapshot.getValue(Posao.class);

                //Konvertuje datum

                String myFormat = "dd.MM.20yy."; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


                final Calendar popuni =Calendar.getInstance();
                popuni.set(bid.getDatumBida().getGodina(),bid.getDatumBida().getMesec(),bid.getDatumBida().getDan(),bid.getDatumBida().getSati(),bid.getDatumBida().getMinuti());
                final String datum=sdf.format(popuni.getTime());


                String vreme=String.format("%02d:%02d",  bid.getDatumBida().getSati(),bid.getDatumBida().getMinuti());


                // bind data from selected element to view through view holder

                if(bid.isSkola())
                {
                    viewHolder.price.setText("Praksa");
                    viewHolder.verifikacijaSkola.setVisibility(View.VISIBLE);
                    //viewHolder.levoTitleBojaSkola.setBackgroundColor(Color.parseColor("#6FD76E"));
                }
                else {
                    viewHolder.price.setText(bid.getNovac() +" RSD");

                }



                viewHolder.time.setText(String.valueOf(posao.getPregovara()));
                //viewHolder.zahhteviContent.setText((String.valueOf(posao.getPregovara()))+ " majstora već pregovara");
                viewHolder.opisPoslaContent.setText(bid.getDodatniopis());


                viewHolder.toAddress.setText("Kreirano: "+datum);

                viewHolder.pledgePrice.setText(String.valueOf(bid.getNovac()));
                viewHolder.cenaContent.setText(String.valueOf(bid.getNovac()));

                viewHolder.datumContent.setText(datum);
                viewHolder.vremeContent.setText(vreme);

                viewHolder.imePoslaContent.setText(posao.getNaslovposla());

                //String[] deloviAdrese=ugovor.getLokacija().split(",");



                viewHolder.ulicaiBr.setText(posao.getLokacija());


                viewHolder.requestsCount.setText(String.valueOf(posao.getBudzet()));
                viewHolder.udaljenostContent.setText(String.valueOf(posao.getBudzet()));



                Calendar vremeUgovora=Calendar.getInstance();
                vremeUgovora.set(posao.getDatumKreiranja().getGodina(),posao.getDatumKreiranja().getMesec(),posao.getDatumKreiranja().getDan());

                Calendar trenutnoCalendar=Calendar.getInstance();

                long dani = (trenutnoCalendar.getTimeInMillis()-vremeUgovora.getTimeInMillis()) / (24 * 60 * 60 * 1000);
                viewHolder.josBrDana.setText(String.valueOf(dani));
                viewHolder.daniContent.setText(String.valueOf(dani));

                String listString = "";

                //viewHolder.kategorijeChip.clearFocus();
                for (String s : posao.getKategorije())
                {

            /*
            Chip chip = new Chip(getContext());
            chip.setText(s);
            chip.setClickable(false);
            chip.clearFocus();

            //chip.setChipText("your...text");
            //chip.setCloseIconEnabled(true);
            //chip.setCloseIconResource(R.drawable.your_icon);
            //chip.setChipIconResource(R.drawable.your_icon);
            //chip.setChipBackgroundColorResource(R.color.red);
            //chip.setTextAppearanceResource(R.style.ChipTextStyle);
            //chip.setElevation(15);


            //viewHolder.kategorijeChip.addView(chip);
            */

                    if(listString.length()>0)
                        listString +=", ";
                    listString += s ;
                }


                viewHolder.ulicaiBr.setText(listString);

/*
        try{
            viewHolder.gradDrzava.setText(deloviAdrese[3]+", "+deloviAdrese[1]+" "+deloviAdrese[4]+", "+deloviAdrese[5]);

        }
        catch (Exception e){

        }
*/

               //uzmi sliku

                DatabaseReference data=FirebaseDatabase.getInstance().getReference("Korisnici").child(bid.getIdmajstora()).child("UriSlike");

                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            String uri=dataSnapshot.getValue(String.class);
                            Picasso.get().load(uri).into(viewHolder.profilna);
                        }
                        else
                        {
                            viewHolder.profilna.setImageResource(R.drawable.ic_korisnik);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                //Uzima naziv posla


                viewHolder.fromAddress.setText(posao.getNaslovposla());


                Pocetna.baza.getReference("Korisnici").child(bid.getIdmajstora()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        poslodavac=dataSnapshot.getValue(Korisnik.class);
                        viewHolder.imePoslodavca.setText(poslodavac.getIme()+" "+poslodavac.getPrezime());



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                viewHolder.majstorNalog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Pocetna.baza.getReference("Korisnici").child(bid.getIdmajstora()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                InfoMajstor.majstor=dataSnapshot.getValue(Korisnik.class);


                                getContext().startActivity(new Intent(getContext(),InfoMajstor.class));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                viewHolder.contentRequestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        final PrettyDialog dialogPonuda= new PrettyDialog(getContext());
                        dialogPonuda
                                .setTitle("Prihvati ponudu")
                                .setMessage("Da li ste sigurni da želite da nastavite da pregovarate sa ovim majstorom, ovo još uvek ne znači da ste sklopili ugovor i moći ćete da pregovarate i sa drugim majstorima koji su dali ponudu?")
                                .setIcon(R.drawable.pdlg_icon_info)
                                .setIconTint(R.color.colorPrimary)
                                .addButton(
                                        "PRIHVATI",     // button text
                                        R.color.pdlg_color_white,  // button text color
                                        R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                            @Override
                                            public void onClick() {


                                                //Uklanjanje bida:
                                                //1. Kod poslova gde se pod id majstora koji je bidovao cuva ceo bid
                                                Pocetna.baza.getReference("SviPoslovi").child(bid.getIdposla()).child("Bidovi").child(bid.getIdmajstora()).removeValue();

                                                //2. Kod majstora (bidovani poslovi) gde se pamti id posla koje je majstor bidovao
                                                Pocetna.baza.getReference("Korisnici").child(bid.getIdmajstora()).child("bidovaniposlovi").child(bid.getIdposla()).removeValue();

                                                //3. Kod poslodavca iz ponuda majstora gde se pod id majstora cuva ceo bid
                                                Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("PonudeMajstora").child(bid.getIdmajstora()).removeValue();


                                                //Dodavanje bida u pregovore:
                                                //1. Dodavanje bida kod poslodavca kod pregovoriPoslodavac pod id majstora pa pod id posla
                                                Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("pregovoriPoslodavac").child(bid.getIdmajstora()).child(bid.getIdposla()).setValue(bid);

                                                //2. Dodavanje bida kod majstora kod pregovoriMajstor pod id posla
                                                Pocetna.baza.getReference("Korisnici").child(bid.getIdmajstora()).child("pregovoriMajstor").child(bid.getIdposla()).setValue(bid);


                                                //Povecavanje proj pregovora kod posla
                                                final DatabaseReference refPregovara =Pocetna.baza.getReference("SviPoslovi").child(bid.getIdposla()).child("pregovara");
                                                refPregovara.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        refPregovara.setValue(dataSnapshot.getValue(Integer.class)+1);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                                idchat=Pocetna.currentFirebaseUser.getUid()+bid.getIdmajstora()+posao.getIdposla();

                                                posaljiPoruku(Pocetna.currentFirebaseUser.getUid(),bid.getIdmajstora(),posao.getOpis());
                                                posaljiPoruku(bid.getIdmajstora(),Pocetna.currentFirebaseUser.getUid(),bid.getDodatniopis());

                                                Intent PokreniActivity=new Intent(getContext(), Pocetna.class);
                                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                PokreniActivity.putExtra("fragment","ponudemajstora");
                                                getContext().startActivity(PokreniActivity);



                                                dialogPonuda.dismiss();
                                            }
                                        }  // button background color

                                )
                                .show();





                    }
                });

                viewHolder.odbijPonudu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final PrettyDialog dialogPonuda= new PrettyDialog(getContext());
                        dialogPonuda
                                .setTitle("Odbij ponudu")
                                .setMessage("Da li ste sigurni da želite da odbijete ovu ponudu, ponuda će biti trajno izbrisana?")
                                .setIcon(R.drawable.pdlg_icon_info)
                                .setIconTint(R.color.colorPrimary)
                                .addButton(
                                        "ODBIJ PONUDU",     // button text
                                        R.color.pdlg_color_white,  // button text color
                                        R.color.colorPrimary,new PrettyDialogCallback() {  // button OnClick listener
                                            @Override
                                            public void onClick() {


                                                //Uklanjanje bida
                                                //1. Kod poslova gde se pod id majstora koji je bidovao cuva ceo bid
                                                Pocetna.baza.getReference("SviPoslovi").child(bid.getIdposla()).child("Bidovi").child(bid.getIdmajstora()).removeValue();

                                                //2. Kod majstora (bidovani poslovi) gde se pamti id posla koje je majstor bidovao
                                                Pocetna.baza.getReference("Korisnici").child(bid.getIdmajstora()).child("bidovaniposlovi").child(bid.getIdposla()).removeValue();

                                                //3. Kod poslodavca iz ponuda majstora gde se pod id majstora cuva ceo bid
                                                Pocetna.baza.getReference("Korisnici").child(Pocetna.currentFirebaseUser.getUid()).child("PonudeMajstora").child(bid.getIdmajstora()).removeValue();


                                                Intent PokreniActivity=new Intent(getContext(), Pocetna.class);
                                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                PokreniActivity.putExtra("fragment","ponudemajstora");
                                                getContext().startActivity(PokreniActivity);



                                                dialogPonuda.dismiss();
                                            }
                                        }  // button background color

                                )
                                .show();



                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return cell;
    }

    private int dan;
    private int mesec;
    private int godina;
    private int sati;
    private int minuti;

    private void datumVreme()
    {
        Calendar cal=Calendar.getInstance();

        mesec=cal.get(Calendar.MONTH);
        dan=cal.get(Calendar.DAY_OF_MONTH);
        godina=cal.get(Calendar.YEAR);

        sati=cal.get(Calendar.HOUR_OF_DAY);
        minuti=cal.get(Calendar.MINUTE);
    }

    public void posaljiPoruku(String salje, final String prima, String poruka)
    {
        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference();

        datumVreme();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("prima",prima);
        hashMap.put("salje",salje);
        hashMap.put("poruka",poruka);
        hashMap.put("dan",dan);
        hashMap.put("mesec",mesec);
        hashMap.put("godina",godina);
        hashMap.put("sati",sati);
        hashMap.put("minuti",minuti);
        // hashMap.put("isseen",false);

        databaseReference2.child("Chats").child(idchat).push().setValue(hashMap);

        final DatabaseReference chatref=FirebaseDatabase.getInstance().getReference("Chatlist").child(idchat).child(Pocetna.currentFirebaseUser.getUid()).child(prima);

        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists())
                {
                    chatref.child("id").setValue(prima);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // simple methods for register ugovor_majstor_kartica state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        //Toast.makeText(getContext(),"Klik",Toast.LENGTH_LONG);
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView price;
        TextView contentRequestBtn;
        TextView pledgePrice;
        TextView fromAddress;
        TextView toAddress;
        TextView requestsCount;
        TextView date;
        TextView time;
        TextView josBrDana;
        TextView ulicaiBr;
        ImageView profilna;
        // TextView gradDrzava;
        TextView datumContent;
        TextView vremeContent;
        ImageView poslodavacNalog;
        ImageView poslodavacAdresa;
        ImageView poslodavacDatumEvent;
        TextView cenaContent;
        TextView daniContent;
        TextView udaljenostContent;
        TextView zahhteviContent;
        TextView imePoslaContent;
        TextView opisPoslaContent;
        TextView odbijPonudu;
        ImageView majstorNalog;
        RelativeLayout levoTitleBojaSkola;
        ImageView verifikacijaSkola;
        //ChipGroup kategorijeChip;


        //content

        TextView imePoslodavca;
    }



}
