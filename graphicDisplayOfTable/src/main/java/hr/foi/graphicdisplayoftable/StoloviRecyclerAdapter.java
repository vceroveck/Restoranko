package hr.foi.graphicdisplayoftable;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.ArrayList;


public class StoloviRecyclerAdapter extends RecyclerView.Adapter<StoloviRecyclerAdapter.StoloviViewHolder> {
    private ArrayList<StoloviRecyclerAdapterItem> mListaStolovi;
    View.OnClickListener onClickListener;

    public static class StoloviViewHolder extends RecyclerView.ViewHolder{
        public Button stol;
        public StoloviViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            stol = itemView.findViewById(R.id.textViewStol);
            stol.setOnClickListener(onClickListener);
        }
    }

    public StoloviRecyclerAdapter(ArrayList<StoloviRecyclerAdapterItem> listaStolova){
        this.mListaStolovi = listaStolova;
    }

    @NonNull
    @Override
    public StoloviViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_stolovi, parent, false);
        StoloviViewHolder stoloviViewHolder = new StoloviViewHolder(view, this.onClickListener);
        return stoloviViewHolder;
    }

    @Override
    public void onBindViewHolder(final StoloviViewHolder holder, int position){
        final StoloviRecyclerAdapterItem currentItem=mListaStolovi.get(position);
        holder.stol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.getOnOdabirStolaCompleteListener().OnOdabirStolaCompleteListener(currentItem.getStol().getTag().toString());
            }
        });
        holder.stol.setText(currentItem.getStol().getText());
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
