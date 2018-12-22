package com.ycw.blu.instagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText socialMediaProfileName, socialMediaProfileBio,
            socialMediaProfileProfession, socialMediaProfileHobbies,
            socialMediaProfileSport;
    private Button btnProfileUpdate;
    private ProgressDialog progressDialog;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        socialMediaProfileName = view.findViewById(R.id.socialMediaProfileName);
        socialMediaProfileBio = view.findViewById(R.id.socialMediaProfileBio);
        socialMediaProfileProfession = view.findViewById(R.id.socialMediaProfileProfession);
        socialMediaProfileHobbies = view.findViewById(R.id.socialMediaProfileHobbies);
        socialMediaProfileSport = view.findViewById(R.id.socialMediaProfileSport);
        btnProfileUpdate = view.findViewById(R.id.btnProfileUpdate);

        final ParseUser appUser = ParseUser.getCurrentUser();
        // set user details
        socialMediaProfileName.setText(
                appUser.get("profileName") != null ?
                        appUser.get("profileName").toString() + "": "");
        socialMediaProfileBio.setText(
                appUser.get("profileBio") != null ?
                        appUser.get("profileBio").toString() + "": "");
        socialMediaProfileProfession.setText(
                appUser.get("profileProfession") != null ?
                        appUser.get("profileProfession").toString() + "": "");
        socialMediaProfileHobbies.setText(
                appUser.get("profileHobbies") != null ?
                        appUser.get("profileHobbies").toString() + "": "");
        socialMediaProfileSport.setText(
                appUser.get("profileSport") != null ?
                        appUser.get("profileSport").toString() + "": "");

        // update user details
        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Updating");
                progressDialog.show();

                appUser.put("profileName", socialMediaProfileName.getText().toString());
                appUser.put("profileBio", socialMediaProfileBio.getText().toString());
                appUser.put("profileProfession", socialMediaProfileProfession.getText().toString());
                appUser.put("profileHobbies", socialMediaProfileHobbies.getText().toString());
                appUser.put("profileSport", socialMediaProfileSport.getText().toString());

                appUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // fragment use getContext()
                            FancyToast.makeText(getContext(),
                                    "Profile save successfully",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,
                                    false).show();
                        }
                        else {
                            FancyToast.makeText(getContext(),
                                    e.getMessage(),
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.ERROR,
                                    false).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

}
