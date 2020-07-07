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

/** Classe che gestisce le query legate alle serie. */
public abstract class SeriesVolley implements Response.ErrorListener, Response.Listener<String>{

    private Context context;
    private RequestQueue requestQueue;

    public abstract void fillSeries(List<Series> seriesList);

    public SeriesVolley(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    /** Ottiene i dati di una serie a partire dall'id.*/
    public void getSeriesFromId(String seriesId){
        String param = "series/" + seriesId + "?";
        seriesApiCall(param);
    }

    /** Ottiene i dati di una serie relative ad un certo eroe.*/
    public void getSeriesRelatedToHero(String heroId){
        String series = "characters/" + heroId + "/series?";
        seriesApiCall(series);
    }

    /** Ottiene i dati di una serie random.*/
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
        String apiKey = "ts=1&apikey=d65eda0ccbbbcc626c35e7de5fdd506b&hash=9c0f64d5214cf16ca91f945f8cfbd5dc&limit=50";
        String url = urlBase + apiKey;  // usiamo una stringa di appoggio così da poter ripetere la chiamata
        url = String.format(url, storyUrl);
        StringRequest sr = new StringRequest(Request.Method.GET,
                url,
                this,
                this);
        requestQueue.add(sr);
    }

    /** In caso ci sia un errore nella query*/
    @Override
    public void onErrorResponse(VolleyError error) {
        //controllo che l'errore non sia dovuto ad una mancanza della connessione ad internet
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            ToastClass toast = new ToastClass(context);
            //mostra un toast che avverte di aver superato il numero di query giornaliere disponibili
            toast.showToast(context.getString(R.string.msg_request_throttled));
        }
        //log del messaggio di errore
        if (error != null && error.getMessage() != null) {
            Log.w("QueryFail", error.getCause());
        }
    }

    /** Caso in cui la query ha successo : l'oggetto viene spacchettato tramite l'api Gson e passato all'Activity
     * tramite il metodo di fillCSeries*/
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
                fillSeries(seriesList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
