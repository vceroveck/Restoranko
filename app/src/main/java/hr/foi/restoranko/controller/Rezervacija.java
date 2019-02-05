package hr.foi.restoranko.controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
import hr.foi.restoranko.model.Restoran;
import hr.foi.restoranko.model.RezerviraniJelovnik;

public class Rezervacija extends AppCompatActivity {
    private Restoran restoran;
    int godina = 0, mjesec, dan;
    TextView odlazak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervacija);
        restoran = getIntent().getExtras().getParcelable("restoranko");

        TextView nazivRestorana = findViewById(R.id.rezervacijaNazivRestorana);
        nazivRestorana.setText(restoran.getNazivRestorana());

        final TextView dolazak = findViewById(R.id.dolazakVrijeme);
        dolazak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odaberiDatum(dolazak);
            }
        });


        odlazak = findViewById(R.id.odlazakVrijeme);
        odlazak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(godina!=0) {
                    Calendar c = Calendar.getInstance();
                    c.set(godina, mjesec, dan);
                    String datum[] = {dodajNulu(dan) + "." + dodajNulu((mjesec + 1)) + "." + godina + "."};
                    odaberiVrijeme(odlazak, datum);
                }
                else {
                    Toast.makeText(Rezervacija.this, "Prvo postavite datum dolaska",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button odabirJelovnika = findViewById(R.id.rezervacijaOdabirJelovnika);
        if(RezerviraniJelovnik.listaRezerviranihJela == null || RezerviraniJelovnik.listaRezerviranihJela.size() == 0) {
            odabirJelovnika.setText(R.string.rezervacijaOdabirMenu);
        }
        odabirJelovnika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rezervacija.this, hr.foi.restoranko.controller.Menu.class);
                intent.putExtra("restoranko", restoran);
                startActivity(intent);
            }
        });

        Button odabirStola = findViewById(R.id.rezervacijaOdabirStola);
        odabirStola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rezervacija.this, OdabirStolaActivity.class);
                intent.putExtra("restoranId", restoran.getRestoranId());
                intent.putExtra("dolazak", dolazak.getText());
                intent.putExtra("odlazak", odlazak.getText());
                startActivity(intent);
            }
        });

        Button potvrda = findViewById(R.id.rezervacijaPotvrda);
        potvrda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(odlazak.getText().toString().equals("Postavi vrijeme") || dolazak.getText().toString().equals("Postavi vrijeme")) {
                    Toast.makeText(Rezervacija.this, "Postavite datum dolaska i odlaska",
                            Toast.LENGTH_SHORT).show();
                }
                else if(RezerviraniJelovnik.listaRezerviranihJela.size() == 0) {
                    Toast.makeText(Rezervacija.this, "Odaberite neko jelo",
                            Toast.LENGTH_SHORT).show();
                }
                //provjera jel odabran stol
                else {
                    //šalji u bazu

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    String key;

                    hr.foi.restoranko.model.Rezervacija rezervacija = new hr.foi.restoranko.model.Rezervacija(Korisnik.prijavljeniKorisnik.getKorisnickoIme(), dolazak.getText().toString(), odlazak.getText().toString(), restoran.getNazivRestorana());
                    key = mDatabase.push().getKey();
                    mDatabase.child("rezervacija").child(key).setValue(rezervacija);

                    int brojJela = 0;

                    for(RezerviraniJelovnik rj : RezerviraniJelovnik.listaRezerviranihJela) {
                        DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference().child("rezerviraniJelovnici").child(String.valueOf(brojJela++)+key);
                        mRef.child("jelovnikId").setValue(rj.getJelovnik().getJelovnikId());
                        mRef.child("kolicina").setValue(rj.getKolicina());
                        mRef.child("rezervacijaId").setValue(rezervacija.getRezervacijaId());
                    }

                    android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(Rezervacija.this).create();
                    alertDialog.setMessage("Uspješno obavljena rezervacija");
                    alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    RezerviraniJelovnik.BrisiCijeluListu();
                                    startActivity(new Intent(Rezervacija.this, Navigation.class));
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }

    private void odaberiDatum(final TextView textView) {
        odlazak.setText(R.string.postaviVrijeme);
        final Calendar c = Calendar.getInstance();
        final String[] datum = {""};
        godina = c.get(Calendar.YEAR);
        mjesec = c.get(Calendar.MONTH);
        dan = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int g, int m, int d) {
                        godina = g;
                        mjesec = m;
                        dan = d;
                        datum[0] = dodajNulu(dan) + "." + dodajNulu((mjesec + 1)) + "." + godina + ".";
                        odaberiVrijeme(textView, datum);
                    }
                }, godina, mjesec, dan);

        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    private void odaberiVrijeme(final TextView textView, final String[] datum) {
        final Calendar c = Calendar.getInstance();
        final int[] sat = {c.get(Calendar.HOUR_OF_DAY)};
        final int[] minuta = {c.get(Calendar.MINUTE)};

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int h, int min) {

                        sat[0] = h;
                        minuta[0] = min;

                        textView.setText(datum[0] + " " + h + ":" + dodajNulu(min));
                    }

                }, sat[0], minuta[0], true);
        timePickerDialog.show();
    }

    private String dodajNulu(int min) {
        if(min<10) return "0" + String.valueOf(min);
        return String.valueOf(min);
    }

}
