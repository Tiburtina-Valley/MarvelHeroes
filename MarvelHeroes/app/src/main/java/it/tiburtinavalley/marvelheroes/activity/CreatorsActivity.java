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

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;

/* Activity di dettaglio di un Creator : vengono mostrati il nome, la foto, i fumetti e le serie a cui ha lavorato*/

public class CreatorsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_close);

        setContentView(R.layout.activity_creator);
        new Holder(Objects.requireNonNull(getIntent().getParcelableExtra("creator")));
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
        private RecyclerView rvCreatorsComics;
        private RecyclerView rvCreatorSeries;
        private ComicsAdapter comAdapter;
        private SeriesAdapter seriesAdapter;
        private ProgressBar loading;
        private ConstraintLayout layout;

        private int loading_count = 0; //contatore per capire quando nascondere la progress bar e mostrare la schermata

        public Holder(Creators creator){
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.label_creator_detail));

            // Lega le View all'xml del layout
            TextView creatorName = findViewById(R.id.tvCreatName);
            ImageView creatorImg = findViewById(R.id.ivCreatorPic);
            rvCreatorsComics = findViewById(R.id.rvCreatCom);
            rvCreatorSeries = findViewById(R.id.rvCreatorSeries);
            loading = findViewById(R.id.progress_loader);
            layout = findViewById(R.id.layout);

            creatorName.setText(creator.getFullName());

            // Query per ottenere la foto del Creator
            String urlThumbnail = creator.getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + creator.getThumbnail().getExtension();
            Glide.with(creatorImg).load(urlThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(creatorImg);

            // Crea i LayoutManager per le RecyclerView, con scorrimento orizzontale
            LinearLayoutManager layoutManagerCreatorComics = new LinearLayoutManager(
                    CreatorsActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreatorsComics.setLayoutManager(layoutManagerCreatorComics);

            LinearLayoutManager layoutManagerCreatorSeries = new LinearLayoutManager(
                    CreatorsActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreatorSeries.setLayoutManager(layoutManagerCreatorSeries);

            ComicsVolley cv = new ComicsVolley(getApplicationContext()) {
                @Override
                public void fillComics(List<Comics> comicsList) {
                    /* non è stato inserito un controllo su possibili liste vuote : questo perché si accede all'activity partendo dai dettagli di un fumetto,
                       quindi sicuramente sarà caricato almeno quel fumetto*/
                    comAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvCreatorsComics.setAdapter(comAdapter);
                    loading_count++;
                    dismissLoading();
                }
            };

            SeriesVolley sv = new SeriesVolley(getApplicationContext()) {
                @Override
                public void fillSeries(List<Series> seriesList) {
                    //se la lista è vuota, toglie la RecyclerView con relativa Label dal Layout
                    if(seriesList.isEmpty()){
                        TextView tvCreatorSeries = findViewById(R.id.tvCreatorSeries); // prende la TextView da oscurare
                        tvCreatorSeries.setTextSize(0);

                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvCreatorSeries.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0); // setta i margini per non lasciare spazi in più
                    }
                    seriesAdapter = new SeriesAdapter(seriesList, getApplicationContext());
                    rvCreatorSeries.setAdapter(seriesAdapter);
                    loading_count++;
                    dismissLoading();
                }
            };

            // query a Volley basate sull'id del Creator
            cv.getComicsByCreators(creator.getId());
            sv.getSeriesByCreator(creator.getId());
        }

        /* Metodo che verifica che tutte le RecyclerView siano state riempite con i dati
          (o siano state oscurate se non ci sono dati da mostrare) per poter togliere la ProgressBar */
        private void dismissLoading() {
            if (loading_count >= 2) {
                loading.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
