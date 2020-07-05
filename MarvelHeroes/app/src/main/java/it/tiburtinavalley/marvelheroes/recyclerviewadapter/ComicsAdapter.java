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
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.ComicsActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Comics;

// Adapter per gestire RecyclerView che contengono View per mostare dei fumetti

public class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicsHolder> implements View.OnClickListener {
    private List<Comics> comics; // Lista dei fumetti che vanno mostrati nella RecyclerView
    Context appContext; // Context dell'Activity corrente

    public ComicsAdapter(List<Comics> all, Context appContext) {
        comics = new ArrayList<>();
        comics.addAll(all);
        this.appContext = appContext;
    }

    // Metodo chiamato ogni volta che serve una nuova riga per la RecyclerView
    @NonNull
    @Override
    public ComicsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_detail_layout, parent, false); // Carichiamo il layout di dettaglio
        cl.setOnClickListener(this);
        return new ComicsHolder(cl); // Ritorna un nuovo Holder, che estende il ViewHolder
    }

    // Chiamato quando avviene un cambiamento in una View
    @Override
    public void onBindViewHolder(@NonNull ComicsHolder holder, int position) {
        String urlThumbnail = comics.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                + "." + comics.get(position).getThumbnail().getExtension();
        Glide.with(holder.itemView).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivComic);
        holder.tvComicName.setText(comics.get(position).getTitle());
    }

    // Ritorna il numero di elementi nella lista di comics
    @Override
    public int getItemCount() {
        return comics.size();
    }

    // Reagisce al click di un elemento nella RecyclerView
    @Override
    public void onClick(View v) {

        // Controlla se la connessone ad Internet è presente
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // Acquisisce la posizione dell'elemento selezionato
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            // Prende il comics dalla lista in base alla posizione
            Comics comic = comics.get(position);

            // Crea un nuovo Intent e vi inserisce il comic, poi chiama l'activity di dettaglio
            Intent i = new Intent(appContext, ComicsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("comic", comic);
            appContext.startActivity(i);
        }

        //Mostra un Toast qualora la connessione ad Internet sia assente
        else {
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.msg_internet_required));
        }
    }

    // Holder che estende la ViewHolder e gestisce una specifica View
    static class ComicsHolder extends RecyclerView.ViewHolder {
        final ImageView ivComic;
        final TextView tvComicName;

        ComicsHolder(@NonNull View itemView) {
            super(itemView);
            ivComic = itemView.findViewById(R.id.ivCreator);
            tvComicName = itemView.findViewById(R.id.tvCreatorName);
        }
    }
}
