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

import java.util.List;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.CreatorsActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Creators;

/** Adapter per gestire RecyclerView che contengono View per mostare dei creatori */
public class CreatorsAdapter extends RecyclerView.Adapter<CreatorsAdapter.CreatorsHolder> implements View.OnClickListener{
    private List<Creators> creators; // Lista degli eroi che vanno mostrati nella RecyclerView
    private Context appContext; // Context dell'Activity corrente

    public CreatorsAdapter(List<Creators> creatorsList, Context appContext){
        this.creators = creatorsList;
        this.appContext = appContext;
    }

    /** Reagisce al click di un elemento nella RecyclerView */
    @Override
    public void onClick(View v) {
        // Controlla se la connessone ad Internet è presente
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // Acquisisce la posizione dell'elemento selezionato
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);

            // Prende il creator dalla lista in base alla posizione
            Creators creator = creators.get(position);

            // Crea un nuovo Intent e vi inserisce il comic, poi chiama l'activity di dettaglio
            Intent i = new Intent(appContext, CreatorsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("creator", creator);
            appContext.startActivity(i);
        }
        //Mostra un Toast qualora la connessione ad Internet sia assente
        else {
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.msg_internet_required));
        }
    }

    /** Metodo chiamato ogni volta che serve una nuova riga per la RecyclerView */
    @NonNull
    @Override
    public CreatorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_creator, parent, false);     // Carichiamo il layout di dettaglio
        cl.setOnClickListener(this);
        return new CreatorsHolder(cl);      //Ritorna un nuovo Holder, che estende il ViewHolder
    }

    /** Chiamato quando avviene un cambiamento in una View */
    @Override
    public void onBindViewHolder(@NonNull CreatorsHolder holder, int position) {
        if(creators.get(position).getThumbnail() != null) {
            String urlThumbnail = creators.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + creators.get(position).getThumbnail().getExtension();
            Glide.with(holder.itemView).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivComic);
        }
        holder.tvComicName.setText(creators.get(position).getFullName());
    }

    /** Ritorna il numero di elementi nella lista di creators */
    @Override
    public int getItemCount() {
        return creators.size();
    }

    /** Holder che estende la ViewHolder e gestisce una specifica View */
    static class CreatorsHolder extends RecyclerView.ViewHolder {
        final ImageView ivComic;
        final TextView tvComicName;

        CreatorsHolder(@NonNull View itemView) {
            super(itemView);
            ivComic = itemView.findViewById(R.id.ivCreator);
            tvComicName = itemView.findViewById(R.id.tvCreatorName);
        }
    }
}
