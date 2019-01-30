package hr.foi.restoranko.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Jelo {
    private long jeloId;
    private String nazivJela;
    private static List<Jelo> listaSvihJela;

    public Jelo(){
        listaSvihJela = new List<Jelo>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@androidx.annotation.Nullable Object o) {
                return false;
            }

            @androidx.annotation.NonNull
            @Override
            public Iterator<Jelo> iterator() {
                return null;
            }

            @androidx.annotation.Nullable
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(@androidx.annotation.Nullable T[] a) {
                return null;
            }

            @Override
            public boolean add(Jelo jelo) {
                return false;
            }

            @Override
            public boolean remove(@androidx.annotation.Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@androidx.annotation.NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@androidx.annotation.NonNull Collection<? extends Jelo> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @androidx.annotation.NonNull Collection<? extends Jelo> c) {
                return false;
            }

            @Override
            public boolean removeAll(@androidx.annotation.NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@androidx.annotation.NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean equals(@androidx.annotation.Nullable Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public Jelo get(int index) {
                return null;
            }

            @Override
            public Jelo set(int index, Jelo element) {
                return null;
            }

            @Override
            public void add(int index, Jelo element) {

            }

            @Override
            public Jelo remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@androidx.annotation.Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@androidx.annotation.Nullable Object o) {
                return 0;
            }

            @androidx.annotation.NonNull
            @Override
            public ListIterator<Jelo> listIterator() {
                return null;
            }

            @androidx.annotation.NonNull
            @Override
            public ListIterator<Jelo> listIterator(int index) {
                return null;
            }

            @androidx.annotation.NonNull
            @Override
            public List<Jelo> subList(int fromIndex, int toIndex) {
                return null;
            }
        };


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

        for(Jelo j:listaSvihJela){
            if(j.jeloId == _id) return j.nazivJela;
        }
        return null;

    }
}
