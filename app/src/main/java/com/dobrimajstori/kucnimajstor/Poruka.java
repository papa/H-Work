package com.dobrimajstori.kucnimajstor;

public class Poruka
{
   private String tekst;
   private String idporuke;
   private String idkorisnika;
   private int dan;
   private int mesec;
   private int godina;
   private int sati;
   private int minuti;

   public Poruka()
   {

   }

   public Poruka(String tekst,String idporuke,String idkorisnika,int dan,int mesec,int godina,int sati,int minuti)
   {
       this.tekst=tekst;
       this.idporuke=idporuke;
       this.idkorisnika=idkorisnika;
       this.dan=dan;
       this.mesec=mesec;
       this.godina=godina;
       this.sati=sati;
       this.minuti=minuti;
   }

    public int getDan() {
        return this.dan;
    }

    public int getGodina() {
        return this.godina;
    }

    public int getMesec() {
        return this.mesec;
    }

    public int getMinuti() {
        return this.minuti;
    }

    public int getSati() {
        return this.sati;
    }

    public String getIdkorisnika() {
        return this.idkorisnika;
    }

    public String getIdporuke() {
        return this.idporuke;
    }

    public String getTekst() {
        return this.tekst;
    }
}
