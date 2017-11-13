package com.funcheap.funmapsf.features.login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.features.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnGuestLogin)
    Button btnGuest;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.imgLogo)
    ImageView imgLogo;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if(isFacebookLoggedIn()){
            btnGuest.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        displaySplashScreen();

        // Skip login to speed up testing
        if (!getResources().getBoolean(R.bool.showLogin)) {
            onLogin(btnGuest);
        }
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });

    }
	    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void displaySplashScreen() {
        DisplaySplashTask task = new DisplaySplashTask();
        task.execute();
    }


    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }


    public void onLogin(View view) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private class DisplaySplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            btnGuest.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            //some heavy processing resulting in a Data String
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            btnGuest.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);

            int height = Resources.getSystem().getDisplayMetrics().heightPixels;

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(btnGuest, View.ALPHA, 0, 1)
                            .setDuration(500),
                    ObjectAnimator.ofFloat(loginButton, View.ALPHA, 0, 1)
                            .setDuration(500),
                    ObjectAnimator.ofFloat(imgLogo, View.TRANSLATION_Y, 0, height / -5)
                            .setDuration(500)
            );
            set.start();
        }
    }
}

