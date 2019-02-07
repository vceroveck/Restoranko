package hr.foi.restoranko.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Recenzija;

public class ListaRecenzija extends ArrayAdapter<Recenzija> {

    private Activity context;
    private List<Recenzija> recenzijaLista;

    public ListaRecenzija(Activity context, List<Recenzija> recenzijaLista){
        super(context, R.layout.recenzije_lista, recenzijaLista);
        this.context = context;
        this.recenzijaLista=recenzijaLista;
    }

    @NotNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.recenzije_lista, null, true);

        TextView txtNazivRestorana = (TextView) listViewItem.findViewById(R.id.txtNazivRestorana);
        TextView txtIspisLjestvice = (TextView) listViewItem.findViewById(R.id.txtIspisLjestvice);
        TextView txtIspisInformacije = (TextView) listViewItem.findViewById(R.id.txtIspisInformacije);

        Recenzija recenzija = recenzijaLista.get(position);

        txtNazivRestorana.setText(recenzija.getNazivRestorana());
        txtIspisLjestvice.setText(recenzija.getRecenzijaLjestvica());
        txtIspisInformacije.setText(recenzija.getRecenzijaPovratnaInfo());

        return listViewItem;
    }
}
