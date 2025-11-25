package com.example.hackheroes;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundMusicService extends Service {

    private MediaPlayer mediaPlayer;
    public int masterVol=100;
    public int musicVol=100;
    public int effectVol=100;
    private final IBinder binder = new MusicBinder();

    public class MusicBinder extends Binder {
        public BackgroundMusicService getService() {
            return BackgroundMusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createPlayer();
    }

    private synchronized void createPlayer() {
        releasePlayer();

        mediaPlayer = MediaPlayer.create(this, R.raw.background);
        if (mediaPlayer == null) return;

        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(1.0f, 1.0f);

        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            createPlayer();
            if (mediaPlayer != null) mediaPlayer.start();
            return true;
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        });
    }

    private synchronized void releasePlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
            } catch (Exception ignored) {}
            try {
                mediaPlayer.release();
            } catch (Exception ignored) {}
            mediaPlayer = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer == null) createPlayer();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public synchronized void setMusicVolume(float vol) {
        if (mediaPlayer != null) mediaPlayer.setVolume(vol, vol);
    }

    public synchronized void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    public synchronized void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start();
    }

    public synchronized void stopMusic() {
        if (mediaPlayer != null) {
            try { mediaPlayer.stop(); } catch (Exception ignored) {}
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }
}
