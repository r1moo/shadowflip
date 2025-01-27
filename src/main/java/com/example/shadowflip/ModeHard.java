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

public class ModeHard extends AppCompatActivity {

    ImageView iv_11, iv_12, iv_13, iv_14, iv_15, iv_16, iv_17, iv_18,
            iv_21, iv_22, iv_23, iv_24, iv_25, iv_26, iv_27, iv_28,
            iv_31, iv_32, iv_33, iv_34, iv_35, iv_36, iv_37, iv_38,
            iv_41, iv_42, iv_43, iv_44, iv_45, iv_46, iv_47, iv_48,
            iv_51, iv_52, iv_53, iv_54, iv_55, iv_56, iv_57, iv_58,
            iv_61, iv_62, iv_63, iv_64, iv_65, iv_66, iv_67, iv_68,
            iv_71, iv_72, iv_73, iv_74, iv_75, iv_76, iv_77, iv_78,
            iv_81, iv_82, iv_83, iv_84, iv_85, iv_86, iv_87, iv_88;


    Integer[] cardsArray = {
            101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132,
            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232
    };

    int image101, image102, image103, image104, image105, image106, image107, image108, image109, image110, image111, image112, image113, image114, image115, image116, image117, image118, image119, image120, image121, image122, image123, image124, image125, image126, image127, image128, image129, image130, image131, image132,
            image201, image202, image203, image204, image205, image206, image207, image208, image209, image210, image211, image212, image213, image214, image215, image216, image217, image218, image219, image220, image221, image222, image223, image224, image225, image226, image227, image228, image229, image230, image231, image232;

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
    String gameMode = "hard";
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
        setContentView(R.layout.mode_hard);
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
                final Dialog dialog = new Dialog(ModeHard.this);
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
                        Intent intent = new Intent(ModeHard.this, ModeHard.class);
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
                        final Dialog dialog = new Dialog(ModeHard.this);
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
                        final Dialog dialog = new Dialog(ModeHard.this);
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
                        Intent intent = new Intent(ModeHard.this, MainMenu.class);
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
        iv_17 = findViewById(R.id.iv_17);
        iv_18 = findViewById(R.id.iv_18);
        iv_21 = findViewById(R.id.iv_21);
        iv_22 = findViewById(R.id.iv_22);
        iv_23 = findViewById(R.id.iv_23);
        iv_24 = findViewById(R.id.iv_24);
        iv_25 = findViewById(R.id.iv_25);
        iv_26 = findViewById(R.id.iv_26);
        iv_27 = findViewById(R.id.iv_27);
        iv_28 = findViewById(R.id.iv_28);
        iv_31 = findViewById(R.id.iv_31);
        iv_32 = findViewById(R.id.iv_32);
        iv_33 = findViewById(R.id.iv_33);
        iv_34 = findViewById(R.id.iv_34);
        iv_35 = findViewById(R.id.iv_35);
        iv_36 = findViewById(R.id.iv_36);
        iv_37 = findViewById(R.id.iv_37);
        iv_38 = findViewById(R.id.iv_38);
        iv_41 = findViewById(R.id.iv_41);
        iv_42 = findViewById(R.id.iv_42);
        iv_43 = findViewById(R.id.iv_43);
        iv_44 = findViewById(R.id.iv_44);
        iv_45 = findViewById(R.id.iv_45);
        iv_46 = findViewById(R.id.iv_46);
        iv_47 = findViewById(R.id.iv_47);
        iv_48 = findViewById(R.id.iv_48);
        iv_51 = findViewById(R.id.iv_51);
        iv_52 = findViewById(R.id.iv_52);
        iv_53 = findViewById(R.id.iv_53);
        iv_54 = findViewById(R.id.iv_54);
        iv_55 = findViewById(R.id.iv_55);
        iv_56 = findViewById(R.id.iv_56);
        iv_57 = findViewById(R.id.iv_57);
        iv_58 = findViewById(R.id.iv_58);
        iv_61 = findViewById(R.id.iv_61);
        iv_62 = findViewById(R.id.iv_62);
        iv_63 = findViewById(R.id.iv_63);
        iv_64 = findViewById(R.id.iv_64);
        iv_65 = findViewById(R.id.iv_65);
        iv_66 = findViewById(R.id.iv_66);
        iv_67 = findViewById(R.id.iv_67);
        iv_68 = findViewById(R.id.iv_68);
        iv_71 = findViewById(R.id.iv_71);
        iv_72 = findViewById(R.id.iv_72);
        iv_73 = findViewById(R.id.iv_73);
        iv_74 = findViewById(R.id.iv_74);
        iv_75 = findViewById(R.id.iv_75);
        iv_76 = findViewById(R.id.iv_76);
        iv_77 = findViewById(R.id.iv_77);
        iv_78 = findViewById(R.id.iv_78);
        iv_81 = findViewById(R.id.iv_81);
        iv_82 = findViewById(R.id.iv_82);
        iv_83 = findViewById(R.id.iv_83);
        iv_84 = findViewById(R.id.iv_84);
        iv_85 = findViewById(R.id.iv_85);
        iv_86 = findViewById(R.id.iv_86);
        iv_87 = findViewById(R.id.iv_87);
        iv_88 = findViewById(R.id.iv_88);

