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

import java.util.ArrayList;
import java.util.List;

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.StoriesActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Stories;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesHolder> implements View.OnClickListener {
    private List<Stories> stories;
    private Context appContext; //servirà per poter lanciare con successo la nuova activity nella onClick

    public StoriesAdapter(List<Stories> all, Context appContext) {
        stories = new ArrayList<>();
        stories.addAll(all);
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public StoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.story_layout, parent, false);
        cl.setOnClickListener(this);
        return new StoriesHolder(cl);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesHolder holder, int position) {
        holder.tvStoryName.setText(stories.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            Stories story = stories.get(position);

            Intent i = new Intent(appContext, StoriesActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("story", story);
            appContext.startActivity(i);
        }
        else {
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.internet_required));
        }
    }

    class StoriesHolder extends RecyclerView.ViewHolder {
        final TextView tvStoryName;

        StoriesHolder(@NonNull View itemView) {
            super(itemView);
            tvStoryName = itemView.findViewById(R.id.tvTitle);
        }
    }
}
