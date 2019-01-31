package hr.foi.restoranko.model;

import java.util.Date;

public class Rezervacija {
    private long rezervacijaId;
    private String korisnik;
    private String dolazak;
    private String odlazak;

    public long getRezervacijaId() {
        return rezervacijaId;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getDolazak() {
        return dolazak;
    }

    public void setDolazak(String dolazak) {
        this.dolazak = dolazak;
    }

    public String getOdlazak() {
        return odlazak;
    }

    public void setOdlazak(String odlazak) {
        this.odlazak = odlazak;
    }

    public Rezervacija(String korisnik, String dolazak, String odlazak) {

        Date date = new Date();

        this.rezervacijaId = date.getTime();
        this.korisnik = korisnik;
        this.dolazak = dolazak;
        this.odlazak = odlazak;
    }
}
