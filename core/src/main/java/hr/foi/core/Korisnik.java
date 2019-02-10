package hr.foi.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Korisnik {
    private String ime;
    private String prezime;
    private String email;
    private String korisnickoIme;
    private String lozinka;
    public String uId;
    public String putanjaSlike;
    public Slika slika;

    public static Korisnik prijavljeniKorisnik;
    private ChangeListener listener;

    public Korisnik(String ime, String prezime, String email, String korisnickoIme, String lozinka, String putanjaSlike, Slika slika) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.putanjaSlike = putanjaSlike;
        this.uId=null;
        this.slika = slika;
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

    public String getPutanjaSlike() { return putanjaSlike; }

    public void setPutanjaSlike(String putanjaSlike) { this.putanjaSlike = putanjaSlike; }

    public Slika getSlika() { return slika; }

    public void setSlika(Slika slika) { this.slika = slika; }


    public void registrirajKorisnika(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("korisnik").child(this.korisnickoIme).setValue(this, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

            }
        });
    }

    public void prijaviKorisnika(String email, String password, final Context context)
    {
        Korisnik.prijavljeniKorisnik=new Korisnik();
        final FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signOut();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            FirebaseUser user = auth.getCurrentUser();
                            final String userID = user.getUid();
                            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user");
                            userReference.orderByChild("email")
                                    .equalTo(user.getEmail())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                                Korisnik.prijavljeniKorisnik.setPrijavljeniKorisnik(userID, datas.child("ime").getValue().toString(), datas.child("prezime").getValue().toString(), datas.child("email").getValue().toString(), datas.child("korisnickoIme").getValue().toString(), datas.child("lozinka").getValue().toString(), datas.child("putanjaSlike").getValue().toString(), null);
                                                Slika slika= Slika.dohvatiSlikuKorisnika(Korisnik.prijavljeniKorisnik);
                                                Korisnik.prijavljeniKorisnik.setSlika(slika);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                        }
                        catch (Exception ex){
                            iznimkaPrijava(ex, context);
                        }
                    }
                });

    }

    private void iznimkaPrijava(Exception ex, Context context)
    {
        Toast.makeText(context, "Greška u prijavi", Toast.LENGTH_SHORT).show();
    }

    public void setListener(ChangeListener listener)
    {
        this.listener=listener;
    }

    public ChangeListener getListener()
    {
        return this.listener;
    }

    private void setPrijavljeniKorisnik(String uId, String ime, String prezime, String email, String korisnickoIme, String lozinka, String putanjaSlike, Slika slika)
    {
        this.uId=uId;
        this.ime=ime;
        this.prezime=prezime;
        this.email=email;
        this.korisnickoIme=korisnickoIme;
        this.lozinka=lozinka;
        this.putanjaSlike=putanjaSlike;
        this.slika = slika;
        if(listener!=null) listener.onChange();
    }
}
