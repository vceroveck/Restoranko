package hr.foi.restoranko.view;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hr.foi.graphicdisplayoftable.Stol;
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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String restoran=this.getActivity().getIntent().getStringExtra("restoranId");
        final String dolazak=this.getActivity().getIntent().getStringExtra("dolazak");
        final String odlazak=this.getActivity().getIntent().getStringExtra("odlazak");
        final ImageView imageView = view.findViewById(R.id.imageViewPrikazStolova);
        final View view1=view;
        Slika slika = new Slika();
        slika.dohvatiIPostaviSliku("gs://hr-foi-restoranko.appspot.com/restoraniTlocrt/"+restoran+".png", imageView, getContext());

        final Spinner spinner=view.findViewById(R.id.spinnerOdabirStola);
        dohvatiStoloveUSpinner(spinner, restoran);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), "OOOO", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = view.findViewById(R.id.buttonPotvrdaOdabiraStola);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String odabraniStol=spinner.getSelectedItem().toString();
                if(odabraniStol=="--Odaberite stol--"){
                    Toast.makeText(view.getContext(), "Niste odabrali ni jedan stol", Toast.LENGTH_LONG).show();
                }
                else{
                    if(ProvjeriDostupnostStola(restoran, odabraniStol, dolazak, odlazak)){
                        //stol je moguce rezervirati
                    }
                }
            }
        });

    }

    private boolean ProvjeriDostupnostStola(String restoran, String odabraniStol, String dolazak, String odlazak) {
        boolean slobodan=false;

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("rezervacija").child(restoran+"_"+odabraniStol);
        databaseReference.orderByChild("odlazak").startAt(dolazak).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        String postojeciDolazak=data.child("dolazak").getValue().toString();
                        Log.i("dolazak", postojeciDolazak);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return slobodan;
    }

    private void dohvatiStoloveUSpinner(final Spinner spinner, String restoran) {
        final ArrayList<String> listaStolova=new ArrayList<>();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("restoran").child(restoran).child("stolovi");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaStolova.add("--Odaberite stol--");
                if(dataSnapshot.exists()){
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        listaStolova.add(data.getKey());
                    }
                }
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,  listaStolova);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

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
