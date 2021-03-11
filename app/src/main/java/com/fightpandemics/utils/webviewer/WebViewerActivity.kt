package com.fightpandemics.utils.webviewer

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.fightpandemics.R
import kotlinx.android.synthetic.main.a_web_viewer.*

class WebViewerActivity :
    AppCompatActivity(),
    WebViewPresenter.Interactor,
    View.OnClickListener {
    private var mUrl: String? = null
    private var filePathCallbackLollipop: ValueCallback<Array<Uri>?>? = null
    private var filePathCallbackNormal: ValueCallback<Uri?>? = null
    private var mPresenter: WebViewPresenter? = null
    private var mCoordinatorLayout: CoordinatorLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        mUrl = intent.getStringExtra(EXTRA_URL)
        setContentView(R.layout.a_web_viewer)
        bindView()
        mPresenter = WebViewPresenter(this, this)
        mPresenter!!.validateUrl(mUrl)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (mUrl != null) {
            outState.putString(EXTRA_URL, mUrl)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        mUrl = savedInstanceState.getString(EXTRA_URL)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode) {
            when (requestCode) {
                REQUEST_FILE_CHOOSER -> {
                    if (filePathCallbackNormal == null) {
                        return
                    }
                    val result = data?.data
                    filePathCallbackNormal!!.onReceiveValue(result)
                    filePathCallbackNormal = null
                }
                REQUEST_FILE_CHOOSER_FOR_LOLLIPOP -> {
                    if (filePathCallbackLollipop == null) {
                        return
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        filePathCallbackLollipop!!.onReceiveValue(
                            WebChromeClient.FileChooserParams.parseResult(
                                resultCode,
                                data
                            )
                        )
                    }
                    filePathCallbackLollipop = null
                }
            }
        } else {
            when (requestCode) {
                REQUEST_PERMISSION_SETTING -> {
                    Toast.makeText(
                        this,
                        R.string.write_permission_denied_message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun bindView() {
        mCoordinatorLayout = findViewById(R.id.a_web_viewer_coordinatorlayout)
        val webSettings = a_web_viewer_wv.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.displayZoomControls = false
        webSettings.builtInZoomControls = true
        webSettings.setSupportZoom(true)
        webSettings.domStorageEnabled = true
        a_web_viewer_wv.webChromeClient = MyWebChromeClient()
        a_web_viewer_wv.webViewClient = MyWebViewClient()
        a_web_viewer_wv.setOnCreateContextMenuListener(this)
    }

    override fun loadUrl(url: String?) {
        url?.let { a_web_viewer_wv?.loadUrl(it) }
    }

    override fun close() {
        finish()
    }

    override fun showToast(toast: Toast?) {
        toast?.show()
    }

    override fun onClick(v: View) {
        TODO()
    }
    override fun onPointerCaptureChanged(hasCapture: Boolean) {
        TODO()
    }
    inner class MyWebChromeClient : WebChromeClient() {
        override fun onJsAlert(
            view: WebView,
            url: String,
            message: String,
            result: JsResult
        ): Boolean {
            val dialog =
                AlertDialog.Builder(this@WebViewerActivity)
                    .setMessage(message)
                    .setPositiveButton(R.string.yes) { _, _ -> result.confirm() }
                    .create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            return true
        }

        override fun onJsConfirm(
            view: WebView,
            url: String,
            message: String,
            result: JsResult
        ): Boolean {
            val dialog =
                AlertDialog.Builder(this@WebViewerActivity)
                    .setMessage(message)
                    .setPositiveButton(R.string.yes) { _, _ -> result.confirm() }
                    .setNegativeButton(R.string.no) { _, _ -> result.cancel() }
                    .create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            return true
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            return if (url.endsWith(".mp4")) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(url), "video/*")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                view.context.startActivity(intent)
                true
            } else if (url.startsWith("tel:") ||
                url.startsWith("sms:") ||
                url.startsWith("smsto:")
            ) {
                getURLIntent(url, view)
                true
            } else if (url.startsWith("mms:") || url.startsWith("mmsto:") || url.startsWith("mailto:")) {
                getURLIntent(url, view)
                true
            } else {
                super.shouldOverrideUrlLoading(view, url)
            }
        }

        private fun getURLIntent(url: String, view: WebView) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            view.context.startActivity(intent)
        }
    }

    companion object {
        private const val REQUEST_FILE_CHOOSER = 0
        private const val REQUEST_FILE_CHOOSER_FOR_LOLLIPOP = 1
        private const val REQUEST_PERMISSION_SETTING = 2
        const val EXTRA_URL = "url"
    }
}
