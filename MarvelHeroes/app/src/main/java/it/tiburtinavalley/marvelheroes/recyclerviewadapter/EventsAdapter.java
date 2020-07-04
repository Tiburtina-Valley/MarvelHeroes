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
import it.tiburtinavalley.marvelheroes.activity.EventsActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Events;
//Definisco l'adapter che gestirà le recycler view degli eventi

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsHolder> implements View.OnClickListener {
    private List<Events> events;
    private Context appContext;


    public EventsAdapter(List<Events> all, Context appContext) {
        events = new ArrayList<>();
        events.addAll(all);
        this.appContext = appContext;
    }

    //Funzione che crea le righe della recycler view
    @NonNull
    @Override
    public EventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_detail_layout, parent, false);
        cl.setOnClickListener(this);
        return new EventsHolder(cl);
    }

    //Funzione che popola le righe della recycler view
    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.EventsHolder holder, int position) {
        String urlThumbnail = events.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                + "." + events.get(position).getThumbnail().getExtension();
        Glide.with(holder.itemView).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivEvent);
        holder.tvEventName.setText(events.get(position).getTitle());
    }

    //Funzione che restituisce la dimensione della lista degli eventi
    @Override
    public int getItemCount() {
        return events.size();
    }



    //Funzione che gestisce il click su un determinato evento nella recycler view, facendo partire correttamente l'activity di dettaglio
    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            Events event = events.get(position);
            Intent i = new Intent(appContext, EventsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("event", event);
            appContext.startActivity(i);
        }
        else {
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.msg_internet_required));
        }
    }

    //Holder che definisce come sono fatte le singole righe della recycler view(collegando lo specifico layout)
    static class EventsHolder extends RecyclerView.ViewHolder {
        final ImageView ivEvent;
        final TextView tvEventName;

        EventsHolder(@NonNull View itemView) {
            super(itemView);
            ivEvent = itemView.findViewById(R.id.ivCreator);
            tvEventName = itemView.findViewById(R.id.tvCreatorName);
        }
    }
}
