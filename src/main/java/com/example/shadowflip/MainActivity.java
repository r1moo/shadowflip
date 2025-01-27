
package com.example.shadowflip;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import android.media.MediaPlayer;
import android.animation.AnimatorSet;

public class MainActivity extends AppCompatActivity {

    TextView tv_Score, tv_Timer, tv_Move;
    ImageView btn_Home, btn_Reset;

    ImageView iv_11, iv_12, iv_13, iv_14, iv_21, iv_22, iv_23, iv_24, iv_31, iv_32, iv_33, iv_34, iv_41, iv_42, iv_43, iv_44;

    Integer[] cardsArray = {101, 102, 103, 104, 105, 106, 107, 108, 201, 202, 203, 204, 205, 206, 207, 208};

    int image101, image102, image103, image104, image105, image106, image107, image108,
            image201, image202, image203, image204, image205, image206, image207, image208;

    int firstCard, secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber = 1;
    int playerScore = 0;
    int playerMove = 0;
    CountDownTimer countDownTimer;
    long timeLeftInMillis = 91000;
    private MediaPlayer mediaPlayer;
    private MediaPlayer sfxCardFade;
    private MediaPlayer sfxBtnClick;
    private MediaPlayer sfxCardFlip;
    private int bgmMuted;
    private SharedPreferences sharedPreferences;
    private ImageView ivClickedFirst;
    private ImageView ivClickedSecond;
    HashMap<Integer, ImageView> cardImageViewMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        bgmMuted = sharedPreferences.getInt("bgmMuted", 0);

        // Initialize MediaPlayer with background music
        mediaPlayer = MediaPlayer.create(this, R.raw.music_game0);
        mediaPlayer.setLooping(true);
        if (bgmMuted == 1) {
            mediaPlayer.setVolume(0, 0);
        } else {
            mediaPlayer.setVolume(1, 1);
        }
        mediaPlayer.start();


        sfxCardFlip = MediaPlayer.create(this,R.raw.sfx_cardflip);
        sfxBtnClick = MediaPlayer.create(this, R.raw.sfx_btnclick);
        sfxCardFade = MediaPlayer.create(this,R.raw.sfx_cardfade);

        tv_Score = findViewById(R.id.tv_Score);
        tv_Move =  findViewById(R.id.tv_Move);
        tv_Timer = findViewById(R.id.tv_Timer);
        btn_Home = findViewById(R.id.btn_Home);
        btn_Reset = findViewById(R.id.btn_Reset);

        iv_11 = findViewById(R.id.iv_11);
        iv_12 = findViewById(R.id.iv_12);
        iv_13 = findViewById(R.id.iv_13);
        iv_14 = findViewById(R.id.iv_14);
        iv_21 = findViewById(R.id.iv_21);
        iv_22 = findViewById(R.id.iv_22);
        iv_23 = findViewById(R.id.iv_23);
        iv_24 = findViewById(R.id.iv_24);
        iv_31 = findViewById(R.id.iv_31);
        iv_32 = findViewById(R.id.iv_32);
        iv_33 = findViewById(R.id.iv_33);
        iv_34 = findViewById(R.id.iv_34);
        iv_41 = findViewById(R.id.iv_41);
        iv_42 = findViewById(R.id.iv_42);
        iv_43 = findViewById(R.id.iv_43);
        iv_44 = findViewById(R.id.iv_44);

        iv_11.setTag("0");
        iv_12.setTag("1");
        iv_13.setTag("2");
        iv_14.setTag("3");
        iv_21.setTag("4");
        iv_22.setTag("5");
        iv_23.setTag("6");
        iv_24.setTag("7");
        iv_31.setTag("8");
        iv_32.setTag("9");
        iv_33.setTag("10");
        iv_34.setTag("11");
        iv_41.setTag("12");
        iv_42.setTag("13");
        iv_43.setTag("14");
        iv_44.setTag("15");



