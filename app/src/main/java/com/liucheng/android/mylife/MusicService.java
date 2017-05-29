package com.liucheng.android.mylife;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created by liucheng on 2017/5/25.
 */

public class MusicService extends Service {

    private MediaPlayer mMediaPlayer;

    public static Intent newService(Context context){
        Intent intent = new Intent(context, MusicService.class);
        return intent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(this, R.raw.bgm);
            mMediaPlayer.start();
            mMediaPlayer.setLooping(true);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }
}
