package com.ycw.blu.instagramclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.w3c.dom.Text;

public class Welcome extends AppCompatActivity {
    private ParseUser currentUser;
    private Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView txtWelcome = findViewById(R.id.txtWelcome);
        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            txtWelcome.setText("Welcome " + currentUser.get("username"));
        }
        else {
            txtWelcome.setText("Login error occur");
        }

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.logOut();

                FancyToast.makeText(Welcome.this,
                        "Logout successfully!",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false).show();

                finish();

            }
        });

    }
}
