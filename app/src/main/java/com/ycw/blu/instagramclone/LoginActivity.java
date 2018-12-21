package com.ycw.blu.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginEmail,loginPassword;
    private Button btnLoginSignup,btnLoginLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        btnLoginSignup = findViewById(R.id.btnLoginSignup);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);

        btnLoginSignup.setOnClickListener(this);
        btnLoginLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
//            ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity ();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginLogin:

                final ParseUser appUser = new ParseUser();
                appUser.logInInBackground(loginEmail.getText().toString(),
                        loginPassword.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null && e == null) {
                                    FancyToast.makeText(LoginActivity.this,
                                            ParseUser.getCurrentUser().getUsername() + " login successfully",
                                            FancyToast.LENGTH_LONG,
                                            FancyToast.SUCCESS,
                                            false).show();
                                    transitionToSocialMediaActivity ();
                                }
                                else {
                                    FancyToast.makeText(LoginActivity.this,
                                            "Login fail \n" + e,
                                            FancyToast.LENGTH_LONG,
                                            FancyToast.ERROR,
                                            false).show();
                                }
                            }
                        });

                break;
            case R.id.btnLoginSignup:

                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);

                break;
        }
    }

    private void transitionToSocialMediaActivity () {
        Intent intent = new Intent(LoginActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
