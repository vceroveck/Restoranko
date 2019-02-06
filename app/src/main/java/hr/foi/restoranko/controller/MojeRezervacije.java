package hr.foi.restoranko.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
import hr.foi.restoranko.model.Restoran;
import hr.foi.restoranko.model.Rezervacija;

public class MojeRezervacije extends AppCompatActivity {
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moje_rezervacije);

        container = (LinearLayout) findViewById(R.id.container);
        DohvatiSveMojeRezervacije();
    }

    private void DohvatiSveMojeRezervacije() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("rezervacija");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    try {
                        String korisnik = datas.child("korisnik").getValue().toString();

                        if (korisnik.equals(Korisnik.prijavljeniKorisnik.getKorisnickoIme())) {
                            TextView nemaRezervacija = (TextView) container.findViewById(R.id.nemaRezervacija);
                            nemaRezervacija.setVisibility(View.GONE);

                            long _rezervacija = (long) datas.child("rezervacijaId").getValue();
                            String _dolazak = datas.child("dolazak").getValue().toString();
                            String _odlazak = datas.child("odlazak").getValue().toString();
                            String _nazivRestorana = datas.child("nazivRestorana").getValue().toString();
                            long _potvrdaDolaska = (long) datas.child("potvrdaDolaska").getValue();

                            hr.foi.restoranko.model.Rezervacija rezervacija = new hr.foi.restoranko.model.Rezervacija(_rezervacija, korisnik, _dolazak, _odlazak, _nazivRestorana, _potvrdaDolaska);
                            Prikazi(rezervacija);
                        }
                    }
                    catch (Exception e){}

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void Prikazi(final Rezervacija rezervacija) {
        LayoutInflater li = LayoutInflater.from(this);
        View divider = li.inflate(R.layout.rezervacija, null, false);

        TextView rezervacijaNaziv = (TextView) divider.findViewById(R.id.rezervacijaNaziv);
        rezervacijaNaziv.setText(rezervacija.getNazivRestorana());

        TextView rezervacijaDatum = (TextView) divider.findViewById(R.id.rezervacijaDatum);
        rezervacijaDatum.setText(rezervacija.getDolazak());

        Button rezervacijaPotvrda = (Button) divider.findViewById(R.id.rezervacijaPotvrda);
        Button rezervacijaRecenzija = (Button) divider.findViewById(R.id.rezervacijaRecenzija);

        rezervacijaPotvrda.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                //tu je za potvrdu dolaska
            }
        });

        rezervacijaRecenzija.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("restoran");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot datas: dataSnapshot.getChildren()){
                            String _nazivRestorana = datas.child("nazivRestorana").getValue().toString();
                            if(rezervacija.getNazivRestorana().equals(_nazivRestorana)) {
                                long _restoranId = (long) datas.child("restoranId").getValue();

                                Intent intent = new Intent(MojeRezervacije.this, Recenzija.class);
                                intent.putExtra("restoranko", _restoranId);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        container.addView(divider);
    }
}
