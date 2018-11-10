package hr.foi.restoranko;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                auth.signOut();
                try {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user == null) return;
                                    final String userID = user.getUid();
                                    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user");
                                    Korisnik.prijavljeniKorisnik = new Korisnik();
                                    userReference.orderByChild("email")
                                            .equalTo(user.getEmail())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                                        Korisnik.prijavljeniKorisnik.setIme(datas.child("ime").getValue().toString());
                                                        Korisnik.prijavljeniKorisnik.setPrezime(datas.child("prezime").getValue().toString());
                                                        Korisnik.prijavljeniKorisnik.setuId(userID);
                                                        Korisnik.prijavljeniKorisnik.setEmail(datas.child("email").getValue().toString());
                                                        Korisnik.prijavljeniKorisnik.setKorisnickoIme(datas.child("korisnickoIme").getValue().toString());
                                                        Korisnik.prijavljeniKorisnik.setLozinka(datas.child("lozinka").getValue().toString());
                                                    }
                                                    korisnikPrijavljen();

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
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
    }
}
