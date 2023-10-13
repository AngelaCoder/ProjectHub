package com.example.application.internshipaspire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class Music extends AppCompatActivity {

    Button btn_play, btn_stop;

    MediaPlayer mediaPlayer;

    int resumePosition = 0; // Variable to store the playback position

    //when the activity closes, we will run this code.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {

            mediaPlayer.release();
            mediaPlayer = null;

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Navigate back to the menu screen
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        btn_play = findViewById(R.id.btn_play);
        btn_stop = findViewById(R.id.btn_stop);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer == null) {
                    // If mediaPlayer is not initialized, create and start it
                    mediaPlayer = MediaPlayer.create(Music.this, R.raw.music);
                    mediaPlayer.setVolume(0.2f, 1.0f);
                    mediaPlayer.start();
                } else {
                    // If mediaPlayer is already initialized, resume from the previous position
                    mediaPlayer.seekTo(resumePosition);
                    mediaPlayer.start();
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    // Store the current playback position
                    resumePosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                }
            }
        });



    }
}