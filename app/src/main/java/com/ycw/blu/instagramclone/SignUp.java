package com.ycw.blu.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText signupEmail,signupUsername,signupPassword;
    private Button btnSignupSignup, btnSignupLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");

        signupEmail = findViewById(R.id.signupEmail);
        signupUsername = findViewById(R.id.signupUsername);
        signupPassword = findViewById(R.id.signupPassword);

        signupPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignupSignup);
                }
                return false;
            }
        });

        btnSignupSignup = findViewById(R.id.btnSignupSignup);
        btnSignupLogin = findViewById(R.id.btnSignupLogin);

        btnSignupSignup.setOnClickListener(this);
        btnSignupLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
//            ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignupSignup:
                if (signupEmail.getText().toString().equals("") ||
                        signupUsername.getText().toString().equals("") ||
                        signupPassword.getText().toString().equals("")) {

                    FancyToast.makeText(SignUp.this,
                            "Email, username, password is required!",
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false).show();
                }
                else {
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(signupEmail.getText().toString());
                    appUser.setUsername(signupUsername.getText().toString());
                    appUser.setPassword(signupPassword.getText().toString());

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up " + signupUsername.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUp.this,
                                        appUser.getUsername() + " is signed up",
                                        FancyToast.LENGTH_LONG,
                                        FancyToast.SUCCESS,
                                        false).show();

                                signupEmail.setText("");
                                signupUsername.setText("");
                                signupPassword.setText("");

                                transitionToSocialMediaActivity ();
                            }
                            else {
                                FancyToast.makeText(SignUp.this,
                                        "Sign up fail \n" + e,
                                        FancyToast.LENGTH_LONG,
                                        FancyToast.ERROR,
                                        false).show();
                            }

                            progressDialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btnSignupLogin:

                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);

                break;
        }
    }

    public void rootLayoutTapped(View view) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void transitionToSocialMediaActivity () {
        Intent intent = new Intent(SignUp.this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
