package com.fightpandemics.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class ImageLoadTask(private val url: String, private val imageView: ImageView) :
    AsyncTask<Void?, Void?, Bitmap?>() {

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        if(result != null) {
            Log.d("TAG", "here")
            imageView.setImageBitmap(result)
        }
    }

    override fun doInBackground(vararg p0: Void?): Bitmap? {
        return try {
            val urlConnection = URL(url)
            val connection: HttpURLConnection = urlConnection
                .openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}