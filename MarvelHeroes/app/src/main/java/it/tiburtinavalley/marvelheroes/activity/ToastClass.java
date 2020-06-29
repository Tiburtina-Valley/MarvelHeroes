package it.tiburtinavalley.marvelheroes.activity;

import android.content.Context;
import android.widget.Toast;

public class ToastClass {

    private Context context;

    public ToastClass(Context context) {
        this.context = context;
    }
    public void showToast(CharSequence text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this.context, text, duration);
        toast.show();
    }

}
