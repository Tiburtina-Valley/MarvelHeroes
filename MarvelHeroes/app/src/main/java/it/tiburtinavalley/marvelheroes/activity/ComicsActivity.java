package it.tiburtinavalley.marvelheroes.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.CreatorsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroDetailAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.CreatorsVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;

/* Activity che mostra i dettagli di un fumetto : gli eroi che vi compaiono, i creatori che l'hanno diseganto, il titolo, numero di pagine, il codice UPC
   e la descrizione del fumetto*/

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
        private TextView tvUpcCode;
        private TextView tvDescription;
        private RecyclerView rvUrls;
        private RecyclerView rvHeroesComics;
        private RecyclerView rvCreatorsComics;
        private HeroDetailAdapter heroDetAdapter;
        private CreatorsAdapter creatorsAdapter;
        private MarvelApiVolley heroVolley;
        private CreatorsVolley creatorsVolley;

        public Holder() {
            ivComicImage = findViewById(R.id.ivStoriesmg);
            tvComicName = findViewById(R.id.tvCreatorName);
            tvPageCount = findViewById(R.id.tvPageCount);
            tvUpcCode = findViewById(R.id.tvUpcCode);
            rvUrls = findViewById(R.id.rvUrls);
            tvDescription = findViewById(R.id.tvDescription);
            rvHeroesComics = findViewById(R.id.rvHeroComics);
            rvCreatorsComics = findViewById(R.id.rvCreatorComics);
            final Context appContext = getApplicationContext();

            heroVolley = new MarvelApiVolley(appContext) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    // se la lista degli eroi è vuota, cancella la RecyclerView degli eroi ed attacca l'ultima RecyclerView alla prima
                    if(heroes.isEmpty()){
                        ((ViewGroup) rvHeroesComics.getParent()).removeView(rvHeroesComics);

                        View tvHeroComics = findViewById(R.id.tvHeroes);
                        ((ViewGroup) tvHeroComics.getParent()).removeView(tvHeroComics);

                        View tvCreators = findViewById(R.id.tvCreatorsComics);
                        ConstraintSet cs = new ConstraintSet();
                        ConstraintLayout comicsConstraintLayout = (ConstraintLayout)findViewById(R.id.comicsConstraintLayout);
                        cs.clone(comicsConstraintLayout);
                        cs.connect(tvCreators.getId(), ConstraintSet.TOP, rvUrls.getId(), ConstraintSet.BOTTOM, 16);
                        cs.applyTo(comicsConstraintLayout);
                    }
                    else {
                        heroDetAdapter = new HeroDetailAdapter(heroes, appContext);
                        rvHeroesComics.setAdapter(heroDetAdapter);
                    }
                }
            };

            creatorsVolley = new CreatorsVolley(getApplicationContext()) {
                @Override
                public void fillCreatorsInfo(List<Creators> creatorsList) {
                    creatorsAdapter = new CreatorsAdapter(creatorsList, appContext);
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

            LinearLayoutManager layoutManagerCreators = new LinearLayoutManager(
                    ComicsActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreatorsComics.setLayoutManager(layoutManagerCreators);
        }

        //Vengono settati tutti i valori degli elementi del fumetto, controllando che non siano null. In tal caso, viene inserita una stringa apposita.
        private void setData() {
            if (comic.getImages() != null || comic.getImages().size() > 0) {
                String urlThumbnail = comic.getThumbnail().getPath().replaceFirst("http", "https")
                       +"/portrait_xlarge" + "." + comic.getThumbnail().getExtension();
                Glide.with(getApplicationContext()).load(urlThumbnail).into(this.ivComicImage);
            }
            tvComicName.setText(comic.getTitle());
            if(!comic.getPageCount().equalsIgnoreCase("") && !comic.getPageCount().equalsIgnoreCase("0")){
                tvPageCount.setText("Total pages : "+comic.getPageCount());
            }
            else{
                tvPageCount.setText(R.string.noPageCount);
            }
            if(!comic.getUpc().equalsIgnoreCase("")) {
                tvUpcCode.setText("Upc code : " + comic.getUpc());
            }
            else{
                tvUpcCode.setText(R.string.noUPC);
            }

            if(comic.getDescription() != null){
                tvDescription.setText(comic.getDescription());
            }
            else{
                tvDescription.setText(R.string.noDescription);
            }

            // Vengono chiamati i metodi delle volley per rimepire le RecyclerView con i valori ritornati dalle query
            urlsAdapter = new UrlsRecyclerView(comic.getUrls()); // gli urls sono già stati caricati precedentemente, quando è stata caricata l'activity per i dettagli dell'eroe
            rvUrls.setAdapter(urlsAdapter);
            String id = comic.getId();
            heroVolley.getHeroesFromComics(id);
            creatorsVolley.getCreatorsByComics(id);
        }
    }
}
