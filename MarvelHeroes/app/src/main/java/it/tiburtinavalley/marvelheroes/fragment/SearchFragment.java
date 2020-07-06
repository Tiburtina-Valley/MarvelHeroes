package it.tiburtinavalley.marvelheroes.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.MainActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroAdapter;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;

public class SearchFragment extends Fragment {

    Holder holder;
    MarvelApiVolley volleyMarvel;
    View rootView;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Prende il context relativo alla activity in cui si trova il fragment
        this.context = Objects.requireNonNull(getActivity()).getApplicationContext();

        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        // Imposta titolo della action bar
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle(R.string.title_search);

        holder = new Holder();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            // Chiude la tastiera quando si esce dal fragment
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    class Holder implements TextView.OnEditorActionListener {
        final RecyclerView rvHeroes;
        final EditText etHeroSearch;
        private ProgressBar loading;

        public Holder() {
            this.rvHeroes = rootView.findViewById(R.id.rvHeroes);
            loading = rootView.findViewById(R.id.progress_loader);

            volleyMarvel = new MarvelApiVolley(rootView.getContext()) {

                @Override
                public void fillList(List<HeroModel> heroes) {
                    fillRecView(heroes);
                }

                private void fillRecView(List<HeroModel> heroes) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvHeroes.setLayoutManager(layoutManager);
                    HeroAdapter mAdapter = new HeroAdapter(heroes, getActivity());
                    rvHeroes.setAdapter(mAdapter);
                    loading.setVisibility(View.GONE);
                }
            };

            this.etHeroSearch = rootView.findViewById(R.id.etHeroSearch);
            etHeroSearch.setOnEditorActionListener(this);

            if (etHeroSearch.requestFocus()) {
                // per aprire la tastiera quando viene richiesto il focus alla EditText
                ((InputMethodManager) Objects.requireNonNull(Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE))).toggleSoftInput(
                        InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY
                );
            }

        }

        /** metodo per nascondere la tastiera una volta effettuata la ricerca */
        public void hideSoftKeyboard(Activity activity){
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            loading.setVisibility(View.VISIBLE);
            String nameStartsWith = etHeroSearch.getText().toString();
            // controllo nel momento in cui si cerca di fare una query senza aver inserito alcuna lettera
            if(nameStartsWith.isEmpty()){
                hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                loading.setVisibility(View.GONE);
                //toast che avverte l'utente dell'errore
                ToastClass toast = new ToastClass(context);
                toast.showToast(getString(R.string.msg_empty_search));
                return true;
            }
            hideSoftKeyboard(Objects.requireNonNull(getActivity()));

            // per verificare la disponibilità della connessione ad interet
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                volleyMarvel.getCharactersInfo(nameStartsWith);
                return true;
            }
            else {
                //toast che avverte l'utente della mancanza di connessione
                loading.setVisibility(View.GONE);
                ToastClass toast = new ToastClass(context);
                toast.showToast(context.getString(R.string.msg_internet_required));
                return false;
            }
        }
    }


}
