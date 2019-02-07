package hr.foi.restoranko.controller;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Restoran;
import hr.foi.restoranko.model.RezerviraniJelovnik;
import hr.foi.restoranko.view.OdabirJelovnika;
import hr.foi.restoranko.view.PotvrdaJelovnika;

public class Menu extends AppCompatActivity {
    private Restoran restoran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int prikaz[] = {0};

        final OdabirJelovnika odabirJelovnika = new OdabirJelovnika();
        final PotvrdaJelovnika fragmentS1 = new PotvrdaJelovnika();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button natrag = (Button) findViewById(R.id.natrag);
        Button potvrdi = (Button) findViewById(R.id.potvrdi);

        natrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(prikaz[0]) {
                    case 0:
                        RezerviraniJelovnik.BrisiCijeluListu();
                        finish();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.jelovnik1, odabirJelovnika).commit();
                        prikaz[0]--;
                        break;
                }
            }
        });

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(prikaz[0]) {
                    case 0:
                        if(RezerviraniJelovnik.listaRezerviranihJela.size() > 0) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.jelovnik1, fragmentS1).commit();
                            prikaz[0] = prikaz[0] + 1;
                        }
                        else {
                            Toast.makeText(getApplication(), "Niste odabrali niti jedan jelovnik", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        finish();

                        prikaz[0]--;
                        break;
                }
            }
        });


        restoran = getIntent().getExtras().getParcelable("restoranko");
        setTitle("Menu restorana " + restoran.getNazivRestorana());

        Bundle bundle = new Bundle();
        long restoranId = restoran.getRestoranId();
        bundle.putLong("restoranId", restoranId );
        odabirJelovnika.setArguments(bundle);

        transaction.replace(R.id.jelovnik1, odabirJelovnika);
        transaction.commit();

    }
}
