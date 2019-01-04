package hr.foi.restoranko.model;

public class OmiljeniRestoran {
    private long restoran;
    private String korisnik;

    public OmiljeniRestoran(long restoran, String korisnik) {
        this.restoran = restoran;
        this.korisnik = korisnik;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public long getRestoran() {
        return restoran;
    }

    public void setRestoran(long restoran) {
        this.restoran = restoran;
    }
}
