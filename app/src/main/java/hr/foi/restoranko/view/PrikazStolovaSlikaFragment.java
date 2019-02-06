package hr.foi.restoranko.view;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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

        final ImageView imageView = view.findViewById(R.id.imageViewPrikazStolova);
        final View view1=view;
        final Slika slika = new Slika();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://hr-foi-restoranko.appspot.com/restoraniTlocrt/1.png");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                slika.setUriSlike(uri);
                Slika.postaviSlikuUImageView(slika, imageView,  view1.getContext());
            }
        });



        Spinner spinner=view.findViewById(R.id.spinnerOdabirStola);
        spinner.setAdapter(dohvatiStolove());

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

    private ArrayAdapter dohvatiStolove()
    {
        String restoran=this.getActivity().getIntent().getStringExtra("restoran");
        final String dolazak=this.getActivity().getIntent().getStringExtra("dolazak");
        String odlazak=this.getActivity().getIntent().getStringExtra("odlazak");


        final ArrayList<String> listaStolova=new ArrayList<>();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("restoran").child("1").child("stolovi");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        listaStolova.add(data.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,  listaStolova);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        return arrayAdapter;

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
