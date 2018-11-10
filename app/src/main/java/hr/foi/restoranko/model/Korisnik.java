package hr.foi.restoranko.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Korisnik {
    private String ime;
    private String prezime;
    private String email;
    private String korisnickoIme;
    private String lozinka;
    public String uId;

    public static Korisnik prijavljeniKorisnik;

    public Korisnik(String ime, String prezime, String email, String korisnickoIme, String lozinka) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.uId=null;
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

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void registrirajKorisnika(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("korisnik").child(this.korisnickoIme).setValue(this, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

            }
        });
    }

    public static FirebaseAuth prijaviKorisnika(FirebaseAuth auth, String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password);
        return auth;
    }

    public static void kreirajPrijavljenogKorisnika(final FirebaseUser user, DatabaseReference userReference, final String userUid)
    {
        userReference.orderByChild(userUid)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        prijavljeniKorisnik = dataSnapshot.getValue(Korisnik.class);
                        prijavljeniKorisnik.setuId(userUid);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
