package it.tiburtinavalley.marvelheroes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.CreatorsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.EventsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroDetailAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.CreatorsVolley;
import it.tiburtinavalley.marvelheroes.volley.EventsVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;

public class SeriesActivity extends AppCompatActivity {
    private Series series;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_close);

        setContentView(R.layout.activity_series);
        series = getIntent().getParcelableExtra("series");
        Holder holder = new Holder();
        holder.setRecyclerViews();
        holder.setData();
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
        private RecyclerView rvCreators;
        private CreatorsAdapter creatorsAdapter;
        private ComicsAdapter comicsAdapter;
        private EventsAdapter eventsAdapter;
        private TextView tvComics;
        private RecyclerView rvComics;
        private TextView tvEvents;
        private TextView tvCreators;
        private TextView tvCharacters;
        private RecyclerView rvEvents;
        private ProgressBar loading;
        private ConstraintLayout layout;

        private int loading_count = 0; //contatore per capire quando nascondere la progress bar e mostrare la schermata

        public Holder() {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_series);

            ivSeriesImage = findViewById(R.id.ivStoriesmg);
            tvSeriesName = findViewById(R.id.tvCreatorName);
            tvStartYear = findViewById(R.id.tvStartYear);
            tvEndYear = findViewById(R.id.tvEndYear);
            rvUrls = findViewById(R.id.rvUrls);
            tvDescription = findViewById(R.id.tvDescription);
            rvCharacters = findViewById(R.id.rvCharacters);
            tvType = findViewById(R.id.btnType);
            tvRating = findViewById(R.id.tvRating);
            rvCreators = findViewById(R.id.rvCreators);
            tvComics = findViewById(R.id.tvComics);
            rvComics = findViewById(R.id.rvComics);
            tvEvents = findViewById(R.id.tvEvents);
            rvEvents = findViewById(R.id.rvEvents);
            tvCharacters = findViewById(R.id.tvCharacters);
            tvCreators = findViewById(R.id.tvCreators);
            loading = findViewById(R.id.progress_loader);
            layout = findViewById(R.id.layout);


            final Context appContext = getApplicationContext();

            /* inizializza una MarvelApiVolley per la ricerca di eroi correlati alla series*/
            MarvelApiVolley heroVolley = new MarvelApiVolley(appContext) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    heroAdapter = new HeroDetailAdapter(heroes, appContext);
                    rvCharacters.setAdapter(heroAdapter);
                    if (heroAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvCharacters.setTextSize(0);
                        tvCharacters.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvCharacters.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);
                    }
                    loading_count++;
                    dismissLoading();
                }
            };

            /* inizializza una CreatorsVolley per la ricerca dei creatori della series*/
            CreatorsVolley creatorsVolley = new CreatorsVolley(getApplicationContext()) {
                @Override
                public void fillCreatorsInfo(List<Creators> creatorsList) {
                    creatorsAdapter = new CreatorsAdapter(creatorsList, getApplicationContext());
                    rvCreators.setAdapter(creatorsAdapter);
                    if (creatorsAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvCreators.setTextSize(0);
                        tvCreators.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvCreators.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);
                    }
                    loading_count++;
                    dismissLoading();
                }
            };

            /* inizializza una EventsVolley per la ricerca di eventi correlati alla series*/
            EventsVolley eventsVolley = new EventsVolley(getApplicationContext()) {
                @Override
                public void fillEvents(List<Events> eventsList) {
                    eventsAdapter = new EventsAdapter(eventsList, getApplicationContext());
                    rvEvents.setAdapter(eventsAdapter);
                    if (eventsAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvEvents.setTextSize(0);
                        tvEvents.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvEvents.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);
                    }
                    loading_count++;
                    dismissLoading();
                }
            };

            /* inizializza una MarvelApiVolley per la ricerca di comics correlati alla series*/
            ComicsVolley comicsVolley = new ComicsVolley(getApplicationContext()) {
                @Override
                public void fillComics(List<Comics> comicsList) {
                    comicsAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvComics.setAdapter(comicsAdapter);
                    if (comicsAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvComics.setTextSize(0);
                        tvComics.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvComics.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);
                    }
                    loading_count++;
                    dismissLoading();
                }
            };

            /* chiamata ai metodi per la ricerca di informazioni correlate alla series*/
            String id = series.getId();
            heroVolley.getHeroesFromSeries(id);
            creatorsVolley.getCreatorsBySeries(id);
            comicsVolley.getComicsBySeries(id);
            eventsVolley.getEventsBySeries(id);
        }

        /** metodo per settare il Layuout delle varie recylerViews presenti nella schermata */
        private void setRecyclerViews(){
            LinearLayoutManager layoutManagerUrls = new LinearLayoutManager(
                    SeriesActivity.this, RecyclerView.HORIZONTAL, false);
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

        /** metodo per settare le informazioni della series selezionata */
        private void setData() {

            //Setta l'immagine di copertina della serie, se presente.
            if (series.getThumbnail() != null) {
                String urlThumbnail = series.getThumbnail().getPath().replaceFirst("http", "https")
                        + "." + series.getThumbnail().getExtension();
                Glide.with(getApplicationContext()).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.ivSeriesImage);
            }

            tvSeriesName.setText(series.getTitle());
            tvStartYear.setText(getString(R.string.label_series_start_date, series.getStartYear()));
            tvEndYear.setText(getString(R.string.label_series_end_date, series.getEndYear()));
            if (!series.getType().equals(""))
                tvType.setText(getString(R.string.label_type, series.getType()));
            if (!series.getRating().equals(""))
                tvRating.setText(getString(R.string.label_rating, series.getRating()));

            if (series.getDescription() != null) {
                tvDescription.setText(series.getDescription());
            }
            else {
                tvDescription.setText(R.string.tv_noDescription);
            }

            //Setta i link associati alla serie.
            UrlsRecyclerView urlsAdapter = new UrlsRecyclerView(series.getUrls());
            rvUrls.setAdapter(urlsAdapter);
        }

        /* Metodo che verifica che tutte le RecyclerView siano state riempite con i dati
           (o siano state oscurate se non ci sono dati da mostrare) per poter togliere la ProgressBar */
        private void dismissLoading() {
            if (loading_count >= 4) {
                loading.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
