package com.dobrimajstori.kucnimajstor;

import java.util.ArrayList;

public class Posao {

    private String opis;
    private String vlasnik;
    //lista ponuda
    private ArrayList<String> kategorije;

    public Posao()
    {

    }

    public Posao(String o,String v,ArrayList<String> k)
    {
        this.opis=o;
        this.kategorije=k;
        this.vlasnik=v;
    }

    public String getOpis() {
        return opis;
    }

    public ArrayList<String> getKategorije() {
        return kategorije;
    }

    public String getVlasnik() {
        return vlasnik;
    }
}
