package com.pankajmehtanewdemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pankajmehtanewdemo.util.Config;

public class YoutubeVideoPlayActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    String videoid;
    YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_video_play);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.DEVELOPER_KEY, this);
        videoid =getIntent().getStringExtra("id");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
        if (!b) {
        //    if (videoid != null) {
                youTubePlayer = player;
                youTubePlayer.setFullscreen(true);
                player.loadVideo(videoid);
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener()
                {
                    @Override
                    public void onFullscreen(boolean fullscreen)
                    {
                        if(fullscreen)
                        {
                            youTubePlayer.play();
                        }
                        else
                        {
                            youTubePlayer.play();
                        }
                    }
                });
            }
       // }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
}
