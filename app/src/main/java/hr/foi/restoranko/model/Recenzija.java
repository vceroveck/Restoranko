package hr.foi.restoranko.model;

public class Recenzija {

    String recenzijaId;
    String recenzijaLjestvica;
    String recenzijaPovratnaInfo;

    public Recenzija(){

    }

    public Recenzija(String recenzijaId, String recenzijaLjestvica, String recenzijaPovratnaInfo) {
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
