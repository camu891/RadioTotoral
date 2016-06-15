package com.matic.laradiodetotoral;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.matic.laradiodetotoral.rss.ReadRSS;
import com.matic.laradiodetotoral.utils.Constants;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ForegroundService";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    private Button btnPrev;
    private Button btnPlay;
    private Button btnNext;

    private FloatingActionButton fab;


    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private boolean intialStage = true;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(pausePlay);


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(pausePlay);


        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        //Lector RSS


        // Obtener el Recycler
        recyclerView = (RecyclerView) findViewById(R.id.reciclador);
        recyclerView.setHasFixedSize(true);

        ReadRSS readRSS = new ReadRSS(this, recyclerView);
        readRSS.execute();


    }

    private View.OnClickListener pausePlay = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // TODO Auto-generated method stub

            actionPausePlay();

        }
    };


    public void actionPausePlay() {
        if (!playPause) {
            btnPlay.setBackgroundResource(android.R.drawable.ic_media_pause);
            fab.setImageResource(android.R.drawable.ic_media_pause);

            if (intialStage)
                new Player()
                        .execute(Constants.URL_STREAM);
            else {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }
            playPause = true;
        } else {
            btnPlay.setBackgroundResource(android.R.drawable.ic_media_play);
            fab.setImageResource(android.R.drawable.ic_media_play);
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
            playPause = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mediaPlayer.setDataSource(params[0]);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        intialStage = true;
                        playPause = false;
                        btnPlay.setBackgroundResource(android.R.drawable.ic_media_play);
                        fab.setImageResource(android.R.drawable.ic_media_play);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage(getString(R.string.str_loading));
            this.progress.show();

        }


        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            mediaPlayer.start();
            intialStage = false;


        }

        public Player() {
            progress = new ProgressDialog(MainActivity.this);
        }


    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        /*if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }*/


        if (!intialStage) {

            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);

            buildNotification(startIntent);


        }


    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void buildNotification(Intent intent) {

        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {


            Notification.Builder mBuilder;
            mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //Notification.MediaStyle style = new Notification.MediaStyle();


            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);


            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher);


            Intent playIntent = new Intent(this, MainActivity.class);
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);
            PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                    playIntent, 0);


            if (playPause) {
                //llamar al metodo on pause
                mBuilder = new Notification.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_media_play)
                        .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("Reproduciendo...")
                        .addAction(android.R.drawable.ic_media_pause, "Stop", pplayIntent);


            } else {

                mBuilder = new Notification.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_media_play)
                        .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("Pausado...")
                        .addAction(android.R.drawable.ic_media_play, "Play", pplayIntent);


            }

            mBuilder.setContentIntent(pendingIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            Log.i(LOG_TAG, "Clicked");

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Log.i(LOG_TAG, "Clicked Play");

        }


        this.finish();


    }


}
