package hr.foi.restoranko.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import hr.foi.restoranko.R;
import hr.foi.restoranko.controller.Navigation;
import hr.foi.restoranko.controller.RestaurantDetails;
import hr.foi.restoranko.model.Jelo;
import hr.foi.restoranko.model.Jelovnik;
import hr.foi.restoranko.model.Restoran;

public class OdabirJelovnika extends Fragment {
    private LinearLayout kontejner;

    public OdabirJelovnika() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_odabir_jelovnika, container, false);
        if(getArguments() != null) {
            kontejner = (LinearLayout) v.findViewById(R.id.container_jelovnik);

            long restoranId = this.getArguments().getLong("restoranId");
            DohvatiSveJelovnike(restoranId);
        }

        return v;
    }

    private void DohvatiSveJelovnike(final long restoranId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("jelovnik");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    if((long) datas.child("restoranId").getValue() == restoranId) {
                        long _id = (long) datas.child("jelovnikId").getValue();
                        String _cijena = datas.child("cijena").getValue().toString();
                        String _naziv = datas.child("naziv").getValue().toString();

                        Jelovnik jelovnik = new Jelovnik(_id, _cijena, _naziv);
                        PrikaziJelovnik(jelovnik);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void PrikaziJelovnik(final Jelovnik jelovnik) {
        LayoutInflater li = LayoutInflater.from(getView().getContext());
        View divider = li.inflate(R.layout.jelovnik, null, false);


        TextView nazivJelovnika = (TextView) divider.findViewById(R.id.nazivJelovnika);
        nazivJelovnika.setText(jelovnik.getNaziv());
        TextView cijena = (TextView) divider.findViewById(R.id.cijena);
        cijena.setText(cijena.getText() + " " + jelovnik.getCijena() + " kn");

        //final TextView ispisJela = (TextView) divider.findViewById(R.id.ispisJela);
        //final String[] svaJela = {null};


        kontejner.addView(divider);
/*
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("jeloNaJelovniku");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    if((long) datas.child("jelovnikId").getValue() == jelovnik.getJelovnikId()) {
                        long _id = (long) datas.child("jeloId").getValue();

                        svaJela[0] += Jelo.vratiNazivJela(_id) + "\n";

                        ispisJela.setText(svaJela[0]);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/
    }

}
