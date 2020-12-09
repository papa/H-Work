package com.dobrimajstori.kucnimajstor;

public class Bid {

    private int novac;
    private String idposla;
    private String dodatniopis;
    private String idPoslodavca;
    private String idmajstora;
    private Datum datumBida;
    private boolean skola;

    public Bid()
    {

    }

    public Bid(int n, String ip, String dp,String ms,Datum datumBida,String idPoslodavca,boolean skola)
    {
        this.novac=n;
        this.idposla=ip;
        this.dodatniopis=dp;
        this.idmajstora=ms;
        this.datumBida=datumBida;
        this.idPoslodavca=idPoslodavca;
        this.skola=skola;
    }

    public boolean isSkola() {
        return skola;
    }

    public int getNovac() {
        return this.novac;
    }

    public String getDodatniopis() {
        return this.dodatniopis;
    }


    public String getIdposla() {
        return this.idposla;
    }

    public String getIdmajstora() {
        return this.idmajstora;
    }

    public Datum getDatumBida() {
        return datumBida;
    }

    public void setIdposla(String idposla) {
        this.idposla = idposla;
    }

    public String getIdPoslodavca() {
        return idPoslodavca;
    }
}
