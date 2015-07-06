package com.cjq.yicaijiaoyu.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.videoPlayer.MediaController;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.OnPreparedListener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by android on 2015/6/16.
 */
public class FullscreenPlayActivity extends BaseActivity {


    private String path;
    private String vid;
    private ProgressBar progressBar;
    private IjkVideoView videoview;
    private MediaController mediaController;
    private int stopPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_play_activity);
        Bundle e = getIntent().getExtras();
        if (e != null) {
            path = e.getString("path");
            vid = e.getString("vid");
            stopPosition = e.getInt("stopPosition",0);
        }

        progressBar = (ProgressBar) findViewById(R.id.loadingprogress);
        videoview = (IjkVideoView) findViewById(R.id.videoview);
        mediaController = new MediaController(this, false);
        videoview.setMediaController(mediaController);
        videoview.setMediaBufferingIndicator(progressBar);
//        if (path != null && path.length() > 0) {
//            progressBar.setVisibility(View.GONE);
//            videoview.setVideoURI(Uri.parse(path));
//        } else {
            videoview.setVid(vid, 1);
//        }
        videoview.seekTo(stopPosition);
        videoview.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(IMediaPlayer mp) {
            }
        });

        // 设置视频尺寸 ，在横屏下效果较明显
        mediaController.setOnVideoChangeListener(new MediaController.OnVideoChangeListener() {
            @Override
            public void onVideoChange(int layout) {
                videoview.setVideoLayout(layout);
                switch (layout) {
                    case IjkVideoView.VIDEO_LAYOUT_ORIGIN:
                        Toast.makeText(FullscreenPlayActivity.this, "VIDEO_LAYOUT_ORIGIN", Toast.LENGTH_SHORT).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_SCALE:
                        Toast.makeText(FullscreenPlayActivity.this, "VIDEO_LAYOUT_SCALE", Toast.LENGTH_SHORT).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_STRETCH:
                        Toast.makeText(FullscreenPlayActivity.this, "VIDEO_LAYOUT_STRETCH", Toast.LENGTH_SHORT).show();
                        break;
                    case IjkVideoView.VIDEO_LAYOUT_ZOOM:
                        Toast.makeText(FullscreenPlayActivity.this, "VIDEO_LAYOUT_ZOOM",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
