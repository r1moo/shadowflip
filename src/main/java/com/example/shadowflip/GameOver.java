package com.example.shadowflip;

import static android.view.Gravity.CENTER;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class GameOver extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private int bgmMuted;
    private SharedPreferences sharedPreferences;
    private MediaPlayer sfxBtnClick;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(this);
        setContentView(R.layout.gameover);
        sfxBtnClick = MediaPlayer.create(this, R.raw.sfx_btnclick);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int lastBGMVolumeLevel = sharedPreferences.getInt("lastBGMVolumeLevel", 50);
        int lastSFXVolumeLevel = sharedPreferences.getInt("lastSFXVolumeLevel", 50);

        mediaPlayer = MediaPlayer.create(this, R.raw.music_gameover0);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        mediaPlayer.setVolume(lastBGMVolumeLevel,lastBGMVolumeLevel);
        sfxBtnClick = MediaPlayer.create(this, R.raw.sfx_btnclick);
        sfxBtnClick.setVolume(lastSFXVolumeLevel,lastSFXVolumeLevel);

        ImageView btn_Settings = findViewById(R.id.btn_Settings);
        btn_Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonClickSound();
                final Dialog dialog = new Dialog(GameOver.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.settings_mainmenu);

                ImageView btn_HowToPlay = dialog.findViewById(R.id.btn_HowToPlay);
                btn_HowToPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playButtonClickSound();
                        final Dialog dialog = new Dialog(GameOver.this);
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
                        final Dialog dialog = new Dialog(GameOver.this);
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

        ImageView btn_Leaderboard = findViewById(R.id.btn_Leaderboard);
        btn_Leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonClickSound();
                final Dialog dialog = new Dialog(GameOver.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.leaderboard);

                TableLayout tbl_Leaderboard = dialog.findViewById(R.id.tbl_Leaderboard);
                ImageView btn_Back = dialog.findViewById(R.id.btn_Back);
                btn_Back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playButtonClickSound();
                        dialog.dismiss();
                    }
                });

                // Get all data from the database
                Cursor res = dbHelper.getAllData();
                int number = 1;
                while (res.moveToNext()) {
                    if (number > 15) {
                        break;
                    }
                    TableRow row = new TableRow(GameOver.this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);

                    TextView rownumber = new TextView(GameOver.this);
                    TextView name = new TextView(GameOver.this);
                    TextView time = new TextView(GameOver.this);
                    TextView moves = new TextView(GameOver.this);
                    TextView score = new TextView(GameOver.this);

                    name.setText(res.getString(0));
                    time.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(res.getLong(1)), TimeUnit.MILLISECONDS.toSeconds(res.getLong(1)) % 60));
                    moves.setText(res.getString(2));
                    score.setText(res.getString(3));
                    rownumber.setText(String.valueOf(number));

                    name.setTextAppearance(R.style.TextAppearance);
                    time.setTextAppearance(R.style.TextAppearance);
                    moves.setTextAppearance(R.style.TextAppearance);
                    score.setTextAppearance(R.style.TextAppearance);
                    rownumber.setTextAppearance(R.style.TextAppearance);

                    int width = 95;
                    name.setLayoutParams(new TableRow.LayoutParams(width, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    moves.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    score.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    rownumber.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));

                    name.setGravity(Gravity.LEFT);
                    time.setGravity(Gravity.CENTER);
                    moves.setGravity(Gravity.CENTER);
                    score.setGravity(Gravity.CENTER);
                    rownumber.setGravity(CENTER);

                    row.addView(rownumber);
                    row.addView(name);
                    row.addView(time);
                    row.addView(moves);
                    row.addView(score);

                    tbl_Leaderboard.addView(row);

                    number++;
                }

                dialog.show();
            }
        });

        ImageView btn_Home = findViewById(R.id.btn_Home);
        btn_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButtonClickSound();
                Intent intent = new Intent(GameOver.this, MainMenu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        TextView tv_Name = findViewById(R.id.tv_Name);
        TextView tv_Time = findViewById(R.id.tv_Time);
        TextView tv_Score = findViewById(R.id.tv_Score);
        TextView tv_Move = findViewById(R.id.tv_Moves);

        // Retrieve values from intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int moves = intent.getIntExtra("moves", 0);
        String name = intent.getStringExtra("name");

        long time = intent.getLongExtra("time", 0);
        long totalSeconds = time / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);

        // Set values to TextViews
        tv_Name.setText(String.valueOf(name));
        tv_Score.setText(String.valueOf(score));
        tv_Move.setText(String.valueOf(moves));
        tv_Time.setText(String.valueOf(formattedTime));
    }

    private void playButtonClickSound() {
        if (sfxBtnClick != null) {
            sfxBtnClick.start();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Pause the background music when the activity is paused
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the background music when the activity is resumed
        bgmMuted = sharedPreferences.getInt("bgmMuted", 0);
        // Apply the mute state to the background music
        if (mediaPlayer != null) {
            if (bgmMuted == 1) {
                mediaPlayer.setVolume(0, 0);
            } else {
                mediaPlayer.setVolume(1, 1);
            }
        }
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
