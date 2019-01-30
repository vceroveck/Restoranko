package hr.foi.restoranko.model;

public class Jelovnik {
    private long jelovnikId;
    private String cijena;
    private String naziv;

    public Jelovnik(long jelovnikId, String cijena, String naziv) {
        this.jelovnikId = jelovnikId;
        this.cijena = cijena;
        this.naziv = naziv;
    }

    public long getJelovnikId() {
        return jelovnikId;
    }

    public String getCijena() {
        return cijena;
    }

    public String getNaziv() {
        return naziv;
    }
}
