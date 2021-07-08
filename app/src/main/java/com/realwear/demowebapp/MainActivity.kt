package com.realwear.demowebapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebSettings.LOAD_NO_CACHE
import android.webkit.WebView
import android.webkit.WebViewClient

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()
        webView = findViewById(R.id.wearwebview)
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.settings.cacheMode = LOAD_NO_CACHE

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return run {
                    if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:///")) {
                        view.loadUrl(url)
                    }
                    true
                }
            }
        }

        val myWebUrl = "file:///android_asset/sample/index.html"
        webView.loadUrl(myWebUrl)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        AlertDialog.Builder(this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Exit!")
                                .setMessage("Are you sure you want to close?")
                                .setPositiveButton("Yes") { _, _: Int -> finish() }
                                .setNegativeButton("No", null)
                                .show()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        private lateinit var webView: WearWebView
    }
}


