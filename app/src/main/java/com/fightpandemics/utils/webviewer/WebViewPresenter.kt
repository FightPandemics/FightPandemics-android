package com.fightpandemics.utils.webviewer

import android.content.Context
import android.text.TextUtils
import android.webkit.URLUtil
import android.widget.Toast
import com.fightpandemics.R
import com.fightpandemics.utils.webviewer.UrlUtils.getHost
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URLEncoder

class WebViewPresenter(
    private val mContext: Context,
    private val mInteractor: Interactor
)  {
    private fun makeToast(text: CharSequence): Toast {
        return Toast.makeText(mContext, text, Toast.LENGTH_LONG)
    }

    fun validateUrl(url: String?) {
        if (URLUtil.isValidUrl(url)) {
            mInteractor.loadUrl(url)
        } else {
            if (!TextUtils.isEmpty(url)) {
                var tempUrl = url
                if (!URLUtil.isHttpUrl(url) && !URLUtil.isHttpsUrl(url)) {
                    tempUrl = "http://$url"
                }
                if (URLUtil.isValidUrl(tempUrl)) {
                    mInteractor.loadUrl(tempUrl)
                } else try {
                    tempUrl =
                        "http://www.google.com/search?q=" + URLEncoder.encode(url, "UTF-8")
                    tempUrl = getHost(tempUrl)
                    mInteractor.loadUrl(tempUrl)
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    mInteractor.showToast(makeToast(mContext.getString(R.string.message_invalid_url)))
                    mInteractor.close()
                } catch (e: MalformedURLException) {
                }
            } else {
                mInteractor.showToast(makeToast(mContext.getString(R.string.message_invalid_url)))
                mInteractor.close()
            }
        }
    }

    interface Interactor {
        fun loadUrl(url: String?)
        fun close()
        fun showToast(toast: Toast?)
    }

}