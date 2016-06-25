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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.matic.laradiodetotoral.rss.ReadRSS;
import com.matic.laradiodetotoral.utils.Constants;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





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

            fab.setImageResource(R.drawable.ic_pause);

            if (intialStage)
                new Player()
                        .execute(Constants.URL_STREAM);
            else {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }
            playPause = true;
        } else {

            fab.setImageResource(R.drawable.ic_play);
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
        if (id == R.id.action_chat) {
            Intent act_chat=new Intent(this,ChatActivity.class);
            this.startActivity(act_chat);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        switch(item.getItemId()){
            case R.id.nav_Lo_ultimo:
                ReadRSS readRSS = new ReadRSS(this, recyclerView);
                readRSS.execute();
                break;
            case R.id.nav_Noticia_Destacado:
                Toast.makeText(MainActivity.this, "Destacados", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_Participar:
                Intent act=new Intent(this,SorteoActivity.class);
                this.startActivity(act);
                break;
            case R.id.nav_comentarios:
                Intent act_chat=new Intent(this,ChatActivity.class);
                this.startActivity(act_chat);
                break;

            //contacto
            case R.id.nav_whatsapp:
                Toast.makeText(MainActivity.this, "Whatsapp: 3524400576 / 3524400575 ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_call:
                Toast.makeText(MainActivity.this, "Tel: 03524470825 / 4700895", Toast.LENGTH_SHORT).show();
                break;

            //redes sociales
            case R.id.nav_facebook:
                Uri uri_fb = Uri.parse(getString(R.string.uri_app_fb));
                Intent ifb = new Intent(Intent.ACTION_VIEW, uri_fb);
                startActivity(ifb);
                Toast.makeText(MainActivity.this, "Facebook Estacion fm", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_twitter:
                Uri uri_tw = Uri.parse(getString(R.string.uri_app_tw));
                Intent itw = new Intent(Intent.ACTION_VIEW, uri_tw);
                startActivity(itw);
                Toast.makeText(MainActivity.this, "Twitter Estacion fm", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_web:
                Uri uri_web = Uri.parse(getString(R.string.url_app));
                Intent iweb = new Intent(Intent.ACTION_VIEW, uri_web);
                startActivity(iweb);
                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                        fab.setImageResource(R.drawable.ic_play);
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
                    R.drawable.ic_play);


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

    }



}
