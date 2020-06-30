package it.tiburtinavalley.marvelheroes.volley;

/* this class is in charge on internet queries*/

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.HeroModel;

public abstract class MarvelApiVolley implements Response.ErrorListener, Response.Listener<String> {
    public boolean showToast = false;
    List<ImageView> heroesImg;
    StringRequest sr;
    private RequestQueue requestQueue;
    private Context context;
    private String urlBase = "https://gateway.marvel.com/v1/public/%s";
    private String apiKey = "ts=1&apikey=68bdde3ebf9ba45c6c11839bd1f51cc3&hash=6433747692d0e40eaf799ef75ccc78ea";//"ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea&limit=100";

    public MarvelApiVolley(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(this.context);
        heroesImg = new ArrayList<>();
    }

    public abstract void fillList(List<HeroModel> heroes);

    public void getCharacterInfoFromId(String heroId) {
        showToast = false;
        String param = "characters/" + heroId + "?";
        callApi(param);
    }

    public void getCharactersInfo(String nameStartsWith) {
        showToast = true;
        String param = "characters?nameStartsWith=" + nameStartsWith + "&";
        callApi(param);
    }

    public void getRandomCharacterInfo() {
        showToast = true;
        Random r = new Random();
        char randomLetter = (char) (r.nextInt(26) + 'a');
        String param = "characters?nameStartsWith=" + randomLetter + "&limit=1&";
        callApi(param);
    }

    /**
     * metodo che esegue una query in base all'ID di un fumetto, per trovare gli eroi correlati
     */
    public void getHeroesFromComics(String comicId) {
        showToast = false;
        String param = "comics/" + comicId + "/characters?";
        callApi(param);
    }

    /**
     * Ottiene tutti gli eroi relativi ad un evento.
     */
    public void getHeroesFromEvents(String eventId) {
        showToast = false;
        String param = "events/" + eventId + "/characters?";
        callApi(param);
    }

    /**
     * Ottiene tutti gli eroi relativi ad una serie.
     */
    public void getHeroesFromSeries(String seriesId) {
        showToast = false;
        String param = "series/" + seriesId + "/characters?";
        callApi(param);
    }

    private void callApi(String parameter) {
        String url = urlBase + apiKey;
        url = String.format(url, parameter);
        sr = new StringRequest(Request.Method.GET,
                url,
                this,
                this);
        requestQueue.add(sr);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null && error.getMessage() != null)
            Log.w("QueryFail", error.getMessage());
    }

    @Override
    public void onResponse(String response) {
        Gson gson = new Gson();
        String heroes;
        try {
            JSONObject jsonObject = new JSONObject(response);
            heroes = jsonObject.getJSONObject("data").getJSONArray("results").toString();
            Type listType = new TypeToken<List<HeroModel>>() {
            }.getType();
            List<HeroModel> heroesList = gson.fromJson(heroes, listType);
            if (heroesList != null) { //controlla che la lista non sia null per evitare NullPointerException
                if (heroesList.size() > 0) { //se la lista non è vuota, Log per vedere quant eroi effettivamente sono stati trovati
                    Log.w("CA", "" + heroesList.size());
                    //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                } else {
                    if (showToast) {
                        ToastClass toast = new ToastClass(context);
                        toast.showToast("No results has to been showed");
                    }
                }
                fillList(heroesList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
