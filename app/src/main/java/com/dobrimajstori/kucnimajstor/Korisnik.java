package com.dobrimajstori.kucnimajstor;

import java.util.ArrayList;
import java.util.List;

public class Korisnik
{
    private String ime;
    private String prezime;
    private String id;
    private String mail;
    private double prosecnaocena;
    //private List<String> kategorije;
    private String opis;
    private String brtel;
    private String lokacija;
    private double lat;
    private double lon;
    //private ArrayList<String> bidovaniposlovi; //oni poslovi koje smo bidovali, koristi se kod majstora;
    private boolean majstor;//Sluzi da zna da li je poslednji put izabran majstor ili poslodavac
    //private ArrayList<String> postavljeniposlovi;
    private int brojposlova;
    //private ArrayList<Bid> ponudeMajstora;//oni poslovi za koje smo dobili prijavu, koristi se kod poslodavca;
    //private ArrayList<Bid> pregovorimajstor;//ponude koje je poslodavac izabrao za pregovaranje i prikazuju  se kod majstora
    //private ArrayList<Bid> pregovoriposlodavac;//ponude koje je poslodavac prihvatio za pregovor
    private int oblastRadaMajstor;
    private String UriSlike;
    private ArrayList<Komentar> komentariPoslodavca;
    private boolean skola;
    private boolean skolaOdobren;
    //private String status;

    public Korisnik()
    {

    }

    public Korisnik(String i,String im,String prez,String m,String op,
                    String brt,String lok,double latt,double lonn,
                   boolean majstr,int oblastRadaMajstor, boolean skola) // mozda da se doda i status
    {
        this.id=i;
        this.ime=im;
        this.prezime=prez;
        this.mail=m;
        this.prosecnaocena=0.0;
        //this.kategorije=kat;
        this.opis=op;
        this.brtel=brt;
        this.lokacija=lok;
        this.lat=latt;
        this.lon=lonn;
        //this.bidovaniposlovi=pp;
        this.majstor=majstr;
        //this.postavljeniposlovi=posp;
        this.brojposlova=0;
        //this.ponudeMajstora =priPos;
        //this.pregovorimajstor =pregovoriMajstor;
        //this.pregovoriposlodavac =pregovoriPoslodavac;
        this.oblastRadaMajstor=oblastRadaMajstor;
        this.UriSlike="prazno";
        this.komentariPoslodavca=new ArrayList<Komentar>();
        this.skola=skola;
        this.skolaOdobren=false;
       // this.status=stat;
    }

    public boolean isSkolaOdobren() {
        return skolaOdobren;
    }

    public boolean isSkola() {
        return skola;
    }

    public ArrayList<Komentar> getKomentariPoslodavca() {
        return komentariPoslodavca;
    }

    public String getId() {
        return this.id;
    }

    public String getBrtel() {
        return this.brtel;
    }

    public String getOpis() {
        return this.opis;
    }

    public String getLokacija() {
        return this.lokacija;
    }

    public String getIme() {
        return this.ime;
    }

    public String getMail() {
        return this.mail;
    }

    public String getPrezime() {
        return this.prezime;
    }

    public double getLon() {
        return this.lon;
    }

    public double getLat() {
        return this.lat;
    }

    public double getProsecnaocena() {
        return this.prosecnaocena;
    }


    public boolean isMajstor(){return  this.majstor;}


    public int getBrojposlova() {
        return this.brojposlova;
    }


    public String imeiPrezime()
    {
        return this.ime + " " + this.prezime;
    }

    public void setOblastRadaMajstor(int oblastRadaMajstor) {
        this.oblastRadaMajstor = oblastRadaMajstor;
    }
    public int getOblastRadaMajstor() {
        return oblastRadaMajstor;
    }

    public String getUriSlike() {
        return UriSlike;
    }
}
