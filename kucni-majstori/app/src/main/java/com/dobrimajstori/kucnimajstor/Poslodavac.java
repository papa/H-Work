package com.dobrimajstori.kucnimajstor;

public class Poslodavac {

    private String mail;
    private String id;
    private String ime;
    private String prezime;
    private String lokacija;
    private double lat;
    private double lon;
    private String brtel;

    //lista postavljenih poslova
    //lista prijavljenih korisnika

    public Poslodavac()
    {

    }

    public Poslodavac(String im,String pre,String m,String i,String lok,double la,double lo,String brt)
    {
        this.ime=im;
        this.prezime=pre;
        this.lokacija=lok;
        this.lat=la;
        this.lon=lo;
        this.mail=m;
        this.id=i;
        this.brtel=brt;
    }

    public String getMail()
    {
        return this.mail;
    }

    public String getId()
    {
        return this.id;
    }

    public String getIme()
    {
        return this.ime;
    }

    public String getPrezime()
    {
        return this.prezime;
    }

    public String getLokacija() {
        return lokacija;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getBrtel() {
        return this.brtel;
    }
}
