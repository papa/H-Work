package com.dobrimajstori.kucnimajstor;

public class PrijavaMajstora {

    private int novac;
    private String idposlodavca;
    private String id;
    private String idposla;
    private String dodatniopis;
    private String majstor;

    public PrijavaMajstora()
    {

    }

    public PrijavaMajstora(int n,String i,String ip,String dp,String ms,String idp)
    {
        this.novac=n;
        this.idposlodavca=idp;
        this.id=i;
        this.idposla=ip;
        this.dodatniopis=dp;
        this.majstor=ms;
    }

    public int getNovac() {
        return this.novac;
    }

    public String getDodatniopis() {
        return this.dodatniopis;
    }

    public String getId() {
        return this.id;
    }

    public String getIdposla() {
        return this.idposla;
    }

    public String getIdposlodavca() {
        return this.idposlodavca;
    }

    public String getMajstor() {
        return this.majstor;
    }

}
