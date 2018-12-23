package com.ycw.blu.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {
    private LinearLayout usersPostsLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        Intent receivedIntentObject = getIntent();
        final String receiveUsername = receivedIntentObject.getStringExtra("username");
//        FancyToast.makeText(UsersPosts.this,
//                receiveUsername,
//                FancyToast.LENGTH_LONG,
//                FancyToast.SUCCESS,
//                false).show();
        usersPostsLinearLayout = (LinearLayout) findViewById(R.id.usersPostsLinearLayout);

        setTitle(receiveUsername);
        ParseQuery<ParseObject> parseQuery = new ParseQuery("Photo");
        parseQuery.whereEqualTo("username", receiveUsername);
        parseQuery.orderByDescending("createdAt");



        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {


                if (e == null && objects.size() > 0) {
                    for (ParseObject post: objects) {
                        final TextView postDescription = new TextView(UsersPosts.this);
                        postDescription.setText(post.get("image_des") != null ?
                                post.get("image_des").toString() : "");

                        ParseFile postPicture = (ParseFile) post.get("picture");

                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if (e == null && data != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(UsersPosts.this);
                                    LinearLayout.LayoutParams imageViewParams =
                                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageViewParams.setMargins(30,15,30,15);
                                    postImageView.setLayoutParams(imageViewParams);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setAdjustViewBounds(true);
                                    postImageView.setImageBitmap(bitmap);


                                    LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    descParams.setMargins(30,15,30,15);
                                    postDescription.setLayoutParams(descParams);
                                    postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setBackgroundColor(Color.BLUE);
                                    postDescription.setTextColor(Color.WHITE);
                                    postDescription.setTextSize(30f);

                                    usersPostsLinearLayout.addView(postImageView);
                                    usersPostsLinearLayout.addView(postDescription);
                                }
                            }
                        });
                    }
                }
                else {
                    FancyToast.makeText(UsersPosts.this,
                            receiveUsername+" doesn't have any post",
                            FancyToast.LENGTH_LONG,
                            FancyToast.INFO,
                            false).show();
                    finish();
                }

                dialog.dismiss();


            }
        });
    }
}
