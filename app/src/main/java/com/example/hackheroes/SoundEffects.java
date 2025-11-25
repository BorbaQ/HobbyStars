package com.example.hackheroes;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundEffects {

    private static SoundPool soundPool;
    private static int clickSound;
    private static int button;
    private static int reward;
    private static float volume = 1f;

    public static void init(Context context) {
        if (soundPool != null) return;

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attrs)
                .build();

        button = soundPool.load(context, R.raw.button,1);
        reward = soundPool.load(context, R.raw.reward,1);
    }

    public static void playClick() {
        if (soundPool != null) {
            soundPool.play(clickSound, volume, volume, 1, 0, 1f);
        }
    }
    public static  void playButton(){
        if (soundPool !=null){
            soundPool.play(button,volume,volume,1,0,1f);
        }
    }
    public static  void playReward(){
        if (soundPool !=null){
            soundPool.play(reward,volume,volume,1,0,1f);
        }
    }

    public static void setVolume(float v) {
        volume = v;
    }
}
