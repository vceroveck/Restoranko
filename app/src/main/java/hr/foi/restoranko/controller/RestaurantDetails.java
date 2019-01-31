package hr.foi.restoranko.controller;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
import hr.foi.restoranko.model.OmiljeniRestoran;
import hr.foi.restoranko.model.Restoran;
import hr.foi.restoranko.view.Slika;

public class RestaurantDetails extends AppCompatActivity {
    private Menu traka;
    private Restoran restoran;
    private OmiljeniRestoran omiljeniRestoran;
    private DatabaseReference mDatabase;
    private String key;

    ImageView slikaRestorana;
    TextView opis, adresa, kontakt, webAdresa;
    Button menu, rezerviraj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        slikaRestorana = (ImageView) findViewById(R.id.restoran_slika);
        opis = (TextView) findViewById(R.id.restoran_opis);
        adresa = (TextView) findViewById(R.id.restoran_adresa);
        kontakt = (TextView) findViewById(R.id.restoran_kontakt);
        webAdresa = (TextView) findViewById(R.id.restoran_webAdresa);
        menu = (Button) findViewById(R.id.menu);
        rezerviraj = (Button) findViewById(R.id.rezerviraj);

        restoran = getIntent().getExtras().getParcelable("restoranko");
        omiljeniRestoran = new OmiljeniRestoran(restoran.getRestoranId(), Korisnik.prijavljeniKorisnik.getKorisnickoIme());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setTitle(restoran.getNazivRestorana());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(restoran.getSlika());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Slika slika = new Slika ();
                slika.setUriSlike(uri);
                restoran.setSlikaRestorana(slika);
                Slika.postaviSlikuUImageView(restoran.getSlikaRestorana(), slikaRestorana, getBaseContext());
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetails.this, hr.foi.restoranko.controller.Menu.class);
                intent.putExtra("restoranko", restoran);
                startActivity(intent);
            }
        });

        rezerviraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetails.this, hr.foi.restoranko.controller.Rezervacija.class);
                intent.putExtra("restoranko", restoran);
                startActivity(intent);
            }
        });

        opis.setText(restoran.getOpis());
        adresa.setText(restoran.getAdresa());
        kontakt.setText(restoran.getKontakt());
        webAdresa.setText(restoran.getWebLink());
    }

    //provjeri je li restoran označen kao favorit
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        traka = menu;
        getMenuInflater().inflate(R.menu.zvijezdica, menu);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("omiljeniRestorani");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean omiljeni = false;
                long idNasegRestorana = omiljeniRestoran.getRestoran();
                String prijavljenKorisnik = omiljeniRestoran.getKorisnik();

                for(DataSnapshot datas: dataSnapshot.getChildren()){

                    long idRestorana = (long) datas.child("restoran").getValue();
                    String korisnik = datas.child("korisnik").getValue().toString();

                    if(korisnik.equals(prijavljenKorisnik) && idRestorana == idNasegRestorana) {
                        key = datas.getKey();
                        omiljeni = true;
                    }

                }
                if(omiljeni) {
                    traka.findItem(R.id.star).setVisible(true);
                    traka.findItem(R.id.star2).setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return true;
    }

    //dodaj/briši restoran iz favorita
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.star: {
                item.setVisible(false);
                traka.findItem(R.id.star2).setVisible(true);
                Toast.makeText(this, "Restoran izbrisan iz favorita", Toast.LENGTH_LONG).show();

                mDatabase.child("omiljeniRestorani").child(key).removeValue();
                key = null;

                break;
            }
            case R.id.star2: {
                item.setVisible(false);
                traka.findItem(R.id.star).setVisible(true);
                Toast.makeText(this, "Restoran dodan u favorite", Toast.LENGTH_LONG).show();

                key = mDatabase.push().getKey();
                mDatabase.child("omiljeniRestorani").child(key).setValue(omiljeniRestoran);

                break;
            }
            case android.R.id.home: {
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
