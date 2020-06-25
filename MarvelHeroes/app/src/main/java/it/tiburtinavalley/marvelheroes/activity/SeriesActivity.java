package it.tiburtinavalley.marvelheroes.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;

public class SeriesActivity extends AppCompatActivity {
    private Series series;
    private UrlsRecyclerView urlsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.series_layout);
        series = getIntent().getParcelableExtra("series");
        Holder holder = new Holder();
        holder.setRecyclerViews();
        holder.setData();
    }

    class Holder {
        private ImageView ivSeriesImage;
        private TextView tvSeriesName;
        private TextView tvPageCount;
        private TextView tvDescription;
        private RecyclerView rvCharacters;
        private RecyclerView rvUrls;
        private HeroAdapter heroAdapter;
        private MarvelApiVolley heroVolley;

        public Holder() {
            ivSeriesImage = findViewById(R.id.ivStoriesmg);
            tvSeriesName = findViewById(R.id.tvStoriesName);
            tvPageCount = findViewById(R.id.tvPageCount);
            rvUrls = findViewById(R.id.rvUrls);
            tvDescription = findViewById(R.id.tvDescription);
            rvCharacters = findViewById(R.id.rvCharacters);
            final Context appContext = getApplicationContext();

            heroVolley = new MarvelApiVolley(appContext) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    heroAdapter = new HeroAdapter(heroes, appContext);
                    rvCharacters.setAdapter(heroAdapter);
                }
            };
        }

        private void setRecyclerViews(){
            LinearLayoutManager layoutManagerUrls = new LinearLayoutManager(SeriesActivity.this, RecyclerView.VERTICAL, false);
            rvUrls.setLayoutManager(layoutManagerUrls);

            LinearLayoutManager layoutManagerHeroes = new LinearLayoutManager(
                    SeriesActivity.this, RecyclerView.HORIZONTAL, false);
            rvCharacters.setLayoutManager(layoutManagerHeroes);
        }

        private void setData() {
            ImageApiVolley imgVolley = new ImageApiVolley(getApplicationContext());
            imgVolley.addHeroImg(ivSeriesImage);
            if (series.getThumbnail() != null) {
                String urlThumbnail = series.getThumbnail().getPath().replaceFirst("http", "https")
                        + "." + series.getThumbnail().getExtension();
                Glide.with(getApplicationContext()).load(urlThumbnail).into(this.ivSeriesImage);
            }
            tvSeriesName.setText(series.getTitle());
            tvPageCount.setText(series.getPageCount());
            if (series.getDescription() != null) {
                tvDescription.setText(series.getDescription());
            }
            urlsAdapter = new UrlsRecyclerView(series.getUrls());
            rvUrls.setAdapter(urlsAdapter);

            String id = series.getId();
            heroVolley.getHeroesFromComics(id);
        }
    }
}
