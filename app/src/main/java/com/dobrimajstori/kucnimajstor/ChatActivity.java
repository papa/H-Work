package com.dobrimajstori.kucnimajstor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    ImageView posaljporuku;
    EditText tekst;
    Poruka poruka;
    DatabaseReference poruke;
    String idporuke;
    String idkorsnika;
    int dan,mesec,godina;
    int sat,minut;
    LinearLayout linlayout;
    RelativeLayout rellayout;
    ScrollView scrollView;
    EditText messageArea;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        posaljporuku= findViewById(R.id.sendButton);
        //tekst=(EditText)findViewById(R.id.tekstporuke);
        poruke=FirebaseDatabase.getInstance().getReference("Poruke");
        idkorsnika=Pocetna.currentFirebaseUser.getUid();
        linlayout= findViewById(R.id.linearlayout);
        rellayout= findViewById(R.id.relativelayout);
        scrollView= findViewById(R.id.scrollView);
        messageArea= findViewById(R.id.messageArea);


       /* posaljporuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String t=tekst.getText().toString();
                if(!t.equals(""))
                {
                    Date date=new Date();

                    Calendar calendar = Calendar.getInstance();
                    godina = calendar.get(Calendar.YEAR);
                    mesec = calendar.get(Calendar.MONTH);
                    dan= calendar.get(Calendar.DAY_OF_MONTH);

                    calendar.setTime(date);

                    sat=calendar.get(Calendar.HOUR_OF_DAY);
                    minut=calendar.get(Calendar.MINUTE);

                   idporuke=poruke.push().getKey();
                   poruka=new Poruka(t,idporuke,idkorsnika,dan,mesec,godina,sat,minut);
                   poruke.child(idporuke).setValue(poruka);
                }

            }
        });*/


       //TODO
        // dodaj reference firebase koje treba

        posaljporuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tekst=messageArea.getText().toString();

                if(!tekst.equals(""))
                {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("message",tekst);
                    //TODO
                    //ubaci i id
                    //reference
                    messageArea.setText("");

                }

            }
        });




    }

    public void addMessageBox(String message,int type)
    {
        TextView textView=new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams linpar2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linpar2.weight = 1.0f;

        if(type==1)
        {
            linpar2.gravity=Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else
        {
            linpar2.gravity=Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }

        textView.setLayoutParams(linpar2);
        linlayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
