package com.baxterpad.towerrules;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.baxterpad.towerrules.util.GUITools;

/**
 * Created by Chen on 8/8/2015.
 */
public class ActivityMain extends ActivityHideSystemUI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ActivityMain",
                "onCreate(): Created activity");

        setContentView(R.layout.activity_main);

        final Resources resources = getResources();
        final View imageView = findViewById(R.id.background_image);
        final ImageButton helpButton = (ImageButton) findViewById(R.id.help_button);
        final ImageButton settingsButton = (ImageButton) findViewById(R.id.settings_button);

        // Get image from ImageView, on phones (not tablets), resize the image to fit
        if (!GUITools.isTablet(getApplicationContext())) {
            ImageView appLogo = (ImageView)findViewById(R.id.app_logo);
            Bitmap bitmap = ((BitmapDrawable)appLogo.getDrawable()).getBitmap();
            if (bitmap != null) {
                Bitmap resized =
                        Bitmap.createScaledBitmap(
                                bitmap,
                                (int)(bitmap.getWidth()*0.5),
                                (int)(bitmap.getHeight()*0.5),
                                true);
                appLogo.setImageBitmap(resized);
            } else {
                // If we can't resize it, then it's probably too big so
                // don't display it.
                appLogo.setVisibility(View.INVISIBLE);
            }
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String backgroundPref = sharedPref.getString(ActivitySettings.KEY_WALLPAPER, "");
        setBackgroundFromPref(imageView, backgroundPref);

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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ActivityMain",
                "onStart(): Started activity");
        GUITools.hideUI(ActivityMain.this);

        // Update activity based on changes to settings
        View imageView = findViewById(R.id.background_image);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String backgroundPref = sharedPref.getString(ActivitySettings.KEY_WALLPAPER, "");
        setBackgroundFromPref(imageView, backgroundPref);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ActivityMain",
                "onResume(): Resumed activity");
    }

    public static void setBackgroundFromPref(View view, String key) {
        Log.d("ActivityMain",
                "setBackgroundFromPref(): Background Pref = " + key);

        if (key == null || key.length() == 0) {
            Log.e("ActivityMain", "onCreate(): Missing background preference. Setting to default");
            view.setBackgroundResource(R.drawable.wood_light_light);
        } else if (key.equals("wood_grain_1")) {
            view.setBackgroundResource(R.drawable.wood_light_light);
        } else if (key.equals("wood_grain_2")) {
            view.setBackgroundResource(R.drawable.wood_light);
        } else if (key.equals("wood_grain_3")) {
            view.setBackgroundResource(R.drawable.wood_dark);
        } else {
            Log.e("ActivityMain", "onCreate(): Unknown background preference. Setting to default");
            view.setBackgroundResource(R.drawable.wood_light_light);
        }
    }

}
