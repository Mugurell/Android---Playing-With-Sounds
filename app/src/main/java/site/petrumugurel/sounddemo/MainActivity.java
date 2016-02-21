package site.petrumugurel.sounddemo;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer  mMediaPlayer;
    private AudioManager mAudioManager; // to interact with android, change volume, etc

    private SeekBar mVolumeSeekBar;
    private SeekBar mScrubSeekBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button playSound  = (Button) findViewById(R.id.mainA_RL_LL_BTN_play);
        final Button pauseSound = (Button) findViewById(R.id.mainA_LL_RL_BTN_pause);

        mMediaPlayer = MediaPlayer.create(this, R.raw.kid_laugh_long);

        /**
         * Set a seekbar for volume.
         */
        // to be able to work with audio on the device
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // set the maximum volume based on maximum volume on system for music
        int maxVolume     = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mVolumeSeekBar = (SeekBar) findViewById(R.id.mainA_RL_SB_volume);
        mVolumeSeekBar.setMax(maxVolume);
        mVolumeSeekBar.setProgress(currentVolume);

        mVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mAudioManager != null) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        /**
         *   Set a seekbar for scrub.
         */
        mScrubSeekBar = (SeekBar) findViewById(R.id.mainA_RL_SB_scrub);
        // set maximum scrub length to the length of the file
        mScrubSeekBar.setMax(mMediaPlayer.getDuration());

        // To update the SeekBar for the elapsed progress:
        // Schedule a certain task to be done every second with the help of a runnable
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mScrubSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
            }
        // run the code immediately and then every second
        }, 0, 500); // 0 - number of ms before running for the first time
                    // 500 - number of ms between successful calls to the timer

        mScrubSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                    mScrubSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // to pause playing when seeking - remove any overlapping sounds
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    pauseSound.setEnabled(false);
                    playSound.setEnabled(false);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    pauseSound.setEnabled(true);
                    playSound.setEnabled(false);
                }
            }
        });


        playSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.seekTo(mScrubSeekBar.getProgress());
                mMediaPlayer.start();
                pauseSound.setEnabled(true);
                playSound.setEnabled(false);
            }
        });
        pauseSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.pause();
                pauseSound.setEnabled(false);
                playSound.setEnabled(true);
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.seekTo(0);
                mMediaPlayer.pause();
                playSound.setEnabled(true);
                pauseSound.setEnabled(false);
            }
        });
    }


    // To get key events even if the window has no focus
    public void takeKeyEvents(boolean get) {
        getWindow().takeKeyEvents(get);
    }

    @Override
    public boolean dispatchKeyEvent (KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
            || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            if (mAudioManager != null && mVolumeSeekBar != null) {
                int currentVolume
                        = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mVolumeSeekBar.setProgress(currentVolume);
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;    // signal we've constructed the menu and want it shown.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.mainM_I_settyings) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                              "No available settyings atm",
                                              Snackbar.LENGTH_LONG);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            snackbar.setActionTextColor(Color.BLUE);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#E18700"));
            snackbar.show();
        }

        return true;    // we've dealt with it, don't want other listeners to intercept it
    }
}
