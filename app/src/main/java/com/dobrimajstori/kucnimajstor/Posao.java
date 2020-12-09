package com.dobrimajstori.kucnimajstor;

import java.util.ArrayList;
import java.util.List;

public class Posao
{
    private String idposla;
    private String opis;
    private String idposlodavca;
    private List<String> kategorije;
   // private ArrayList<String> bidovi;
    private String naslovposla;
    private int budzet;
    private List<List<Bid>> bidovi;
    private Datum datumKreiranja;
    private double lat;
    private double lon;
    private boolean ugovoren;
    private String lokacija;
    private int pregovara;

    public Posao()
    {

    }

    public Posao(String idp,String nasp,String o,String p,List<String> k,ArrayList<String> b,int budzet,Datum datumKreiranja, double lat, double lon,String lokacija)
    {
        this.idposla=idp;
        this.opis=o;
        this.kategorije=k;
        this.idposlodavca=p;
        //this.bidovi=b;
        this.naslovposla=nasp;
        this.budzet=budzet;
        this.datumKreiranja=datumKreiranja;
        this.lat=lat;
        this.lon=lon;
        this.ugovoren=false;
        this.lokacija=lokacija;
        this.pregovara=0;

    }

    public String getOpis() {
        return this.opis;
    }

    public List<String> getKategorije() {
        return this.kategorije;
    }

    public String getPoslodavac() {
        return this.idposlodavca;
    }

    //public ArrayList<String> getBidovi() {
       // return this.bidovi;
    //}

    public String getIdposla() {
        return this.idposla;
    }

    public String getNaslovposla() {
        return this.naslovposla;
    }

    public String getIdposlodavca() {
        return this.idposlodavca;
    }

    public int getBudzet(){return this.budzet;}

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Datum getDatumKreiranja() {
        return datumKreiranja;
    }

    public boolean isUgovoren() {
        return ugovoren;
    }

    public void setUgovoren(boolean ugovoren) {
        this.ugovoren = ugovoren;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public int getPregovara() {
        return pregovara;
    }
}
