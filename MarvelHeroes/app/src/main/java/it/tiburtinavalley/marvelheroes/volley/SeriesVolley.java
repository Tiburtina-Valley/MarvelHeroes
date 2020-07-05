package it.tiburtinavalley.marvelheroes.volley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.Random;

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Series;

/** Classe che gestisce le query legate alle series. */
public abstract class SeriesVolley implements Response.ErrorListener, Response.Listener<String>{

    private Context context;
    private RequestQueue requestQueue;

    public abstract void fillSeries(List<Series> seriesList);

    public SeriesVolley(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getSeriesFromId(String seriesId){
        String param = "series/" + seriesId + "?";
        seriesApiCall(param);
    }

    public void getSeriesRelatedToHero(String heroId){
        String series = "characters/" + heroId + "/series?";
        seriesApiCall(series);
    }

    public void getRandomSeries(){
        Random r = new Random();
        char randomLetter = (char) (r.nextInt(26) + 'a');
        String param = "/series?titleStartsWith=" + randomLetter + "&limit=1&";
        seriesApiCall(param);
    }

    /** metodo per cercare le series filtrando mediante l'id di un creator */
    public void getSeriesByCreator(String creatorId){
        String creator = "creators/"+ creatorId+ "/series?";
        seriesApiCall(creator);
    }

    /** metodo per cercare le series filtrando mediante l'id di un evento */
    public void getSeriesByEvent(String eventId){
        String param = "events/"+eventId+"/series?";
        seriesApiCall(param);
    }

    /** Inserisco la specifica richiesta nella coda. */
    private void seriesApiCall(String storyUrl){
        String urlBase = "https://gateway.marvel.com/v1/public/%s";
        //"ts=1&apikey=d65eda0ccbbbcc626c35e7de5fdd506b&hash=9c0f64d5214cf16ca91f945f8cfbd5dc&limit=100";//"ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea&limit=100";//"ts=1&apikey=68bdde3ebf9ba45c6c11839bd1f51cc3&hash=6433747692d0e40eaf799ef75ccc78ea";
        String apiKey = "ts=1&apikey=a5f7b1501c40d87b927d3176fe38f22f&hash=dad24154bc30827c2290b5bd86f088fa&limit=50";
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
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            ToastClass toast = new ToastClass(context);
            toast.showToast(context.getString(R.string.msg_request_throttled));
        }
        if (error != null && error.getMessage() != null) {
            Log.w("QueryFail", error.getCause());
        }
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
            if (seriesList != null) {
                //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                fillSeries(seriesList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
