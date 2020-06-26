package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
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
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;

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
        private ComicsAdapter comAdapter;

        public Holder(Creators creator){
            creatorName = findViewById(R.id.tvCreatName);
            creatorImg = findViewById(R.id.ivCreatorPic);
            rvCreatorsComics = findViewById(R.id.rvCreatCom);

            creatorName.setText(creator.getFullName());
            String urlThumbnail = creator.getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + creator.getThumbnail().getExtension();
            Glide.with(creatorImg).load(urlThumbnail).into(creatorImg);

            LinearLayoutManager layoutManagerCreatorComics = new LinearLayoutManager(
                    CreatorsActivity.this, RecyclerView.HORIZONTAL, false);
            rvCreatorsComics.setLayoutManager(layoutManagerCreatorComics);

            ComicsVolley cv = new ComicsVolley(getApplicationContext()) {
                @Override
                public void fillComics(List<Comics> comicsList) {
                    comAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvCreatorsComics.setAdapter(comAdapter);
                }
            };

            cv.getComicsByCreators(creator.getId());
        }
    }
}