        iv_11.setTag("0");
        iv_12.setTag("1");
        iv_13.setTag("2");
        iv_14.setTag("3");
        iv_15.setTag("4");
        iv_16.setTag("5");
        iv_17.setTag("6");
        iv_18.setTag("7");
        iv_21.setTag("8");
        iv_22.setTag("9");
        iv_23.setTag("10");
        iv_24.setTag("11");
        iv_25.setTag("12");
        iv_26.setTag("13");
        iv_27.setTag("14");
        iv_28.setTag("15");
        iv_31.setTag("16");
        iv_32.setTag("17");
        iv_33.setTag("18");
        iv_34.setTag("19");
        iv_35.setTag("20");
        iv_36.setTag("21");
        iv_37.setTag("22");
        iv_38.setTag("23");
        iv_41.setTag("24");
        iv_42.setTag("25");
        iv_43.setTag("26");
        iv_44.setTag("27");
        iv_45.setTag("28");
        iv_46.setTag("29");
        iv_47.setTag("30");
        iv_48.setTag("31");
        iv_51.setTag("32");
        iv_52.setTag("33");
        iv_53.setTag("34");
        iv_54.setTag("35");
        iv_55.setTag("36");
        iv_56.setTag("37");
        iv_57.setTag("38");
        iv_58.setTag("39");
        iv_61.setTag("40");
        iv_62.setTag("41");
        iv_63.setTag("42");
        iv_64.setTag("43");
        iv_65.setTag("44");
        iv_66.setTag("45");
        iv_67.setTag("46");
        iv_68.setTag("47");
        iv_71.setTag("48");
        iv_72.setTag("49");
        iv_73.setTag("50");
        iv_74.setTag("51");
        iv_75.setTag("52");
        iv_76.setTag("53");
        iv_77.setTag("54");
        iv_78.setTag("55");
        iv_81.setTag("56");
        iv_82.setTag("57");
        iv_83.setTag("58");
        iv_84.setTag("59");
        iv_85.setTag("60");
        iv_86.setTag("61");
        iv_87.setTag("62");
        iv_88.setTag("63");

