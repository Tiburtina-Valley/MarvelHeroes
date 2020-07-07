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
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.CreatorsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroDetailAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.CreatorsVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;

/** Activity che mostra i dettagli di un fumetto : gli eroi che vi compaiono, i creatori che l'hanno diseganto, il titolo, numero di pagine, il codice UPC
   e la descrizione del fumetto*/

public class ComicsActivity extends AppCompatActivity {
    private Comics comic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar(); // Settiamo nell'ActionBar l'opzione per tornare alla Home
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_close);

        setContentView(R.layout.activity_comic);
        comic = getIntent().getParcelableExtra("comic"); // Estraiamo il Comic di cui vogliamo mostrare i dettagli
        new Holder();
    }

    /** Metodo per tornare alla Home Page dopo la pressione dlel'icona "X" */
    @Override
    public boolean onSupportNavigateUp() {
        Context appContext = getApplicationContext();
        Intent i = new Intent(appContext, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Flag per far si che le activity precedentemente aperte vengano chiuse dopo che parta la MainActivity
        appContext.startActivity(i);
        return true;
    }

    class Holder {
        private ImageView ivComicImage;
        private TextView tvComicName;
        private TextView tvPageCount;
        private TextView tvUpcCode;
        private TextView tvDescription;
        private RecyclerView rvUrls;
        private RecyclerView rvHeroesComics;
        private RecyclerView rvCreatorsComics;
        private HeroDetailAdapter heroDetAdapter;
        private CreatorsAdapter creatorsAdapter;
        private ProgressBar loading;
        private ConstraintLayout layout;

        private int loading_count = 0; // Counter per far sparire la ProgessBar

        public Holder() {

            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_comic);

            // Leghiamo gli elementi di View all'xml mediante l'id
            ivComicImage = findViewById(R.id.ivStoriesmg);
            tvComicName = findViewById(R.id.tvCreatorName);
            tvPageCount = findViewById(R.id.tvPageCount);
            tvUpcCode = findViewById(R.id.tvUpcCode);
            rvUrls = findViewById(R.id.rvUrls);
            tvDescription = findViewById(R.id.tvDescription);
            rvHeroesComics = findViewById(R.id.rvHeroComics);
            rvCreatorsComics = findViewById(R.id.rvCreatorComics);
            loading = findViewById(R.id.progress_loader);
            layout = findViewById(R.id.layout);


            final Context appContext = getApplicationContext();

            /** Se la lista degli eroi è vuota, nasconde la RecyclerView degli eroi
                Prende la TextView da oscurare
                Setta i margini per non lasciare spazi in più
                Crea un nuovo adapter e lo assegna alla RecyclerView
                Incrementa il contatore e controlla se tutte le RecyclerView sono state riempite coi dati */
                MarvelApiVolley heroVolley = new MarvelApiVolley(appContext) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    // Se la lista degli eroi è vuota, nasconde la RecyclerView degli eroi
                    if (heroes.isEmpty()) {
                        TextView tvHeroes = findViewById(R.id.tvHeroes); // Prende la TextView da oscurare
                        tvHeroes.setTextSize(0);
                        tvHeroes.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvHeroes.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0); // Setta i margini per non lasciare spazi in più
                    } else {
                        // Crea un nuovo adapter e lo assegna alla RecyclerView
                        heroDetAdapter = new HeroDetailAdapter(heroes, appContext);
                        rvHeroesComics.setAdapter(heroDetAdapter);
                    }
                    // Incrementa il contatore e controlla se tutte le RecyclerView sono state riempite coi dati
                    loading_count++;
                    dismissLoading();
                }
            };

            /** Se la lista degli eroi è vuota, nasconde la RecyclerView dei Creators
                Prende la TextView da oscurare
                Setta i margini per non lasciare spazi in più */
            CreatorsVolley creatorsVolley = new CreatorsVolley(getApplicationContext()) {
                @Override
                public void fillCreatorsInfo(List<Creators> creatorsList) {
                    // Se la lista degli eroi è vuota, nasconde la RecyclerView dei Creators
                    if (creatorsList.isEmpty()) {
                        TextView tvCreators = findViewById(R.id.tvCreatorsComics); // Prende la TextView da oscurare
                        tvCreators.setTextSize(0);
                        tvCreators.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvCreators.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0); // Setta i margini per non lasciare spazi in più
                    } else {
                        creatorsAdapter = new CreatorsAdapter(creatorsList, appContext);
                        rvCreatorsComics.setAdapter(creatorsAdapter);
                    }
                    loading_count++;
                    dismissLoading();
                }
            };
            // Vengono chiamati i metodi delle volley per rimepire le RecyclerView con i valori ritornati dalle query
            UrlsRecyclerView urlsAdapter = new UrlsRecyclerView(comic.getUrls()); // gli urls sono già stati caricati precedentemente, quando è stata caricata l'activity per i dettagli dell'eroe
            rvUrls.setAdapter(urlsAdapter);

            // Effettua le query al portale Marvel basandosi sull'id del fumetto
            String id = comic.getId();
            heroVolley.getHeroesFromComics(id);
            creatorsVolley.getCreatorsByComics(id);

            // Assegna i LayoutManager alle RecyclerView e setta i dati nelle View dell'Holder
            setRecyclerViews();
            setData();
        }

        // Funzione che si occupa di settare i LayoutManager per le RecyclerView
        private void setRecyclerViews() {

            // Ogni RecyclerView avrà gli elementi che scorrono in orizzontale

            LinearLayoutManager layoutManagerUrls = new LinearLayoutManager(
                    ComicsActivity.this, RecyclerView.HORIZONTAL, false);
            rvUrls.setLayoutManager(layoutManagerUrls);

            LinearLayoutManager layoutManagerHeroes = new LinearLayoutManager(
                    ComicsActivity.this, RecyclerView.HORIZONTAL, false);
            rvHeroesComics.setLayoutManager(layoutManagerHeroes);

            LinearLayoutManager layoutManagerCreators = new LinearLayoutManager(
                    ComicsActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreatorsComics.setLayoutManager(layoutManagerCreators);
        }

        /** Vengono settati tutti i valori degli elementi del fumetto, controllando che non siano null. In tal caso, viene inserita una stringa apposita. */
        private void setData() {

            // Se il path dell'immagine è presente, la carica usando l'API Glide
            if (comic.getThumbnail() != null) {
                String urlThumbnail = comic.getThumbnail().getPath().replaceFirst("http", "https")
                        + "/portrait_xlarge" + "." + comic.getThumbnail().getExtension();
                Glide.with(getApplicationContext()).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.ivComicImage);
            }
            tvComicName.setText(comic.getTitle());

            // Controllo se i dati sono presenti, se non lo sono viene caricata una stringa di defualt dal file strings.xml
            if (!comic.getPageCount().equalsIgnoreCase("") && !comic.getPageCount().equalsIgnoreCase("0")) {
                tvPageCount.setText(getString(R.string.label_pages, comic.getPageCount()));
            } else {
                tvPageCount.setText(R.string.tv_noPageCount);
            }
            if (!comic.getUpc().equalsIgnoreCase("")) {
                tvUpcCode.setText(getString(R.string.label_upc, comic.getUpc()));
            } else {
                tvUpcCode.setText(R.string.tv_noUPC);
            }

            if (comic.getDescription() != null) {
                tvDescription.setText(comic.getDescription());
            } else {
                tvDescription.setText(R.string.tv_noDescription);
            }
        }

        /** Metodo che verifica che tutte le RecyclerView siano state riempite con i dati
           (o siano state oscurate se non ci sono dati da mostrare) per poter togliere la ProgressBar */
        private void dismissLoading() {
            if (loading_count >= 2) {
                loading.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
