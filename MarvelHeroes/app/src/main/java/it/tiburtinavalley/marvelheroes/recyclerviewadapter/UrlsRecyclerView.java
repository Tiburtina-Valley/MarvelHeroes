package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class UrlsRecyclerView extends RecyclerView.Adapter<UrlsRecyclerView.Holder> implements View.OnClickListener {
    private List<Urls> urls;
    private Context appContext;




    public UrlsRecyclerView(List<Urls> urlsList){
        urls = urlsList;
    }

    @NonNull
    @Override
    public UrlsRecyclerView.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    @Override
    public void onClick(View v) {
        int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
        Urls url=urls.get(position);
        Uri uri = Uri.parse(url.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        appContext.startActivity(intent);}


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
