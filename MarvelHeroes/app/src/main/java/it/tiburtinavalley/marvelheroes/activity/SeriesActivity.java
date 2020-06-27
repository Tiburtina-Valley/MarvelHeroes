package it.tiburtinavalley.marvelheroes.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.CreatorsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.EventsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroDetailAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.CreatorsVolley;
import it.tiburtinavalley.marvelheroes.volley.EventsVolley;
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
        private TextView tvStartYear;
        private TextView tvEndYear;
        private TextView tvDescription;
        private TextView tvType;
        private TextView tvRating;
        private RecyclerView rvCharacters;
        private RecyclerView rvUrls;
        private HeroDetailAdapter heroAdapter;
        private MarvelApiVolley heroVolley;
        private RecyclerView rvCreators;
        private CreatorsVolley creatorsVolley;
        private CreatorsAdapter creatorsAdapter;
        private ComicsVolley comicsVolley;
        private ComicsAdapter comicsAdapter;
        private EventsVolley eventsVolley;
        private EventsAdapter eventsAdapter;
        private TextView tvComics;
        private RecyclerView rvComics;
        private TextView tvEvents;
        private TextView tvCreators;
        private RecyclerView rvEvents;

        public Holder() {
            ivSeriesImage = findViewById(R.id.ivStoriesmg);
            tvSeriesName = findViewById(R.id.tvCreatorName);
            tvStartYear = findViewById(R.id.tvStartYear);
            tvEndYear = findViewById(R.id.tvEndYear);
            rvUrls = findViewById(R.id.rvUrls);
            tvDescription = findViewById(R.id.tvDescription);
            rvCharacters = findViewById(R.id.rvCharacters);
            tvType = findViewById(R.id.tvType);
            tvRating = findViewById(R.id.tvRating);
            rvCreators = findViewById(R.id.rvCreators);
            tvComics = findViewById(R.id.tvComics);
            rvComics = findViewById(R.id.rvComics);
            tvEvents = findViewById(R.id.tvEvents);
            rvEvents = findViewById(R.id.rvEvents);
            tvCreators = findViewById(R.id.tvCreators);

            final Context appContext = getApplicationContext();

            heroVolley = new MarvelApiVolley(appContext) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    heroAdapter = new HeroDetailAdapter(heroes, appContext);
                    rvCharacters.setAdapter(heroAdapter);
                }
            };

            creatorsVolley = new CreatorsVolley(getApplicationContext()) {
                @Override
                public void fillCreatorsInfo(List<Creators> creatorsList) {
                    creatorsAdapter = new CreatorsAdapter(creatorsList, getApplicationContext());
                    rvCreators.setAdapter(creatorsAdapter);
                    if (creatorsAdapter.getItemCount() == 0) {
                        tvCreators.setTextSize(0);
                        tvCreators.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvCreators.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);
                    }
                }
            };

            eventsVolley = new EventsVolley(getApplicationContext()) {
                @Override
                public void fillEvents(List<Events> eventsList) {
                    eventsAdapter = new EventsAdapter(eventsList, getApplicationContext());
                    rvEvents.setAdapter(eventsAdapter);
                    if (eventsAdapter.getItemCount() == 0) {
                        tvEvents.setTextSize(0);
                        tvEvents.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvEvents.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);
                    }
                }
            };

            comicsVolley = new ComicsVolley(getApplicationContext()) {
                @Override
                public void fillComics(List<Comics> comicsList) {
                    comicsAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvComics.setAdapter(comicsAdapter);
                    if (comicsAdapter.getItemCount() == 0) {
                        tvComics.setTextSize(0);
                        tvComics.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvComics.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);
                    }
                }
            };

            String id = series.getId();
            heroVolley.getHeroesFromSeries(id);
            creatorsVolley.getCreatorsBySeries(id);
            comicsVolley.getComicsBySeries(id);
            eventsVolley.getEventsBySeries(id);
        }

        private void setRecyclerViews(){
            LinearLayoutManager layoutManagerUrls = new LinearLayoutManager(
                    SeriesActivity.this, RecyclerView.VERTICAL, false);
            rvUrls.setLayoutManager(layoutManagerUrls);

            LinearLayoutManager layoutManagerHeroes = new LinearLayoutManager(
                    SeriesActivity.this, RecyclerView.HORIZONTAL, false);
            rvCharacters.setLayoutManager(layoutManagerHeroes);

            LinearLayoutManager layoutManagerCreators = new LinearLayoutManager(
                    SeriesActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreators.setLayoutManager(layoutManagerCreators);

            LinearLayoutManager layoutManagerComics = new LinearLayoutManager(
                    SeriesActivity.this, RecyclerView.HORIZONTAL, false);
            rvComics.setLayoutManager(layoutManagerComics);

            LinearLayoutManager layoutManagerEvents = new LinearLayoutManager(
                    SeriesActivity.this, RecyclerView.HORIZONTAL, false);
            rvEvents.setLayoutManager(layoutManagerEvents);
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
            tvStartYear.setText(getString(R.string.series_start_date) + " " + series.getStartYear());
            tvEndYear.setText(getString(R.string.series_end_date) + " " + series.getEndYear());
            if (!series.getType().equals(""))
                tvType.setText(getString(R.string.type) + " " + series.getType());
            if (!series.getRating().equals(""))
                tvRating.setText(getString(R.string.rating) + " " + series.getRating());

            if (series.getDescription() != null) {
                tvDescription.setText(series.getDescription());
            }
            else {
                tvDescription.setText(R.string.noDescription);
            }
            urlsAdapter = new UrlsRecyclerView(series.getUrls());
            rvUrls.setAdapter(urlsAdapter);
        }
    }
}
