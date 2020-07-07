package it.tiburtinavalley.marvelheroes.activity;

import android.content.Context;
import android.widget.Toast;

/** Classe che viene chiamata se c'è bisogno di mostrare un Toast */
public class ToastClass {

    private Context context; //necessita del Context dell'Activity corrente quando viene invocata la classe

    public ToastClass(Context context) {
        this.context = context;
    }
    public void showToast(CharSequence text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this.context, text, duration);
        toast.show();
    }

}
