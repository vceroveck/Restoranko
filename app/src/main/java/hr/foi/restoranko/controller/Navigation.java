package hr.foi.restoranko.controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private ListView mDrawerList;
    private ArrayAdapter<Object> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        firebaseAuth = FirebaseAuth.getInstance();

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();


        addDrawerItems();
        setupDrawer();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


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

        View header=getLayoutInflater().inflate(R.layout.listview_item_header, null);
        ((ImageView) header.findViewById(R.id.imgViewIcon)).setImageDrawable(getResources().getDrawable(R.drawable.logo1));
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
