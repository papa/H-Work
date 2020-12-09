package com.dobrimajstori.kucnimajstor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>
{
    public static final int MSG_TYPE_LEVO=0;
    public static final int MSG_TYPE_DESNO=1;

    private Context context;
    private List<Chat> mChat;
    private String imageUrl;

    private String id;

    FirebaseUser fuser;

    public MessageAdapter(Context cont,List<Chat> chats,String imgurl)
    {
        this.mChat=chats;
        this.context=cont;
        this.imageUrl=imgurl;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        if(viewType == MSG_TYPE_DESNO)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_desno,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_levo,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, int position)
    {
        Chat chat=mChat.get(position);

       /* DatabaseReference data=FirebaseDatabase.getInstance().getReference("Korisnici").child(chat.get).child("UriSlike");

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
        });*/



       int dan=chat.getDan();
       int mesec=chat.getMesec();
       int godina=chat.getGodina();
       int sati=chat.getSati();
       int minuti=chat.getMinuti();

       holder.prikaziporuku.setText(chat.getPoruka());

       holder.datumvreme.setText(dan +"."+ mesec +"."+ godina +"."+
       " "+ sati +":"+ minuti);

       holder.datumvreme.setVisibility(View.INVISIBLE);

       //holder.profilna.setImageResource(R.drawable.ic_korisnik);

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView prikaziporuku;
        public ImageView profilna;
        public TextView datumvreme;
        public CircleImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imageView=itemView.findViewById(R.id.pslika);
            prikaziporuku=itemView.findViewById(R.id.poruka);
            profilna=itemView.findViewById(R.id.pslika);
            datumvreme=itemView.findViewById(R.id.datumivreme);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view)
        {
            //TODO
            //uradi ovo za promenu visibility

            TextView dv=view.findViewById(R.id.datumivreme);

            if(dv.getVisibility()==View.INVISIBLE)
            {
                dv.setVisibility(View.VISIBLE);
            }
            else
            {
                dv.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public int getItemViewType(int position)
    {
       fuser=FirebaseAuth.getInstance().getCurrentUser();

       if(mChat.get(position).getSalje().equals(fuser.getUid()))
       {
           return MSG_TYPE_DESNO;
       }
       else
       {
           return MSG_TYPE_LEVO;
       }
    }
}
