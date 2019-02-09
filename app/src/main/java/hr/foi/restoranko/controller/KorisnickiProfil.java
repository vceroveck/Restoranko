package hr.foi.restoranko.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
import hr.foi.restoranko.view.Slika;

public class KorisnickiProfil extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    ImageView slikaProfila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnicki_profil);

        slikaProfila = (ImageView) findViewById(R.id.imgKorisnik);

        UcitajKorisnickePodatke();

        Button forgotPassword = (Button) findViewById(R.id.btnPromijenitiLozinku);
        Button promijeniSlikuProfila = (Button) findViewById(R.id.btnPromijenitiSlikuProfila);
        Button prikazFavorita = (Button) findViewById(R.id.btnPrikazFavorita);
        Button prikazRezervacija = (Button) findViewById(R.id.btnPrikazRezervacija);





        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KorisnickiProfil.this, Password.class));
            }
        });

        promijeniSlikuProfila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OdaberiSliku();
            }
        });

        prikazFavorita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KorisnickiProfil.this, Favoriti.class));
            }
        });

        prikazRezervacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KorisnickiProfil.this, MojeRezervacije.class));
            }
        });
    }

    private void PostaviSliku(Uri filePath) {
        Slika slika = new Slika(filePath);
        Korisnik.prijavljeniKorisnik.setSlika(slika);
        Slika.postaviSlikuUImageView(slika, slikaProfila, getBaseContext());
        Slika.PohraniSlikuUbazu(slika, getBaseContext(), KorisnickiProfil.this);
    }

    private void  UcitajKorisnickePodatke(){
        Slika.postaviSlikuUImageView(Korisnik.prijavljeniKorisnik.getSlika(), slikaProfila, getBaseContext());
        TextView Ime = (TextView) findViewById(R.id.outputKorisnikIme);
        TextView Prezime = (TextView) findViewById(R.id.outputKorisnikPrezime);
        Ime.setText(Korisnik.prijavljeniKorisnik.getIme());
        Prezime.setText(Korisnik.prijavljeniKorisnik.getPrezime());
    }

    private void OdaberiSliku(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,  "Odaberi sliku"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri filePath;
        ImageView imageView;
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode ==RESULT_OK && data!=null && data.getData()!=null){
            filePath = data.getData();
            try{
                imageView = (ImageView) findViewById(R.id.imgKorisnik);
                imageView.setImageURI(null);
                imageView.setImageURI(filePath);
                imageView.refreshDrawableState();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            PostaviSliku(filePath);
        }
    }
}
