package hr.foi.restoranko;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
<<<<<<< HEAD
=======
import android.os.Bundle;
import android.util.Log;
>>>>>>> registracija
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
<<<<<<< HEAD
=======
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
>>>>>>> registracija
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.foi.restoranko.model.Korisnik;

public class Registration extends AppCompatActivity {
    EditText ime,prezime,korime,email,lozinka,plozinka;
    String imeVrijednost, prezimeVrijednost, korimeVrijednost, emailVrijednost, lozinkaVrijednost;
    Button registracija;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registracija = findViewById(R.id.registerButton);
        ime = findViewById(R.id.nameValue);
        prezime = findViewById(R.id.surnameValue);
        korime = findViewById(R.id.usernameValue);
        email = findViewById(R.id.emailValue);
        lozinka = findViewById(R.id.passwordValue);
        plozinka = findViewById(R.id.confirmPasswordValue);

        registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm.isAcceptingText()) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                SignUp();
            }
        });
    }

    private void SignUp() {
        imeVrijednost = ime.getText().toString();
        prezimeVrijednost = prezime.getText().toString();
        korimeVrijednost = korime.getText().toString();
        lozinkaVrijednost = lozinka.getText().toString();

        emailVrijednost = email.getText().toString();
        Pattern pattern = Pattern.compile("(?=.{10,30}$)^[a-z0-9A-Z]+\\.?[a-z0-9A-Z]*@[a-z0-9A-Z]+(\\.[a-zA-Z]{2,})");
        Matcher matcher = pattern.matcher(emailVrijednost);
        boolean emailProvjera = matcher.lookingAt();

        if(imeVrijednost.isEmpty() || prezimeVrijednost.isEmpty() || korimeVrijednost.isEmpty() || emailVrijednost.isEmpty() || lozinkaVrijednost.isEmpty()) {
            Toast.makeText(this, "Niste unijeli sve podatke", Toast.LENGTH_LONG).show();
        }
        else if(lozinkaVrijednost.length() < 8) {
            Toast.makeText(this, "Lozinka mora imati najmanje 8 znakova", Toast.LENGTH_LONG).show();
        }
        else if(!lozinkaVrijednost.equals(plozinka.getText().toString())){
            Toast.makeText(this, "Lozinke nisu iste", Toast.LENGTH_LONG).show();
        }
        else if(!emailProvjera){
            Toast.makeText(this, "E-Mail je neispravan", Toast.LENGTH_LONG).show();
        }
        else {
            //provjeri dal već postoji taj korisnik u bazi, ako ne onda šalji u bazu
            final Query provjeraKorIme = FirebaseDatabase.getInstance().getReference("user").orderByChild("korisnickoIme").equalTo(korimeVrijednost);
            provjeraKorIme.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean postoji = false;
                    for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {
                        postoji = true;
                    }
                    SaljiUBazu(postoji);
                    provjeraKorIme.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
            }
        }

        private void SaljiUBazu(boolean potvrda) {

        if(potvrda) {
            Toast.makeText(this, "Korisničko ime već postoji u bazi", Toast.LENGTH_LONG).show();
        }
        else {
            final Korisnik korisnik = new Korisnik(imeVrijednost, prezimeVrijednost, emailVrijednost, korimeVrijednost, lozinkaVrijednost);
            auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(emailVrijednost, lozinkaVrijednost)
                    .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Registration.this, "Registration failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
                                alertDialog.setMessage("Uspješna registracija");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                final FirebaseUser user = auth.getCurrentUser();
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                ref.child("user").child(user.getUid()).setValue(korisnik);
                                                user.sendEmailVerification();

                                                dialog.dismiss();
                                                startActivity(new Intent(Registration.this, MainActivity.class));
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    });
        }
    }
}
