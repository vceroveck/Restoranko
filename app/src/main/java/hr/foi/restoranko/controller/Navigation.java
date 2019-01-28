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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import java.util.List;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
import hr.foi.restoranko.model.Restoran;
import hr.foi.restoranko.view.Slika;
import hr.foi.restoranko.view.SuccessListener;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    private LinearLayout container;
    private View ucitavanje;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private ListView mDrawerList;
    private ArrayAdapter<Object> mAdapter;
    private SearchView pretraga;
    private List<Restoran> listaRestorana = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        firebaseAuth = FirebaseAuth.getInstance();

        drawer = findViewById(R.id.drawer_layout);
        container = (LinearLayout) findViewById(R.id.container);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        PostaviPretraguRestorana();
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

    private void PostaviPretraguRestorana() {
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

                    final Restoran novi = new Restoran(_id, _adresa, _kontakt, _naziv, _opis, _slika, _link);
                    listaRestorana.add(novi);
                    DohvatiSlikuRestorana(novi);
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


    private void addDrawerItems(){
        Object[] items={"Korisnički profil", "Notifikacije", "Odjava"};
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
                break;
            }
            case 3:{
                Logout();
                break;
            }
        }
    }
}