        cardImageViewMap.put(0, iv_11);
        cardImageViewMap.put(1, iv_12);
        cardImageViewMap.put(2, iv_13);
        cardImageViewMap.put(3, iv_14);
        cardImageViewMap.put(4, iv_15);
        cardImageViewMap.put(5, iv_16);
        cardImageViewMap.put(6, iv_17);
        cardImageViewMap.put(7, iv_18);
        cardImageViewMap.put(8, iv_21);
        cardImageViewMap.put(9, iv_22);
        cardImageViewMap.put(10, iv_23);
        cardImageViewMap.put(11, iv_24);
        cardImageViewMap.put(12, iv_25);
        cardImageViewMap.put(13, iv_26);
        cardImageViewMap.put(14, iv_27);
        cardImageViewMap.put(15, iv_28);
        cardImageViewMap.put(16, iv_31);
        cardImageViewMap.put(17, iv_32);
        cardImageViewMap.put(18, iv_33);
        cardImageViewMap.put(19, iv_34);
        cardImageViewMap.put(20, iv_35);
        cardImageViewMap.put(21, iv_36);
        cardImageViewMap.put(22, iv_37);
        cardImageViewMap.put(23, iv_38);
        cardImageViewMap.put(24, iv_41);
        cardImageViewMap.put(25, iv_42);
        cardImageViewMap.put(26, iv_43);
        cardImageViewMap.put(27, iv_44);
        cardImageViewMap.put(28, iv_45);
        cardImageViewMap.put(29, iv_46);
        cardImageViewMap.put(30, iv_47);
        cardImageViewMap.put(31, iv_48);
        cardImageViewMap.put(32, iv_51);
        cardImageViewMap.put(33, iv_52);
        cardImageViewMap.put(34, iv_53);
        cardImageViewMap.put(35, iv_54);
        cardImageViewMap.put(36, iv_55);
        cardImageViewMap.put(37, iv_56);
        cardImageViewMap.put(38, iv_57);
        cardImageViewMap.put(39, iv_58);
        cardImageViewMap.put(40, iv_61);
        cardImageViewMap.put(41, iv_62);
        cardImageViewMap.put(42, iv_63);
        cardImageViewMap.put(43, iv_64);
        cardImageViewMap.put(44, iv_65);
        cardImageViewMap.put(45, iv_66);
        cardImageViewMap.put(46, iv_67);
        cardImageViewMap.put(47, iv_68);
        cardImageViewMap.put(48, iv_71);
        cardImageViewMap.put(49, iv_72);
        cardImageViewMap.put(50, iv_73);
        cardImageViewMap.put(51, iv_74);
        cardImageViewMap.put(52, iv_75);
        cardImageViewMap.put(53, iv_76);
        cardImageViewMap.put(54, iv_77);
        cardImageViewMap.put(55, iv_78);
        cardImageViewMap.put(56, iv_81);
        cardImageViewMap.put(57, iv_82);
        cardImageViewMap.put(58, iv_83);
        cardImageViewMap.put(59, iv_84);
        cardImageViewMap.put(60, iv_85);
        cardImageViewMap.put(61, iv_86);
        cardImageViewMap.put(62, iv_87);
        cardImageViewMap.put(63, iv_88);

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
        iv_17.setOnClickListener(clickListener);
        iv_18.setOnClickListener(clickListener);
        iv_21.setOnClickListener(clickListener);
        iv_22.setOnClickListener(clickListener);
        iv_23.setOnClickListener(clickListener);
        iv_24.setOnClickListener(clickListener);
        iv_25.setOnClickListener(clickListener);
        iv_26.setOnClickListener(clickListener);
        iv_27.setOnClickListener(clickListener);
        iv_28.setOnClickListener(clickListener);
        iv_31.setOnClickListener(clickListener);
        iv_32.setOnClickListener(clickListener);
        iv_33.setOnClickListener(clickListener);
        iv_34.setOnClickListener(clickListener);
        iv_35.setOnClickListener(clickListener);
        iv_36.setOnClickListener(clickListener);
        iv_37.setOnClickListener(clickListener);
        iv_38.setOnClickListener(clickListener);
        iv_41.setOnClickListener(clickListener);
        iv_42.setOnClickListener(clickListener);
        iv_43.setOnClickListener(clickListener);
        iv_44.setOnClickListener(clickListener);
        iv_45.setOnClickListener(clickListener);
        iv_46.setOnClickListener(clickListener);
        iv_47.setOnClickListener(clickListener);
        iv_48.setOnClickListener(clickListener);
        iv_51.setOnClickListener(clickListener);
        iv_52.setOnClickListener(clickListener);
        iv_53.setOnClickListener(clickListener);
        iv_54.setOnClickListener(clickListener);
        iv_55.setOnClickListener(clickListener);
        iv_56.setOnClickListener(clickListener);
        iv_57.setOnClickListener(clickListener);
        iv_58.setOnClickListener(clickListener);
        iv_61.setOnClickListener(clickListener);
        iv_62.setOnClickListener(clickListener);
        iv_63.setOnClickListener(clickListener);
        iv_64.setOnClickListener(clickListener);
        iv_65.setOnClickListener(clickListener);
        iv_66.setOnClickListener(clickListener);
        iv_67.setOnClickListener(clickListener);
        iv_68.setOnClickListener(clickListener);
        iv_71.setOnClickListener(clickListener);
        iv_72.setOnClickListener(clickListener);
        iv_73.setOnClickListener(clickListener);
        iv_74.setOnClickListener(clickListener);
        iv_75.setOnClickListener(clickListener);
        iv_76.setOnClickListener(clickListener);
        iv_77.setOnClickListener(clickListener);
        iv_78.setOnClickListener(clickListener);
        iv_81.setOnClickListener(clickListener);
        iv_82.setOnClickListener(clickListener);
        iv_83.setOnClickListener(clickListener);
        iv_84.setOnClickListener(clickListener);
        iv_85.setOnClickListener(clickListener);
        iv_86.setOnClickListener(clickListener);
        iv_87.setOnClickListener(clickListener);
        iv_88.setOnClickListener(clickListener);

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
                } else if (cardsArray[card] == 119) {
                    iv.setImageResource(image119);
                } else if (cardsArray[card] == 120) {
                    iv.setImageResource(image120);
                } else if (cardsArray[card] == 121) {
                    iv.setImageResource(image121);
                } else if (cardsArray[card] == 122) {
                    iv.setImageResource(image122);
                } else if (cardsArray[card] == 123) {
                    iv.setImageResource(image123);
                } else if (cardsArray[card] == 124) {
                    iv.setImageResource(image124);
                } else if (cardsArray[card] == 125) {
                    iv.setImageResource(image125);
                } else if (cardsArray[card] == 126) {
                    iv.setImageResource(image126);
                } else if (cardsArray[card] == 127) {
                    iv.setImageResource(image127);
                } else if (cardsArray[card] == 128) {
                    iv.setImageResource(image128);
                } else if (cardsArray[card] == 129) {
                    iv.setImageResource(image129);
                } else if (cardsArray[card] == 130) {
                    iv.setImageResource(image130);
                } else if (cardsArray[card] == 131) {
                    iv.setImageResource(image131);
                } else if (cardsArray[card] == 132) {
                    iv.setImageResource(image132);
                } else if (cardsArray[card] == 219) {
                    iv.setImageResource(image219);
                } else if (cardsArray[card] == 220) {
                    iv.setImageResource(image220);
                } else if (cardsArray[card] == 221) {
                    iv.setImageResource(image221);
                } else if (cardsArray[card] == 222) {
                    iv.setImageResource(image222);
                } else if (cardsArray[card] == 223) {
                    iv.setImageResource(image223);
                } else if (cardsArray[card] == 224) {
                    iv.setImageResource(image224);
                } else if (cardsArray[card] == 225) {
                    iv.setImageResource(image225);
                } else if (cardsArray[card] == 226) {
                    iv.setImageResource(image226);
                } else if (cardsArray[card] == 227) {
                    iv.setImageResource(image227);
                } else if (cardsArray[card] == 228) {
                    iv.setImageResource(image228);
                } else if (cardsArray[card] == 229) {
                    iv.setImageResource(image229);
                } else if (cardsArray[card] == 230) {
                    iv.setImageResource(image230);
                } else if (cardsArray[card] == 231) {
                    iv.setImageResource(image231);
                } else if (cardsArray[card] == 232) {
                    iv.setImageResource(image232);
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

            iv_17.setEnabled(false);
            iv_18.setEnabled(false);
            iv_27.setEnabled(false);
            iv_28.setEnabled(false);
            iv_37.setEnabled(false);
            iv_38.setEnabled(false);
            iv_47.setEnabled(false);
            iv_48.setEnabled(false);
            iv_57.setEnabled(false);
            iv_58.setEnabled(false);
            iv_67.setEnabled(false);
            iv_68.setEnabled(false);
            iv_71.setEnabled(false);
            iv_72.setEnabled(false);
            iv_73.setEnabled(false);
            iv_74.setEnabled(false);
            iv_75.setEnabled(false);
            iv_76.setEnabled(false);
            iv_77.setEnabled(false);
            iv_78.setEnabled(false);
            iv_81.setEnabled(false);
            iv_82.setEnabled(false);
            iv_83.setEnabled(false);
            iv_84.setEnabled(false);
            iv_85.setEnabled(false);
            iv_86.setEnabled(false);
            iv_87.setEnabled(false);
            iv_88.setEnabled(false);

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
                fadeOutAnimation(iv_17);
            } else if (clickedFirst == 7){
                fadeOutAnimation(iv_18);
            } else if (clickedFirst == 8){
                fadeOutAnimation(iv_21);
            } else if (clickedFirst == 9){
                fadeOutAnimation(iv_22);
            } else if (clickedFirst == 10){
                fadeOutAnimation(iv_23);
            } else if (clickedFirst == 11){
                fadeOutAnimation(iv_24);
            } else if (clickedFirst == 12){
                fadeOutAnimation(iv_25);
            } else if (clickedFirst == 13){
                fadeOutAnimation(iv_26);
            } else if (clickedFirst == 14){
                fadeOutAnimation(iv_27);
            } else if (clickedFirst == 15){
                fadeOutAnimation(iv_28);
            } else if (clickedFirst == 16){
                fadeOutAnimation(iv_31);
            } else if (clickedFirst == 17){
                fadeOutAnimation(iv_32);
            } else if (clickedFirst == 18){
                fadeOutAnimation(iv_33);
            } else if (clickedFirst == 19){
                fadeOutAnimation(iv_34);
            } else if (clickedFirst == 20){
                fadeOutAnimation(iv_35);
            } else if (clickedFirst == 21){
                fadeOutAnimation(iv_36);
            } else if (clickedFirst == 22){
                fadeOutAnimation(iv_37);
            } else if (clickedFirst == 23){
                fadeOutAnimation(iv_38);
            } else if (clickedFirst == 24){
                fadeOutAnimation(iv_41);
            } else if (clickedFirst == 25){
                fadeOutAnimation(iv_42);
            } else if (clickedFirst == 26){
                fadeOutAnimation(iv_43);
            } else if (clickedFirst == 27){
                fadeOutAnimation(iv_44);
            } else if (clickedFirst == 28){
                fadeOutAnimation(iv_45);
            } else if (clickedFirst == 29){
                fadeOutAnimation(iv_46);
            } else if (clickedFirst == 30){
                fadeOutAnimation(iv_47);
            } else if (clickedFirst == 31){
                fadeOutAnimation(iv_48);
            } else if (clickedFirst == 32){
                fadeOutAnimation(iv_51);
            } else if (clickedFirst == 33){
                fadeOutAnimation(iv_52);
            } else if (clickedFirst == 34){
                fadeOutAnimation(iv_53);
            } else if (clickedFirst == 35){
                fadeOutAnimation(iv_54);
            } else if (clickedFirst == 36){
                fadeOutAnimation(iv_55);
            } else if (clickedFirst == 37){
                fadeOutAnimation(iv_56);
            } else if (clickedFirst == 38){
                fadeOutAnimation(iv_57);
            } else if (clickedFirst == 39){
                fadeOutAnimation(iv_58);
            } else if (clickedFirst == 40){
                fadeOutAnimation(iv_61);
            } else if (clickedFirst == 41){
                fadeOutAnimation(iv_62);
            } else if (clickedFirst == 42){
                fadeOutAnimation(iv_63);
            } else if (clickedFirst == 43){
                fadeOutAnimation(iv_64);
            } else if (clickedFirst == 44){
                fadeOutAnimation(iv_65);
            } else if (clickedFirst == 45){
                fadeOutAnimation(iv_66);
            } else if (clickedFirst == 46){
                fadeOutAnimation(iv_67);
            } else if (clickedFirst == 47){
                fadeOutAnimation(iv_68);
            } else if (clickedFirst == 48){
                fadeOutAnimation(iv_71);
            } else if (clickedFirst == 49){
                fadeOutAnimation(iv_72);
            } else if (clickedFirst == 50){
                fadeOutAnimation(iv_73);
            } else if (clickedFirst == 51){
                fadeOutAnimation(iv_74);
            } else if (clickedFirst == 52){
                fadeOutAnimation(iv_75);
            } else if (clickedFirst == 53){
                fadeOutAnimation(iv_76);
            } else if (clickedFirst == 54){
                fadeOutAnimation(iv_77);
            } else if (clickedFirst == 55){
                fadeOutAnimation(iv_78);
            } else if (clickedFirst == 56){
                fadeOutAnimation(iv_81);
            } else if (clickedFirst == 57){
                fadeOutAnimation(iv_82);
            } else if (clickedFirst == 58){
                fadeOutAnimation(iv_83);
            } else if (clickedFirst == 59){
                fadeOutAnimation(iv_84);
            } else if (clickedFirst == 60){
                fadeOutAnimation(iv_85);
            } else if (clickedFirst == 61){
                fadeOutAnimation(iv_86);
            } else if (clickedFirst == 62){
                fadeOutAnimation(iv_87);
            } else if (clickedFirst == 63){
                fadeOutAnimation(iv_88);
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
                fadeOutAnimation(iv_17);
            } else if (clickedSecond == 7){
                fadeOutAnimation(iv_18);
            } else if (clickedSecond == 8){
                fadeOutAnimation(iv_21);
            } else if (clickedSecond == 9){
                fadeOutAnimation(iv_22);
            } else if (clickedSecond == 10){
                fadeOutAnimation(iv_23);
            } else if (clickedSecond == 11){
                fadeOutAnimation(iv_24);
            } else if (clickedSecond == 12){
                fadeOutAnimation(iv_25);
            } else if (clickedSecond == 13){
                fadeOutAnimation(iv_26);
            } else if (clickedSecond == 14){
                fadeOutAnimation(iv_27);
            } else if (clickedSecond == 15){
                fadeOutAnimation(iv_28);
            } else if (clickedSecond == 16){
                fadeOutAnimation(iv_31);
            } else if (clickedSecond == 17){
                fadeOutAnimation(iv_32);
            } else if (clickedSecond == 18){
                fadeOutAnimation(iv_33);
            } else if (clickedSecond == 19){
                fadeOutAnimation(iv_34);
            } else if (clickedSecond == 20){
                fadeOutAnimation(iv_35);
            } else if (clickedSecond == 21){
                fadeOutAnimation(iv_36);
            } else if (clickedSecond == 22){
                fadeOutAnimation(iv_37);
            } else if (clickedSecond == 23){
                fadeOutAnimation(iv_38);
            } else if (clickedSecond == 24){
                fadeOutAnimation(iv_41);
            } else if (clickedSecond == 25){
                fadeOutAnimation(iv_42);
            } else if (clickedSecond == 26){
                fadeOutAnimation(iv_43);
            } else if (clickedSecond == 27){
                fadeOutAnimation(iv_44);
            } else if (clickedSecond == 28){
                fadeOutAnimation(iv_45);
            } else if (clickedSecond == 29){
                fadeOutAnimation(iv_46);
            } else if (clickedSecond == 30){
                fadeOutAnimation(iv_47);
            } else if (clickedSecond == 31){
                fadeOutAnimation(iv_48);
            } else if (clickedSecond == 32){
                fadeOutAnimation(iv_51);
            } else if (clickedSecond == 33){
                fadeOutAnimation(iv_52);
            } else if (clickedSecond == 34){
                fadeOutAnimation(iv_53);
            } else if (clickedSecond == 35){
                fadeOutAnimation(iv_54);
            } else if (clickedSecond == 36){
                fadeOutAnimation(iv_55);
            } else if (clickedSecond == 37){
                fadeOutAnimation(iv_56);
            } else if (clickedSecond == 38){
                fadeOutAnimation(iv_57);
            } else if (clickedSecond == 39){
                fadeOutAnimation(iv_58);
            } else if (clickedSecond == 40){
                fadeOutAnimation(iv_61);
            } else if (clickedSecond == 41){
                fadeOutAnimation(iv_62);
            } else if (clickedSecond == 42){
                fadeOutAnimation(iv_63);
            } else if (clickedSecond == 43){
                fadeOutAnimation(iv_64);
            } else if (clickedSecond == 44){
                fadeOutAnimation(iv_65);
            } else if (clickedSecond == 45){
                fadeOutAnimation(iv_66);
            } else if (clickedSecond == 46){
                fadeOutAnimation(iv_67);
            } else if (clickedSecond == 47){
                fadeOutAnimation(iv_68);
            } else if (clickedSecond == 48){
                fadeOutAnimation(iv_71);
            } else if (clickedSecond == 49){
                fadeOutAnimation(iv_72);
            } else if (clickedSecond == 50){
                fadeOutAnimation(iv_73);
            } else if (clickedSecond == 51){
                fadeOutAnimation(iv_74);
            } else if (clickedSecond == 52){
                fadeOutAnimation(iv_75);
            } else if (clickedSecond == 53){
                fadeOutAnimation(iv_76);
            } else if (clickedSecond == 54){
                fadeOutAnimation(iv_77);
            } else if (clickedSecond == 55){
                fadeOutAnimation(iv_78);
            } else if (clickedSecond == 56){
                fadeOutAnimation(iv_81);
            } else if (clickedSecond == 57){
                fadeOutAnimation(iv_82);
            } else if (clickedSecond == 58){
                fadeOutAnimation(iv_83);
            } else if (clickedSecond == 59){
                fadeOutAnimation(iv_84);
            } else if (clickedSecond == 60){
                fadeOutAnimation(iv_85);
            } else if (clickedSecond == 61){
                fadeOutAnimation(iv_86);
            } else if (clickedSecond == 62){
                fadeOutAnimation(iv_87);
            } else if (clickedSecond == 63){
                fadeOutAnimation(iv_88);
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
                iv_17.setEnabled(true);
                iv_18.setEnabled(true);
                iv_27.setEnabled(true);
                iv_28.setEnabled(true);
                iv_37.setEnabled(true);
                iv_38.setEnabled(true);
                iv_47.setEnabled(true);
                iv_48.setEnabled(true);
                iv_57.setEnabled(true);
                iv_58.setEnabled(true);
                iv_67.setEnabled(true);
                iv_68.setEnabled(true);
                iv_71.setEnabled(true);
                iv_72.setEnabled(true);
                iv_73.setEnabled(true);
                iv_74.setEnabled(true);
                iv_75.setEnabled(true);
                iv_76.setEnabled(true);
                iv_77.setEnabled(true);
                iv_78.setEnabled(true);
                iv_81.setEnabled(true);
                iv_82.setEnabled(true);
                iv_83.setEnabled(true);
                iv_84.setEnabled(true);
                iv_85.setEnabled(true);
                iv_86.setEnabled(true);
                iv_87.setEnabled(true);
                iv_88.setEnabled(true);
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
                iv_66.getVisibility() == View.INVISIBLE &&
                iv_15.getVisibility() == View.INVISIBLE &&
                iv_16.getVisibility() == View.INVISIBLE &&
                iv_17.getVisibility() == View.INVISIBLE &&
                iv_18.getVisibility() == View.INVISIBLE &&
                iv_25.getVisibility() == View.INVISIBLE &&
                iv_26.getVisibility() == View.INVISIBLE &&
                iv_27.getVisibility() == View.INVISIBLE &&
                iv_28.getVisibility() == View.INVISIBLE &&
                iv_35.getVisibility() == View.INVISIBLE &&
                iv_36.getVisibility() == View.INVISIBLE &&
                iv_37.getVisibility() == View.INVISIBLE &&
                iv_38.getVisibility() == View.INVISIBLE &&
                iv_45.getVisibility() == View.INVISIBLE &&
                iv_46.getVisibility() == View.INVISIBLE &&
                iv_47.getVisibility() == View.INVISIBLE &&
                iv_48.getVisibility() == View.INVISIBLE &&
                iv_55.getVisibility() == View.INVISIBLE &&
                iv_56.getVisibility() == View.INVISIBLE &&
                iv_57.getVisibility() == View.INVISIBLE &&
                iv_58.getVisibility() == View.INVISIBLE &&
                iv_65.getVisibility() == View.INVISIBLE &&
                iv_66.getVisibility() == View.INVISIBLE &&
                iv_67.getVisibility() == View.INVISIBLE &&
                iv_68.getVisibility() == View.INVISIBLE &&
                iv_71.getVisibility() == View.INVISIBLE &&
                iv_72.getVisibility() == View.INVISIBLE &&
                iv_73.getVisibility() == View.INVISIBLE &&
                iv_74.getVisibility() == View.INVISIBLE &&
                iv_75.getVisibility() == View.INVISIBLE &&
                iv_76.getVisibility() == View.INVISIBLE &&
                iv_77.getVisibility() == View.INVISIBLE &&
                iv_78.getVisibility() == View.INVISIBLE &&
                iv_81.getVisibility() == View.INVISIBLE &&
                iv_82.getVisibility() == View.INVISIBLE &&
                iv_83.getVisibility() == View.INVISIBLE &&
                iv_84.getVisibility() == View.INVISIBLE &&
                iv_85.getVisibility() == View.INVISIBLE &&
                iv_86.getVisibility() == View.INVISIBLE &&
                iv_87.getVisibility() == View.INVISIBLE &&
                iv_88.getVisibility() == View.INVISIBLE) {

            String Name = tv_Name.getText().toString();
            long Time = Timer;
            int Moves = playerMove;
            int Score = playerScore;
            String Mode = gameMode;
            handler.removeCallbacksAndMessages(null);
            Intent intent = new Intent(ModeHard.this, GameOver.class);
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
        image109 = R.drawable.img_bellion;
        image110 = R.drawable.img_legia;
        image111 = R.drawable.img_yogumunt;
        image112 = R.drawable.img_dragonblade;
        image113 = R.drawable.img_rasaka;
        image114 = R.drawable.img_knight;
        image115 = R.drawable.img_knightrasaka;
        image116 = R.drawable.img_byung;
        image117 = R.drawable.img_hunter;
        image118 = R.drawable.img_sillad;
        image119 = R.drawable.img_querehsha;
        image120 = R.drawable.img_antares;
        image121 = R.drawable.img_baran;
        image122 = R.drawable.img_chiyul;
        image123 = R.drawable.img_kan;
        image124 = R.drawable.img_sung;
        image125 = R.drawable.img_woo;
        image126 = R.drawable.img_father;
        image127 = R.drawable.img_baek;
        image128 = R.drawable.img_cha;
        image129 = R.drawable.img_go;
        image130 = R.drawable.img_goto;
        image131 = R.drawable.img_rakan;
        image132 = R.drawable.img_tarnak;

        image201 = R.drawable.img_ashborn;
        image202 = R.drawable.img_beru;
        image203 = R.drawable.img_greed;
        image204 = R.drawable.img_igris;
        image205 = R.drawable.img_iron;
        image206 = R.drawable.img_kamish;
        image207 = R.drawable.img_tank;
        image208 = R.drawable.img_tusk;
        image209 = R.drawable.img_bellion;
        image210 = R.drawable.img_legia;
        image211 = R.drawable.img_yogumunt;
        image212 = R.drawable.img_dragonblade;
        image213 = R.drawable.img_rasaka;
        image214 = R.drawable.img_knight;
        image215 = R.drawable.img_knightrasaka;
        image216 = R.drawable.img_byung;
        image217 = R.drawable.img_hunter;
        image218 = R.drawable.img_sillad;
        image219 = R.drawable.img_querehsha;
        image220 = R.drawable.img_antares;
        image221 = R.drawable.img_baran;
        image222 = R.drawable.img_chiyul;
        image223 = R.drawable.img_kan;
        image224 = R.drawable.img_sung;
        image225 = R.drawable.img_woo;
        image226 = R.drawable.img_father;
        image227 = R.drawable.img_baek;
        image228 = R.drawable.img_cha;
        image229 = R.drawable.img_go;
        image230 = R.drawable.img_goto;
        image231 = R.drawable.img_rakan;
        image232 = R.drawable.img_tarnak;
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

