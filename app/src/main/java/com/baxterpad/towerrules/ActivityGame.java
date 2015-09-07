package com.baxterpad.towerrules;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.baxterpad.towerrules.util.GUITools;

/**
 * Created by Chen on 8/8/2015.
 */
public class ActivityGame extends ActivityHideSystemUI {

    /**
     * For preventing double clicks
     */
    private long mLastClickTime = 0;

    /**
     * Instance of TextSwitcher used for animations during text changes.
     */
    private TextSwitcher textSwitcher;
    private Resources resources;
    private Button restartButton;
    private View rulesTextView;

    /**
     * Declare the in and out animations and initialize them
     */
    private Animation fade_in;
    private Animation fade_out;
    private Animation slide_in;
    private Animation slide_out;

    /**
     * Game variables
     */
    private enum GameState { GAME_START, IN_PROGRESS, GAME_OVER };

    private static GameState game_state = GameState.GAME_START;
    private static int score = 0;

    private final static int TEXT_SIZE_NORMAL = 50;
    private final static int TEXT_SIZE_SMALL = 45;
    private final static int TEXT_SIZE_SMALLER = 40;
    private final static int TEXT_SIZE_TINY = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ActivityGame", "onCreate(): Creating view");
        setContentView(R.layout.activity_game);

        // UI objects for modifying the UI based on game state
        resources = getResources();

        //final View restartButtonView = findViewById(R.id.reset_button_view);
        restartButton = (Button) findViewById(R.id.restart_button);
        rulesTextView = findViewById(R.id.text_switcher_view);

        // Update background image from preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String backgroundPref = sharedPref.getString(ActivitySettings.KEY_WALLPAPER, "");
        ActivityMain.setBackgroundFromPref(rulesTextView, backgroundPref);

