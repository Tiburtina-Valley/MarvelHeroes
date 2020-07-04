package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Urls;

/* RecyclerView che mostra gli url legati ad un elemento, che può essere un fumetto, una serie o un evento*/

public class UrlsRecyclerView extends RecyclerView.Adapter<UrlsRecyclerView.Holder> implements View.OnClickListener {
    private List<Urls> urls; // Lista degli eroi che vanno mostrati nella RecyclerView
    private Context appContext;



    public UrlsRecyclerView(List<Urls> urlsList){
        urls = urlsList;
    }

    // Metodo chiamato ogni volta che serve una nuova riga per la RecyclerView
    @NonNull
    @Override
    public UrlsRecyclerView.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.url_layout, parent, false);
        return new Holder(cl);
    }

    // Chiamato quando avviene un cambiamento in una View
    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        String type = urls.get(position).getType();
        // Controlla il tipo di url
        if (type.equals(appContext.getString(R.string.btn_check_resource))) {
            type = appContext.getString(R.string.btn_resource);
        }
        else if (type.equals(appContext.getString(R.string.btn_check_purchase))) {
            type = appContext.getString(R.string.btn_purchase);
        }
        else if (type.equals(appContext.getString(R.string.btn_check_wiki))) {
            type = appContext.getString(R.string.btn_wiki);
        }
        else if (type.equals(appContext.getString(R.string.btn_check_reader))) {
            type = appContext.getString(R.string.btn_reader);
        }
        holder.btnType.setText(type);
        holder.btnType.setOnClickListener((View v) -> {
            Urls url = urls.get(position);
            Uri uri = Uri.parse(url.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            appContext.startActivity(intent);
        });
    }

    // Ritorna il numero di elementi nella lista di creators
    @Override
    public int getItemCount() {
        return urls.size();
    }

    // Reagisce al click di un elemento nella RecyclerView
    @Override
    public void onClick(View v) {
        // Controlla se la connessone ad Internet è presente
        ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // Acquisisce la posizione dell'elemento selezionato
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            // Prende il comics dalla lista in base alla posizione
            Urls url = urls.get(position);

            // Apre l'url nel browser
            Uri uri = Uri.parse(url.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            appContext.startActivity(intent);
        }
        //Mostra un Toast qualora la connessione ad Internet sia assente
        else {
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.msg_internet_required));
        }
    }

    // Holder che estende la ViewHolder e gestisce una specifica View
    class Holder extends RecyclerView.ViewHolder {
        private Button btnType;

        public Holder(@NonNull View itemView) {
            super(itemView);
            btnType = itemView.findViewById(R.id.btnType);
            appContext = itemView.getContext();
        }
    }
}
