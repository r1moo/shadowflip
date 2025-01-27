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

public class ModeMedium extends AppCompatActivity {

    ImageView iv_11, iv_12, iv_13, iv_14, iv_15, iv_16,
            iv_21, iv_22, iv_23, iv_24, iv_25, iv_26,
            iv_31, iv_32, iv_33, iv_34, iv_35, iv_36,
            iv_41, iv_42, iv_43, iv_44, iv_45, iv_46,
            iv_51, iv_52, iv_53, iv_54, iv_55, iv_56,
            iv_61, iv_62, iv_63, iv_64, iv_65, iv_66;

    Integer[] cardsArray = {101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118,
                            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218};

    int image101, image102, image103, image104, image105, image106, image107, image108, image109, image110, image111, image112, image113, image114, image115, image116, image117, image118,
        image201, image202, image203, image204, image205, image206, image207, image208, image209, image210, image211, image212, image213, image214, image215, image216, image217, image218;

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
    String gameMode = "medium";
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
        setContentView(R.layout.mode_medium);
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
                final Dialog dialog = new Dialog(ModeMedium.this);
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
                        Intent intent = new Intent(ModeMedium.this, ModeMedium.class);
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
                        final Dialog dialog = new Dialog(ModeMedium.this);
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
                        final Dialog dialog = new Dialog(ModeMedium.this);
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
                        Intent intent = new Intent(ModeMedium.this, MainMenu.class);
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
        iv_15 = findViewById(R.id.iv_15);
        iv_16 = findViewById(R.id.iv_16);
        iv_21 = findViewById(R.id.iv_21);
        iv_22 = findViewById(R.id.iv_22);
        iv_23 = findViewById(R.id.iv_23);
        iv_24 = findViewById(R.id.iv_24);
        iv_25 = findViewById(R.id.iv_25);
        iv_26 = findViewById(R.id.iv_26);
        iv_31 = findViewById(R.id.iv_31);
        iv_32 = findViewById(R.id.iv_32);
        iv_33 = findViewById(R.id.iv_33);
        iv_34 = findViewById(R.id.iv_34);
        iv_35 = findViewById(R.id.iv_35);
        iv_36 = findViewById(R.id.iv_36);
        iv_41 = findViewById(R.id.iv_41);
        iv_42 = findViewById(R.id.iv_42);
        iv_43 = findViewById(R.id.iv_43);
        iv_44 = findViewById(R.id.iv_44);
        iv_45 = findViewById(R.id.iv_45);
        iv_46 = findViewById(R.id.iv_46);
        iv_51 = findViewById(R.id.iv_51);
        iv_52 = findViewById(R.id.iv_52);
        iv_53 = findViewById(R.id.iv_53);
        iv_54 = findViewById(R.id.iv_54);
        iv_55 = findViewById(R.id.iv_55);
        iv_56 = findViewById(R.id.iv_56);
        iv_61 = findViewById(R.id.iv_61);
        iv_62 = findViewById(R.id.iv_62);
        iv_63 = findViewById(R.id.iv_63);
        iv_64 = findViewById(R.id.iv_64);
        iv_65 = findViewById(R.id.iv_65);
        iv_66 = findViewById(R.id.iv_66);

        iv_11.setTag("0");
        iv_12.setTag("1");
        iv_13.setTag("2");
        iv_14.setTag("3");
        iv_15.setTag("4");
        iv_16.setTag("5");
        iv_21.setTag("6");
        iv_22.setTag("7");
        iv_23.setTag("8");
        iv_24.setTag("9");
        iv_25.setTag("10");
        iv_26.setTag("11");
        iv_31.setTag("12");
        iv_32.setTag("13");
        iv_33.setTag("14");
        iv_34.setTag("15");
        iv_35.setTag("16");
        iv_36.setTag("17");
        iv_41.setTag("18");
        iv_42.setTag("19");
        iv_43.setTag("20");
        iv_44.setTag("21");
        iv_45.setTag("22");
        iv_46.setTag("23");
        iv_51.setTag("24");
        iv_52.setTag("25");
        iv_53.setTag("26");
        iv_54.setTag("27");
        iv_55.setTag("28");
        iv_56.setTag("29");
        iv_61.setTag("30");
        iv_62.setTag("31");
        iv_63.setTag("32");
        iv_64.setTag("33");
        iv_65.setTag("34");
        iv_66.setTag("35");

