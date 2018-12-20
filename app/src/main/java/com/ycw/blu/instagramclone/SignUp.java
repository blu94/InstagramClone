package com.ycw.blu.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private Button btnSubmit, getAllKickBoxer, btnNextActivity;
    private EditText edtName, edtPunchSpeed, edtPunchPower, edtKickSpeed, edtKickPower;
    private TextView txtGetData;
    private String allKickBoxer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = findViewById(R.id.edtName);
        edtPunchSpeed = findViewById(R.id.edtPunchSpeed);
        edtPunchPower = findViewById(R.id.edtPunchPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        edtKickPower = findViewById(R.id.edtKickPower);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(SignUp.this);

        txtGetData = findViewById(R.id.txtGetData);
//        txtGetData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ParseQuery <ParseObject> parseQuery = ParseQuery.getQuery("kickBoxer");
//                parseQuery.getInBackground("9tm6S9z3LP", new GetCallback<ParseObject>() {
//                    @Override
//                    public void done(ParseObject object, ParseException e) {
//                        if (object != null && e == null) {
//                            txtGetData.setText("Name : " + object.get("name").toString()
//                                    + "\nKick Speed : " + object.get("punchSpeed").toString()
//                                    + "\nKick Power : " + object.get("punchPower").toString()
//                                    + "\nPunch Speed : " + object.get("kickSpeed").toString()
//                                    + "\nPunch Power : " + object.get("kickPower").toString());
//                        }
//                        else {
//                            FancyToast.makeText(SignUp.this,
//                                    "" + e,
//                                    FancyToast.LENGTH_LONG,
//                                    FancyToast.ERROR,
//                                    true).show();
//                        }
//                    }
//                });
//            }
//        });

        getAllKickBoxer = findViewById(R.id.getAllKickBoxer);
        getAllKickBoxer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allKickBoxer = "";

                ParseQuery <ParseObject> queryAll = ParseQuery.getQuery("kickBoxer");
//                queryAll.whereGreaterThan("punchPower", 3000);
                queryAll.whereGreaterThanOrEqualTo("punchPower", 300);
                queryAll.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                for (ParseObject kickBoxer : objects) {
                                    allKickBoxer = allKickBoxer + "\nName : " + kickBoxer.get("name") +
                                            "\nPunch Power : "+kickBoxer.get("punchPower") +
                                            "\nPunch Speed : "+kickBoxer.get("punchSpeed");
                                }
//                                FancyToast.makeText(SignUp.this,
//                                        "Successs\n" + allKickBoxer,
//                                        FancyToast.SUCCESS,
//                                        FancyToast.LENGTH_LONG,
//                                        false)
//                                        .show();
                                txtGetData.setText(allKickBoxer);
                            }
                        }
                    }
                });
            }
        });

        btnNextActivity = findViewById(R.id.btnNextActivity);
        btnNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,
                        SignUpLoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {

        try {
            final ParseObject kickBoxer = new ParseObject("kickBoxer");
            kickBoxer.put("name", edtName.getText().toString());
            kickBoxer.put("punchSpeed", Integer.parseInt(edtPunchSpeed.getText().toString()));
            kickBoxer.put("punchPower", Integer.parseInt(edtPunchPower.getText().toString()));
            kickBoxer.put("kickSpeed", Integer.parseInt(edtKickSpeed.getText().toString()));
            kickBoxer.put("kickPower", Integer.parseInt(edtKickPower.getText().toString()));
            kickBoxer.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUp.this,
                                kickBoxer.get("name") + " is saved successfully",
                                FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS,
                                true).show();
                    }
                    else {
                        FancyToast.makeText(SignUp.this,
                                "" + e,
                                FancyToast.LENGTH_LONG,
                                FancyToast.ERROR,
                                true).show();

                    }
                }
            });
        }
        catch (Exception e) {
            FancyToast.makeText(SignUp.this,
                    "" + e,
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    true).show();
        }

    }
}
