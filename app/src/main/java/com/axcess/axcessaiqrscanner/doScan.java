package com.axcess.axcessaiqrscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class doScan extends AppCompatActivity implements ZXingScannerView.ResultHandler  {

    private static final String TAG = "1" ;
    private ZXingScannerView mScannerView;
    public String urlres;
    String passthisto;
    Button btnoptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_scan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnoptions = (Button)findViewById(R.id.options);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view4
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_view);
        preview.addView(mScannerView);

        SharedPreferences shared = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();

        passthisto = shared.getString("passthis", "");
        int j = shared.getInt("key", 0);

        btnoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(doScan.this, OptionActivity.class);
                // intent.putExtra("passthis",dopass);
                startActivity(intent);


            }
        });




    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results

        final String dopass = getIntent().getExtras().getString("passthis");
        String posturl = "https://axcess.ai/?action=eventstub&t=eventticket&token="+ passthisto +"&subticket=";
        urlres = rawResult.getText().toString();
        //urlres = URLEncoder.encode(urlres);
        posturl = posturl + urlres;
        //posturl =  URLEncoder.encode(posturl);
       // Toast.makeText(getApplicationContext(),posturl,Toast.LENGTH_SHORT).show();

        Log.d("url", posturl);


        final RequestQueue requestQueue = Volley.newRequestQueue(doScan.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, posturl,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("server says", response);

                        String getout = response.toString();
                        getout = getout.trim();

                        Intent intent = new Intent(doScan.this, scan_success.class);
                        intent.putExtra("message",getout);
                        intent.putExtra("passthis",passthisto);
                        intent.putExtra("isQr",true);
                        startActivity(intent);
                        finish();

                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(doScan.this);
                        builder.setMessage(getout)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        */
                        requestQueue.stop();

                        //Toast.makeText(getApplicationContext(),getout,Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                error.printStackTrace();
                requestQueue.stop();
            }
        });

        requestQueue.add(stringRequest);


        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}


