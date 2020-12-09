package com.dobrimajstori.kucnimajstor;

import java.util.Calendar;

public class Datum {


    private int godina;
    private int mesec;
    private int dan;
    private int sati;
    private int minuti;


    public Datum()
    {

    }
    public Datum(int godina,int mesec, int dan, int sati, int minuti)
    {
        this.godina=godina;
        this.mesec=mesec;
        this.dan=dan;
        this.sati=sati;
        this.minuti=minuti;
    }

    public int getDan() {
        return dan;
    }

    public int getGodina() {
        return godina;
    }

    public int getMesec() {
        return mesec;
    }

    public int getMinuti() {
        return minuti;
    }

    public int getSati() {
        return sati;
    }

    public void setDan(int dan) {
        this.dan = dan;
    }

    public void setGodina(int godina) {
        this.godina = godina;
    }

    public void setMesec(int mesec) {
        this.mesec = mesec;
    }

    public void setMinuti(int minuti) {
        this.minuti = minuti;
    }

    public void setSati(int sati) {
        this.sati = sati;
    }
}
