package it.tiburtinavalley.marvelheroes.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.ComicsActivity;
import it.tiburtinavalley.marvelheroes.activity.HeroDetailActivity;
import it.tiburtinavalley.marvelheroes.activity.MainActivity;
import it.tiburtinavalley.marvelheroes.activity.SeriesActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;

public class HomeFragment extends Fragment {
    final int MAX_ATTEMPTS = 2;
    final int MAX_STRING_LENGTH = 150;
    MarvelApiVolley apiHero;
    ComicsVolley apiComic;
    SeriesVolley apiSeries;
    int heroAttempts;
    int comicAttempts;
    int seriesAttempts;
    String defaultHeroId;
    String defaultComicId;
    String defaultStoriesId;
    Holder holder;
    View rootView;
    private Context context;
    private HeroModel heroOfTheDay;
    private Comics comicOfTheDay;
    private Series seriesOfTheDay;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = Objects.requireNonNull(getActivity()).getApplicationContext();
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle(R.string.title_home);

        defaultHeroId = getString(R.string.default_hero_id);

        sp = getActivity().getPreferences(Context.MODE_PRIVATE); //prende il file da scrivere
        editor = sp.edit();

        defaultHeroId = getString(R.string.default_hero_id);
        defaultComicId = getString(R.string.default_comic_id);
        defaultStoriesId = getString(R.string.default_series_id);

        heroAttempts = 0;
        comicAttempts = 0;
        seriesAttempts = 0;

        holder = new Holder();

