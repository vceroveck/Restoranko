package hr.foi.restoranko.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import hr.foi.restoranko.R;
import hr.foi.restoranko.controller.PrikazStolova;

public class PrikazStolovaListaFragment extends Fragment implements PrikazStolova {
    private String name="Putem liste";
    private OnOdabirStolaCompleteListener onOdabirStolaCompleteListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_lista_stolovi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setOnCompleteListener(OnOdabirStolaCompleteListener onCompleteListener) {
        this.onOdabirStolaCompleteListener=onCompleteListener;
    }
}
