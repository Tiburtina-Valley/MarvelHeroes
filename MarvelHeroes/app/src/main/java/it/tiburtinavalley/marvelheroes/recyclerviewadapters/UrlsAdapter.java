package it.tiburtinavalley.marvelheroes.recyclerviewadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Urls;

/* RecyclerView che mostra gli url legati ad un elemento, che può essere un fumetto, una serie o un evento*/

public class UrlsAdapter extends RecyclerView.Adapter<UrlsAdapter.Holder> {
    private List<Urls> urls;

    public UrlsAdapter(List<Urls> urlsList){
        urls = urlsList;
    }

    @NonNull
    @Override
    public UrlsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.url_layout, parent, false);
        return new Holder(cl);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tvType.setText("Type : "+urls.get(position).getType());
        holder.tvUrl.setText(urls.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView tvUrl;
        private TextView tvType;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvUrl = itemView.findViewById(R.id.tvUrl);
            tvType = itemView.findViewById(R.id.tvType);
        }
    }
}
