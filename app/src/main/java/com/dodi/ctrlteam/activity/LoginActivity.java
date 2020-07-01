package com.dodi.ctrlteam.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ale.infra.contact.IRainbowContact;
import com.ale.rainbowsdk.RainbowSdk;
import com.dodi.ctrlteam.R;
import com.dodi.ctrlteam.common.UserPreferences;
import com.dodi.ctrlteam.common.Utils;
import com.dodi.ctrlteam.rainbow.RainbowConnection;
import com.dodi.ctrlteam.rainbow.RainbowConnectionListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements RainbowConnectionListener.Connection, RainbowConnectionListener.Login {

    private String emailFinal = "";

    // UI references.
    @BindView(R.id.login_progress) View mProgressView;
    @BindView(R.id.login_form) View mLoginFormView;
    @BindView(R.id.editTextEmail) AutoCompleteTextView mEmailView;
    @BindView(R.id.editTextPassword) EditText mPasswordView;
    @BindView(R.id.cirLoginButton) Button mEmailSignInButton;
    @BindView(R.id.actionpassword) ImageView actionpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        ButterKnife.bind(this);

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        mEmailSignInButton.setOnClickListener(view -> attemptLogin());

        boolean connectted = RainbowSdk.instance().connection().isConnected();
        if (connectted) {
            if (!RainbowConnection.getUserLoginInCache().isEmpty()) {
                mEmailView.setText(RainbowConnection.getUserLoginInCache());
            }

            if (!RainbowConnection.getUserPasswordInCache().isEmpty()) {
                mPasswordView.setText(RainbowConnection.getUserPasswordInCache());
            }
        }

        this.RainbowConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    private void RainbowConnecting() {
        showProgress(true);
        RainbowConnection.startConnection(LoginActivity.this, this);
    }

    private void attemptLogin() {
        Utils.hideSoftKey(mEmailView);

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            RainbowLogin(email, password);
        }
    }

    // rainbow login
    private void RainbowLogin(String email, String password) {
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        // request login
        RainbowConnection.startSignIn(email, password, this);
    }

    @Override
    public void onConnectionSuccess() {
        runOnUiThread(() -> {
            showProgress(false);
            // Toast.makeText(LoginActivity.this, "Connection Success", Toast.LENGTH_SHORT).show();
            // check user login -> request login if true
            if (UserPreferences.getDataBool(UserPreferences.loginKey, getApplicationContext())) {
                emailFinal = UserPreferences.getData(UserPreferences.emailKey, getApplicationContext());
                RainbowLogin(emailFinal, UserPreferences.getData(UserPreferences.passKey, getApplicationContext()));
            }
        });
    }

    @Override
    public void onConnectionFailed(String error) {
        runOnUiThread(() -> {
            showProgress(false);
            Toast.makeText(LoginActivity.this, "Connection " + error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onSignInSuccess() {
        runOnUiThread(() -> {
            // Toast.makeText(LoginActivity.this , "Sign In Success" , Toast.LENGTH_SHORT).show();
            //Do something on UI Thread here
//            showProgress(false);

            IRainbowContact detailUser = RainbowSdk.instance().myProfile().getConnectedUser();

            // -- BOA : set user preference => Local Storage
            UserPreferences.setDataBool(UserPreferences.loginKey, true, getApplicationContext());
            UserPreferences.setData(UserPreferences.emailKey, mEmailView.getText().toString(), getApplicationContext());
            UserPreferences.setData(UserPreferences.passKey, mPasswordView.getText().toString(), getApplicationContext());
            UserPreferences.setData(UserPreferences.userIdKey, detailUser.getContactId(), getApplicationContext());
            UserPreferences.setData(UserPreferences.userIdKey, detailUser.getContactId(), getApplicationContext());
            UserPreferences.setData(UserPreferences.userFavWord, "a", getApplicationContext());

            startActivity(new Intent(LoginActivity.this, HomeScreen_Activity.class));
        });
    }

    @Override
    public void onSignInFailed(String error) {
        runOnUiThread(() -> {
            if (!mEmailView.getText().toString().isEmpty() || ((emailFinal != null))) {
                Toast.makeText(LoginActivity.this, error + ". Please try again.", Toast.LENGTH_SHORT).show();
            }
            //Do something on UI Thread here
            showProgress(false);
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void onActionPassword(View view) {
        if (mPasswordView.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
            mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            actionpassword.setImageResource(R.drawable.hide_password);
        } else {
            mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            actionpassword.setImageResource(R.drawable.show_password);
        }
    }
}

