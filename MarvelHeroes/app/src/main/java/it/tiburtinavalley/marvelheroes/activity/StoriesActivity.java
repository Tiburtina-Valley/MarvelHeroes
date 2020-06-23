package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.tiburtinavalley.marvelheroes.model.Stories;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class StoriesActivity extends AppCompatActivity {
    private Stories story;
    private UrlsRecyclerView urlsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stories_layout);
        story = getIntent().getParcelableExtra("story");
        Holder holder = new Holder();
    }

    class Holder {
        private ImageView ivStoryImage;
        private TextView tvStoryName;
        private TextView tvPageCount;
        private RecyclerView rvUrls;
        private RecyclerView.LayoutManager layoutManagerUrls;

        public Holder() {
            ivStoryImage = findViewById(R.id.ivStoriesmg);
            tvStoryName = findViewById(R.id.tvStoriesName);
            tvPageCount = findViewById(R.id.tvPageCount);
            rvUrls = findViewById(R.id.rvUrls);
            setData();
        }

        private void setData() {
            ImageApiVolley imgVolley = new ImageApiVolley(getApplicationContext());
            imgVolley.addHeroImg(ivStoryImage);
            if (story.getImages().size() > 0) {
                String urlThumbnail = story.getImages().get(0).getPath().replaceFirst("http", "https")
                        + "." + story.getImages().get(0).getExtension();
                Glide.with(getApplicationContext()).load(urlThumbnail).into(this.ivStoryImage);
            }
            tvStoryName.setText(story.getTitle());
            tvPageCount.setText(story.getPageCount());
            layoutManagerUrls = new LinearLayoutManager(StoriesActivity.this);
            rvUrls.setLayoutManager(layoutManagerUrls);
            urlsAdapter = new UrlsRecyclerView(story.getUrls());
            rvUrls.setAdapter(urlsAdapter);
        }
    }
}
