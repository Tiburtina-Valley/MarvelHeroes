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
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.CreatorsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.EventsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.CreatorsVolley;
import it.tiburtinavalley.marvelheroes.volley.EventsVolley;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;


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
        private RecyclerView rvCreatorsComics;
        private HeroAdapter heroAdapter;
        private EventsAdapter eventsAdapter;
        private CreatorsAdapter creatorsAdapter;
        private MarvelApiVolley heroVolley;
        private EventsVolley eventsVolley;
        private CreatorsVolley creatorsVolley;

        public Holder() {
            ivComicImage = findViewById(R.id.ivStoriesmg);
            tvComicName = findViewById(R.id.tvStoriesName);
            tvPageCount = findViewById(R.id.tvPageCount);
            rvUrls = findViewById(R.id.rvUrls);
            rvHeroesComics = findViewById(R.id.rvHeroComics);
            rvEventsComics = findViewById(R.id.rvEventsComics);
            rvCreatorsComics = findViewById(R.id.rvCreatorComics);
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

            creatorsVolley = new CreatorsVolley(getApplicationContext()) {
                @Override
                public void fillCreatorsInfo(List<Creators> creatorsList) {
                    creatorsAdapter = new CreatorsAdapter(creatorsList, getApplicationContext());
                    rvCreatorsComics.setAdapter(creatorsAdapter);
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

            LinearLayoutManager layoutManagerCreators = new LinearLayoutManager(
                    ComicsActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreatorsComics.setLayoutManager(layoutManagerCreators);
        }

        private void setData() {
            ImageApiVolley imgVolley = new ImageApiVolley(getApplicationContext());
            imgVolley.addHeroImg(ivComicImage);
            if (comic.getImages() != null || comic.getImages().size() > 0) {
                String urlThumbnail = comic.getThumbnail().getPath().replaceFirst("http", "https")
                        + "." + comic.getThumbnail().getExtension();
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
            creatorsVolley.getCreatorsByComics(id);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
