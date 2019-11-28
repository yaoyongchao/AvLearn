package com.yyc.avlearn.primary

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View


import com.yyc.avlearn.R
import com.yyc.avlearn.base.BaseActivity
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_image_one.*

/**
 * Android音视频开发（-）：通过三种方式绘制图片
 */

class ImageOneActivity : BaseActivity() {
    lateinit var bitmap: Bitmap
    override fun getLayout(): Int {
        return R.layout.activity_image_one
    }

    override fun initViews() {
        title = "一：通过三种方式绘制图片"
        bitmap = BitmapFactory.decodeResource(resources,R.drawable.ic_one)
        twoSurface()
    }

    override fun initListener() {
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
    }

    fun imageOne() {
        img.visibility = View.VISIBLE
        img.setImageBitmap(bitmap)
    }

    fun twoSurface() {
        //为SurfasceHolder添加一个SurfaceHolder.CallBack回调接口
        surface.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder?) {
            }

            override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
                if (surfaceHolder == null)
                    return
                var paint = Paint()
                paint.isAntiAlias = true
                paint.style = Paint.Style.STROKE

                var canvas = surfaceHolder.lockCanvas()//锁定当前surfaceView的画布
                canvas.drawBitmap(bitmap,0f,0f,paint);//执行绘制操作
                surfaceHolder.unlockCanvasAndPost(canvas)//解除锁定并显示在界面上
            }

        })


    }

    fun  customThree() {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.btn1 -> {imageOne()}
            R.id.btn2 -> {
                Thread(
                    Runnable {
                        log("开启了子线程")
                        twoSurface()
                    }
                ).start()
            }
            R.id.btn3 -> {customThree()}
        }
    }

}
