package site.petrumugurel.sounddemo;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.kid_laugh);

        Button playSound = (Button) findViewById(R.id.mainA_RL_LL_BTN_play);
        playSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });

        Button pauseSound = (Button) findViewById(R.id.mainA_LL_RL_BTN_pause);
        pauseSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });
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
                                               Snackbar .LENGTH_LONG);
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
