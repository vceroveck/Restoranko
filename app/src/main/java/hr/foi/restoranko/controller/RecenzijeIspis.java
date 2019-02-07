package hr.foi.restoranko.controller;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Recenzija;

public class RecenzijeIspis extends AppCompatActivity {

    DatabaseReference bazaRecenzije;

    ListView listaRecenzije;

    List<Recenzija> recenzijaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recenzije_ispis);

        bazaRecenzije = FirebaseDatabase.getInstance().getReference("recenzije");

       listaRecenzije = (ListView) findViewById(R.id.listRecenzije);

       recenzijaList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        bazaRecenzije.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                recenzijaList.clear();
                for(DataSnapshot recenzijeSnapshot: dataSnapshot.getChildren()){
                    Recenzija recenzija = recenzijeSnapshot.getValue(Recenzija.class);

                    recenzijaList.add(recenzija);
                }

                ListaRecenzija adapter = new ListaRecenzija(RecenzijeIspis.this, recenzijaList);
                listaRecenzije.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
