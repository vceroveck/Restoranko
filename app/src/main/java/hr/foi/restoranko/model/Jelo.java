package hr.foi.restoranko.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Jelo {
    public long jeloId;
    public String nazivJela;
    public static List<Jelo> listaSvihJela = new ArrayList<>();

    public Jelo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("jelo");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                        long _id = (long) datas.child("jeloId").getValue();
                        String _nazivJela = (String) datas.child("nazivJela").getValue();

                        Jelo novo = new Jelo(_id, _nazivJela);
                        listaSvihJela.add(novo);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public Jelo(long jeloId, String nazivJela) {
        this.jeloId = jeloId;
        this.nazivJela = nazivJela;
    }

    public static String vratiNazivJela(long _id) {

        for(Jelo j : listaSvihJela){
            if(j.jeloId == _id) return j.nazivJela;
        }
        return null;

    }
}
