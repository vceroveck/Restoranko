package hr.foi.restoranko.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
import hr.foi.restoranko.view.ChangeListener;

public class MainActivity extends AppCompatActivity {
    Button registration;
    Button logIn;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (LinearLayout) findViewById(R.id.polaEkrana);

        //Provjera je li mobitel povezan na internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
            Toast.makeText(this, "Aplikacija zahtjeva pristup internetu", Toast.LENGTH_LONG).show();
            finish();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("email", "");
        String password = preferences.getString("lozinka", "");
        if(!email.isEmpty())
        {
            Korisnik korisnik=new Korisnik();
            korisnik.prijaviKorisnika(email, password, this);
            Korisnik.prijavljeniKorisnik.setListener(new ChangeListener() {
                @Override
                public void onChange() {
                    prijaviKorisnika();
                }
            });
        }
        else {
            View spinner = (View) findViewById(R.id.umetnutiSpinner);
            View gumbi = (View) findViewById(R.id.umetnutiGumbi);
            spinner.setVisibility(View.GONE);
            gumbi.setVisibility(View.VISIBLE);

            registration = findViewById(R.id.btn_registration);
            registration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), Registration.class));
                }
            });

            logIn = (Button) findViewById(R.id.btn_signin);
            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(v.getContext(), LogIn.class));
                }
            });
        }
    }

    private void prijaviKorisnika() {
        startActivity(new Intent(MainActivity.this, Navigation.class));
    }
}
