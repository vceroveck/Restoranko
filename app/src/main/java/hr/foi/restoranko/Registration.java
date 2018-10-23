package hr.foi.restoranko;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    EditText ime,prezime,korime,email,lozinka,plozinka;
    String imeVrijednost, prezimeVrijednost, korimeVrijednost, emailVrijednost, lozinkaVrijednost;
    Button registracija;

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
            Toast.makeText(this, "You didn't enter all required data", Toast.LENGTH_LONG).show();
        }
        else if(lozinkaVrijednost.length() < 8) {
            Toast.makeText(this, "Password must contain minimum 8 characters", Toast.LENGTH_LONG).show();
        }
        else if(!lozinkaVrijednost.equals(plozinka.getText().toString())){
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_LONG).show();
        }
        else if(!emailProvjera){
            Toast.makeText(this, "E-Mail is invalid", Toast.LENGTH_LONG).show();
        }
        //provjeri dal već postoji taj korisnik u bazi
        else {
            //šalji u bazu

            AlertDialog alertDialog = new AlertDialog.Builder(Registration.this).create();
            alertDialog.setMessage("Successfully registered");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            //zatvori aktivnost
        }

    }
}
