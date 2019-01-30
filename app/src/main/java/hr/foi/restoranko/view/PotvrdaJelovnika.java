package hr.foi.restoranko.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.foi.restoranko.R;

public class PotvrdaJelovnika extends Fragment {

    public PotvrdaJelovnika() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_potvrda_jelovnika, container, false);
    }

}
