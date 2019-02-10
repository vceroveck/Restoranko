package hr.foi.restoranko.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Jelo;
import hr.foi.restoranko.model.Korisnik;
import hr.foi.restoranko.model.Restoran;
import hr.foi.restoranko.model.RezerviraniJelovnik;
import hr.foi.restoranko.view.Slika;
import hr.foi.restoranko.view.SuccessListener;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    private LinearLayout container;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private ListView mDrawerList;
    private ArrayAdapter<Object> mAdapter;
    private SearchView pretraga;
    private boolean pocetakPrograma = true;

    public static List<Restoran> listaRestorana = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);

        listaRestorana.clear();
        firebaseAuth = FirebaseAuth.getInstance();
        Jelo jelo = new Jelo();
        RezerviraniJelovnik rezerviraniJelovnik = new RezerviraniJelovnik();

        drawer = findViewById(R.id.drawer_layout);
        container = (LinearLayout) findViewById(R.id.container);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        PostaviElementeNavigacije();
        DohvatiSveRestorane();

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Postavljanje elemenata za filtriranje i pretraživanje restorana
    private void PostaviElementeNavigacije() {
        final Spinner spinner = (Spinner) findViewById(R.id.sortirajGumb);
        List<String> categories = new ArrayList<String>();
        categories.add("Bez filtera");
        categories.add("Top 10 prema broju pregleda");
        categories.add("Top 10 s najviše oznaka 'omiljeno'");
        categories.add("Top 10 s najboljim ocjenama");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        if(pocetakPrograma) {
                            pocetakPrograma = false;
                        }
                        else {
                            PretragaRestorana("");
                        }
                        break;
                    case 1:
                        container.removeAllViews();

                        Collections.sort(listaRestorana, new Comparator<Restoran>() {
                            public int compare(Restoran se1, Restoran se2) {
                                return (int) (se2.getBrojPregleda() - se1.getBrojPregleda());
                            }
                        });

                        for(int j = 0; j<10; j++) {
                            Restoran restoran = listaRestorana.get(j);
                            PrikaziRestoran(restoran);
                        }

                        break;
                    case 2:
                        container.removeAllViews();

                        Collections.sort(listaRestorana, new Comparator<Restoran>() {
                            public int compare(Restoran se1, Restoran se2) {
                                return (int) (se2.getBrojOznakaOmiljeno() - se1.getBrojOznakaOmiljeno());
                            }
                        });

                        for(int j = 0; j<10; j++) {
                            Restoran restoran = listaRestorana.get(j);
                            PrikaziRestoran(restoran);
                        }

                        break;
                    case 3:
                        container.removeAllViews();

                        Collections.sort(listaRestorana, new Comparator<Restoran>() {
                            public int compare(Restoran se1, Restoran se2) {
                                return (int) (se2.getProsjecnaOcjena() - se1.getProsjecnaOcjena());
                            }
                        });

                        for(int j = 0; j<10; j++) {
                            Restoran restoran = listaRestorana.get(j);
                            Log.v("--->", String.valueOf(restoran.getProsjecnaOcjena()));
                            PrikaziRestoran(restoran);
                        }
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        pretraga = (SearchView)findViewById(R.id.pretraga);
        pretraga.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String tekst) {
                pretraga.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String tekst) {
                PretragaRestorana(tekst);
                return true;
            }
        });

    }

    //Pretraga restorana prema zadanom tekstu
    private void PretragaRestorana(String tekst) {
        container.removeAllViews();
        for(int i = 0; i<listaRestorana.size(); i++) {
            Restoran restoran = listaRestorana.get(i);
            if(restoran.getNazivRestorana().toLowerCase().contains(tekst.toLowerCase())) PrikaziRestoran(restoran);
        }
    }

    //Metoda koja dohvaća restorane iz baze
    private void DohvatiSveRestorane() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("restoran");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){

                    long _id = (long) datas.child("restoranId").getValue();
                    String _adresa = datas.child("adresa").getValue().toString();
                    String _kontakt = datas.child("kontakt").getValue().toString();
                    String _naziv = datas.child("nazivRestorana").getValue().toString();
                    String _opis = datas.child("opis").getValue().toString();
                    String _slika = datas.child("slika").getValue().toString();
                    String _link = datas.child("webLink").getValue().toString();
                    long _brojPregleda = (long) datas.child("brojPregleda").getValue();

                    final Restoran novi = new Restoran(_id, _adresa, _kontakt, _naziv, _opis, _slika, _link, _brojPregleda);
                    listaRestorana.add(novi);
                    DohvatiSlikuRestorana(novi);
                    DohvatiOstaleAtribute(novi);
                }

                View spinner = (View) findViewById(R.id.ucitavanjeRestorana);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //Dohvaćanje atributa potrebnih za filtriranje restorana
    private void DohvatiOstaleAtribute(final Restoran novi) {
        final long[] brojOmiljeno = {0};

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("omiljeniRestorani");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {

                    long _id = (long) datas.child("restoran").getValue();
                    if (_id == novi.getRestoranId()) {
                        brojOmiljeno[0] = brojOmiljeno[0] + 1;
                        novi.setBrojOznakaOmiljeno(brojOmiljeno[0]);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final long[] broj = {0};
        final long[] prosjecnaOcjena = {0};

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("recenzije");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){

                    String _nazivRestorana = datas.child("nazivRestorana").getValue().toString();
                    if(_nazivRestorana.equals(novi.getNazivRestorana())) {
                        broj[0] = broj[0] + 1;

                        switch(datas.child("recenzijaLjestvica").getValue().toString()) {
                            case "fenomenalno":
                                prosjecnaOcjena[0] += 5;
                                break;
                            case "super":
                                prosjecnaOcjena[0] += 4;
                                break;
                            case "jako loše":
                                prosjecnaOcjena[0] += 1;
                                break;
                            case "dobro":
                                prosjecnaOcjena[0] += 3;
                                break;
                            default:
                                prosjecnaOcjena[0] += 2;
                                break;
                        }

                        novi.setProsjecnaOcjena((double)prosjecnaOcjena[0]/broj[0]);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    //Metoda koja dohvaća sliku restorana
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

    //Metoda kojom se restoran prikazuje na zaslonu
    private void PrikaziRestoran(final Restoran restoran) {
        LayoutInflater li = LayoutInflater.from(this);
        View divider = li.inflate(R.layout.restorani, null, false);

        TextView textView = (TextView) divider.findViewById(R.id.restoran_naziv);
        textView.setText(restoran.getNazivRestorana());

        final ImageView slikaRestorana = (ImageView) divider.findViewById(R.id.restoran_slika);
        Slika.postaviSlikuUImageView(restoran.getSlikaRestorana(), slikaRestorana, getBaseContext());

        divider.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                Intent intent = new Intent(Navigation.this, RestaurantDetails.class);
                intent.putExtra("restoranko", restoran);
                startActivity(intent);
            }
        });

        container.addView(divider);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case  R.id.nav_odjava:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogoutFragment());
                Logout();
                break;
            case R.id.nav_korisnicki_profil:
                startActivity(new Intent(Navigation.this, KorisnickiProfil.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Odjava korisnika s sustava čime se brišu podaci o prijavi s uređaja
    private void Logout(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", null);
        editor.putString("lozinka", null);
        editor.apply();

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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.otvoriNavigacija, R.string.zatvoriNavigacija) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigacija!");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //Postavljanje glavnog izbornika
    private void addDrawerItems(){
        Object[] items={"Korisnički profil", "Recenzije", "Odjava"};
        mAdapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, items);
        mDrawerList.setAdapter(mAdapter);

        final View header=getLayoutInflater().inflate(R.layout.listview_item_header, null);
        final ImageView slikaProfila = ((ImageView) header.findViewById(R.id.imgViewIcon));


        final Slika slika = new Slika();

        slika.dohvatiSlikuKorisnika(Korisnik.prijavljeniKorisnik, new SuccessListener() {
            @Override
            public void addOnSuccessListener(Object object) {
                Slika.postaviSlikuUImageView((Slika) object, slikaProfila, header.getContext());
                slikaProfila.refreshDrawableState();
            }
        });

        ((TextView) header.findViewById(R.id.txtViewName)).setText(Korisnik.prijavljeniKorisnik.getIme()+" "+Korisnik.prijavljeniKorisnik.getPrezime());
        mDrawerList.addHeaderView(header);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener(position);
            }
        });

    }

    //Postavljanje glavnog izbornika
    private void onItemClickListener(int position){

        switch (position){
            case 0: {
                startActivity(new Intent(Navigation.this, KorisnickiProfil.class));
                break;
            }
            case 1:{
                startActivity(new Intent(Navigation.this, KorisnickiProfil.class));
                break;
            }
            case 2:{
                startActivity(new Intent(Navigation.this, RecenzijeIspis.class));
                break;
            }
            case 3:{
                Logout();
                break;
            }
        }
    }

}
