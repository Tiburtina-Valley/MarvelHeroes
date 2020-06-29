package it.tiburtinavalley.marvelheroes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;

public class HomeFragment extends Fragment {
    MarvelApiVolley apiHero;
    ComicsVolley apiComic;
    SeriesVolley apiSeries;
    Holder holder;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        holder = new Holder();

        apiHero = new MarvelApiVolley(getContext()) {
            @Override
            public void fillList(List<HeroModel> heroes) {
                holder.fillHeroInfo(heroes);
            }
        };

        apiComic = new ComicsVolley(getContext()){
            @Override
            public void fillComics(List<Comics> comicsList) {
                holder.fillComicInfo(comicsList);
            }
        };

        apiSeries = new SeriesVolley(getContext()) {
            @Override
            public void fillSeries(List<Series> seriesList) {
                holder.fillSeriesInfo(seriesList);
            }
        };

        apiHero.getRandomCharacterInfo();
        apiComic.getRandomComic();
        apiSeries.getRandomSeries();

        return rootView;
    }


    class Holder {
        private TextView tvHeroName;
        private TextView tvComicTitle;
        private TextView tvSeriesTitle;
        private TextView tvHeroDescription;
        private TextView tvComicDescription;
        private TextView tvSeriesDescription;
        private ImageView ivHero;
        private ImageView ivComic;
        private ImageView ivSeries;
        private ProgressBar loading;
        private ConstraintLayout layout;

        private int loading_count = 0;

        public Holder() {
            tvHeroName = rootView.findViewById(R.id.tvHeroName);
            tvComicTitle = rootView.findViewById(R.id.tvComicTitle);
            tvSeriesTitle = rootView.findViewById(R.id.tvSeriesTitle);
            tvHeroDescription = rootView.findViewById(R.id.tvHeroDescription);
            tvComicDescription = rootView.findViewById(R.id.tvComicDescription);
            tvSeriesDescription = rootView.findViewById(R.id.tvSeriesDescription);
            ivHero = rootView.findViewById(R.id.ivHero);
            ivComic = rootView.findViewById(R.id.ivComic);
            ivSeries = rootView.findViewById(R.id.ivSeries);
            loading = rootView.findViewById(R.id.progress_loader);
            layout = rootView.findViewById(R.id.layout);
        }

        private void fillSeriesInfo(List<Series> seriesList) {
            if(!seriesList.isEmpty()) {
                Series series = seriesList.get(0);
                if(series.getDescription() == null || series.getDescription().isEmpty()){
                    apiSeries.getRandomSeries();
                    return;
                }
                String title = series.getTitle() != null ? series.getTitle() : "";
                tvSeriesTitle.setText(title);
                String description = series.getDescription() != null ? series.getDescription() : "";
                if(!description.isEmpty())
                    tvSeriesDescription.setText(description);

                if (!series.getThumbnail().getPath().equalsIgnoreCase("")
                        && !series.getThumbnail().getExtension().equalsIgnoreCase("")) {
                    String urlThumbnail = series.getThumbnail().getPath().replaceFirst("http", "https")
                            + "." + series.getThumbnail().getExtension();
                    Glide.with(rootView).load(urlThumbnail).into(ivSeries);
                }
                loading_count++;
                dismissLoading();
            }
        }

        private void fillComicInfo(List<Comics> comicsList) {
            if(!comicsList.isEmpty()) {
                Comics comic = comicsList.get(0);
                if(comic.getDescription() == null || comic.getDescription().isEmpty()){
                    apiComic.getRandomComic();
                    return;
                }
                tvComicTitle.setText(comic.getTitle());
                if(comic.getDescription() != null && !comic.getDescription().isEmpty());
                    tvComicDescription.setText(comic.getDescription());

                if (!comic.getThumbnail().getPath().equalsIgnoreCase("")
                        && !comic.getThumbnail().getExtension().equalsIgnoreCase("")) {
                    String urlThumbnail = comic.getThumbnail().getPath().replaceFirst("http", "https")
                            + "." + comic.getThumbnail().getExtension();
                    Glide.with(rootView).load(urlThumbnail).into(ivComic);
                }
                loading_count++;
                dismissLoading();
            }
        }

        private void fillHeroInfo(List<HeroModel> heroes){
            if(!heroes.isEmpty()) {
                HeroModel hero = heroes.get(0);
                if(hero.getDescription() == null || hero.getDescription().isEmpty()){
                    apiHero.getRandomCharacterInfo();
                    return;
                }
                tvHeroName.setText(hero.getName());
                if(hero.getDescription() != null && !hero.getDescription().isEmpty());
                    tvHeroDescription.setText(hero.getDescription());

                if (!hero.getThumbnail().getPath().equalsIgnoreCase("")
                        && !hero.getThumbnail().getExtension().equalsIgnoreCase("")) {
                    String urlThumbnail = hero.getThumbnail().getPath().replaceFirst("http", "https")
                            + "." + hero.getThumbnail().getExtension();
                    Glide.with(rootView).load(urlThumbnail).into(ivHero);
                }
                loading_count++;
                dismissLoading();
            }
        }

        private void dismissLoading() {
            if (loading_count >= 2) {
                loading.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    }


}
