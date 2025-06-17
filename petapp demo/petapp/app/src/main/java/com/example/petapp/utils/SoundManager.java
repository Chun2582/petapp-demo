package com.example.petapp.utils;
import android.content.Context;
import android.media.MediaPlayer;
import com.example.petapp.R;

public class SoundManager {
    private MediaPlayer backgroundMusic, soundEffects;
    private Context context;

    public SoundManager(Context context) { this.context = context; }

    public void playBackgroundMusic() {
        if (backgroundMusic == null) {
            backgroundMusic = MediaPlayer.create(context,R.raw.bg_music);
            backgroundMusic.setLooping(true);
        }
        if (!backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
        // 背景音樂邏輯（需要音檔）
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void playFeedSound() { /* 餵食音效 */ }
    public void playPlaySound() { /* 玩耍音效 */ }
    public void playSleepSound() { /* 睡覺音效 */ }

    public void release() {
        if (backgroundMusic != null) backgroundMusic.release();
        if (soundEffects != null) soundEffects.release();
    }
}