package it.tiburtinavalley.marvelheroes.volley;

import android.content.Context;
import android.util.Log;

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
import java.util.List;

import it.tiburtinavalley.marvelheroes.model.Series;

public abstract class SeriesVolley implements Response.ErrorListener, Response.Listener<String>{
    private String urlBase = "https://gateway.marvel.com/v1/public/characters/%s";
    private String apiKey = "ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea";
    private RequestQueue requestQueue;

    public abstract void fillSeries(List<Series> seriesList);

    public SeriesVolley(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getStoriesInfo(String heroId){
        String serie = heroId + "/series?";
        seriesApiCall(serie);
    }

    private void seriesApiCall(String storyUrl){
        String url = urlBase + apiKey;
        url = String.format(url, storyUrl);
        StringRequest sr = new StringRequest(Request.Method.GET,
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
        String comics;
        try {
            JSONObject jsonObject = new JSONObject(response);
            comics = jsonObject.getJSONObject("data").getJSONArray("results").toString();
            Type listType = new TypeToken<List<Series>>() {
            }.getType();
            List<Series> seriesList = gson.fromJson(comics, listType);
            if (seriesList != null && seriesList.size() > 0) {
                //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                fillSeries(seriesList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}