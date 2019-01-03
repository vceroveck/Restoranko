package hr.foi.restoranko.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Restoran;

public class RestaurantDetails extends AppCompatActivity {
    private Menu traka;
    private Restoran restoran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        restoran = getIntent().getExtras().getParcelable("restoranko");

        setTitle(restoran.getNazivRestorana());
    }

    //provjeri je li restoran označen kao favorit
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        traka = menu;
        getMenuInflater().inflate(R.menu.zvijezdica, menu);

        //provjeri jeli ti omiljeno
        //ako je onda ovo prvo
        //ako ne onda ovo drugo

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

                //baza

                break;
            }
            case R.id.star2:
            {
                item.setVisible(false);
                traka.findItem(R.id.star).setVisible(true);
                Toast.makeText(this, "Restoran dodan u favorite", Toast.LENGTH_LONG).show();

                //baza

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
