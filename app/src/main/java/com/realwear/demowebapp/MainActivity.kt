package com.realwear.demowebapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.*
import android.webkit.WebSettings.LOAD_NO_CACHE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "WeeverApp"
        private const val FILE_REQUEST = 12345
    }

    var webView: WebView? = null
    //callback for the file chooser override
    var mFilePathCallbackArray: ValueCallback<Array<Uri>>? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //permissions request for camera access
        val permissionsRequested = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissionsRequested, FILE_REQUEST)

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

        //necessary addition to add the support for the file chooser
        webView?.webChromeClient = object : WebChromeClient() {
            // Need to accept permissions to use the camera
            override fun onPermissionRequest(request: PermissionRequest) {
                //request.grant(request.resources)
                runOnUiThread {
                    request.grant(request.resources)
                }
            }

            //default behavior not support in webviews at this time, must create your own
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                mFilePathCallbackArray = filePathCallback

                //Forcing Camera to open, more complicated implementations are possible
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                getContent.launch(intent)

                return true
            }
        }

        //val myWebUrl = "https://realwear.rw.dev.weeverops.com/login"
        val myWebUrl = "https://pixochi.github.io/native-camera-in-mobile-browsers/"
        webView?.loadUrl(myWebUrl)
    }

    //logic for granting permissions, in this case specifically for the camera access
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            FILE_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    finish()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    //logic to retrieve the result from the camera activity and pass it back to the javascript call
    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val imageUri: Uri = data?.data as Uri

            if(imageUri.toString() == "")
                mFilePathCallbackArray?.onReceiveValue(null)
            mFilePathCallbackArray?.onReceiveValue(Array(1){ imageUri})
        }
        else{
            mFilePathCallbackArray?.onReceiveValue(null)
        }
    }
}


