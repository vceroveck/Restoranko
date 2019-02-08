package hr.foi.restoranko.view;

import android.view.View;
import android.widget.Button;

public class StoloviRecyclerAdapterItem {
    private Button stol;
    private OnOdabirStolaCompleteListener onOdabirStolaCompleteListener;

    public StoloviRecyclerAdapterItem(Button stol, OnOdabirStolaCompleteListener onOdabirStolaCompleteListener){
        this.stol = stol;
        this.onOdabirStolaCompleteListener=onOdabirStolaCompleteListener;
    }

    public Button getStol(){
        return stol;
    }

    public OnOdabirStolaCompleteListener getOnOdabirStolaCompleteListener() {
        return onOdabirStolaCompleteListener;
    }
}
