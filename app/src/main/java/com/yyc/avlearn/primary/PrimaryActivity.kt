package com.yyc.avlearn.primary

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import com.yyc.avlearn.R
import com.yyc.avlearn.base.BaseActivity
import kotlinx.android.synthetic.main.activity_primary.*

class PrimaryActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_primary
    }

    override fun initViews() {
    }

    override fun initListener() {
        btn1.setOnClickListener {
            doNext(ImageOneActivity::class.java)
        }
        btn2.setOnClickListener {
            doNext(AudioRecordActivity::class.java)
        }
        btn3.setOnClickListener {
            doNext(ThreeAudioTrackActivity::class.java)
        }
    }
}
