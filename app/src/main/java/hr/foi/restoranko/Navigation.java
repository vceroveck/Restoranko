package hr.foi.restoranko;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Navigation extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    //private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        //drawer = findViewById(R.id.drawer_layout);

        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer,
                //R.string.otvoriNavigacija, R.string.zatvoriNavigacija);
        //drawer.addDrawerListener(toggle);
        //toggle.syncState();
    }

    //public void onBackPressed(){
        //if(drawer.isDrawerOpen(GravityCompat.START)){
            //drawer.closeDrawer(GravityCompat.START);
        //} else{
            //super.onBackPressed();
        //}
    //}

    //private void setSupportActionBar(Toolbar toolbar) {
    //}

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
