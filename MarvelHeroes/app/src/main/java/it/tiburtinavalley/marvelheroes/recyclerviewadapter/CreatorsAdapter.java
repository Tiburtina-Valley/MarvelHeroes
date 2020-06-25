package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class CreatorsAdapter extends RecyclerView.Adapter<CreatorsAdapter.CreatorsHolder> implements View.OnClickListener{
    private List<Creators> creators;
    private ImageApiVolley imgVolley;

    public CreatorsAdapter(List<Creators> creatorsList){
        this.creators = creatorsList;
    }

    @Override
    public void onClick(View v) {
        //TODO Open creator description
    }

    @NonNull
    @Override
    public CreatorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        imgVolley = new ImageApiVolley(parent.getContext());
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.creator_layout, parent, false);
        cl.setOnClickListener(this);
        return new CreatorsHolder(cl);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatorsHolder holder, int position) {
        imgVolley.addHeroImg(holder.ivComic);
        if(creators.get(position).getThumbnail() != null) {
            String urlThumbnail = creators.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + creators.get(position).getThumbnail().getExtension();
            Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivComic);
        }
        holder.tvComicName.setText(creators.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return creators.size();
    }

    class CreatorsHolder extends RecyclerView.ViewHolder {
        final ImageView ivComic;
        final TextView tvComicName;

        CreatorsHolder(@NonNull View itemView) {
            super(itemView);
            ivComic = itemView.findViewById(R.id.ivCreator);
            tvComicName = itemView.findViewById(R.id.tvCreatorName);
        }
    }
}
