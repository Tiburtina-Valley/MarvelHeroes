package it.tiburtinavalley.marvelheroes.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    class Holder {
        final RecyclerView rvHeroes;
        final EditText etHeroSearch;

        public Holder() {
            this.rvHeroes = rootView.findViewById(R.id.rvHeroes);

            volleyMarvel = new MarvelApiVolley(rootView.getContext()) {

                @Override
                public void fillList(List<HeroModel> heroes) {
                    fillRecView(heroes);
                }

                private void fillRecView(List<HeroModel> heroes) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvHeroes.setLayoutManager(layoutManager);
                    HeroAdapter mAdapter = new HeroAdapter(heroes, (Context) getActivity());
                    rvHeroes.setAdapter(mAdapter);
                }
            };

            this.etHeroSearch = rootView.findViewById(R.id.etHeroSearch);
            etHeroSearch.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        String nameStartsWith = etHeroSearch.getText().toString();
                        volleyMarvel.getCharactersInfo(nameStartsWith);
                        return true;
                    }
                    return false;

                }
            });


            etHeroSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String nameStartsWith = etHeroSearch.getText().toString();
                    volleyMarvel.getCharactersInfo(nameStartsWith);
                    return true;
                }
            });


            if (etHeroSearch.requestFocus()) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                        InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY
                );
            }

        }



        public void hideSoftKeyboard(Activity activity){
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }

    }


}
