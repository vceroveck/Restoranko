package hr.foi.restoranko.model;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import hr.foi.restoranko.view.Slika;

@SuppressLint("ParcelCreator")
public class Restoran implements Parcelable {
    private long restoranId;
    private String adresa;
    private String kontakt;
    private String nazivRestorana;
    private String opis;
    private String slika;
    private String webLink;
    private Slika slikaRestorana;
    private long brojPregleda;
    private long brojOznakaOmiljeno;
    private double prosjecnaOcjena;

    public Restoran(long restoranId, String adresa, String kontakt, String nazivRestorana, String opis, String slika, String webLink, long brojPregleda) {
        this.restoranId = restoranId;
        this.adresa = adresa;
        this.kontakt = kontakt;
        this.nazivRestorana = nazivRestorana;
        this.opis = opis;
        this.slika = slika;
        this.webLink = webLink;
        this.brojPregleda = brojPregleda;
        this.brojOznakaOmiljeno = 0;
        this.prosjecnaOcjena = 0;
    }

    protected Restoran(Parcel in) {
        restoranId = in.readLong();
        adresa = in.readString();
        kontakt = in.readString();
        nazivRestorana = in.readString();
        opis = in.readString();
        slika = in.readString();
        webLink = in.readString();
    }

    public static final Creator<Restoran> CREATOR = new Creator<Restoran>() {
        @Override
        public Restoran createFromParcel(Parcel in) {
            return new Restoran(in);
        }

        @Override
        public Restoran[] newArray(int size) {
            return new Restoran[size];
        }
    };

    public long getRestoranId() {
        return restoranId;
    }

    public void setRestoranId(long restoranId) {
        this.restoranId = restoranId;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public String getNazivRestorana() {
        return nazivRestorana;
    }

    public void setNazivRestorana(String nazivRestorana) {
        this.nazivRestorana = nazivRestorana;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public Slika getSlikaRestorana() {
        return slikaRestorana;
    }

    public void setSlikaRestorana(Slika slikaRestorana) {
        this.slikaRestorana = slikaRestorana;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(restoranId);
        dest.writeString(adresa);
        dest.writeString(kontakt);
        dest.writeString(nazivRestorana);
        dest.writeString(opis);
        dest.writeString(slika);
        dest.writeString(webLink);
    }

    public long getBrojPregleda() {
        return brojPregleda;
    }

    public long getBrojOznakaOmiljeno() {
        return brojOznakaOmiljeno;
    }

    public void setBrojOznakaOmiljeno(long brojOznakaOmiljeno) {
        this.brojOznakaOmiljeno = brojOznakaOmiljeno;
    }

    public double getProsjecnaOcjena() {
        return prosjecnaOcjena;
    }

    public void setProsjecnaOcjena(double prosjecnaOcjena) {
        this.prosjecnaOcjena = prosjecnaOcjena;
    }
}
