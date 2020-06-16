package it.tiburtinavalley.marvelheroes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import it.tiburtinavalley.marvelheroes.Model.HeroModel;
import it.tiburtinavalley.marvelheroes.Model.Items;


public class HeroDetailActivity extends AppCompatActivity {

    private HeroModel hm;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_detail_layout);
        hm = getIntent().getParcelableExtra("hero");
        Holder holder = new Holder();
        holder.setDetails(hm);
    }

    class Holder{
        private ImageView ivHeroPhoto;
        private TextView tvHeroName;
        private TextView tvHeroId;
        private TextView tvHeroDescription;
        private VolleyClass vMarvel;
        final RecyclerView rvComics;
        final RecyclerView rvSeries;
        final RecyclerView rvStories;

        public Holder(){
            rvComics = findViewById(R.id.rvComics);
            rvSeries = findViewById(R.id.rvSeries);
            rvStories = findViewById(R.id.rvStories);
            ivHeroPhoto = findViewById(R.id.ivHeroPhoto);
            tvHeroId = findViewById(R.id.tvHeroId);
            tvHeroName = findViewById(R.id.tvHeroName);
            tvHeroDescription = findViewById(R.id.tvHeroDescription);
            vMarvel = new VolleyClass(getApplicationContext()) {
                @Override
                void fillList(List<HeroModel> heroes) {
                    Log.w("fine", "dummy log");
                }
            };
            fillRecView();
        }

        private void fillRecView(){
            RecyclerView.LayoutManager layoutManagerComics = new LinearLayoutManager(HeroDetailActivity.this);
            rvComics.setLayoutManager(layoutManagerComics);
            ComicsAdapter cAdapter = new ComicsAdapter(hm.getComics().getItems());
            rvComics.setAdapter(cAdapter);

            RecyclerView.LayoutManager layoutManagerSeries = new LinearLayoutManager(HeroDetailActivity.this);
            rvSeries.setLayoutManager(layoutManagerSeries);
            SeriesAdapter sAdapter = new SeriesAdapter(hm.getSeries().getItems());
            rvSeries.setAdapter(sAdapter);

            RecyclerView.LayoutManager layoutManagerStories = new LinearLayoutManager(HeroDetailActivity.this);
            rvStories.setLayoutManager(layoutManagerStories);
            StoriesAdapter stAdapter = new StoriesAdapter(hm.getStories().getItems());
            rvStories.setAdapter(stAdapter);
        }

        public void setDetails(HeroModel hm){
            this.tvHeroName.setText(hm.getName());
            this.tvHeroId.setText(hm.getId());
            this.tvHeroDescription.setText(hm.getDescription());
            vMarvel.setHeroesImg(this.ivHeroPhoto);
            vMarvel.imgCall(hm.getThumbnail().getPath()+"."+hm.getThumbnail().getExtension());
        }
    }

    private class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicsHolder> implements View.OnClickListener {
        private final List<Items> items;
        ComicsAdapter(List<Items> all) {
            items = new ArrayList<>();
            items.addAll(all);
        }

        @NonNull
        @Override
        public ComicsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ConstraintLayout cl;
            cl = (ConstraintLayout) LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_detail_layout, parent, false);
            cl.setOnClickListener(this);
            return new ComicsHolder(cl);
        }

        // This method sets the layout of the hero
        @Override
        public void onBindViewHolder(@NonNull ComicsHolder holder, int position) {
            holder.tvResUri.setText(items.get(position).getResourceURI());
            holder.tvName.setText(items.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        //
        @Override
        public void onClick(View v) {
            //this method will get
        }

        class ComicsHolder extends RecyclerView.ViewHolder {
            final TextView tvResUri;
            final TextView tvName;

            ComicsHolder(@NonNull View itemView) {
                super(itemView);
                tvResUri = itemView.findViewById(R.id.tvResUri);
                tvName = itemView.findViewById(R.id.tvName);
            }
        }
    }

    private class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder> implements View.OnClickListener {
        private final List<Items> items;
        SeriesAdapter(List<Items> all) {
            items = new ArrayList<>();
            items.addAll(all);
        }

        @NonNull
        @Override
        public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ConstraintLayout cl;
            cl = (ConstraintLayout) LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_detail_layout, parent, false);
            cl.setOnClickListener(this);
            return new SeriesHolder(cl);
        }

        // This method sets the layout of the hero
        @Override
        public void onBindViewHolder(@NonNull SeriesHolder holder, int position) {
            holder.tvResUri.setText(items.get(position).getResourceURI());
            holder.tvName.setText(items.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        //
        @Override
        public void onClick(View v) {
            //this method will get
        }

        class SeriesHolder extends RecyclerView.ViewHolder {
            final TextView tvResUri;
            final TextView tvName;

            SeriesHolder(@NonNull View itemView) {
                super(itemView);
                tvResUri = itemView.findViewById(R.id.tvResUri);
                tvName = itemView.findViewById(R.id.tvName);
            }
        }
    }

    private class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesHolder> implements View.OnClickListener {
        private final List<Items> items;
        StoriesAdapter(List<Items> all) {
            items = new ArrayList<>();
            items.addAll(all);
        }

        @NonNull
        @Override
        public StoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ConstraintLayout cl;
            cl = (ConstraintLayout) LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_detail_layout, parent, false);
            cl.setOnClickListener(this);
            return new StoriesHolder(cl);
        }

        // This method sets the layout of the hero
        @Override
        public void onBindViewHolder(@NonNull StoriesHolder holder, int position) {
            holder.tvResUri.setText(items.get(position).getResourceURI());
            holder.tvName.setText(items.get(position).getName());
            holder.tvType.setText(items.get(position).getType());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        //
        @Override
        public void onClick(View v) {
            //this method will get
        }

        class StoriesHolder extends RecyclerView.ViewHolder {
            final TextView tvResUri;
            final TextView tvName;
            final TextView tvType;

            StoriesHolder(@NonNull View itemView) {
                super(itemView);
                tvResUri = itemView.findViewById(R.id.tvResUri);
                tvName = itemView.findViewById(R.id.tvName);
                tvType = itemView.findViewById(R.id.tvType);
            }
        }
    }
}
