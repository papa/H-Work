package com.dobrimajstori.kucnimajstor;

import android.location.Location;
import android.view.View;

import java.nio.file.attribute.DosFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Ugovor
{
    private String idugovora;//poslodavacID+majstorID
    private String posao;
    private String poslodavac;
    private String majstor;
    private int cena;
    private Datum dandolaska;
    private double lat;
    private double lon;
    private boolean potpisan;
    private String poruka;
    private boolean raskidPoslodavac;
    private boolean raskidMajstor;
    private String lokacija;

    private View.OnClickListener requestBtnClickListener;

    public Ugovor()
    {

    }

    public Ugovor(String idugovora, String posao, String odkoga, String zakoga, int cena, Datum dandolaska, double lat, double lon,String poruka,String lokacija)
    {
        this.idugovora=idugovora;
        this.posao=posao;
        this.poslodavac=odkoga;
        this.majstor=zakoga;
        this.cena=cena;
        this.dandolaska=dandolaska;
        this.lat=lat;
        this.lon=lon;
        this.potpisan=false;
        this.poruka=poruka;
        this.raskidMajstor=false;
        this.raskidPoslodavac=false;
        this.lokacija=lokacija;
    }


    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public String getPosao() {
        return posao;
    }

    public void setPosao(String posao) {
        this.posao = posao;
    }

    public String getPoslodavac() {
        return poslodavac;
    }

    public void setOdkoga(String odkoga) {
        this.majstor = odkoga;
    }

    public String getMajstor() {
        return majstor;
    }

    public void setMajstor(String zakoga) {
        this.majstor = zakoga;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public Datum getDandolaska() {
        return dandolaska;
    }

    public void setDandolaska(Datum dandolaska) {
        this.dandolaska = dandolaska;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isPotpisan() {
        return potpisan;
    }

    public void setPotpisan(boolean potpisan) {
        this.potpisan = potpisan;
    }

    public String getIdugovora()
    {
        return this.idugovora;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public Boolean getRaskidMajstor() {
        return raskidMajstor;
    }

    public Boolean getRaskidPoslodavac() {
        return raskidPoslodavac;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public void setRaskidMajstor(boolean raskidMajstor) {
        this.raskidMajstor = raskidMajstor;
    }

    public void setIdugovora(String idugovora) {
        this.idugovora = idugovora;
    }

    public void setRaskidPoslodavac(boolean raskidPoslodavac) {
        this.raskidPoslodavac = raskidPoslodavac;
    }
}
