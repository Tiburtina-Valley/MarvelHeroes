package it.tiburtinavalley.marvelheroes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Objects;

import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.CreatorsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroDetailAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.CreatorsVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;

public class EventsActivity extends AppCompatActivity {
    private Events event;

    /**Creo l'activity, prelevando dal bundle i dati dell'evento selezionato,e impostando il layout.*/
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_close);

        setContentView(R.layout.activity_events);
        event = getIntent().getParcelableExtra("event");
        new Holder();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Context appContext = getApplicationContext();
        Intent i = new Intent(appContext, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        appContext.startActivity(i);
        return true;
    }

    /**Definisco l'holder con tutti gli elementi della view.*/
    class Holder {
        private ImageView ivEventImage;
        private TextView tvEventName;
        private TextView startDate;
        private TextView endDate;
        private TextView description;
        private RecyclerView rvUrls;
        private RecyclerView rvCreators;
        private RecyclerView rvHeroes;
        private RecyclerView rvComics;
        private RecyclerView rvSeries;
        private MarvelApiVolley heroVolley;
        private CreatorsVolley creatorsVolley;
        private ComicsVolley comicsVolley;
        private SeriesVolley seriesVolley;
        private HeroDetailAdapter heroAdapter;
        private ComicsAdapter comicsAdapter;
        private SeriesAdapter seriesAdapter;
        private CreatorsAdapter creatorsAdapter;
        private TextView tvSeries;
        private TextView tvCreators;
        private TextView tvComics;
        private TextView tvHeroes;
        private ProgressBar loading;
        private ConstraintLayout layout;

        private int loading_count = 0; //contatore per capire quando nascondere la progress bar e mostrare la schermata

        /**Inizializzo l'holder collegando gli attributi java all'xml.*/
        public Holder() {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.label_event_detail));


            ivEventImage = findViewById(R.id.ivEventImg);
            tvEventName = findViewById(R.id.tvEventTitle);
            startDate = findViewById(R.id.tvEventStartDate);
            endDate = findViewById(R.id.tvEventEndDate);
            description = findViewById(R.id.tvEventDescription);
            rvUrls = findViewById(R.id.rvUrlsEvent);
            rvCreators = findViewById(R.id.rvEventCreators);
            rvHeroes = findViewById(R.id.rvEventHeroes);
            rvComics = findViewById(R.id.rvEventComics);
            rvSeries = findViewById(R.id.rvEventSeries);
            tvSeries = findViewById(R.id.ivSeries);
            tvCreators = findViewById(R.id.tvCreators);
            tvComics = findViewById(R.id.tvComics);
            tvHeroes = findViewById(R.id.tvHeroes);
            loading = findViewById(R.id.progress_loader);
            layout = findViewById(R.id.layout);

            //Setto le recyclers views.
            setRecyclerViews();

            //Creo una volley per gestire le query degli eroi.
            heroVolley = new MarvelApiVolley(getApplicationContext()) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    heroAdapter = new HeroDetailAdapter(heroes, getApplicationContext());
                    rvHeroes.setAdapter(heroAdapter);
                    if (heroAdapter.getItemCount() == 0) {
                        tvHeroes.setTextSize(0);
                        tvHeroes.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvHeroes.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);
                    }
                    loading_count++;
                    dismissLoading();
                }
            };


            /**Creo una volley per gestire le query dei creators.*/
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
                    loading_count++;
                    dismissLoading();
                }
            };

            /**Creo una volley per gestire le query dei comics.*/
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
                    loading_count++;
                    dismissLoading();
                }
            };


            /**Creo una volley per gestire le query delle serie.*/
            seriesVolley = new SeriesVolley(getApplicationContext()) {
                @Override
                public void fillSeries(List<Series> seriesList) {
                    seriesAdapter = new SeriesAdapter(seriesList, getApplicationContext());
                    rvSeries.setAdapter(seriesAdapter);
                    if (seriesAdapter.getItemCount() == 0) {
                        tvSeries.setTextSize(0);
                        tvSeries.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvSeries.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);
                    }
                    loading_count++;
                    dismissLoading();
                }
            };
            //Setto tutti gli elementi della view.
            setData();
        }


        /**Definisco tutti i layout manager delle recyclers view,collegandole anche ai rispettivi adapter.*/
        private void setRecyclerViews() {
            LinearLayoutManager layoutManagerHeroes = new LinearLayoutManager(
                    EventsActivity.this, RecyclerView.HORIZONTAL, false);
            rvHeroes.setLayoutManager(layoutManagerHeroes);

            LinearLayoutManager layoutManagerUrls = new LinearLayoutManager(
                    EventsActivity.this, RecyclerView.HORIZONTAL, false);
            rvUrls.setLayoutManager(layoutManagerUrls);

            LinearLayoutManager layoutManagerCreators = new LinearLayoutManager(
                    EventsActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreators.setLayoutManager(layoutManagerCreators);

            LinearLayoutManager layoutManagerComics = new LinearLayoutManager(
                    EventsActivity.this, RecyclerView.HORIZONTAL, false);
            rvComics.setLayoutManager(layoutManagerComics);

            LinearLayoutManager layoutManagerSeries = new LinearLayoutManager(
                    EventsActivity.this, RecyclerView.HORIZONTAL, false);
            rvSeries.setLayoutManager(layoutManagerSeries);
        }


        /**Definisco la funzione che setta tutti gli elementi della view.*/
        private void setData() {
            //Setto titolo,descrizione,e data inizio/fine dell'evento.
            tvEventName.setText(event.getTitle());
            startDate.setText(event.getStart());
            endDate.setText(event.getEnd());
            if (event.getDescription() != null) {
                description.setText(event.getDescription());
            } else {
                description.setText(R.string.tv_noDescription);
            }

            //Setto l'immagine dell'evento.
            String urlThumbnail = event.getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + event.getThumbnail().getExtension();
            Glide.with(getApplicationContext()).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivEventImage);

            //Inizializzo le query per settare gli eroi, i creators, le serie e i comics.
            String id = event.getId();
            heroVolley.getHeroesFromEvents(id);
            creatorsVolley.getCreatorsByEvents(id);
            seriesVolley.getSeriesByEvent(id);
            comicsVolley.getComicsByEvent(id);

            //Setto i link dell'evento.
            UrlsRecyclerView urlsAdapter = new UrlsRecyclerView(event.getUrls());
            rvUrls.setAdapter(urlsAdapter);

        }
        /** Metodo che verifica che tutte le RecyclerView siano state riempite con i dati
           (o siano state oscurate se non ci sono dati da mostrare) per poter togliere la ProgressBar */
        private void dismissLoading() {
            if (loading_count >= 4) {
                loading.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    }
}

