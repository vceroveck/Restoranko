package hr.foi.restoranko.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import hr.foi.restoranko.R;
import hr.foi.restoranko.controller.PrikazStolova;

public class PrikazStolovaSlikaFragment extends Fragment implements PrikazStolova
{
    String name = "Putem slike";

    private OnOdabirStolaCompleteListener onOdabirStolaCompleteListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_slika_stolovi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = view.findViewById(R.id.buttonPotvrdaOdabiraStola);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String odabraniStol=spinner.getSelectedItem().toString();
                if(odabraniStol =="--Odaberite stol--"){
                    Toast.makeText(view.getContext(), "Niste odabrali ni jedan stol", Toast.LENGTH_LONG).show();
                }
                else{
                    final boolean[] slobodan=new boolean[1];
                    ProvjeriDostupnostStola(slobodan, restoran, odabraniStol, dolazak, odlazak, new OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            if (slobodan[0]) {
                                Intent returnDataIntent = getActivity().getIntent();
                                returnDataIntent.putExtra("stolId", odabraniStol);
                                onOdabirStolaCompleteListener.OnOdabirStolaCompleteListener(returnDataIntent);
                            } else {
                                Toast.makeText(getContext(), "Stol nije moguće rezervirati", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void ProvjeriDostupnostStola(final boolean[] slobodan, String restoran, String odabraniStol, final String dolazak, final String odlazak, final OnCompleteListener onCompleteListener) {

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("rezervacija").child(restoran+"_"+odabraniStol);
        databaseReference.orderByChild("odlazak").startAt(dolazak).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        String postojeciDolazak=data.child("dolazak").getValue().toString();
                        if(Long.parseLong(postojeciDolazak)<Long.parseLong(odlazak)){
                            slobodan[0]=false;
                            onCompleteListener.onComplete();
                        }
                        else{
                            slobodan[0]=true;
                            onCompleteListener.onComplete();
                        }
                    }
                }
                else{
                    slobodan[0]=true;
                    onCompleteListener.onComplete();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    @Override
    public void setOnCompleteListener(OnOdabirStolaCompleteListener onCompleteListener) {
        this.onOdabirStolaCompleteListener = onCompleteListener;
    }


}
