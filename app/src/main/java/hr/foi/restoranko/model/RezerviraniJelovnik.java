package hr.foi.restoranko.model;

import java.util.ArrayList;
import java.util.List;

public class RezerviraniJelovnik {
    private long kolicina;
    private Jelovnik jelovnik;
    public static List<RezerviraniJelovnik> listaRezerviranihJela;

    public Jelovnik getJelovnik() {
        return jelovnik;
    }

    public long getKolicina() { return kolicina; }

    public void setKolicina(long kolicina) {
        this.kolicina = kolicina;
    }

    public RezerviraniJelovnik(){
        listaRezerviranihJela = new ArrayList<>();
    }

    public RezerviraniJelovnik(Jelovnik _jelovnik, long _kolicina) {
        this.kolicina = _kolicina;
        this.jelovnik = _jelovnik;
    }

    public static void DodajUListu(Jelovnik jelovnik, long kolicina) {
        listaRezerviranihJela.add(new RezerviraniJelovnik(jelovnik, kolicina));
    }
    public static void BrisiCijeluListu(){
        listaRezerviranihJela.clear();
    }

}
