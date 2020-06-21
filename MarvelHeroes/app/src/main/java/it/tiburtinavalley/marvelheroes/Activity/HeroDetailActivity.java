package it.tiburtinavalley.marvelheroes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import it.tiburtinavalley.marvelheroes.Model.Comics;
import it.tiburtinavalley.marvelheroes.Model.HeroModel;
import it.tiburtinavalley.marvelheroes.Model.Series;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.Volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.Volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.Volley.SeriesVolley;


public class HeroDetailActivity extends AppCompatActivity {

    private HeroModel hm;
    private ComicsVolley cVolley;
    private SeriesVolley sVolley;
    private ImageApiVolley imgVolley;
    private RecyclerView.LayoutManager layoutManagerInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_detail_layout);
        imgVolley = new ImageApiVolley(getApplicationContext());
        hm = getIntent().getParcelableExtra("hero");
        Holder holder = new Holder();
        holder.setDetails(hm);
    }

    class Holder implements View.OnClickListener{
        private final RecyclerView rvRelatedInfo;
        private ImageView ivHeroPhoto;
        private TextView tvHeroName;
        private TextView tvHeroId;
        private TextView tvHeroDescription;
        private Button btnComics;
        private Button btnSeries;
        private ComicsAdapter cAdapter;
        private SeriesAdapter sAdapter;

        public Holder() {
            rvRelatedInfo = findViewById(R.id.rvRelatedInfo);
            ivHeroPhoto = findViewById(R.id.ivHeroPhoto);
            tvHeroId = findViewById(R.id.tvHeroId);
            tvHeroName = findViewById(R.id.tvHeroName);
            tvHeroDescription = findViewById(R.id.tvHeroDescription);
            btnComics = findViewById(R.id.btnComics);
            btnComics.setOnClickListener(this);
            btnSeries = findViewById(R.id.btnSeries);
            btnSeries.setOnClickListener(this);
            layoutManagerInfos = new LinearLayoutManager(HeroDetailActivity.this);
            rvRelatedInfo.setLayoutManager(layoutManagerInfos);
            cVolley = new ComicsVolley(getApplicationContext()) {

                @Override
                public void fillComics(List<Comics> comicsList) {
                    cAdapter = new ComicsAdapter(comicsList);
                    rvRelatedInfo.setAdapter(cAdapter);
                }
            };
            sVolley = new SeriesVolley(getApplicationContext()) {
                @Override
                public void fillSeries(List<Series> seriesList) {
                    sAdapter = new SeriesAdapter(seriesList);
                    rvRelatedInfo.setAdapter(sAdapter);
                }
            };
        }


        public void setDetails(HeroModel hero) {
            this.tvHeroName.setText(hero.getName());
            this.tvHeroId.setText(hero.getId());
            this.tvHeroDescription.setText(hero.getDescription());
            imgVolley.addHeroImg(this.ivHeroPhoto);
            imgVolley.getImageFromUrl(hero.getThumbnail().getPath()
                    + "." + hero.getThumbnail().getExtension());
            // TODO: fill comics, series and stories
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btnComics){
                cVolley.getComicsInfo(hm.getId());
            }
            else if(view.getId() == R.id.btnSeries){
                sVolley.getStoriesInfo(hm.getId());
            }
        }
    }

    private class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicsHolder> implements View.OnClickListener {
        private List<Comics> comics;

        public ComicsAdapter(List<Comics> all) {
            comics = new ArrayList<>();
            comics.addAll(all);
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
            imgVolley.addHeroImg(holder.ivComic);
            imgVolley.getImageFromUrl(comics.get(position).getThumbnail().getPath()+"."+comics.get(position).getThumbnail().getExtension());
            holder.tvComicName.setText(comics.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return comics.size();
        }

        @Override
        public void onClick(View v) {
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            Comics comic = comics.get(position);

            Intent i = new Intent(getApplicationContext(), ComicsActivity.class);
            i.putExtra("comic", comic);
            startActivity(i);
        }

        class ComicsHolder extends RecyclerView.ViewHolder {
            final ImageView ivComic;
            final TextView tvComicName;

            ComicsHolder(@NonNull View itemView) {
                super(itemView);
                ivComic = itemView.findViewById(R.id.ivComic);
                tvComicName = itemView.findViewById(R.id.tvComicName);
            }
        }
    }

    private class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder> implements View.OnClickListener {
        private List<Series> series;

        public SeriesAdapter(List<Series> all) {
            series = new ArrayList<>();
            series.addAll(all);
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
            imgVolley.addHeroImg(holder.ivComic);
            imgVolley.getImageFromUrl(series.get(position).getThumbnail().getPath()+"."+series.get(position).getThumbnail().getExtension());
            holder.tvComicName.setText(series.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return series.size();
        }

        //
        @Override
        public void onClick(View v) {
            //TODO : display new activity based on user search
        }

        class SeriesHolder extends RecyclerView.ViewHolder {
            final ImageView ivComic;
            final TextView tvComicName;

            SeriesHolder(@NonNull View itemView) {
                super(itemView);
                ivComic = itemView.findViewById(R.id.ivComic);
                tvComicName = itemView.findViewById(R.id.tvComicName);
            }
        }
    }
}
