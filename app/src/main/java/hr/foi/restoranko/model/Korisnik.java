package hr.foi.restoranko.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hr.foi.restoranko.LogIn;

public class Korisnik {
    private String ime;
    private String prezime;
    private String email;
    private String korisnickoIme;
    private String lozinka;
    Korisnik prijavljeniKorisnik;

    public Korisnik(String ime, String prezime, String email, String korisnickoIme, String lozinka) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }

    public Korisnik() {
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }


    public void registrirajKorisnika(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("korisnik").child(this.korisnickoIme).setValue(this, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

            }
        });
    }

    public static FirebaseUser prijaviKorisnika(String email, String password)
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password);
        FirebaseUser user=auth.getCurrentUser();
        return user;
    }

}
