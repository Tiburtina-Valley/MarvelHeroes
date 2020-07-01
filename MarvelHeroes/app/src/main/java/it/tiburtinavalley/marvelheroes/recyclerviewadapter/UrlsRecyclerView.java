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
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        String type = urls.get(position).getType();

        holder.btnType.setText(type.substring(0, 1).toUpperCase() + type.substring(1));
        holder.btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Urls url = urls.get(position);
                Uri uri = Uri.parse(url.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                appContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            Urls url = urls.get(position);
            Uri uri = Uri.parse(url.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            appContext.startActivity(intent);
        }
        else {
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.msg_internet_required));
        }
    }


    class Holder extends RecyclerView.ViewHolder {
        private Button btnType;

        public Holder(@NonNull View itemView) {
            super(itemView);
            btnType = itemView.findViewById(R.id.btnType);
            appContext = itemView.getContext();
        }
    }



}