        cardImageViewMap.put(0, iv_11);
        cardImageViewMap.put(1, iv_12);
        cardImageViewMap.put(2, iv_13);
        cardImageViewMap.put(3, iv_14);
        cardImageViewMap.put(4, iv_21);
        cardImageViewMap.put(5, iv_22);
        cardImageViewMap.put(6, iv_23);
        cardImageViewMap.put(7, iv_24);
        cardImageViewMap.put(8, iv_31);
        cardImageViewMap.put(9, iv_32);
        cardImageViewMap.put(10, iv_33);
        cardImageViewMap.put(11, iv_34);
        cardImageViewMap.put(12, iv_41);
        cardImageViewMap.put(13, iv_42);
        cardImageViewMap.put(14, iv_43);
        cardImageViewMap.put(15, iv_44);


        // Initialize MediaPlayer objects for the sound effects
        MediaPlayer flipCardSoundPlayer = MediaPlayer.create(this, R.raw.sfx_click);

        //load card images
        frontOfCardsResources();

        //shuffle images
        Collections.shuffle(Arrays.asList(cardsArray));

        startCountdownTimer();

        btn_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonClickSound();
                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        btn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonClickSound();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        ImageView btn_unmute = findViewById(R.id.btn_unmute);
        btn_unmute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMute(btn_unmute);
            }
        });
        if (bgmMuted == 1) {
            btn_unmute.setImageResource(R.drawable.btn_mute);
        } else {
            btn_unmute.setImageResource(R.drawable.btn_unmute);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff((ImageView) view, theCard);
                ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, "rotationY", 0f, 180f);
                rotationAnimator.setDuration(600); // Set duration as needed

                AnimatorSet frontAnimator = new AnimatorSet();
                frontAnimator.play(rotationAnimator);

                frontAnimator.start();
            }
        };

        iv_11.setOnClickListener(clickListener);
        iv_12.setOnClickListener(clickListener);
        iv_13.setOnClickListener(clickListener);
        iv_14.setOnClickListener(clickListener);
        iv_21.setOnClickListener(clickListener);
        iv_22.setOnClickListener(clickListener);
        iv_23.setOnClickListener(clickListener);
        iv_24.setOnClickListener(clickListener);
        iv_31.setOnClickListener(clickListener);
        iv_32.setOnClickListener(clickListener);
        iv_33.setOnClickListener(clickListener);
        iv_34.setOnClickListener(clickListener);
        iv_41.setOnClickListener(clickListener);
        iv_42.setOnClickListener(clickListener);
        iv_43.setOnClickListener(clickListener);
        iv_44.setOnClickListener(clickListener);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void toggleMute(ImageView btnMute) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (bgmMuted == 0) {
            mediaPlayer.setVolume(0, 0);
            btnMute.setImageResource(R.drawable.btn_mute);
            bgmMuted = 1;
        } else {
            mediaPlayer.setVolume(1, 1);
            btnMute.setImageResource(R.drawable.btn_unmute);
            bgmMuted = 0;
        }
        // Save the mute state in SharedPreferences
        editor.putInt("bgmMuted", bgmMuted);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Cancel the countdown timer when MainActivity is paused
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the background music when the activity is resumed
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        startCountdownTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void playCardFlipSound() {
        if (sfxCardFlip != null) {
            sfxCardFlip.start();
        }
    }
    private void playButtonClickSound() {
        if (sfxBtnClick != null) {
            sfxBtnClick.start();
        }
    }


    private void playCardFadeSound() {
        if (sfxCardFade != null) {
            sfxCardFade.start();
        }
    }

    private void doStuff(ImageView iv, int card){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Set the correct image to the imageview
                if(cardsArray[card] == 101){
                    iv.setImageResource(image101);
                } else if (cardsArray[card] == 102) {
                    iv.setImageResource(image102);
                } else if (cardsArray[card] == 103) {
                    iv.setImageResource(image103);
                } else if (cardsArray[card] == 104) {
                    iv.setImageResource(image104);
                } else if (cardsArray[card] == 105) {
                    iv.setImageResource(image105);
                } else if (cardsArray[card] == 106) {
                    iv.setImageResource(image106);
                } else if (cardsArray[card] == 107) {
                    iv.setImageResource(image107);
                } else if (cardsArray[card] == 108) {
                    iv.setImageResource(image108);
                } else if (cardsArray[card] == 201) {
                    iv.setImageResource(image201);
                } else if (cardsArray[card] == 202) {
                    iv.setImageResource(image202);
                } else if (cardsArray[card] == 203) {
                    iv.setImageResource(image203);
                } else if (cardsArray[card] == 204) {
                    iv.setImageResource(image204);
                } else if (cardsArray[card] == 205) {
                    iv.setImageResource(image205);
                } else if (cardsArray[card] == 206) {
                    iv.setImageResource(image206);
                } else if (cardsArray[card] == 207) {
                    iv.setImageResource(image207);
                } else if (cardsArray[card] == 208) {
                    iv.setImageResource(image208);
                }
                playCardFlipSound();
            }
        }, 300);

        // Check which image is selected and save it to temporary variable
        if(cardNumber == 1){
            firstCard = cardsArray[card];
            if(firstCard > 200){
                firstCard = firstCard - 100;
            }
            cardNumber = 2;
            clickedFirst = card;
            ivClickedFirst = iv;

            iv.setEnabled(false);
        } else if (cardNumber == 2) {
            secondCard = cardsArray[card];
            if (secondCard > 200) {
                secondCard = secondCard - 100;
            }
            cardNumber = 1;
            clickedSecond = card;
            ivClickedSecond = iv;

            iv_11.setEnabled(false);
            iv_12.setEnabled(false);
            iv_13.setEnabled(false);
            iv_14.setEnabled(false);
            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_23.setEnabled(false);
            iv_24.setEnabled(false);
            iv_31.setEnabled(false);
            iv_32.setEnabled(false);
            iv_33.setEnabled(false);
            iv_34.setEnabled(false);
            iv_41.setEnabled(false);
            iv_42.setEnabled(false);
            iv_43.setEnabled(false);
            iv_44.setEnabled(false);

            // Delay before checking if the selected images are equal
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Check if the selected images are equal
                    calculate();
                }
            }, 1000); // Adjust the delay time as needed
        }
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                // Pass score and moves to GameOver activity
                Intent intent = new Intent(MainActivity.this, GameOver.class);
                intent.putExtra("score", playerScore);
                intent.putExtra("moves", playerMove);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        tv_Timer.setText(timeLeftFormatted);
    }

    private void calculate(){
        playerMove++;
        tv_Move.setText(String.valueOf(playerMove));
        //if images are equal remove them and add point
        if(firstCard == secondCard){
            if(clickedFirst == 0){
                fadeOutAnimation(iv_11);
            } else if (clickedFirst == 1){
                fadeOutAnimation(iv_12);
            } else if (clickedFirst == 2){
                fadeOutAnimation(iv_13);
            } else if (clickedFirst == 3){
                fadeOutAnimation(iv_14);
            } else if (clickedFirst == 4){
                fadeOutAnimation(iv_21);
            } else if (clickedFirst == 5){
                fadeOutAnimation(iv_22);
            } else if (clickedFirst == 6){
                fadeOutAnimation(iv_23);
            } else if (clickedFirst == 7){
                fadeOutAnimation(iv_24);
            } else if (clickedFirst == 8){
                fadeOutAnimation(iv_31);
            } else if (clickedFirst == 9){
                fadeOutAnimation(iv_32);
            } else if (clickedFirst == 10){
                fadeOutAnimation(iv_33);
            } else if (clickedFirst == 11){
                fadeOutAnimation(iv_34);
            } else if (clickedFirst == 12){
                fadeOutAnimation(iv_41);
            } else if (clickedFirst == 13){
                fadeOutAnimation(iv_42);
            } else if (clickedFirst == 14){
                fadeOutAnimation(iv_43);
            } else if (clickedFirst == 15){
                fadeOutAnimation(iv_44);
            }
            if(clickedSecond == 0){
                fadeOutAnimation(iv_11);
            } else if (clickedSecond == 1){
                fadeOutAnimation(iv_12);
            } else if (clickedSecond == 2){
                fadeOutAnimation(iv_13);
            } else if (clickedSecond == 3){
                fadeOutAnimation(iv_14);
            } else if (clickedSecond == 4){
                fadeOutAnimation(iv_21);
            } else if (clickedSecond == 5){
                fadeOutAnimation(iv_22);
            } else if (clickedSecond == 6){
                fadeOutAnimation(iv_23);
            } else if (clickedSecond == 7){
                fadeOutAnimation(iv_24);
            } else if (clickedSecond == 8){
                fadeOutAnimation(iv_31);
            } else if (clickedSecond == 9){
                fadeOutAnimation(iv_32);
            } else if (clickedSecond == 10){
                fadeOutAnimation(iv_33);
            } else if (clickedSecond == 11){
                fadeOutAnimation(iv_34);
            } else if (clickedSecond == 12){
                fadeOutAnimation(iv_41);
            } else if (clickedSecond == 13){
                fadeOutAnimation(iv_42);
            } else if (clickedSecond == 14){
                fadeOutAnimation(iv_43);
            } else if (clickedSecond == 15){
                fadeOutAnimation(iv_44);
            }
            //add points
            playerScore++;
            tv_Score.setText(String.valueOf(playerScore));
            playCardFadeSound();
        }

        else {
            // Flip cards back if don't match
            flipCardBack(ivClickedFirst);
            flipCardBack(ivClickedSecond);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_11.setEnabled(true);
                iv_12.setEnabled(true);
                iv_13.setEnabled(true);
                iv_14.setEnabled(true);
                iv_21.setEnabled(true);
                iv_22.setEnabled(true);
                iv_23.setEnabled(true);
                iv_24.setEnabled(true);
                iv_31.setEnabled(true);
                iv_32.setEnabled(true);
                iv_33.setEnabled(true);
                iv_34.setEnabled(true);
                iv_41.setEnabled(true);
                iv_42.setEnabled(true);
                iv_43.setEnabled(true);
                iv_44.setEnabled(true);
            }
        }, 600);
        //check if the game is over
        checkEnd();
    }

    private void flipCardBack(ImageView imageView) {
        playCardFlipSound();
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(imageView, "rotationY", 180f, 0f);
        rotationAnimator.setDuration(600); // Set duration as needed
        rotationAnimator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.drawable.backflip);
            }
        }, 300);
    }

    private void fadeOutAnimation(ImageView imageView) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(50); // Set duration as needed (500 milliseconds in this example)
        imageView.startAnimation(alphaAnimation);
        imageView.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.drawable.backflip);
            }

        }, 200);
    }

    private void checkEnd(){
        if (iv_11.getVisibility() == View.INVISIBLE &&
                iv_12.getVisibility() == View.INVISIBLE &&
                iv_13.getVisibility() == View.INVISIBLE &&
                iv_14.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE &&
                iv_22.getVisibility() == View.INVISIBLE &&
                iv_23.getVisibility() == View.INVISIBLE &&
                iv_24.getVisibility() == View.INVISIBLE &&
                iv_31.getVisibility() == View.INVISIBLE &&
                iv_32.getVisibility() == View.INVISIBLE &&
                iv_33.getVisibility() == View.INVISIBLE &&
                iv_34.getVisibility() == View.INVISIBLE &&
                iv_41.getVisibility() == View.INVISIBLE &&
                iv_42.getVisibility() == View.INVISIBLE &&
                iv_43.getVisibility() == View.INVISIBLE &&
                iv_44.getVisibility() == View.INVISIBLE) {

            // Reshuffle the cards
            Collections.shuffle(Arrays.asList(cardsArray));

            // Reset card visibility
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (ImageView imageView : cardImageViewMap.values()) {
                        imageView.setRotationY(0f);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                        alphaAnimation.setDuration(200); // Set duration as needed (500 milliseconds in this example)
                        imageView.startAnimation(alphaAnimation);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }, 200);

            // Reset card image resources
            frontOfCardsResources();

            // Start the timer again
            startCountdownTimer();
        }
    }

    private void frontOfCardsResources(){
        image101 = R.drawable.img_ashborn;
        image102 = R.drawable.img_beru;
        image103 = R.drawable.img_greed;
        image104 = R.drawable.img_igris;
        image105 = R.drawable.img_iron;
        image106 = R.drawable.img_kamish;
        image107 = R.drawable.img_tank;
        image108 = R.drawable.img_tusk;
        image201 = R.drawable.img_ashborn;
        image202 = R.drawable.img_beru;
        image203 = R.drawable.img_greed;
        image204 = R.drawable.img_igris;
        image205 = R.drawable.img_iron;
        image206 = R.drawable.img_kamish;
        image207 = R.drawable.img_tank;
        image208 = R.drawable.img_tusk;

    }
}