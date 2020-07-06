package it.tiburtinavalley.marvelheroes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import it.tiburtinavalley.marvelheroes.dao.AppDatabase;
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.EventsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.EventsVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;

/** Activity che mostra i dettagli di un eroe presente tra i preferiti: il nome, una descrizione se presente,
 * i fumetti, le serie e gli eventi collegati*/
public class FavoriteHeroDetail extends AppCompatActivity{

    private HeroModel hm;
    private ComicsVolley cVolley;
    private SeriesVolley seVolley;
    private EventsVolley eVolley;

    private Boolean isFavorite = false;
    private Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoBar);
        setContentView(R.layout.activity_hero_detail);

        hm = getIntent().getParcelableExtra("hero");    // Estraiamo la serie di cui vogliamo mostrare i dettagli
        holder = new Holder();
        holder.setRecyclerViews();
        holder.setDetails(hm);      //incarichiamo l'holder di mostrare i dettagli sullo schemrmo
    }

    @Override
    public boolean onSupportNavigateUp() {
        Context appContext = getApplicationContext();
        Intent i = new Intent(appContext, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);     // per rimuovere dallo stack le activity precedentemente aperte
        appContext.startActivity(i);
        return true;
    }

    /** necessario per far sparire la lodaing bar nel momento in cui si torna alla schermata dopo
     aver premuto il tasto back */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        holder.dismissLoading();
    }

    class Holder  implements View.OnClickListener {
        private final RecyclerView rvComics;
        private final RecyclerView rvSeries;
        private final RecyclerView rvEvents;
        private ImageView ivHeroPhoto;
        private TextView tvComics;
        private TextView tvSeries;
        private TextView tvEvents;
        private TextView tvHeroDescription;
        private ComicsAdapter cAdapter;
        private SeriesAdapter sAdapter;
        private EventsAdapter eAdapter;
        private FloatingActionButton btnAddFavorite;
        private ProgressBar loading;
        private ConstraintLayout layout;

        private int loading_count = 0;  //contatore per capire quando nascondere la progress bar e mostrare la schermata

        public Holder() {
            /*
            recupero la toolbar dichiarata nell'xml e la setto come actionbar principale,
            la recupero di nuovo come oggetto ActionBar e ne imposto il bottone X
            */
            Toolbar toolbar = findViewById(R.id.anim_toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_close);

            tvComics=findViewById(R.id.tvComics);
            tvSeries=findViewById(R.id.tvSeries);
            tvEvents=findViewById(R.id.tvEvents);
            rvEvents=findViewById(R.id.rvEvents);
            rvComics = findViewById(R.id.rvComics);
            rvSeries = findViewById(R.id.rvSeries);
            ivHeroPhoto = findViewById(R.id.ivHeroPhoto);
            tvHeroDescription = findViewById(R.id.tvHeroDescription);

            btnAddFavorite = findViewById(R.id.btnAddFavorite);
            btnAddFavorite.setOnClickListener(this);
            loading = findViewById(R.id.progress_loader);
            layout = findViewById(R.id.linearLayout);

            initFavoriteBtn();

            cVolley = new ComicsVolley(getApplicationContext()) {
                /** inizializza una ComicsVolley per la ricerca di comics correlati all'eroe*/
                @Override
                public void fillComics(List<Comics> comicsList) {
                    //controlla se sono stati trovati dei fumetti legati agli eroi
                    if(comicsList.isEmpty()){
                        TextView tvComics = findViewById(R.id.tvComics); // prende la TextView da oscurare
                        tvComics.setTextSize(0);

                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvComics.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0); // setta i margini per non lasciare spazi in più
                    }
                    cAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvComics.setAdapter(cAdapter);
                    if (cAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvComics.setTextSize(0);
                        tvComics.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvComics.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);}
                    loading_count++;
                    dismissLoading();
                }
            };

            seVolley = new SeriesVolley(getApplicationContext()) {
                /** inizializza una SeriesVolley per la ricerca di serie correlati all'eroe'*/
                @Override
                public void fillSeries(List<Series> seriesList) {
                    sAdapter = new SeriesAdapter(seriesList, getApplicationContext());
                    rvSeries.setAdapter(sAdapter);
                    if (sAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvSeries.setTextSize(0);
                        tvSeries.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvSeries.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);}
                    loading_count++;
                    dismissLoading();
                }
            };

            eVolley= new EventsVolley(getApplicationContext()) {
                /** inizializza una EventsVolley per la ricerca di eventi correlati all'eore*/
                @Override
                public void fillEvents(List<Events> eventsList) {
                    eAdapter=new EventsAdapter(eventsList,getApplicationContext());
                    rvEvents.setAdapter(eAdapter);
                    if (eAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvEvents.setTextSize(0);
                        tvEvents.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvEvents.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);}
                    loading_count++;
                    dismissLoading();

                }
            };
        }

        /** metodo per settare il Layuout delle varie recylerViews presenti nella schermata */
        private void setRecyclerViews() {
            LinearLayoutManager layoutManagerComics = new LinearLayoutManager(
                    FavoriteHeroDetail.this, RecyclerView.HORIZONTAL, false);
            rvComics.setLayoutManager(layoutManagerComics);

            LinearLayoutManager layoutManagerSeries = new LinearLayoutManager(
                    FavoriteHeroDetail.this, RecyclerView.HORIZONTAL, false);
            rvSeries.setLayoutManager(layoutManagerSeries);

            LinearLayoutManager layoutManagerEvents = new LinearLayoutManager(
                    FavoriteHeroDetail.this, RecyclerView.HORIZONTAL, false);
            rvEvents.setLayoutManager(layoutManagerEvents);
        }

        /** metodo per settare le informazioni dell'eroe selezionato */
        public void setDetails(HeroModel hero) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(hm.getName());

            if(hero.getDescription() != null && !hero.getDescription().equalsIgnoreCase("")) {
                this.tvHeroDescription.setText(hero.getDescription());
            }
            else{
                tvHeroDescription.setText(R.string.tv_noDescription);
            }

            //Setta l'immagine dell'eroe, se presente, usando l'API Glide
            if (hero.getResourceURI() != null) {
                Log.w("1","resource");
                String urlThumbnail = hero.getResourceURI().replaceFirst("http", "https")
                        + ".jpg";
                Glide.with(getApplicationContext()).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.ivHeroPhoto);
            }

            // chiama i metodi per riempire le RecyclerView di fumetti, serie e eventi
            cVolley.getComicsRelatedToHero(hm.getId());
            seVolley.getSeriesRelatedToHero(hm.getId());
            eVolley.getEventInfo(hm.getId());
            dismissLoading();
        }

        public void onClick(View v) {
            if (v.getId() == R.id.btnAddFavorite) {

                if (!isFavorite) {
                    // Creazione entità Hero per DB
                    HeroEntity hero = new HeroEntity(Integer.parseInt(hm.getId()), hm.getName(),hm.getThumbnail().getPath(), hm.getDescription());
                    // Salvataggio nel DB utilizzando l'istanza gestita da Room
                    AppDatabase.getInstance(getApplicationContext()).heroDao().insertHero(hero);


                    Toast toast = Toast.makeText(getApplicationContext(), "hero saved", Toast.LENGTH_LONG);
                    toast.show();

                } else {
                    // Cancellazione dell'Hero del DB mediante identificatore
                    AppDatabase.getInstance(getApplicationContext()).heroDao().deleteHeroFromId(Integer.parseInt(hm.getId()));

                    Toast toast = Toast.makeText(getApplicationContext(), "hero removed", Toast.LENGTH_LONG);
                    toast.show();
                }

                initFavoriteBtn();
            }
        }

        /** Controlla se l'hero è presente nel DB come preferito effettuando una query con Room.
         * In base a questo, imposta nel layout l'icona corretta per il tasto preferiti*/
        private void initFavoriteBtn()
        {
            isFavorite = AppDatabase.getInstance(getApplicationContext()).heroDao().hasHero(Integer.parseInt(hm.getId()));

            if (isFavorite){    //se trona true vuol dire che era gia un preferito e imposto l'icona cuore pieno
                btnAddFavorite.setImageResource(R.drawable.ic_action_favourites);
            } else {    //se torna false metto il cuore vuoto
                btnAddFavorite.setImageResource(R.drawable.ic_action_favourite_border);
            }
        }

        /** Metodo che verifica che tutte le RecyclerView siano state riempite con i dati
           (o siano state oscurate se non ci sono dati da mostrare) per poter togliere la ProgressBar */
        private void dismissLoading() {
            if (loading_count >= 3) {
                loading.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    }
}