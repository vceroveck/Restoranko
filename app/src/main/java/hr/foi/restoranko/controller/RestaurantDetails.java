package hr.foi.restoranko.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
import hr.foi.restoranko.model.OmiljeniRestoran;
import hr.foi.restoranko.model.Restoran;

public class RestaurantDetails extends AppCompatActivity {
    private Menu traka;
    private Restoran restoran;
    private OmiljeniRestoran omiljeniRestoran;
    private DatabaseReference mDatabase;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        restoran = getIntent().getExtras().getParcelable("restoranko");
        omiljeniRestoran = new OmiljeniRestoran(restoran.getRestoranId(), Korisnik.prijavljeniKorisnik.getKorisnickoIme());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setTitle(restoran.getNazivRestorana());
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

        switch(item.getItemId()){
            case R.id.star:
            {
                item.setVisible(false);
                traka.findItem(R.id.star2).setVisible(true);
                Toast.makeText(this, "Restoran izbrisan iz favorita", Toast.LENGTH_LONG).show();

                mDatabase.child("omiljeniRestorani").child(key).removeValue();
                key = null;

                break;
            }
            case R.id.star2:
            {
                item.setVisible(false);
                traka.findItem(R.id.star).setVisible(true);
                Toast.makeText(this, "Restoran dodan u favorite", Toast.LENGTH_LONG).show();

                key = mDatabase.push().getKey();
                mDatabase.child("omiljeniRestorani").child(key).setValue(omiljeniRestoran);

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
