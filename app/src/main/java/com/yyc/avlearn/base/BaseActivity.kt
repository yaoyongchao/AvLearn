package com.yyc.avlearn.base

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes

import com.yyc.avlearn.R
import com.yyc.avlearn.primary.ImageOneActivity
import kotlinx.android.synthetic.main.activity_base.*
import java.util.zip.Inflater

abstract class BaseActivity : AppCompatActivity(),View.OnClickListener {
    val TAG = "av"
    lateinit var mContext :Context
    var title =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        mContext = this
        initBaseViews()
    }

    private fun initBaseViews() {
        llyt_body.addView(LayoutInflater.from(mContext).inflate(getLayout(),null))
        initViews()
        initListener()

        tv_title.text = title
        img_back.setOnClickListener {
            finish()
        }
    }

    abstract fun getLayout(): Int
    abstract fun initViews()
    abstract fun initListener()



    fun doNext(cls: Class<*>) {
        startActivity(Intent(mContext,cls))
    }

    override fun onClick(v: View?) {

    }

    fun log(logStr: String) {
        Log.e(TAG,logStr)
    }
}
