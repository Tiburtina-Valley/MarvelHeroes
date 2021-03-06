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
import it.tiburtinavalley.marvelheroes.activity.HeroDetailActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.HeroModel;

/** Classe adapter che gestisce le recycler view dei dettagli dell'eroe*/
public class HeroDetailAdapter extends RecyclerView.Adapter<HeroDetailAdapter.Holder> implements View.OnClickListener {
    private final List<HeroModel> heroes;
    private Context appContext;

    public HeroDetailAdapter(List<HeroModel> all, Context appContext) {
        heroes = new ArrayList<>();
        heroes.addAll(all);
        this.appContext = appContext;
    }

    /** Metodo che crea le righe della recycler view, gonfiando il corrispettivo layout xml.
     * Associa anche un OnClickListener*/
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_detail_layout, parent, false);   // Carichiamo il layout di dettaglio
        cl.setOnClickListener(this);
        return new Holder(cl);      //Ritorna un nuovo Holder, che estende il ViewHolder
    }


    /** Metodo che setta il layout dell'eroe */
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        HeroModel hero = heroes.get(position);
        holder.tvHeroName.setText(hero.getName());
        if (hero.getThumbnail()!=null) {
            String urlThumbnail = hero.getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + hero.getThumbnail().getExtension();
            Glide.with(holder.itemView).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivHeroPic);
        }
    }
    

    @Override
    public int getItemCount() {
        return heroes.size();
    }

    /** Metodo che gestisce il click su un determinato eroe nella recycler view, facendo partire l'activity di dettaglio */
    @Override
    public void onClick(View v) {
        //controllo della connessione
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            //far partire una nuova activity di dettaglio dell'eroe selezionato
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            HeroModel hero = heroes.get(position);      // Prende l'eroe dalla lista in base alla posizione

            // Crea un nuovo Intent, vi inserisce l'eroe e chiama l'activity di dettaglio
            Intent i = new Intent(appContext, HeroDetailActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("hero", hero);
            appContext.startActivity(i);
        }
        else {
            //toast che avverte in caso di mancanza di connessione ad internet
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.msg_internet_required));
        }
    }

    /** Holder che definisce come sono fatte le singole entry della recycler view */
    static class Holder extends RecyclerView.ViewHolder {
        final TextView tvHeroName;
        final ImageView ivHeroPic;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvHeroName = itemView.findViewById(R.id.tvCreatorName);
            ivHeroPic = itemView.findViewById(R.id.ivCreator);
        }
    }
}