        if ((!sp.contains("hero") || !sp.contains("comic") || !sp.contains("series")) || !sp.getString("day", "").equalsIgnoreCase(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))) { //controlliamo se è passata la mezzanotte

            holder.checkConnection();
            editor.putString("day", String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));

            apiHero = new MarvelApiVolley(getContext()) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    holder.fillHeroInfo(heroes);
                }
            };

            apiComic = new ComicsVolley(getContext()) {
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
        }
        else if (sp.contains("hero") && sp.contains("comic") && sp.contains("series")) { //datas are saved
            holder.setAll();
        }
        return rootView;
    }


    class Holder implements View.OnClickListener {
        private CardView cvHero;
        private CardView cvComic;
        private CardView cvSeries;
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
        RequestOptions requestOptions = new RequestOptions();

        private int loading_count = 0;

        public Holder() {
            cvHero = rootView.findViewById(R.id.cvHero);
            cvComic = rootView.findViewById(R.id.cvComic);
            cvSeries = rootView.findViewById(R.id.cvSeries);

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

            cvHero.setOnClickListener(this);
            cvComic.setOnClickListener(this);
            cvSeries.setOnClickListener(this);
        }


        private void setAll() {


            heroOfTheDay = setHeroFromCache();
            tvHeroName.setText(heroOfTheDay.getName());

            if ((heroOfTheDay.getDescription().length() + heroOfTheDay.getName().length()) > MAX_STRING_LENGTH) {
                tvHeroDescription.setText(heroOfTheDay.getDescription().substring(0, MAX_STRING_LENGTH -heroOfTheDay.getName().length()) + "...");
            } else {
                tvHeroDescription.setText(heroOfTheDay.getDescription());
            }

            String urlHero = heroOfTheDay.getThumbnail().getPath().replaceFirst("http", "https")
                    +"/portrait_xlarge" + "." + heroOfTheDay.getThumbnail().getExtension();
            Glide.with(Objects.requireNonNull(getActivity())).setDefaultRequestOptions(requestOptions).load(urlHero).into(ivHero);

            comicOfTheDay = setComicFromCache();
            tvComicTitle.setText(comicOfTheDay.getTitle());

            if ((comicOfTheDay.getDescription().length() + comicOfTheDay.getTitle().length() ) > MAX_STRING_LENGTH) {
                tvComicDescription.setText(comicOfTheDay.getDescription().substring(0, MAX_STRING_LENGTH -comicOfTheDay.getTitle().length()) + "...");
            } else {
                tvComicDescription.setText(comicOfTheDay.getDescription());
            }

            String urlComics = comicOfTheDay.getThumbnail().getPath().replaceFirst("http", "https")
                    +"/portrait_xlarge" + "." + comicOfTheDay.getThumbnail().getExtension();
            Glide.with(getActivity()).setDefaultRequestOptions(requestOptions).load(urlComics).into(ivComic);

            seriesOfTheDay = setSeriesFromCache();
            tvSeriesTitle.setText(seriesOfTheDay.getTitle());
            if ((seriesOfTheDay.getDescription().length() + seriesOfTheDay.getTitle().length() )> MAX_STRING_LENGTH) {
                tvSeriesDescription.setText(seriesOfTheDay.getDescription().substring(0, MAX_STRING_LENGTH -seriesOfTheDay.getTitle().length()) + "...");
            } else {
                tvSeriesDescription.setText(seriesOfTheDay.getDescription());
            }


            String urlSeries = seriesOfTheDay.getThumbnail().getPath().replaceFirst("http", "https")
                    +"/portrait_xlarge" + "." + seriesOfTheDay.getThumbnail().getExtension();
            Glide.with(getActivity()).setDefaultRequestOptions(requestOptions).load(urlSeries).into(ivSeries);

            loading_count = 3;
            dismissLoading();
        }


        private void checkConnection() {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
                loading.setVisibility(View.GONE);
                ToastClass toast = new ToastClass(context);
                toast.showToast(context.getString(R.string.msg_internet_required));
            }
        }

        private void fillSeriesInfo(List<Series> seriesList) {
            // if null, activity was probably destroyed
            if (getActivity() == null)
                return;

            seriesAttempts++;

            if (!seriesList.isEmpty()) {
                seriesOfTheDay = seriesList.get(0);
                if (seriesOfTheDay.getDescription() == null || seriesOfTheDay.getDescription().isEmpty()) {
                    if (seriesAttempts >= MAX_ATTEMPTS) {
                        apiSeries.getSeriesFromId(defaultStoriesId);
                    } else {
                        apiSeries.getRandomSeries();
                    }
                    return;
                }

                seriesAttempts = 0;

                String title = seriesOfTheDay.getTitle() != null ? seriesOfTheDay.getTitle() : "";
                tvSeriesTitle.setText(title);
                String description = seriesOfTheDay.getDescription() != null ? seriesOfTheDay.getDescription() : "";
                if (!description.isEmpty()) {
                    if ((seriesOfTheDay.getDescription().length() + seriesOfTheDay.getTitle().length() )> MAX_STRING_LENGTH) {
                        tvSeriesDescription.setText(seriesOfTheDay.getDescription().substring(0, MAX_STRING_LENGTH -seriesOfTheDay.getTitle().length()) + "...");
                    } else {
                        tvSeriesDescription.setText(seriesOfTheDay.getDescription());
                    }
                }
                if (!seriesOfTheDay.getThumbnail().getPath().equalsIgnoreCase("")
                        && !seriesOfTheDay.getThumbnail().getExtension().equalsIgnoreCase("")) {
                    String urlThumbnail = seriesOfTheDay.getThumbnail().getPath().replaceFirst("http", "https")
                            + "." + seriesOfTheDay.getThumbnail().getExtension();
                    Glide.with(getActivity()).setDefaultRequestOptions(requestOptions).load(urlThumbnail).into(ivSeries);
                }
                Gson gson = new Gson();
                String json = gson.toJson(seriesOfTheDay);
                editor.putString("series", json);
                editor.apply(); // usiamo il metod di apply, in esecuzione su un thread

                loading_count++;
                dismissLoading();
                }
            }

            private void fillComicInfo (List < Comics > comicsList) {
                // if null, activity was probably destroyed
                if (getActivity() == null)
                    return;

                comicAttempts++;

                if (!comicsList.isEmpty()) {
                    comicOfTheDay = comicsList.get(0);
                    if (comicOfTheDay.getDescription() == null || comicOfTheDay.getDescription().isEmpty()) {
                        if (comicAttempts >= MAX_ATTEMPTS) {
                            apiComic.getComicFromId(defaultComicId);
                        } else {
                            apiComic.getRandomComic();
                        }
                        return;
                    }

                    comicAttempts = 0;
                    tvComicTitle.setText(comicOfTheDay.getTitle());
                    if (comicOfTheDay.getDescription() != null && !comicOfTheDay.getDescription().isEmpty()) {
                        if ((comicOfTheDay.getDescription().length() + comicOfTheDay.getTitle().length() ) > MAX_STRING_LENGTH) {
                            tvComicDescription.setText(comicOfTheDay.getDescription().substring(0, MAX_STRING_LENGTH -comicOfTheDay.getTitle().length()) + "...");
                        } else {
                            tvComicDescription.setText(comicOfTheDay.getDescription());
                        }
                    }

                    if (!comicOfTheDay.getThumbnail().getPath().equalsIgnoreCase("")
                            && !comicOfTheDay.getThumbnail().getExtension().equalsIgnoreCase("")) {
                        String urlThumbnail = comicOfTheDay.getThumbnail().getPath().replaceFirst("http", "https")
                                + "." + comicOfTheDay.getThumbnail().getExtension();
                        Glide.with(getActivity()).setDefaultRequestOptions(requestOptions).load(urlThumbnail).into(ivComic);
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(comicOfTheDay);
                    editor.putString("comic", json);
                    editor.apply();

                    loading_count++;
                    dismissLoading();
                }
            }

            private void fillHeroInfo (List < HeroModel > heroes) {
                // if null, activity was probably destroyed
                if (getActivity() == null)
                    return;

                heroAttempts++;

                if (!heroes.isEmpty()) {
                    heroOfTheDay = heroes.get(0);
                    if (heroOfTheDay.getDescription() == null || heroOfTheDay.getDescription().isEmpty()) {
                        if (heroAttempts >= MAX_ATTEMPTS) {
                            apiHero.getCharacterInfoFromId(defaultHeroId);
                        } else {
                            apiHero.getRandomCharacterInfo();
                        }
                        return;
                    }

                    heroAttempts = 0;

                    tvHeroName.setText(heroOfTheDay.getName());
                    if (heroOfTheDay.getDescription() != null && !heroOfTheDay.getDescription().isEmpty()) {
                        if ((heroOfTheDay.getDescription().length() + heroOfTheDay.getName().length()) > MAX_STRING_LENGTH) {
                            tvHeroDescription.setText(heroOfTheDay.getDescription().substring(0, MAX_STRING_LENGTH -heroOfTheDay.getName().length()) + "...");
                        } else {
                            tvHeroDescription.setText(heroOfTheDay.getDescription());
                        }
                    }

                    if (!heroOfTheDay.getThumbnail().getPath().equalsIgnoreCase("")
                            && !heroOfTheDay.getThumbnail().getExtension().equalsIgnoreCase("")) {
                        String urlThumbnail = heroOfTheDay.getThumbnail().getPath().replaceFirst("http", "https")
                                + "." + heroOfTheDay.getThumbnail().getExtension();
                        Glide.with(getActivity()).setDefaultRequestOptions(requestOptions).load(urlThumbnail).into(ivHero);
                    }

                    Gson gson = new Gson();
                    String json = gson.toJson(heroOfTheDay);
                    editor.putString("hero", json);
                    editor.apply();

                    loading_count++;
                    dismissLoading();
                }
            }

            private void dismissLoading () {
                if (loading_count >= 3) {
                    loading.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        @Override
        public void onClick(View view) {
            // click on Hero
            if (view.getId() == R.id.cvHero) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                assert cm != null;
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    if(heroOfTheDay == null){
                        heroOfTheDay = setHeroFromCache();
                    }
                    Intent i = new Intent(context, HeroDetailActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("hero", heroOfTheDay);
                    context.startActivity(i);
                } else {
                    ToastClass toast = new ToastClass(context);
                    toast.showToast(Objects.requireNonNull(getContext()).getString(R.string.msg_internet_required));
                }
            }
            // click on Comic
            else if (view.getId() == R.id.cvComic) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                assert cm != null;
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    if(comicOfTheDay == null){
                        comicOfTheDay = setComicFromCache();
                    }
                    Intent i = new Intent(context, ComicsActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("comic", comicOfTheDay);
                    context.startActivity(i);
                } else {
                    ToastClass toast = new ToastClass(context);
                    toast.showToast(context.getString(R.string.msg_internet_required));
                }
            }
            // click on Series
            else if (view.getId() == R.id.cvSeries) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                assert cm != null;
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    if(seriesOfTheDay == null){
                        seriesOfTheDay = setSeriesFromCache();
                    }
                    Intent i = new Intent(context, SeriesActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("series", seriesOfTheDay);
                    context.startActivity(i);
                } else {                                                      //toast che avverte in caso di mancanza di connessione ad internet
                    ToastClass toast = new ToastClass(context);
                    toast.showToast(context.getString(R.string.msg_internet_required));
                }
            }
        }

        private HeroModel setHeroFromCache(){
            Gson gson = new Gson();
            String json = sp.getString("hero", "");
            return gson.fromJson(json, HeroModel.class);
        }

        private Comics setComicFromCache(){
            Gson gson = new Gson();
            String json = sp.getString("comic", "");
            return gson.fromJson(json, Comics.class);
        }

        private Series setSeriesFromCache(){
            Gson gson = new Gson();
            String json = sp.getString("series", "");
            return gson.fromJson(json, Series.class);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
