package hr.foi.restoranko.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import hr.foi.restoranko.R;
import hr.foi.restoranko.controller.PrikazStolova;

public class PrikazStolovaSlikaFragment extends Fragment implements PrikazStolova
{
    String name = "Putem slike";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_slika_stolovi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner=view.findViewById(R.id.spinnerOdabirStola);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), "OOOO", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*private ArrayAdapter dohvatiStolove()
    {
        String restoran=this.getActivity().getIntent().getStringExtra("restoran");
        String dolazak=this.getActivity().getIntent().getStringExtra("dolazak");
        String odlazak=this.getActivity().getIntent().getStringExtra("odlazak");


        String[] listaStolova=new String[]{};
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("restoran").child(restoran).child("stolovi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

    @Override
    public Fragment getFragment()
    {
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
