package hr.foi.restoranko.model;

public class Recenzija {

    long restoranId;
    String nazivRestorana;
    String recenzijaId;
    String recenzijaLjestvica;
    String recenzijaPovratnaInfo;

    public Recenzija(){

    }

    public Recenzija(long restoranId, String nazivRestorana, String recenzijaId, String recenzijaLjestvica, String recenzijaPovratnaInfo) {
        this.restoranId = restoranId;
        this.nazivRestorana=nazivRestorana;
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

    public String getNazivRestorana(){ return  nazivRestorana;}
}
