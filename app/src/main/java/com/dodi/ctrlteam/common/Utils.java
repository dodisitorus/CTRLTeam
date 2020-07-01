package com.dodi.ctrlteam.common;

import android.content.Context;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    public static String base64Encode(String email, String password) {
        String auth = email + ":" + password;
        return Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
    }

    public static void hideSoftKey(View view) {
        if (view.getContext() != null) {
            InputMethodManager iml = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (iml != null) {
                iml.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}