        textSwitcher = (TextSwitcher) findViewById(R.id.text_switcher);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(ActivityGame.this);
                textView.setTextSize(TEXT_SIZE_NORMAL);
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                return textView;
            }
        });
        textSwitcher.setText(resources.getString(R.string.first_block));

        fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
        slide_in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slide_out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        // Set the animation type of textSwitcher
        // Slide animation seems smoother than fade
        //textSwitcher.setInAnimation(fade_in);
        //textSwitcher.setOutAnimation(fade_out);
        textSwitcher.setInAnimation(slide_in);
        textSwitcher.setOutAnimation(slide_out);

        // Action based on click
        rulesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ActivityGame",
                        "onClick(): Click detected (Game state: " + game_state + ")");
                if (game_state == GameState.GAME_OVER) {
                    return;
                }

                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                getNextRule();
            }
        });

        restartButton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Log.d("ActivityGame",
                        "onLongClick(): Long click detected (Game state: " + game_state + ")");
                if (game_state == GameState.IN_PROGRESS) {
                    game_state = GameState.GAME_OVER;

                    textSwitcher.setInAnimation(slide_in);
                    textSwitcher.setOutAnimation(slide_out);

                    Log.i("ActivityGame", "onLongClick(): Score = " + score);
                    String text = String.format(resources.getString(R.string.game_over), score);
                    adjustTextSize(TEXT_SIZE_NORMAL);
                    textSwitcher.setText(text);
                    restartButton.setText(R.string.replay_button_text);

                } else if (game_state == GameState.GAME_START ||
                        game_state == GameState.GAME_OVER) {
                    restartGame();
                    finish();
                    Log.i("ActivityGame", "onLongClick(): End game");
                }

                GUITools.hideUI(ActivityGame.this);
                return true;
            }
        });
        Log.d("ActivityGame", "onCreate(): Done");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ActivityGame", "onStart(): Starting game");

        Rules.initializeRules();

        restartGame();

        GUITools.hideUI(ActivityGame.this);
    }

    @Override
    public void onBackPressed() {
        Log.i("ActivityGame", "onBackPressed(): Back press detected");
        // Only show this popup if game state is IN_PROGRESS
        // Otherwise, the game is in a state where the back button will just take you
        // to the main page.
        if (game_state == GameState.IN_PROGRESS) {
            new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit the game?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Same behavior as if the user holds down the restart button
                        game_state = GameState.GAME_OVER;

                        textSwitcher.setInAnimation(slide_in);
                        textSwitcher.setOutAnimation(slide_out);

                        Log.i("ActivityGame", "onBackPressed(): Score = " + score);
                        String text = String.format(resources.getString(R.string.game_over), score);
                        adjustTextSize(TEXT_SIZE_NORMAL);
                        textSwitcher.setText(text);
                        restartButton.setText(R.string.replay_button_text);

                        // Usually, you'll have a finish() call here but we don't want
                        // that behavior. Rather we want to take the user back to the
                        // score page.
                        GUITools.hideUI(ActivityGame.this);
                    }
                })
                // Positive button is "no" to set the ordering when it is displayed
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GUITools.hideUI(ActivityGame.this);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        GUITools.hideUI(ActivityGame.this);
                    }
                })
                .show();
        } else {
            restartGame();
            finish();
            Log.i("ActivityGame", "onBackPressed(): End game");
        }
    }

    private static void restartGame() {
        Log.i("ActivityGame", "restartGame(): Restarting game");
        Rules.shuffleRules();
        score = 0;
        game_state = GameState.GAME_START;
    }

    /**
     * Game logic code. This will also update the UI based on the state of the game.
     *
     * @TODO better design this and separate game logic from UI code
     *
     * game state: waiting for start (hide 'restart' button)
     * game state: once started, display 'restart' button, display rule
     * game event: on tap, display new rule
     * game event: on restart, go back to start
     */
    private void getNextRule() {
        Log.i("ActivityGame",
                "getNextRule(): Getting next rule (Game state: " + game_state + ", Score: " + score + ")");
        game_state = GameState.IN_PROGRESS;

        String rule = Rules.nextRule();
        //@TODO make sure rules are limited to 256 characters?
        autoAdjustTextSizeAndSetText(rule);
        score++;

        try {
            // Sleep to allow animation to finish ?
            Thread.sleep(100);
        } catch (InterruptedException e) { /* Do nothing */ }
    }

    private void adjustTextSize(int textSize) {
        Log.d("ActivityGame", "adjustTextSize(): Changing text size to " + textSize);

        // There should always be 2 child views (current and next)
        Log.d("ActivityGame", "adjustTextSize(): Child count = " + textSwitcher.getChildCount());

        TextView textView1 = (TextView) textSwitcher.getChildAt(0);
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        // This fixes the alignment issues when switching text
        textView1.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        textView1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        Log.d("ActivityGame", "adjustTextSize(): Text on child 1 = " + textView1.getText());

        if (textSwitcher.getChildCount() > 1) {
            TextView textView2 = (TextView) textSwitcher.getChildAt(1);
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            textView2.setLayoutParams(
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            textView2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            Log.d("ActivityGame", "adjustTextSize(): Text on child 2 = " + textView2.getText());
        }
    }

    private void autoAdjustTextSizeAndSetText(String text) {
        // Calculate based on screen size and string length, whether to use normal, small, or tiny text size.

        int strLength = text.length();
        // 0.75 - ldpi
        // 1.0 - mdpi
        // 1.5 - hdpi
        // 2.0 - xhdpi
        // 3.0 - xxhdpi
        // 4.0 - xxxhdpi

        int textSize = TEXT_SIZE_NORMAL;
        String tmpText = text;

        if (!GUITools.isTablet(getApplicationContext())) {
            if (strLength >= 255) {
                tmpText = text.substring(0,255) + "...";
                textSize = 25;
            } else if (strLength >= 150) {
                textSize = 25;
            } else if (strLength >= 115) {
                textSize = 30;
            } else if (strLength >= 100) {
                textSize = 35;
            } else if (strLength >= 85) {
                textSize = 40;
            } else if (strLength >= 70) {
                textSize = 45;
            } else {
                textSize = TEXT_SIZE_NORMAL;
            }
        } else {
            Log.d("ActivityGame", "autoAdjustTextSizeAndSetText(): Tablet detected, not changing text size");
        }

        Log.d("ActivityGame", "autoAdjustTextSizeAndSetText(): Rule string length = " + strLength);
        adjustTextSize(textSize);
        // No animation
        //textSwitcher.setCurrentText(tmpText);
        // With animation
        textSwitcher.setText(tmpText);
    }
}

// adb devices
// adb shell
// adb logcat ActivityGame:D *:S
// adb logcat -c