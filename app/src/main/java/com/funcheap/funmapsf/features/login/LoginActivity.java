package com.funcheap.funmapsf.features.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.features.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.btnFB)
    Button btnFB;
    @BindView(R.id.btnGoogle)
    Button btnGoogle;
    @BindView(R.id.btnGuestLogin)
    Button btnGuest;

    @BindView(R.id.splash_layout)
    LinearLayout splash_screen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    public void onLogin(View view) {
        btnGuest.setVisibility(View.GONE);
        btnGoogle.setVisibility(View.GONE);
        btnFB.setVisibility(View.GONE);
        DisplaySplashTask task = new DisplaySplashTask();
        task.execute();

    }

    private class DisplaySplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            splash_screen.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            //some heavy processing resulting in a Data String
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);

        }
    }
}

