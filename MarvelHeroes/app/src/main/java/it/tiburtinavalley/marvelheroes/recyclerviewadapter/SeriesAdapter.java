package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.SeriesActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Series;

/** Classe adapter che gestisce le recycler view delle series*/
public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder> implements View.OnClickListener {
    private List<Series> series;
    Context appContext;

    public SeriesAdapter(List<Series> all, Context appContext) {
        series = new ArrayList<>();
        series.addAll(all);
        this.appContext = appContext;
    }

    /** Metodo che crea le righe della recycler view, gonfiando il corrispettivo layout xml.
     * Associa anche un OnClickListener*/
    @NonNull
    @Override
    public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_detail_layout, parent, false);   // Carichiamo il layout di dettaglio
        cl.setOnClickListener(this);
        return new SeriesHolder(cl);    //Ritorna un nuovo Holder, che estende il ViewHolder
    }

    /** Metodo che setta il layout delle series */
    @Override
    public void onBindViewHolder(@NonNull SeriesHolder holder, int position) {
        if(series.get(position).getThumbnail() != null) {
            String urlThumbnail = series.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + series.get(position).getThumbnail().getExtension();
            Glide.with(holder.itemView).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivSeries);
        }
        holder.tvSeriesName.setText(series.get(position).getTitle());
    }

    /** Ritorna il numero di elementi della lista delle serie */
    @Override
    public int getItemCount() {
        return series.size();
    }

    /** Metodo che gestisce il click su una determinata series nella recycler view, facendo partire l'activity di dettaglio */
    @Override
    public void onClick(View v) {
        //controllo della connessione
        ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            //far partire una nuova activity di dettaglio per la serie selezionata
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            Series serie = series.get(position);    // Prende la serie dalla lista in base alla posizione

            // Crea un nuovo Intent, vi inserisce la serie e chiama l'activity di dettaglio
            Intent i = new Intent(appContext, SeriesActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("series", serie);
            appContext.startActivity(i);
        } else {
            //toast che avverte in caso di mancanza di connessione ad internet
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.msg_internet_required));
        }
    }

    /** Holder che definisce come sono fatte le singole entry della recycler view */
    static class SeriesHolder extends RecyclerView.ViewHolder {
        final ImageView ivSeries;
        final TextView tvSeriesName;

        SeriesHolder(@NonNull View itemView) {
            super(itemView);
            ivSeries = itemView.findViewById(R.id.ivCreator);
            tvSeriesName = itemView.findViewById(R.id.tvCreatorName);
        }
    }
}
