package com.yyc.avlearn.primary

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import com.yyc.avlearn.AudioFileFunc

import com.yyc.avlearn.R
import com.yyc.avlearn.base.BaseActivity
import kotlinx.android.synthetic.main.activity_audio_record.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

/**
 * AudioRecord时Android系统提供的用于实现录音的功能类
 * 实现Android录音的流程为
 * 1、构造一个AudioRecord对象， 其中需要的最小轮缓存buffer大小可以通过getMinBufferSize方法得到，如果Buffer容量过小， 将导致对象构造的失败
 * 2、初始化一二bugger，该bugger大于等于AudioRecord对象用于写声音数据的buffer大小。
 * 3、开始录音
 * 4、创建一个数据流， 一边从AudioRecord中读取生硬数据到初始化的buffer，一边讲buffer中的数据导入数据流
 * 5、关闭数据流
 * 6、停止录音
 *
 * 录音一定要注意录音权限
 */
class AudioRecordActivity : BaseActivity() {
    //生命AudioRecord对象
    private var audioRecord:AudioRecord ?= null
    //生命recoordBuffer的大小字段
    private var recordBufSize = 0
    private var isRecording = false

    //AudioName裸音音频数据文件  麦克风
    var audioName = ""
    //可播放的音频文件
    var newAudioName = ""

    override fun getLayout(): Int {
        return R.layout.activity_audio_record
    }

    override fun initViews() {
        title = "使用AudioRecord采集音频PCM并保存到文件"
        createAudioRecord()
    }

    override fun initListener() {
        btn_start.setOnClickListener {
            startRecord()
        }
        btn_end.setOnClickListener {
            endRecord()
        }
        btn_pause.setOnClickListener {
            pauseRecord()
        }
    }

    fun createAudioRecord() {
        //获取音频文件路径
        audioName = AudioFileFunc.getRawFilePath()
        log("裸文件路径：" + audioName)
        newAudioName = AudioFileFunc.getWavFilePath()
        log("播放文件路径：" + newAudioName)

        //获取缓存区字节大小
        recordBufSize = AudioRecord.getMinBufferSize(AudioFileFunc.AUDIO_SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_STEREO,AudioFormat.ENCODING_PCM_16BIT)
        log("0123456")
        //创建AudioRecord对象
        audioRecord = AudioRecord(AudioFileFunc.AUDIO_INPUT,AudioFileFunc.AUDIO_SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_STEREO,AudioFormat.ENCODING_PCM_16BIT,recordBufSize)


        log("audioRecord:" + audioRecord)

    }

    fun startRecord() {
        audioRecord?.startRecording()
        isRecording = true
        //开启音频文件写入线程
        Thread(AudioRecordThread()).start()
    }

    fun pauseRecord() {
        isRecording = false
    }

    fun  endRecord() {
        closeRecord()
    }

    fun closeRecord() {
        if (audioRecord != null) {
            log("关闭录音")
            isRecording = false
            audioRecord?.stop()
            audioRecord?.release()//释放资源
            audioRecord  = null
        }
    }

    /**
     * 这里将数据写入文件， 但是并不能播放， 因为AudioRecord获得的音频时原始的裸音频，
     * 如果需要播放就必须加入一些格式或者编码的头信息，但是这样的好处就是你可以对音频的裸数据进行处理， 比如你要做一个爱说话的Tom猫
     * 在这里尽心音频的处理，然后重新封装 所以说这样得到的音频比较容易做一些音频的处理。
     */
    private fun writeDataToFile() {
        //创建一个byte数组用来存一些字节数据， 大小为缓冲大小
        var audioData = ByteArray(recordBufSize)
        var fos:FileOutputStream
        var readSize = 0
        try {
            var file = File(audioName)
            if (file.exists()) {
                file.delete()
            }
            //建立一个可存取字节的文件
            fos = FileOutputStream(file)
            while (isRecording == true) {
                readSize = audioRecord?.read(audioData,0,recordBufSize)!!
                if (AudioRecord.ERROR_INVALID_OPERATION != readSize && fos != null) {
                    fos.write(audioData)
                }
            }

            if (fos != null)
                fos.close()//关闭写入流
        } catch (e:Exception) {
            log("Bug writeData:" + e.message.toString())

        }
    }

