package com.funcheap.funmapsf.features.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.funcheap.funmapsf.R;
import com.funcheap.funmapsf.features.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.funcheap.funmapsf.R.id.btnFB;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    public void onLogin(View view)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}

