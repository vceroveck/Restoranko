package hr.foi.restoranko.controller;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import hr.foi.restoranko.model.Restoran;
import hr.foi.restoranko.view.Slika;

public class Favoriti extends AppCompatActivity {
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoriti);

        container = (LinearLayout) findViewById(R.id.container);

        DohvatiSveRestorane();
    }

    @Override
    public void onBackPressed() {
        container.removeAllViews();
        finish();
    }

    private void PrikaziRestoran(final Restoran restoran) {
        LayoutInflater li = LayoutInflater.from(this);
        View divider = li.inflate(R.layout.restorani, null, false);

        TextView textView = (TextView) divider.findViewById(R.id.restoran_naziv);
        textView.setText(restoran.getNazivRestorana());

        final ImageView slikaRestorana = (ImageView) divider.findViewById(R.id.restoran_slika);
        Slika.postaviSlikuUImageView(restoran.getSlikaRestorana(), slikaRestorana, getBaseContext());

        divider.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                Intent intent = new Intent(Favoriti.this, RestaurantDetails.class);
                intent.putExtra("restoranko", restoran);
                startActivity(intent);
            }
        });

        container.addView(divider);
    }

    private void DohvatiSveRestorane() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("omiljeniRestorani");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){

                    long _restoran = (long) datas.child("restoran").getValue();
                    String _korisnik = datas.child("korisnik").getValue().toString();

                    if(_korisnik.equals(Korisnik.prijavljeniKorisnik.getKorisnickoIme())) {
                        for(Restoran r : Navigation.listaRestorana) {
                            if(r.getRestoranId() == _restoran) {
                                DohvatiSlikuRestorana(r);

                                View spinner = (View) findViewById(R.id.nemaOmiljenih);
                                spinner.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void DohvatiSlikuRestorana(final Restoran restoran) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(restoran.getSlika());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Slika slika = new Slika ();
                slika.setUriSlike(uri);
                restoran.setSlikaRestorana(slika);
                PrikaziRestoran(restoran);
            }
        });
    }
}
