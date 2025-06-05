package com.dabai.qrtools

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "关于本程序"
        val webview = WebView(this)
        setContentView(webview)
        webview.loadUrl("https://github.com/dabai2017/QRTools/blob/master/README.md")


    }
}
