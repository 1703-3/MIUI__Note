package net.micode.notes.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarUtils {

    public static void tip(View view, String content) {
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).show();
    }

}
