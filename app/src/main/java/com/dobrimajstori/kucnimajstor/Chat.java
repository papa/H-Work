package com.dobrimajstori.kucnimajstor;

public class Chat
{
    private String salje;
    private String prima;
    private String poruka;
    private int dan;
    private int mesec;
    private int godina;
    private int sati;
    private int minuti;
    //private boolean isseen;

    //TODO
    //uradi isSeen

    public Chat()
    {

    }

    public Chat(String s,String p,String por,int dan,int mesec,int godina,int sati,int minuti)
    {
        this.salje=s;
        this.prima=p;
        this.poruka=por;
        this.dan=dan;
        this.mesec=mesec;
        this.godina=godina;
        this.sati=sati;
        this.minuti=minuti;
        //this.isseen=isseen;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public void setPrima(String prima) {
        this.prima = prima;
    }

    public String getSalje() {
        return salje;
    }

    public String getPoruka() {
        return poruka;
    }

    public String getPrima() {
        return prima;
    }

    public void setSalje(String salje) {
        this.salje = salje;
    }

    public int getDan() {
        return dan;
    }

    public void setDan(int dan) {
        this.dan = dan;
    }

    public int getMesec() {
        return mesec;
    }

    public void setMesec(int mesec) {
        this.mesec = mesec;
    }

    public int getGodina() {
        return godina;
    }

    public void setGodina(int godina) {
        this.godina = godina;
    }

    public int getSati() {
        return sati;
    }

    public void setSati(int sati) {
        this.sati = sati;
    }

    public int getMinuti() {
        return minuti;
    }

    public void setMinuti(int minuti) {
        this.minuti = minuti;
    }

    /* public boolean isIsseen()
    {
        return isseen;
    }*/

     /*public void setisSeen(boolean isseen)
     {
         this.isseen=isseen;
     }*/
}
