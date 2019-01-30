package hr.foi.restoranko.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Restoran;
import hr.foi.restoranko.view.OdabirJelovnika;

public class Menu extends AppCompatActivity {
    private Restoran restoran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fragment fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        restoran = getIntent().getExtras().getParcelable("restoranko");
        setTitle("Menu restorana " + restoran.getNazivRestorana());

        Bundle bundle = new Bundle();
        long restoranId = restoran.getRestoranId();
        bundle.putLong("restoranId", restoranId );
        OdabirJelovnika odabirJelovnika = new OdabirJelovnika();
        odabirJelovnika.setArguments(bundle);

        transaction.replace(R.id.jelovnik1, odabirJelovnika);
        transaction.commit();


    }
}