    /**
     * 这里得到可播放的音频文件
     */
    fun copyWaveFile(inFileName : String,outFileName : String) {
        var inStream :FileInputStream
        var outStream: FileOutputStream

        var totalAudioLen = 0L
        var totalDataLen:Long = totalAudioLen + 36
        var longSampleRate:Long = AudioFileFunc.AUDIO_SAMPLE_RATE.toLong()
        var channels = 2
        var byteRate:Long = (16*AudioFileFunc.AUDIO_SAMPLE_RATE * channels / 8).toLong()
        var data = ByteArray(recordBufSize)
        try {
            inStream = FileInputStream(inFileName)
            outStream = FileOutputStream(outFileName)
            totalAudioLen = inStream.channel.size()
            totalDataLen = totalAudioLen + 36
            WriteWaveFileHeader(outStream,totalAudioLen,totalDataLen,longSampleRate,channels,byteRate)
            while (inStream.read(data) != -1) {
                outStream.write(data)
            }

            inStream.close()
            outStream.close()
        } catch (e:Exception) {
            log("Bug copy:" + e.message.toString())
        }
    }


    /**
     *
     * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。
     *
     * 为我为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav
     *
     * 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有
     *
     * 自己特有的头文件。
     *
     */
    @Throws(IOException::class)
    private fun WriteWaveFileHeader(
        out: FileOutputStream, totalAudioLen: Long,

        totalDataLen: Long, longSampleRate: Long, channels: Int, byteRate: Long
    ) {

        val header = ByteArray(44)

        header[0] = 'R'.toByte() // RIFF/WAVE header

        header[1] = 'I'.toByte()

        header[2] = 'F'.toByte()

        header[3] = 'F'.toByte()

        header[4] = (totalDataLen and 0xff).toByte()

        header[5] = (totalDataLen shr 8 and 0xff).toByte()

        header[6] = (totalDataLen shr 16 and 0xff).toByte()

        header[7] = (totalDataLen shr 24 and 0xff).toByte()

        header[8] = 'W'.toByte()

        header[9] = 'A'.toByte()

        header[10] = 'V'.toByte()

        header[11] = 'E'.toByte()

        header[12] = 'f'.toByte() // 'fmt ' chunk

        header[13] = 'm'.toByte()

        header[14] = 't'.toByte()

        header[15] = ' '.toByte()

        header[16] = 16 // 4 bytes: size of 'fmt ' chunk

        header[17] = 0

        header[18] = 0

        header[19] = 0

        header[20] = 1 // format = 1

        header[21] = 0

        header[22] = channels.toByte()

        header[23] = 0

        header[24] = (longSampleRate and 0xff).toByte()

        header[25] = (longSampleRate shr 8 and 0xff).toByte()

        header[26] = (longSampleRate shr 16 and 0xff).toByte()

        header[27] = (longSampleRate shr 24 and 0xff).toByte()

        header[28] = (byteRate and 0xff).toByte()

        header[29] = (byteRate shr 8 and 0xff).toByte()

        header[30] = (byteRate shr 16 and 0xff).toByte()

        header[31] = (byteRate shr 24 and 0xff).toByte()

        header[32] = (2 * 16 / 8).toByte() // block align

        header[33] = 0

        header[34] = 16 // bits per sample

        header[35] = 0

        header[36] = 'd'.toByte()

        header[37] = 'a'.toByte()

        header[38] = 't'.toByte()

        header[39] = 'a'.toByte()

        header[40] = (totalAudioLen and 0xff).toByte()

        header[41] = (totalAudioLen shr 8 and 0xff).toByte()

        header[42] = (totalAudioLen shr 16 and 0xff).toByte()

        header[43] = (totalAudioLen shr 24 and 0xff).toByte()

        out.write(header, 0, 44)

    }

    inner class AudioRecordThread : Runnable {
        override fun run() {
            //往文件中写入裸数据
            writeDataToFile()
            //给裸数据加上头文件
            copyWaveFile(audioName,newAudioName)
        }

    }
}
