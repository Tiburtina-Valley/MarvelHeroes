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
import it.tiburtinavalley.marvelheroes.model.Creators;

public abstract class CreatorsVolley implements Response.ErrorListener, Response.Listener<String>{
    private String urlBase = "https://gateway.marvel.com/v1/public/comics/%s";
    private String apiKey = "ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea&limit=100";
    private RequestQueue requestQueue;

    public abstract void fillCreatorsInfo(List<Creators> creatorsList);

    public CreatorsVolley(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getCreatorsByComics(String comicId){
        String creator = comicId + "/creators?";
        creatorsApiCall(creator);
    }

    public void getCreatorsByEvents(String eventId){
        String creator = eventId + "/creators?";
        creatorsApiCall(creator);
    }

    public void getCreatorsBySeries(String serieId){
        String creator = serieId + "/creators?";
        creatorsApiCall(creator);
    }

    public void getCreatorsByStories(String storyId){
        String creator = storyId + "/creators";
        creatorsApiCall(creator);
    }


    private void creatorsApiCall(String creatorUrl){
        String url = urlBase + apiKey; // usiamo una stringa di appoggio così da poter ripetere la chiamata
        url = String.format(url, creatorUrl);
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
        String creators;
        try {
            JSONObject jsonObject = new JSONObject(response);
            creators = jsonObject.getJSONObject("data").getJSONArray("results").toString();
            Type listType = new TypeToken<List<Creators>>() {
            }.getType();
            List<Creators> creatorsList = gson.fromJson(creators, listType);
            if (creatorsList != null) {
                Log.w("CA", "" + creatorsList.size());
                //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                fillCreatorsInfo(creatorsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
