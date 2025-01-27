package com.example.shadowflip;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
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

public class ModeEasy extends AppCompatActivity {

    ImageView iv_11, iv_12, iv_13, iv_14, iv_21, iv_22, iv_23, iv_24, iv_31, iv_32, iv_33, iv_34, iv_41, iv_42, iv_43, iv_44;

    Integer[] cardsArray = {101, 102, 103, 104, 105, 106, 107, 108, 201, 202, 203, 204, 205, 206, 207, 208};

    int image101, image102, image103, image104, image105, image106, image107, image108,
            image201, image202, image203, image204, image205, image206, image207, image208;

    int firstCard, secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber = 1;
    private ImageView ivClickedFirst;
    private ImageView ivClickedSecond;
    HashMap<Integer, ImageView> cardImageViewMap = new HashMap<>();

    //for text
    int playerScore = 0;
    int playerMove = 0;
    int moveCombo = 0;
    long Timer = 0;
    String gameMode = "easy";
    TextView tv_Score, tv_Timer, tv_Move, tv_Name;
    private Handler handler = new Handler();

    //sfx & music
    private MediaPlayer mediaPlayer;
    private MediaPlayer sfxCardFade;
    private MediaPlayer sfxBtnClick;
    private MediaPlayer sfxCardFlip;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        dbHelper = new DatabaseHelper(this);
        setContentView(R.layout.mode_easy);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String playerName = sharedPreferences.getString("playerName", "");
        int lastBGMVolumeLevel = sharedPreferences.getInt("lastBGMVolumeLevel", 50);
        int lastSFXVolumeLevel = sharedPreferences.getInt("lastSFXVolumeLevel", 50);

        mediaPlayer = MediaPlayer.create(this, R.raw.music_game0);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        mediaPlayer.setVolume(lastBGMVolumeLevel,lastBGMVolumeLevel);
        sfxBtnClick = MediaPlayer.create(this, R.raw.sfx_btnclick);
        sfxBtnClick.setVolume(lastSFXVolumeLevel,lastSFXVolumeLevel);
        sfxCardFlip = MediaPlayer.create(this,R.raw.sfx_cardflip);
        sfxCardFlip.setVolume(lastSFXVolumeLevel,lastSFXVolumeLevel);
        sfxCardFade = MediaPlayer.create(this,R.raw.sfx_cardfade);
        sfxCardFade.setVolume(lastSFXVolumeLevel,lastSFXVolumeLevel);

        tv_Score = findViewById(R.id.tv_Score);
        tv_Move =  findViewById(R.id.tv_Move);
        tv_Timer = findViewById(R.id.tv_Timer);

        tv_Name = findViewById(R.id.tv_Name);
        tv_Name.setText(playerName);

        //Settings button
        ImageView btn_Settings = findViewById(R.id.btn_Settings);
        btn_Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonClickSound();
                final Dialog dialog = new Dialog(ModeEasy.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.settings_ingame);

                ImageView btn_Restart = dialog.findViewById(R.id.btn_Restart);
                btn_Restart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playButtonClickSound();
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        Intent intent = new Intent(ModeEasy.this, ModeEasy.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                });

                ImageView btn_HowToPlay = dialog.findViewById(R.id.btn_HowToPlay);
                btn_HowToPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playButtonClickSound();
                        final Dialog dialog = new Dialog(ModeEasy.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.howtoplay);
                        dialog.show();

                        ImageView btn_Back = dialog.findViewById(R.id.btn_Back);
                        btn_Back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playButtonClickSound();
                                dialog.dismiss();
                            }
                        });
                    }
                });

                ImageView btn_About = dialog.findViewById(R.id.btn_About);
                btn_About.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playButtonClickSound();
                        final Dialog dialog = new Dialog(ModeEasy.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.aboutus);
                        dialog.show();

                        ImageView btn_Back = dialog.findViewById(R.id.btn_Back);
                        btn_Back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playButtonClickSound();
                                dialog.dismiss();
                            }
                        });
                    }
                });

                ImageView btn_Home = dialog.findViewById(R.id.btn_Home);
                btn_Home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playButtonClickSound();
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        Intent intent = new Intent(ModeEasy.this, MainMenu.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                });

                ImageView btn_Back = dialog.findViewById(R.id.btn_Back);
                btn_Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playButtonClickSound();
                        dialog.dismiss();
                    }
                });

                SeekBar sb_BGM = dialog.findViewById(R.id.sb_BGM);
                sb_BGM.setMax(100);
                int lastBGMVolumeLevel = sharedPreferences.getInt("lastBGMVolumeLevel", 50);
                sb_BGM.setProgress(lastBGMVolumeLevel);
                sb_BGM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeFraction = (float) progress / 100.0f;
                        mediaPlayer.setVolume(volumeFraction, volumeFraction);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("lastBGMVolumeLevel", progress);
                        editor.apply();
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });

                SeekBar sb_SFX = dialog.findViewById(R.id.sb_SFX);
                sb_SFX.setMax(100);
                int lastSFXVolumeLevel = sharedPreferences.getInt("lastSFXVolumeLevel", 50);
                sb_SFX.setProgress(lastSFXVolumeLevel);
                sb_SFX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeFraction = (float) progress / 100.0f;
                        sfxBtnClick.setVolume(volumeFraction, volumeFraction);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("lastSFXVolumeLevel", progress);
                        editor.apply();
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                dialog.show();
            }
        });

        MediaPlayer flipCardSoundPlayer = MediaPlayer.create(this, R.raw.sfx_click);
        frontOfCardsResources();
        Collections.shuffle(Arrays.asList(cardsArray));

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


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff((ImageView) view, theCard);
                ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, "rotationY", 0f, 180f);
                rotationAnimator.setDuration(600);
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
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculate();
                }
            }, 1000);
        }
    }


    private void calculate(){
        playerMove++;
        moveCombo++;
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

            if (moveCombo <= 1)
                playerScore += 50;
            else if (moveCombo == 2)
                playerScore += 30;
            else if (moveCombo == 3)
                playerScore += 20;
            else if (moveCombo == 4)
                playerScore += 10;
            else playerScore += 5;
            tv_Score.setText(String.valueOf(playerScore));
            playCardFadeSound();
            moveCombo = 0;
        }

        else {
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

    private void startTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Timer += 1000;
                updateTimerText();
                handler.postDelayed(this, 1000);
            }
        }, 1000); // Start the timer after 1 second initially
    }
    private void updateTimerText() {
        int minutes = (int) (Timer / 1000) / 60;
        int seconds = (int) (Timer / 1000) % 60;
        String timeElapsedFormatted = String.format("%02d:%02d", minutes, seconds);
        tv_Timer.setText(timeElapsedFormatted);
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

            String Name = tv_Name.getText().toString();
            long Time = Timer;
            int Moves = playerMove;
            int Score = playerScore;
            String Mode = gameMode;
            handler.removeCallbacksAndMessages(null);
            Intent intent = new Intent(ModeEasy.this, GameOver.class);
            intent.putExtra("score", Score);
            intent.putExtra("moves", Moves);
            intent.putExtra("time", Time);
            intent.putExtra("name", Name);
            insertGameData(Name, Time, Moves, Score, Mode);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }
    private void insertGameData(String Name, long Time, int Moves, int Score, String Mode) {
        dbHelper.addData(Name, Time, Moves, Score, Mode);
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

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        // Resume the background music when the activity is resumed
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
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
}

