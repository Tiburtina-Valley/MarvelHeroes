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
import it.tiburtinavalley.marvelheroes.model.Stories;

public abstract class StoriesVolley implements Response.ErrorListener, Response.Listener<String>{
    private String urlBase = "https://gateway.marvel.com/v1/public/characters/%s";
    private String apiKey = "ts=1&apikey=a5f7b1501c40d87b927d3176fe38f22f&hash=dad24154bc30827c2290b5bd86f088fa&limit=100";//"ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea&limit=100";//"ts=1&apikey=68bdde3ebf9ba45c6c11839bd1f51cc3&hash=6433747692d0e40eaf799ef75ccc78ea";
    private RequestQueue requestQueue;

    public abstract void fillStories(List<Stories> storiesList);

    public StoriesVolley(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getStoriesInfo(String heroId){
        String story = heroId + "/stories?";
        storiesApiCall(story);
    }

    private void storiesApiCall(String storyUrl){
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
            Type listType = new TypeToken<List<Stories>>() {
            }.getType();
            List<Stories> storiesList = gson.fromJson(comics, listType);
            if (storiesList != null && storiesList.size() > 0) {
                //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                fillStories(storiesList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
