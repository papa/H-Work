package com.dobrimajstori.kucnimajstor;

public class Komentar {

    private String ime;
    private String prezime;
    private int ocena;
    private String komentar;
    private String uriSlika;
    private Datum datumKomentara;

    public Komentar(){

    }

    public Komentar(String ime, String prezime, String komentar, int ocena, String uriSlika,Datum datumKomentara)
    {
        this.ime=ime;
        this.prezime=prezime;
        this.komentar=komentar;
        this.ocena=ocena;
        this.uriSlika=uriSlika;
        this.datumKomentara=datumKomentara;
    }

    public Datum getDatumKomentara() {
        return datumKomentara;
    }

    public int getOcena() {
        return ocena;
    }

    public String getIme() {
        return ime;
    }

    public String getKomentar() {
        return komentar;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getUriSlika() {
        return uriSlika;
    }
}
