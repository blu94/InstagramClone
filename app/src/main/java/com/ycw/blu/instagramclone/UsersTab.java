package com.ycw.blu.instagramclone;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private ListView userTabListView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private TextView userTabLoadingTxt;

    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);
        userTabListView = view.findViewById(R.id.userTabListView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
        userTabLoadingTxt = view.findViewById(R.id.userTabLoadingTxt);

        userTabListView.setOnItemClickListener(UsersTab.this);
        userTabListView.setOnItemLongClickListener(UsersTab.this);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {
                            arrayList.add(user.getUsername());

                        }

                        userTabListView.setAdapter(arrayAdapter);
                        userTabLoadingTxt.animate().alpha(0).setDuration(500);
                        userTabListView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), UsersPosts.class);
        intent.putExtra("username",arrayList.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ParseQuery <ParseUser> friend = ParseUser.getQuery();
        friend.whereEqualTo("username", arrayList.get(position));
        friend.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null) {
//                    FancyToast.makeText(getContext(),
//                            user.get("profileProfession") + "",
//                            FancyToast.LENGTH_LONG,
//                            FancyToast.SUCCESS,
//                            false
//                            ).show();
                    final PrettyDialog userProfileDialog = new PrettyDialog(getContext());
                    userProfileDialog.setTitle(user.getUsername()+"'s Info")
                            .setMessage(
                                    user.get("profileName")+"\n"+
                                            user.get("profileBio")+"\n"+
                                            user.get("profileProfession")+"\n"+
                                            user.get("profileHobbies")+"\n"+
                                            user.get("profileSport")+"\n"
                            )
                            .setIcon(R.drawable.ic_person_black_24dp)
                            .addButton("OK",
                                    R.color.pdlg_color_white,
                                    R.color.pdlg_color_green,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            userProfileDialog.dismiss();
                                        }
                                    })
                            .show();
                }
            }
        });
        return true;
    }
}
