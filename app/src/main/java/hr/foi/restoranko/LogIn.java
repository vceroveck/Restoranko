package hr.foi.restoranko;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hr.foi.restoranko.model.Korisnik;

public class LogIn extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        final FirebaseAuth auth=FirebaseAuth.getInstance();
        Button btnLogIn=(Button) findViewById(R.id.btnLogin);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtEmail=(EditText) findViewById(R.id.txtEmail);
                String email=txtEmail.getText().toString();
                EditText txtPassword=(EditText) findViewById(R.id.txtLozinka);
                String password=txtPassword.getText().toString();
                FirebaseUser user=Korisnik.prijaviKorisnika(auth ,email, password);
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                DatabaseReference userReference=reference.child("user");
                if(user==null){
                    Toast.makeText(LogIn.this, "Nepostojeći korisnik" + user, Toast.LENGTH_SHORT).show();
                }
                else {
                    
                    userReference.orderByChild(user.getUid())
                            .limitToFirst(1)
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Korisnik korisnik = dataSnapshot.getValue(Korisnik.class);
                                    startActivity(new Intent(LogIn.this, Pocetna.class));
                                    Toast.makeText(LogIn.this, "Ime: " + korisnik.getIme() + " Prezime: " + korisnik.getPrezime(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }
        });
    }
}
