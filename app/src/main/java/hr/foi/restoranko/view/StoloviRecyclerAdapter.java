package hr.foi.restoranko.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.ArrayList;

import hr.foi.graphicdisplayoftable.Stol;
import hr.foi.restoranko.R;

public class StoloviRecyclerAdapter extends RecyclerView.Adapter<StoloviRecyclerAdapter.StoloviViewHolder> {
    private ArrayList<StoloviRecyclerAdapterItem> mListaStolovi;

    public static class StoloviViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public StoloviViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewStol);
        }
    }

    public StoloviRecyclerAdapter(ArrayList<StoloviRecyclerAdapterItem> listaStolova){
        this.mListaStolovi = listaStolova;
    }

    @NonNull
    @Override
    public StoloviViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_stolovi, parent, false);
        StoloviViewHolder stoloviViewHolder = new StoloviViewHolder(view);
        return stoloviViewHolder;
    }

    @Override
    public void onBindViewHolder(StoloviViewHolder holder, int position){
        StoloviRecyclerAdapterItem currentItem=mListaStolovi.get(position);

        holder.textView.setText(currentItem.getStol());
        Log.i("stolovic", "jey");
    }

    @Override
    public int getItemCount() {
        if(mListaStolovi!=null){
            return mListaStolovi.size();
        }
        else {
            return 0;
        }
    }

}
