package hr.foi.restoranko.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import java.util.Calendar;
import java.util.Locale;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
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

    //Dohvaćanje svih korisnikovih rezervacija
    private void DohvatiSveMojeRezervacije() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("user").child(Korisnik.prijavljeniKorisnik.getuId()).child("rezervacijeKorisnika").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    //Log.i("rezervacija", data.getKey());
                    final String sifraStola=data.child("stol").getValue().toString();
                    final String idRezervacije=data.getKey();
                    reference.child("rezervacija").child(sifraStola).orderByKey().equalTo(idRezervacije).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data:dataSnapshot.getChildren()){
                                TextView nemaRezervacija = (TextView) container.findViewById(R.id.nemaRezervacija);
                                nemaRezervacija.setVisibility(View.GONE);

                                Calendar cal = Calendar.getInstance(Locale.GERMANY);
                                cal.setTimeInMillis(Long.parseLong(data.child("dolazak").getValue().toString()));
                                String _dolazak = DateFormat.format("dd.MM.yyyy. HH:mm", cal).toString();
                                cal.setTimeInMillis(Long.parseLong(data.child("odlazak").getValue().toString()));
                                String _odlazak=DateFormat.format("dd.MM.yyyy. HH:mm", cal).toString();

                                long _rezervacija = 0;
                                String _nazivRestorana = data.child("nazivRestorana").getValue().toString();
                                long _potvrdaDolaska = (long) data.child("potvrdaDolaska").getValue();

                                hr.foi.restoranko.model.Rezervacija rezervacija = new hr.foi.restoranko.model.Rezervacija(_rezervacija, Korisnik.prijavljeniKorisnik.getKorisnickoIme(), _dolazak, _odlazak, _nazivRestorana, _potvrdaDolaska);
                                Prikazi(rezervacija);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Prikaz svih rezervacija
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
                startActivity(new Intent(MojeRezervacije.this, QrScener.class));
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
                                intent.putExtra("restoranko", _nazivRestorana);
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
