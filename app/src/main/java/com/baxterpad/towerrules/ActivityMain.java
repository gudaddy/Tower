package com.baxterpad.towerrules;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Chen on 8/8/2015.
 */
public class ActivityMain extends ActivityHideSystemUI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        addPreferencesFromResource(R.xml.preferences);

        setContentView(R.layout.activity_main);

        final Resources resources = getResources();
        final View imageView = findViewById(R.id.background_image);
        final ImageButton helpButton = (ImageButton) findViewById(R.id.help_button);
        final ImageButton settingsButton = (ImageButton) findViewById(R.id.settings_button);

//        final View logoView = findViewById(R.id.company_logo);
//        final View appLogoView = findViewById(R.id.app_logo);
//        final View welcomeMessageView = findViewById(R.id.welcome_message);

//        logoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO open url to our website?
//            }
//        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityGame.class);
                startActivity(intent);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityHelp.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivitySettings.class);
                startActivity(intent);
            }
        });
    }
}
