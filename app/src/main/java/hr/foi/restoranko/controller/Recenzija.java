package hr.foi.restoranko.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.restoranko.R;

public class Recenzija extends AppCompatActivity {

    RatingBar ratingBar;
    TextView ljestvica;
    EditText povratnaInformacija;
    Button posalji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recenzija);

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
                    povratnaInformacija.setText("");
                    ratingBar.setRating(0);
                    Toast.makeText(Recenzija.this, "Hvala što ste podijelili povratnu informaciju!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
