package hr.foi.restoranko;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hr.foi.restoranko.listeners.ChangeListener;
import hr.foi.restoranko.model.Korisnik;

public class LogIn extends AppCompatActivity {
    FirebaseUser user=null;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Button btnLogIn=(Button) findViewById(R.id.btnLogin);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
                String email = txtEmail.getText().toString();
                EditText txtPassword = (EditText) findViewById(R.id.txtLozinka);
                String password = txtPassword.getText().toString();
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
    }

    private void korisnikPrijavljen(){
        Toast.makeText(LogIn.this, "Uspješna prijava " + Korisnik.prijavljeniKorisnik.getIme() + " " + Korisnik.prijavljeniKorisnik.getPrezime(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LogIn.this, Navigation.class));
    }
}
