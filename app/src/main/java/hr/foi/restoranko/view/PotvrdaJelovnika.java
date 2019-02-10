package hr.foi.restoranko.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.RezerviraniJelovnik;

public class PotvrdaJelovnika extends Fragment {
    View v;

    public PotvrdaJelovnika() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_potvrda_jelovnika, container, false);

        UcitajStavke();
        IzracunajCijenu();

        return v;
    }

    //Metoda pomoću koje se dohvaćaju svi naručeni jelovnici
    private void UcitajStavke() {
        TableLayout tl = (TableLayout) v.findViewById(R.id.jelovnici);

        for(final RezerviraniJelovnik j : RezerviraniJelovnik.listaRezerviranihJela) {
            TableRow tr = new TableRow(v.getContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv1 = new TextView(v.getContext());
            tv1.setText(j.getJelovnik().getNaziv());
            tr.addView(tv1);

            final TextView tv2 = new TextView(v.getContext());
            tv2.setText(String.valueOf(j.getKolicina()));
            tr.addView(tv2);

            final TextView tv3 = new TextView(v.getContext());
            tv3.setText(String.format("%.2f" , Double.valueOf(j.getJelovnik().getCijena()) * j.getKolicina() ));
            tr.addView(tv3);

            Button b1 = new Button(v.getContext());
            b1.setText("+");
            TableRow.LayoutParams lp = new TableRow.LayoutParams(5, 100);
            b1.setLayoutParams(lp);
            tr.addView(b1);

            Button b2 = new Button(v.getContext());
            b2.setText("-");
            b2.setLayoutParams(lp);
            tr.addView(b2);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long kol = j.getKolicina();
                    j.setKolicina(kol+1);
                    tv2.setText(String.valueOf(j.getKolicina()));
                    tv3.setText(String.format("%.2f" , Double.valueOf(j.getJelovnik().getCijena()) * j.getKolicina() ));
                    IzracunajCijenu();
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long kol = j.getKolicina();
                    if(kol != 1) {
                        j.setKolicina(kol-1);
                        tv2.setText(String.valueOf(j.getKolicina()));
                        tv3.setText(String.format("%.2f" , Double.valueOf(j.getJelovnik().getCijena()) * j.getKolicina() ));
                        IzracunajCijenu();
                    }
                }
            });


            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    //Metoda koja izračunava ukupnu cijenu jela
    private void IzracunajCijenu() {
        double ukupnaCijena = 0;
        for(RezerviraniJelovnik rj : RezerviraniJelovnik.listaRezerviranihJela) {
            ukupnaCijena += Double.valueOf(rj.getJelovnik().getCijena()) * rj.getKolicina();
        }

        TextView cijena = (TextView) v.findViewById(R.id.ispisUkupneCijene);
        cijena.setText("Ukupna cijena: " + String.format("%.2f" , ukupnaCijena) + " kn");
    }

}
