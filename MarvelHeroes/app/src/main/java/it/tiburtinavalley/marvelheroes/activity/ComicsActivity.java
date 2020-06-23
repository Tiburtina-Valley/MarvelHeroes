package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

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
        private RecyclerView.LayoutManager layoutManagerUrls;

        public Holder() {
            ivComicImage = findViewById(R.id.ivComicImg);
            tvComicName = findViewById(R.id.tvComicName);
            tvPageCount = findViewById(R.id.tvPageCount);
            rvUrls = findViewById(R.id.rvUrls);
            setData();
        }

        private void setData() {
            ImageApiVolley imgVolley = new ImageApiVolley(getApplicationContext());
            imgVolley.addHeroImg(ivComicImage);
            if (comic.getImages().size() > 0) {
                String urlThumbnail = comic.getImages().get(0).getPath().replaceFirst("http", "https")
                        + "." + comic.getImages().get(0).getExtension();
                Glide.with(getApplicationContext()).load(urlThumbnail).into(this.ivComicImage);
            }
            tvComicName.setText(comic.getTitle());
            tvPageCount.setText(comic.getPageCount());
            layoutManagerUrls = new LinearLayoutManager(ComicsActivity.this);
            rvUrls.setLayoutManager(layoutManagerUrls);
            urlsAdapter = new UrlsRecyclerView(comic.getUrls());
            rvUrls.setAdapter(urlsAdapter);
        }
    }
}
