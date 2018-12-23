package com.ycw.blu.instagramclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener {
    private ImageView sharePicturePlaceholder;
    private EditText sharePictureDescription;
    private Button sharePictureShareBtn;
    private Bitmap receivedImageBitmap;

    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);
        sharePicturePlaceholder = view.findViewById(R.id.sharePicturePlaceholder);
        sharePictureDescription = view.findViewById(R.id.sharePictureDescription);
        sharePictureShareBtn = view.findViewById(R.id.sharePictureShareBtn);

        sharePicturePlaceholder.setOnClickListener(SharePictureTab.this);
        sharePictureShareBtn.setOnClickListener(SharePictureTab.this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sharePicturePlaceholder:
                if (android.os.Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext()
                                ,Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                }
                else {
                    getChosenImage();
                }
                break;
            case R.id.sharePictureShareBtn:
                if (receivedImageBitmap != null) {
                    if (sharePictureDescription.getText().toString().equals("")) {
                        FancyToast.makeText(getContext(),
                                "Please enter description",
                                FancyToast.LENGTH_LONG,
                                FancyToast.ERROR,
                                false).show();
                    }
                    else {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("pic.png",bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", sharePictureDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(getContext(),
                                            "Image upload successfully!",
                                            FancyToast.LENGTH_LONG,
                                            FancyToast.SUCCESS,
                                            false
                                    ).show();
                                }
                                else {
                                    e.printStackTrace();
                                    FancyToast.makeText(getContext(),
                                            "Error occur "+e.getMessage(),
                                            FancyToast.LENGTH_LONG,
                                            FancyToast.ERROR,
                                            false
                                    ).show();
                                }
                                progressDialog.dismiss();
                            }
                        });

                    }
                }
                else {
                    FancyToast.makeText(getContext(),
                            "You must select an image!",
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false).show();
                }
                break;
        }
    }

    private void getChosenImage() {
//        FancyToast.makeText(getContext(),
//                "Access grant",
//                FancyToast.LENGTH_LONG,
//                FancyToast.SUCCESS,
//                false).show();
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri selectedImage = data.getData();
                    String[] stringFilePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            stringFilePathColumn,
                            null,
                            null,
                            null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(stringFilePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                    sharePicturePlaceholder.setImageBitmap(receivedImageBitmap);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
