package com.yyc.avlearn

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.yyc.avlearn.base.BaseActivity
import com.yyc.avlearn.primary.ImageOneActivity
import com.yyc.avlearn.primary.PrimaryActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : BaseActivity() {
    var permiss = arrayOf(Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.MODIFY_AUDIO_SETTINGS)
//    var permiss = arrayOf(Manifest.permission.RECORD_AUDIO,Manifest.permission.MODIFY_AUDIO_SETTINGS)
    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        title = "音视频开发练习Demo"

        verifyStoragePermissions()

    }

    override fun initListener() {
        btn_primary.setOnClickListener {
            doNext(PrimaryActivity::class.java)
        }
    }


    fun verifyStoragePermissions() {
        try {
            log("检测是否有读写权限")
            var permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                //没有写的权限  去申请权限，会弹出对话框
                ActivityCompat.requestPermissions(this,
                    permiss,1)
            }
        } catch (e:Exception) {
            log("bug" + e.toString())
        }
    }

}
