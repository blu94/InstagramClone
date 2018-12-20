package com.ycw.blu.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ABoNycJPr6fQjCUYYAVjgFIRAeXNeOYInR6qg4zL")
                // if defined
                .clientKey("tWh3HWG5ws0Y0gv9Mp65oDXZNIp21Btxp1YKMCYq")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
