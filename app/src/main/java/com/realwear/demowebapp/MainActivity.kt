package com.realwear.demowebapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebSettings.LOAD_NO_CACHE
import android.webkit.WebView
import android.webkit.WebViewClient

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "WeeverApp"
    }

    var webView: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()
        webView = findViewById(R.id.wearwebview)
        webView?.settings?.let {
            it.domStorageEnabled = true
            it.allowFileAccess = true
            it.cacheMode = LOAD_NO_CACHE
            it.javaScriptEnabled = true
        }
        //static method to allow for remote debugging
        WebView.setWebContentsDebuggingEnabled(true)

        webView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return run {
                    if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:///")) {
                        view.loadUrl(url)
                    }
                    true
                }
            }
        }

        val myWebUrl = "https://realwear.rw.dev.weeverops.com/login"
        webView?.loadUrl(myWebUrl)
    }
}


