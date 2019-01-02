package hr.foi.restoranko.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import hr.foi.restoranko.R;
import hr.foi.restoranko.view.LogoutFragment;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    private LinearLayout container;
    private View ucitavanje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        firebaseAuth = FirebaseAuth.getInstance();

        drawer = findViewById(R.id.drawer_layout);
        container = (LinearLayout) findViewById(R.id.container);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //postavljanje padajućeg izbornika
        Spinner spinner = (Spinner) findViewById(R.id.sortirajGumb);
        List<String> categories = new ArrayList<String>();
        categories.add("SORTIRAJ");
        categories.add("Najviše pregleda");
        categories.add("Najviše oznaka 'omiljeno'");
        categories.add("Najbolje ocijenjeni");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        //testni ispis restorana
        LayoutInflater li = LayoutInflater.from(this);
        ucitavanje = li.inflate(R.layout.loading, null, false);
        container.addView(ucitavanje);
        UcitajRestorane();
    }

    private void UcitajRestorane() {
        container.removeView(ucitavanje);
        for(int i = 0; i<20 ; i++){
            LayoutInflater li = LayoutInflater.from(this);
            View divider = li.inflate(R.layout.restorani, null, false);
            TextView textView = (TextView) divider.findViewById(R.id.restoran_naziv);
            textView.setText("Restoran " + i);

            //uglavnom kombiniraj nešto s id
//            divider.setId(i);
            final int id = i;

            divider.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(Navigation.this, RestaurantDetails.class);
                    Bundle b = new Bundle();
                    b.putInt("key", id); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            });

            container.addView(divider);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            divider.startAnimation(animation);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case  R.id.nav_odjava:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogoutFragment());
                Logout();
                break;
            case R.id.nav_korisnicki_profil:
                startActivity(new Intent(Navigation.this, KorisnickiProfil.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(Navigation.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.logoutMenu:{
             Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
