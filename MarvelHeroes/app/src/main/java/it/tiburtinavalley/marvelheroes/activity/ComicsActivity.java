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

import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.EventsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.EventsVolley;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;

public class ComicsActivity extends AppCompatActivity {
    private Comics comic;
    private UrlsRecyclerView urlsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_layout);
        comic = getIntent().getParcelableExtra("comic");
        Holder holder = new Holder();
    }

    class Holder {
        private ImageView ivComicImage;
        private TextView tvComicName;
        private TextView tvPageCount;
        private RecyclerView rvUrls;
        private RecyclerView rvHeroesComics;
        private RecyclerView rvEventsComics;
        private HeroAdapter heroAdapter;
        private SeriesAdapter seriesAdapter;
        private EventsAdapter eventsAdapter;
        private MarvelApiVolley heroVolley;
        private EventsVolley eventsVolley;

        public Holder() {
            ivComicImage = findViewById(R.id.ivStoriesmg);
            tvComicName = findViewById(R.id.tvStoriesName);
            tvPageCount = findViewById(R.id.tvPageCount);
            rvUrls = findViewById(R.id.rvUrls);
            rvHeroesComics = findViewById(R.id.rvHeroComics);
            rvEventsComics = findViewById(R.id.rvEventsComics);
            final Context appContext = getApplicationContext();

            heroVolley = new MarvelApiVolley(appContext) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    heroAdapter = new HeroAdapter(heroes, appContext);
                    rvHeroesComics.setAdapter(heroAdapter);
                }
            };

            eventsVolley = new EventsVolley(appContext) {
                @Override
                public void fillEvents(List<Events> eventsList) {
                    eventsAdapter = new EventsAdapter(eventsList, appContext);
                    rvEventsComics.setAdapter(eventsAdapter);
                }
            };

            setRecyclerViews();
            setData();
        }

        // funzione che si occupa di settare i LayoutManager per le RecyclerView
        private void setRecyclerViews(){
            LinearLayoutManager layoutManagerUrls = new LinearLayoutManager(ComicsActivity.this);
            rvUrls.setLayoutManager(layoutManagerUrls);

            LinearLayoutManager layoutManagerHeroes = new LinearLayoutManager(
                    ComicsActivity.this, RecyclerView.HORIZONTAL, false);
            rvHeroesComics.setLayoutManager(layoutManagerHeroes);

            LinearLayoutManager layoutManagerEvents = new LinearLayoutManager(
                    ComicsActivity.this, RecyclerView.HORIZONTAL, false);
            rvEventsComics.setLayoutManager(layoutManagerEvents);
        }

        private void setData() {
            ImageApiVolley imgVolley = new ImageApiVolley(getApplicationContext());
            imgVolley.addHeroImg(ivComicImage);
            if (comic.getImages() != null || comic.getImages().size() > 0) {
                String urlThumbnail = comic.getImages().get(0).getPath().replaceFirst("http", "https")
                        + "." + comic.getImages().get(0).getExtension();
                Glide.with(getApplicationContext()).load(urlThumbnail).into(this.ivComicImage);
            }
            tvComicName.setText(comic.getTitle());
            tvPageCount.setText(comic.getPageCount());
            urlsAdapter = new UrlsRecyclerView(comic.getUrls());
            rvUrls.setAdapter(urlsAdapter);

            // Riempie le RecyclerView
            String id = comic.getId();
            heroVolley.getHeroesFromComics(id);
            eventsVolley.getEventsFromComics(id);
        }
    }
}
