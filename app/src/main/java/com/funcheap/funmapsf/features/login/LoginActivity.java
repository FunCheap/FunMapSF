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

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.features.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnGoogle)
    Button btnGoogle;
    @BindView(R.id.btnGuestLogin)
    Button btnGuest;
    @BindView(R.id.imgLogo)
    ImageView imgLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        displaySplashScreen();

    }

    private void displaySplashScreen() {
        DisplaySplashTask task = new DisplaySplashTask();
        task.execute();
    }


    public void onLogin(View view) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private class DisplaySplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            btnGuest.setVisibility(View.INVISIBLE);
            btnGoogle.setVisibility(View.INVISIBLE);
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
            btnGoogle.setVisibility(View.VISIBLE);

            int height = Resources.getSystem().getDisplayMetrics().heightPixels;

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(btnGuest, View.ALPHA, 0, 1)
                            .setDuration(500),
                    ObjectAnimator.ofFloat(btnGoogle, View.ALPHA, 0, 1)
                            .setDuration(500),
                    ObjectAnimator.ofFloat(imgLogo, View.TRANSLATION_Y, 0, height / -5)
                            .setDuration(500)
            );
            set.start();
        }
    }
}

