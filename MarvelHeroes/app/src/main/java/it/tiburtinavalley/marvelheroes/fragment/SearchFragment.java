package it.tiburtinavalley.marvelheroes.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.MainActivity;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroAdapter;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;

public class SearchFragment extends Fragment {

    Holder holder;
    MarvelApiVolley volleyMarvel;
    ImageApiVolley imgVolley;
    View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        imgVolley = new ImageApiVolley(getActivity().getApplicationContext());

        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        holder = new Holder();

        return rootView;
    }

    class Holder {
        final RecyclerView rvHeroes;
        final EditText etHeroSearch;

        public Holder() {
            this.rvHeroes = rootView.findViewById(R.id.rvHeroes);

            volleyMarvel = new MarvelApiVolley(getActivity().getApplicationContext()) {

                @Override
                public void fillList(List<HeroModel> heroes) {
                    fillRecView(heroes);
                }

                private void fillRecView(List<HeroModel> heroes) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvHeroes.setLayoutManager(layoutManager);
                    HeroAdapter mAdapter = new HeroAdapter(heroes, getActivity().getApplicationContext());
                    rvHeroes.setAdapter(mAdapter);
                }
            };

            this.etHeroSearch = rootView.findViewById(R.id.etHeroSearch);
            this.etHeroSearch.setOnEditorActionListener(new ConfButtonListener());
        }


        public class ConfButtonListener  implements TextView.OnEditorActionListener {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String nameStartsWith = etHeroSearch.getText().toString();
                    volleyMarvel.getCharactersInfo(nameStartsWith);
                    hideSoftKeyboard(getActivity());
                    return true;
                }
                else {
                    return false;
                }
            }
        }

        public void hideSoftKeyboard(Activity activity){
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }

    }


}
