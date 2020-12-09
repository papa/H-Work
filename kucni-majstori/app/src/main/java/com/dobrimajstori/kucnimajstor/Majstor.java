package com.dobrimajstori.kucnimajstor;

import java.util.ArrayList;

public class Majstor {

    private String ime;
    private String prezime;
    private String id;
    private String mail;
    private double prosecnaocena;
    private ArrayList<String> kategorije;
    String opis;
    private double procenat;
    private String brtel;

    //lista prijava za posao

    public Majstor()
    {

    }

    public Majstor(String im,String pre,String i,String m,double ps,ArrayList<String> k,String o,double pro,String brt)
    {
        this.ime=im;
        this.prezime=pre;
        this.id=i;
        this.mail=m;
        this.prosecnaocena=ps;
        this.kategorije=k;
        this.opis=o;
        this.procenat=pro;
        this.brtel=brt;
    }

    public String getIme()
    {
        return this.ime;
    }

    public String getPrezime()
    {
        return this.prezime;
    }

    public String getId()
    {
        return this.id;
    }

    public String getMail()
    {
        return this.mail;
    }

    public double getProsecnaocena()
    {
        return this.prosecnaocena;
    }

    public ArrayList<String> getKategorije()
    {
        return this.kategorije;
    }

    public String getOpis()
    {
        return this.opis;
    }

    public double getProcenat() {
        return this.procenat;
    }

    public String getBrtel() {
        return this.brtel;
    }
}
