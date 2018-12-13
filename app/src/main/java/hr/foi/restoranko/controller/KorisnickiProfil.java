package hr.foi.restoranko.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.autofill.AutofillValue;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;

public class KorisnickiProfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnicki_profil);

        UcitajKorisnickePodatke();
    }

    private void  UcitajKorisnickePodatke(){
        TextView Ime = (TextView) findViewById(R.id.outputKorisnikIme);
        TextView Prezime = (TextView) findViewById(R.id.outputKorisnikPrezime);
        ImageView slikaProfila = (ImageView) findViewById(R.id.imgKorisnik);

        Ime.setText(Korisnik.prijavljeniKorisnik.getIme());
        Prezime.setText(Korisnik.prijavljeniKorisnik.getPrezime());
        Picasso.get().load(Korisnik.prijavljeniKorisnik.getSlika()).into(slikaProfila);

    }
}
