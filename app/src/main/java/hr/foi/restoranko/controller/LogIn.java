package hr.foi.restoranko.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
import hr.foi.restoranko.view.ChangeListener;

public class LogIn extends AppCompatActivity {
    String email, password;
    FirebaseUser user=null;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Button btnLogIn=(Button) findViewById(R.id.btnLogin);
        Button btnscener= (Button) findViewById(R.id.btnscener) ;
        Button generator=(Button) findViewById(R.id.generator);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
                email = txtEmail.getText().toString();
                EditText txtPassword = (EditText) findViewById(R.id.txtLozinka);
                password = txtPassword.getText().toString();
                try {
                    Korisnik korisnik=new Korisnik();
                    korisnik.prijaviKorisnika(email, password, LogIn.this);
                    Korisnik.prijavljeniKorisnik.setListener(new ChangeListener() {
                        @Override
                        public void onChange() {
                            korisnikPrijavljen();
                        }
                    });
                }
                catch (Exception ex){
                    Toast.makeText(LogIn.this, "Greška u prijavi ili neispravni podaci!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnscener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, QrScener.class));
            }
        });

        generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, QRGenerator.class));

            }
        });
    }

    private void korisnikPrijavljen(){
        Toast.makeText(LogIn.this, "Uspješna prijava " + Korisnik.prijavljeniKorisnik.getIme() + " " + Korisnik.prijavljeniKorisnik.getPrezime(), Toast.LENGTH_SHORT).show();

        CheckBox spremitiPrijavu = (CheckBox) findViewById(R.id.spremitiPrijavu);
        if(spremitiPrijavu.isChecked()) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", email);
            editor.putString("lozinka", password);
            editor.apply();
        }

        startActivity(new Intent(LogIn.this, Navigation.class));
    }
}
