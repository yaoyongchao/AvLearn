package com.yyc.avlearn.primary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.yyc.avlearn.R

class CustomViewOne @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var paint = Paint()
    lateinit var bitmap : Bitmap
    lateinit var mContext: Context

    init {
        initViews()
    }

    private fun initViews() {
        paint.isAntiAlias = true

        paint.style = Paint.Style.STROKE
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_one)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //不建议在onDraw上面做任何分配内存的操作
        if (bitmap != null)
            canvas?.drawBitmap(bitmap,0f,0f,paint)
    }
}
