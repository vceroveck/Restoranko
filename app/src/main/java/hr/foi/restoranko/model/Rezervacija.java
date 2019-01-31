package hr.foi.restoranko.model;

import java.util.Date;

public class Rezervacija {
    private long rezervacijaId;
    private String korisnik;
    private String dolazak;
    private String odlazak;
    private String nazivRestorana;
    private long potvrdaDolaska;

    public Rezervacija(long rezervacija, String korisnik, String dolazak, String odlazak, String nazivRestorana, long potvrdaDolaska) {
        this.rezervacijaId = rezervacija;
        this.korisnik = korisnik;
        this.dolazak = dolazak;
        this.odlazak = odlazak;
        this.nazivRestorana = nazivRestorana;
        this.potvrdaDolaska = potvrdaDolaska;
    }

    public long getRezervacijaId() {
        return rezervacijaId;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getNazivRestorana() {
        return nazivRestorana;
    }

    public String getDolazak() {
        return dolazak;
    }

    public String getOdlazak() {
        return odlazak;
    }

    public long getPotvrdaDolaska() {
        return potvrdaDolaska;
    }

    public Rezervacija(String korisnik, String dolazak, String odlazak, String nazivRestorana) {

        Date date = new Date();

        this.rezervacijaId = date.getTime();
        this.korisnik = korisnik;
        this.dolazak = dolazak;
        this.odlazak = odlazak;
        this.nazivRestorana = nazivRestorana;
        this.potvrdaDolaska = 0;
    }
}
