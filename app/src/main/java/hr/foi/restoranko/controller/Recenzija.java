package hr.foi.restoranko.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hr.foi.restoranko.R;

public class Recenzija extends AppCompatActivity {

    RatingBar ratingBar;
    TextView ljestvica;
    EditText povratnaInformacija;
    Button posalji;

    DatabaseReference bazaRecenzije;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recenzija);

        bazaRecenzije = FirebaseDatabase.getInstance().getReference("recenzije");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ljestvica = (TextView) findViewById(R.id.txtLjestvica);
        povratnaInformacija = (EditText) findViewById(R.id.txtPovratnaInformacija);
        posalji = (Button) findViewById(R.id.btnPosalji);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ljestvica.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()){
                    case 1:
                        ljestvica.setText("jako loše");
                        break;
                    case 2:
                        ljestvica.setText("potrebno poboljšanje");
                        break;
                    case 3:
                        ljestvica.setText("dobro");
                        break;
                    case 4:
                        ljestvica.setText("super");
                        break;
                    case 5:
                        ljestvica.setText("fenomenalno");
                        break;
                    default:
                        ljestvica.setText("");
                }
            }
        });

        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(povratnaInformacija.getText().toString().isEmpty()){
                    Toast.makeText(Recenzija.this, "Ispunite tekstualni okvir za povratnu informaciju!", Toast.LENGTH_LONG).show();
                }else{
                    dodajRecenziju();
                    povratnaInformacija.setText("");
                    ratingBar.setRating(0);
                    Toast.makeText(Recenzija.this, "Hvala što ste podijelili povratnu informaciju!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dodajRecenziju(){
        long restoranId = getIntent().getExtras().getLong("restoranko");

        String postavljenaLjestvica = ljestvica.getText().toString().trim();
        String informacija = povratnaInformacija.getText().toString().trim();

        String id = bazaRecenzije.push().getKey();

        hr.foi.restoranko.model.Recenzija recenzija = new hr.foi.restoranko.model.Recenzija(restoranId, id, postavljenaLjestvica, informacija);

        bazaRecenzije.child(id).setValue(recenzija);
        finish();
    }
}
