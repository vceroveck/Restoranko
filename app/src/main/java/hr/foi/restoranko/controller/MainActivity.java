package hr.foi.restoranko.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import hr.foi.restoranko.R;

public class MainActivity extends AppCompatActivity {
    Button registration;
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registration = findViewById(R.id.btn_registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), Registration.class));
            }
        });

        logIn=(Button) findViewById(R.id.btn_signin);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), LogIn.class));
            }
        });



    }
}
