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

import it.tiburtinavalley.marvelheroes.model.HeroModel;

public abstract class MarvelApiVolley implements Response.ErrorListener, Response.Listener<String> {
    List<ImageView> heroesImg;
    StringRequest sr;
    private RequestQueue requestQueue;
    private String urlBase = "https://gateway.marvel.com/v1/public/%s";
    private String apiKey = "ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea";

    public abstract void fillList(List<HeroModel> heroes);

    public MarvelApiVolley(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        heroesImg = new ArrayList<>();
    }


    public void getCharactersInfo(String nameStartsWith) {
        String param = "characters?nameStartsWith=" + nameStartsWith+"&";
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
            if (heroesList != null && heroesList.size() > 0) {
                Log.w("CA", "" + heroesList.size());
                //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                fillList(heroesList);
                System.out.println(heroesList.get(0).getComics());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