        cardImageViewMap.put(0, iv_11);
        cardImageViewMap.put(1, iv_12);
        cardImageViewMap.put(2, iv_13);
        cardImageViewMap.put(3, iv_14);
        cardImageViewMap.put(4, iv_15);
        cardImageViewMap.put(5, iv_16);
        cardImageViewMap.put(6, iv_21);
        cardImageViewMap.put(7, iv_22);
        cardImageViewMap.put(8, iv_23);
        cardImageViewMap.put(9, iv_24);
        cardImageViewMap.put(10, iv_25);
        cardImageViewMap.put(11, iv_26);
        cardImageViewMap.put(12, iv_31);
        cardImageViewMap.put(13, iv_32);
        cardImageViewMap.put(14, iv_33);
        cardImageViewMap.put(15, iv_34);
        cardImageViewMap.put(16, iv_35);
        cardImageViewMap.put(17, iv_36);
        cardImageViewMap.put(18, iv_41);
        cardImageViewMap.put(19, iv_42);
        cardImageViewMap.put(20, iv_43);
        cardImageViewMap.put(21, iv_44);
        cardImageViewMap.put(22, iv_45);
        cardImageViewMap.put(23, iv_46);
        cardImageViewMap.put(24, iv_51);
        cardImageViewMap.put(25, iv_52);
        cardImageViewMap.put(26, iv_53);
        cardImageViewMap.put(27, iv_54);
        cardImageViewMap.put(28, iv_55);
        cardImageViewMap.put(29, iv_56);
        cardImageViewMap.put(30, iv_61);
        cardImageViewMap.put(31, iv_62);
        cardImageViewMap.put(32, iv_63);
        cardImageViewMap.put(33, iv_64);
        cardImageViewMap.put(34, iv_65);
        cardImageViewMap.put(35, iv_66);

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
        iv_15.setOnClickListener(clickListener);
        iv_16.setOnClickListener(clickListener);
        iv_21.setOnClickListener(clickListener);
        iv_22.setOnClickListener(clickListener);
        iv_23.setOnClickListener(clickListener);
        iv_24.setOnClickListener(clickListener);
        iv_25.setOnClickListener(clickListener);
        iv_26.setOnClickListener(clickListener);
        iv_31.setOnClickListener(clickListener);
        iv_32.setOnClickListener(clickListener);
        iv_33.setOnClickListener(clickListener);
        iv_34.setOnClickListener(clickListener);
        iv_35.setOnClickListener(clickListener);
        iv_36.setOnClickListener(clickListener);
        iv_41.setOnClickListener(clickListener);
        iv_42.setOnClickListener(clickListener);
        iv_43.setOnClickListener(clickListener);
        iv_44.setOnClickListener(clickListener);
        iv_45.setOnClickListener(clickListener);
        iv_46.setOnClickListener(clickListener);
        iv_51.setOnClickListener(clickListener);
        iv_52.setOnClickListener(clickListener);
        iv_53.setOnClickListener(clickListener);
        iv_54.setOnClickListener(clickListener);
        iv_55.setOnClickListener(clickListener);
        iv_56.setOnClickListener(clickListener);
        iv_61.setOnClickListener(clickListener);
        iv_62.setOnClickListener(clickListener);
        iv_63.setOnClickListener(clickListener);
        iv_64.setOnClickListener(clickListener);
        iv_65.setOnClickListener(clickListener);
        iv_66.setOnClickListener(clickListener);

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
                } else if(cardsArray[card] == 109){
                    iv.setImageResource(image109);
                } else if (cardsArray[card] == 110) {
                    iv.setImageResource(image110);
                } else if (cardsArray[card] == 111) {
                    iv.setImageResource(image111);
                } else if (cardsArray[card] == 112) {
                    iv.setImageResource(image112);
                } else if (cardsArray[card] == 113) {
                    iv.setImageResource(image113);
                } else if (cardsArray[card] == 114) {
                    iv.setImageResource(image114);
                } else if (cardsArray[card] == 115) {
                    iv.setImageResource(image115);
                } else if (cardsArray[card] == 116) {
                    iv.setImageResource(image116);
                } else if (cardsArray[card] == 117) {
                    iv.setImageResource(image117);
                } else if (cardsArray[card] == 118) {
                    iv.setImageResource(image118);
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
                } else if (cardsArray[card] == 209) {
                    iv.setImageResource(image209);
                } else if (cardsArray[card] == 210) {
                    iv.setImageResource(image210);
                } else if (cardsArray[card] == 211) {
                    iv.setImageResource(image211);
                } else if (cardsArray[card] == 212) {
                    iv.setImageResource(image212);
                } else if (cardsArray[card] == 213) {
                    iv.setImageResource(image213);
                } else if (cardsArray[card] == 214) {
                    iv.setImageResource(image214);
                } else if (cardsArray[card] == 215) {
                    iv.setImageResource(image215);
                } else if (cardsArray[card] == 216) {
                    iv.setImageResource(image216);
                } else if (cardsArray[card] == 217) {
                    iv.setImageResource(image217);
                } else if (cardsArray[card] == 218) {
                    iv.setImageResource(image218);
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

            iv_15.setEnabled(false);
            iv_16.setEnabled(false);
            iv_25.setEnabled(false);
            iv_26.setEnabled(false);
            iv_35.setEnabled(false);
            iv_36.setEnabled(false);
            iv_45.setEnabled(false);
            iv_46.setEnabled(false);
            iv_51.setEnabled(false);
            iv_52.setEnabled(false);
            iv_53.setEnabled(false);
            iv_54.setEnabled(false);
            iv_55.setEnabled(false);
            iv_56.setEnabled(false);
            iv_61.setEnabled(false);
            iv_62.setEnabled(false);
            iv_63.setEnabled(false);
            iv_64.setEnabled(false);
            iv_65.setEnabled(false);
            iv_66.setEnabled(false);

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
                fadeOutAnimation(iv_15);
            } else if (clickedFirst == 5){
                fadeOutAnimation(iv_16);
            } else if (clickedFirst == 6){
                fadeOutAnimation(iv_21);
            } else if (clickedFirst == 7){
                fadeOutAnimation(iv_22);
            } else if (clickedFirst == 8){
                fadeOutAnimation(iv_23);
            } else if (clickedFirst == 9){
                fadeOutAnimation(iv_24);
            } else if (clickedFirst == 10){
                fadeOutAnimation(iv_25);
            } else if (clickedFirst == 11){
                fadeOutAnimation(iv_26);
            } else if (clickedFirst == 12){
                fadeOutAnimation(iv_31);
            } else if (clickedFirst == 13){
                fadeOutAnimation(iv_32);
            } else if (clickedFirst == 14){
                fadeOutAnimation(iv_33);
            } else if (clickedFirst == 15){
                fadeOutAnimation(iv_34);
            } else if (clickedFirst == 16){
                fadeOutAnimation(iv_35);
            } else if (clickedFirst == 17){
                fadeOutAnimation(iv_36);
            } else if (clickedFirst == 18){
                fadeOutAnimation(iv_41);
            } else if (clickedFirst == 19){
                fadeOutAnimation(iv_42);
            } else if (clickedFirst == 20){
                fadeOutAnimation(iv_43);
            } else if (clickedFirst == 21){
                fadeOutAnimation(iv_44);
            } else if (clickedFirst == 22){
                fadeOutAnimation(iv_45);
            } else if (clickedFirst == 23){
                fadeOutAnimation(iv_46);
            } else if (clickedFirst == 24){
                fadeOutAnimation(iv_51);
            } else if (clickedFirst == 25){
                fadeOutAnimation(iv_52);
            } else if (clickedFirst == 26){
                fadeOutAnimation(iv_53);
            } else if (clickedFirst == 27){
                fadeOutAnimation(iv_54);
            } else if (clickedFirst == 28){
                fadeOutAnimation(iv_55);
            } else if (clickedFirst == 29){
                fadeOutAnimation(iv_56);
            } else if (clickedFirst == 30){
                fadeOutAnimation(iv_61);
            } else if (clickedFirst == 31){
                fadeOutAnimation(iv_62);
            } else if (clickedFirst == 32){
                fadeOutAnimation(iv_63);
            } else if (clickedFirst == 33){
                fadeOutAnimation(iv_64);
            } else if (clickedFirst == 34){
                fadeOutAnimation(iv_65);
            } else if (clickedFirst == 35){
                fadeOutAnimation(iv_66);
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
                fadeOutAnimation(iv_15);
            } else if (clickedSecond == 5){
                fadeOutAnimation(iv_16);
            } else if (clickedSecond == 6){
                fadeOutAnimation(iv_21);
            } else if (clickedSecond == 7){
                fadeOutAnimation(iv_22);
            } else if (clickedSecond == 8){
                fadeOutAnimation(iv_23);
            } else if (clickedSecond == 9){
                fadeOutAnimation(iv_24);
            } else if (clickedSecond == 10){
                fadeOutAnimation(iv_25);
            } else if (clickedSecond == 11){
                fadeOutAnimation(iv_26);
            } else if (clickedSecond == 12){
                fadeOutAnimation(iv_31);
            } else if (clickedSecond == 13){
                fadeOutAnimation(iv_32);
            } else if (clickedSecond == 14){
                fadeOutAnimation(iv_33);
            } else if (clickedSecond == 15){
                fadeOutAnimation(iv_34);
            } else if (clickedSecond == 16){
                fadeOutAnimation(iv_35);
            } else if (clickedSecond == 17){
                fadeOutAnimation(iv_36);
            } else if (clickedSecond == 18){
                fadeOutAnimation(iv_41);
            } else if (clickedSecond == 19){
                fadeOutAnimation(iv_42);
            } else if (clickedSecond == 20){
                fadeOutAnimation(iv_43);
            } else if (clickedSecond == 21){
                fadeOutAnimation(iv_44);
            } else if (clickedSecond == 22){
                fadeOutAnimation(iv_45);
            } else if (clickedSecond == 23){
                fadeOutAnimation(iv_46);
            } else if (clickedSecond == 24){
                fadeOutAnimation(iv_51);
            } else if (clickedSecond == 25){
                fadeOutAnimation(iv_52);
            } else if (clickedSecond == 26){
                fadeOutAnimation(iv_53);
            } else if (clickedSecond == 27){
                fadeOutAnimation(iv_54);
            } else if (clickedSecond == 28){
                fadeOutAnimation(iv_55);
            } else if (clickedSecond == 29){
                fadeOutAnimation(iv_56);
            } else if (clickedSecond == 30){
                fadeOutAnimation(iv_61);
            } else if (clickedSecond == 31){
                fadeOutAnimation(iv_62);
            } else if (clickedSecond == 32){
                fadeOutAnimation(iv_63);
            } else if (clickedSecond == 33){
                fadeOutAnimation(iv_64);
            } else if (clickedSecond == 34){
                fadeOutAnimation(iv_65);
            } else if (clickedSecond == 35){
                fadeOutAnimation(iv_66);
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
                iv_15.setEnabled(true);
                iv_16.setEnabled(true);
                iv_25.setEnabled(true);
                iv_26.setEnabled(true);
                iv_35.setEnabled(true);
                iv_36.setEnabled(true);
                iv_45.setEnabled(true);
                iv_46.setEnabled(true);
                iv_51.setEnabled(true);
                iv_52.setEnabled(true);
                iv_53.setEnabled(true);
                iv_54.setEnabled(true);
                iv_55.setEnabled(true);
                iv_56.setEnabled(true);
                iv_61.setEnabled(true);
                iv_62.setEnabled(true);
                iv_63.setEnabled(true);
                iv_64.setEnabled(true);
                iv_65.setEnabled(true);
                iv_66.setEnabled(true);
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
                iv_44.getVisibility() == View.INVISIBLE &&
                iv_51.getVisibility() == View.INVISIBLE &&
                iv_52.getVisibility() == View.INVISIBLE &&
                iv_53.getVisibility() == View.INVISIBLE &&
                iv_54.getVisibility() == View.INVISIBLE &&
                iv_61.getVisibility() == View.INVISIBLE &&
                iv_62.getVisibility() == View.INVISIBLE &&
                iv_63.getVisibility() == View.INVISIBLE &&
                iv_64.getVisibility() == View.INVISIBLE &&
                iv_65.getVisibility() == View.INVISIBLE &&
                iv_66.getVisibility() == View.INVISIBLE) {

            String Name = tv_Name.getText().toString();
            long Time = Timer;
            int Moves = playerMove;
            int Score = playerScore;
            String Mode = gameMode;
            handler.removeCallbacksAndMessages(null);
            Intent intent = new Intent(ModeMedium.this, GameOver.class);
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
        image107 = R.drawable.img_sillad;
        image108 = R.drawable.img_baran;
        image109 = R.drawable.img_antares;
        image110 = R.drawable.img_tarnak;
        image111 = R.drawable.img_rakan;
        image112 = R.drawable.img_goto;
        image113 = R.drawable.img_cha;
        image114 = R.drawable.img_baek;
        image115 = R.drawable.img_sung;
        image116 = R.drawable.img_woo;
        image117 = R.drawable.img_knightrasaka;
        image118 = R.drawable.img_bellion;

        image201 = R.drawable.img_ashborn;
        image202 = R.drawable.img_beru;
        image203 = R.drawable.img_greed;
        image204 = R.drawable.img_igris;
        image205 = R.drawable.img_iron;
        image206 = R.drawable.img_kamish;
        image207 = R.drawable.img_sillad;
        image208 = R.drawable.img_baran;
        image209 = R.drawable.img_antares;
        image210 = R.drawable.img_tarnak;
        image211 = R.drawable.img_rakan;
        image212 = R.drawable.img_goto;
        image213 = R.drawable.img_cha;
        image214 = R.drawable.img_baek;
        image215 = R.drawable.img_sung;
        image216 = R.drawable.img_woo;
        image217 = R.drawable.img_knightrasaka;
        image218 = R.drawable.img_bellion;
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

