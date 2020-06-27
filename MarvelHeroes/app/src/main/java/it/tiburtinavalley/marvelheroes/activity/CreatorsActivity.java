package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;

public class CreatorsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creators_detail_layout);
        Holder hold = new Holder((Creators)getIntent().getParcelableExtra("creator"));
    }

    class Holder{
        private TextView creatorName;
        private ImageView creatorImg;
        private RecyclerView rvCreatorsComics;
        private RecyclerView rvCreatorSeries;
        private ComicsAdapter comAdapter;
        private SeriesAdapter seriesAdapter;

        public Holder(Creators creator){
            creatorName = findViewById(R.id.tvCreatName);
            creatorImg = findViewById(R.id.ivCreatorPic);
            rvCreatorsComics = findViewById(R.id.rvCreatCom);
            rvCreatorSeries = findViewById(R.id.rvCreatorSeries);

            creatorName.setText(creator.getFullName());
            String urlThumbnail = creator.getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + creator.getThumbnail().getExtension();
            Glide.with(creatorImg).load(urlThumbnail).into(creatorImg);

            LinearLayoutManager layoutManagerCreatorComics = new LinearLayoutManager(
                    CreatorsActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreatorsComics.setLayoutManager(layoutManagerCreatorComics);

            LinearLayoutManager layoutManagerCreatorSeries = new LinearLayoutManager(
                    CreatorsActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreatorSeries.setLayoutManager(layoutManagerCreatorSeries);

            ComicsVolley cv = new ComicsVolley(getApplicationContext()) {
                @Override
                public void fillComics(List<Comics> comicsList) {
                    comAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvCreatorsComics.setAdapter(comAdapter);
                }
            };

            SeriesVolley sv = new SeriesVolley(getApplicationContext()) {
                @Override
                public void fillSeries(List<Series> seriesList) {
                    if(seriesList.isEmpty()){
                        TextView tvCreatorSeries = findViewById(R.id.tvCreatorSeries); // prende la TextView da oscurare
                        tvCreatorSeries.setTextSize(0);

                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvCreatorSeries.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0); // setta i margini per non lasciare spazi in più
                    }
                    seriesAdapter = new SeriesAdapter(seriesList, getApplicationContext());
                    rvCreatorSeries.setAdapter(seriesAdapter);
                }
            };

            cv.getComicsByCreators(creator.getId());
            sv.getSeriesByCreator(creator.getId());
        }
    }
}
