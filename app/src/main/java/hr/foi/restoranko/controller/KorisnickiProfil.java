package hr.foi.restoranko.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hr.foi.restoranko.R;

public class KorisnickiProfil extends AppCompatActivity {

    private Button forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnicki_profil);

        forgotPassword = (Button)findViewById(R.id.btnPromijenitiLozinku);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KorisnickiProfil.this, Password.class));
            }
        });
    }
}
