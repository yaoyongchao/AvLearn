package com.yyc.avlearn.primary

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import com.yyc.avlearn.R
import com.yyc.avlearn.base.BaseActivity

/**
 * Android 音视频开发(三)：使用 AudioTrack 播放PCM音频
 */
class ThreeAudioTrackActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_three_audio_track
    }

    override fun initViews() {
        title = "使用 AudioTrack 播放PCM音频"


    }

    override fun initListener() {

    }
}
