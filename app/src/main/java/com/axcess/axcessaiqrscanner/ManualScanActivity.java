package com.axcess.axcessaiqrscanner;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

/**
 * A login screen that offers login via email/password.
 */
public class ManualScanActivity extends AppCompatActivity {


    private WebView mWebview;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mscan);

        mWebview  = findViewById(R.id.webview);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mWebview.setBackgroundColor(Color.TRANSPARENT);

        SharedPreferences shared = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();

        String passthisto = shared.getString("passthis", "");
        int j = shared.getInt("key", 0);


        mWebview.loadUrl("https://axcess.ai/manualverify.php?axcess="+passthisto);
        Log.d("wepass",passthisto );

    }




}

