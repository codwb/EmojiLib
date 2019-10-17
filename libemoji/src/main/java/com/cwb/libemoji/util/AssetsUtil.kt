package com.cwb.libemoji.util

import android.content.Context
import java.nio.charset.Charset
import java.io.*


/**
 *   Create by cwb on 2019/10/15
 *
 *   Describe: 加载资源文件
 */
object AssetsUtil {

    fun getIdByName(context: Context, name: String): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

    private const val BUFF_SIZE = 2048

    fun getJson(fileName: String, context: Context): String {
        //将json数据变成字符串
        val stringBuilder = StringBuilder()
        try {
            //获取assets资源管理器
            val assetManager = context.assets
            //通过管理器打开文件并读取
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName)
                )
            )
            var line: String? = ""
            do {
                line = bf.readLine()
                if (line == null) break
                stringBuilder.append(line)
            } while (true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }

    fun readAssets2String(context: Context, assetsFilePath: String, charsetName: String): String? {
        val inputStream: InputStream
        try {
            inputStream = context.assets.open(assetsFilePath)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        val bytes = is2Bytes(inputStream) ?: return null
        return if (charsetName.isBlank()) {
            String(bytes)
        } else {
            try {
                String(bytes, Charset.forName(charsetName))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                ""
            }

        }
    }

    private fun is2Bytes(`is`: InputStream?): ByteArray? {
        if (`is` == null) return null
        var os: ByteArrayOutputStream? = null
        try {
            os = ByteArrayOutputStream()
            val b = ByteArray(BUFF_SIZE)
            var len: Int
            len = `is`.read(b, 0, BUFF_SIZE)
            while (len != -1) {
                os.write(b, 0, len)
                len = `is`.read(b, 0, BUFF_SIZE)
            }
            return os.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}