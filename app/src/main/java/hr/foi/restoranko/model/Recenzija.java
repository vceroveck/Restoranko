package hr.foi.restoranko.model;

public class Recenzija {

    long restoranId;
    String recenzijaId;
    String recenzijaLjestvica;
    String recenzijaPovratnaInfo;

    public Recenzija(){

    }

    public Recenzija(long restoranId, String recenzijaId, String recenzijaLjestvica, String recenzijaPovratnaInfo) {
        this.restoranId = restoranId;
        this.recenzijaId = recenzijaId;
        this.recenzijaLjestvica = recenzijaLjestvica;
        this.recenzijaPovratnaInfo = recenzijaPovratnaInfo;
    }

    public String getRecenzijaId() {
        return recenzijaId;
    }

    public String getRecenzijaLjestvica() {
        return recenzijaLjestvica;
    }

    public String getRecenzijaPovratnaInfo() {
        return recenzijaPovratnaInfo;
    }
}
