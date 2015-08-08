package com.baxterpad.towerrules;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
    private static String[] current_rules;
    private static int rule_index = 0;

    private final static int TEXT_SIZE_NORMAL = 50;
    private final static int TEXT_SIZE_SMALL = 40;
    private final static int TEXT_SIZE_TINY = 25;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        // UI objects for modifying the UI based on game state
        final Resources resources = getResources();

        //final View restartButtonView = findViewById(R.id.reset_button_view);
        final Button restartButton = (Button) findViewById(R.id.restart_button);
        final View rulesTextView = findViewById(R.id.text_switcher_view);

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

        // set the animation type of textSwitcher
        textSwitcher.setInAnimation(fade_in);
        textSwitcher.setOutAnimation(fade_out);

        // Action based on click
        rulesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (game_state == GameState.GAME_OVER) {
                    return;
                }

                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                getNextRule(textSwitcher, resources);
            }
        });

        restartButton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (game_state == GameState.IN_PROGRESS) {
                    game_state = GameState.GAME_OVER;

                    textSwitcher.setInAnimation(slide_in);
                    textSwitcher.setOutAnimation(slide_out);

                    String text = String.format(resources.getString(R.string.game_over), score);
                    adjustTextSize(textSwitcher, TEXT_SIZE_NORMAL);
                    textSwitcher.setText(text);
                    restartButton.setText(R.string.button_replay);

                } else if (game_state == GameState.GAME_START ||
                        game_state == GameState.GAME_OVER) {
                    restartGame();
                    finish();
                }
                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        try {
            Rules.initializeRules();
        } catch (Exception e) {
            e.printStackTrace();
        }

        restartGame();
    }


    private static void restartGame() {
        current_rules = Rules.shuffleRules();
        rule_index = 0;
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
     * @param textSwitcher
     */
    private static void getNextRule(
        TextSwitcher textSwitcher,
        Resources resources) {

        game_state = GameState.IN_PROGRESS;

        if (rule_index >= current_rules.length) {
            current_rules = Rules.shuffleRules();
            rule_index = 0;
        }

        //@TODO formatting - change font size based on length and screen orientation
        //@TODO make sure rules are limited to 256 characters?
        //adjustTextSize(textSwitcher, TEXT_SIZE_NORMAL);
        //textSwitcher.setText(current_rules[rule_index]);
        autoAdjustTextSizeAndSetText(
                textSwitcher,
                current_rules[rule_index],
                resources);
        //android:textSize="50sp"
        // portrait 14 * 11 = 154
        // landscape 6 * 21 = 126
        rule_index++;
        score++;

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) { /* Do nothing */ }
    }

    private static void adjustTextSize(
            TextSwitcher textSwitcher,
            int textSize) {
        TextView textView = (TextView) textSwitcher.getChildAt(0);
        textView.setTextSize(textSize);
    }

    private static void autoAdjustTextSizeAndSetText(
        TextSwitcher textSwitcher,
        String text,
        Resources resources) {
        // TODO get screen res or size?
        // Calculate based on screen size and string length, whether to use normal, small, or tiny text size.
        TextView textView = (TextView) textSwitcher.getChildAt(0);
        int strLength = text.length();
        // 0.75 - ldpi
        // 1.0 - mdpi
        // 1.5 - hdpi
        // 2.0 - xhdpi
        // 3.0 - xxhdpi
        // 4.0 - xxxhdpi

//        float density = resources.getDisplayMetrics().density;
//        Log.i("info", "density: " + density); //??? adb logcat???

        int textSize = TEXT_SIZE_NORMAL;
//        if (density > 1.99) {
//            textSize = TEXT_SIZE_NORMAL;
//        } else if (density > 0.99) {
//            textSize = TEXT_SIZE_SMALL;
//        } else {
//            textSize = TEXT_SIZE_TINY;
//        }
        if (strLength > 256) {
            text = text.substring(0,256);
            textSize = TEXT_SIZE_TINY;
        } else if (strLength > 128) {
            textSize = TEXT_SIZE_SMALL;
        }
        //http://stackoverflow.com/questions/16017165/auto-fit-textview-for-android/
        textView.setTextSize(textSize);
        textSwitcher.setText(text);
    }

}
