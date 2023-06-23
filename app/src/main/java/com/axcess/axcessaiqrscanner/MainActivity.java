package com.axcess.axcessaiqrscanner;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
//import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Pattern;

//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.RequestBody;
//import com.muddzdev.styleabletoast.StyleableToast;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    Button button;
    TextView textView;
    SharedPreferences sharedpreferences;
    int autoSave;
    public String serverurl = "https://axcess.ai/qracess.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int sdkInt = android.os.Build.VERSION.SDK_INT;
        if (sdkInt > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll()
                    .build();

            StrictMode.setThreadPolicy(policy);


            sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
            int j = sharedpreferences.getInt("key", 0);
            if (j > 0) {
                Intent activity = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(activity);
            }


            button = (Button) findViewById(R.id.btn);
            textView = (TextView) findViewById(R.id.setinternet);


            final EditText et = (EditText) findViewById(R.id.et);


            ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    //we have WIFI

                    button.setEnabled(true);
                    textView.setText("");
                }
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                    button.setEnabled(true);
                    textView.setText("");
                }
            } else {
                //we have no connection :(
                Toast.makeText(getApplicationContext(), "Check Internet & Restart App", Toast.LENGTH_LONG).show();
                button.setEnabled(false);
                textView.setText("No Internet Connection");
            }

            if (checkPermission()) {
                //main logic or main code

                // . write your main code to execute, It will execute if the permission is already given.

            } else {
                requestPermission();
            }

/*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String responseBody;
                OkHttpClient client = new OkHttpClient();

                final String codein = et.getText().toString();
                String finalurl = serverurl+ "?query="+codein;

                Log.i("My app", "testing button " + finalurl);
                // String contentType = fileSource.toURL().openConnection().getContentType();

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("rawcard","mydard" )
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()

                        .url(finalurl)//your webservice url
                        .post(requestBody)
                        .build();
                try {
                    //String responseBody;
                    okhttp3.Response response = client.newCall(request).execute();
                    // Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        Log.i("response",""+response.message());

                    }
                    String resp = response.message();
                    responseBody =  response.body().string();
                    Log.i("response2",responseBody);

                    //responseBody = "asts";



                    Log.i("resp_MSG",resp);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("resp_TAG", "I got an error", e);
                }






            }

        });

*/

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String codein = et.getText().toString();
                    String finalurl = serverurl + "?query=" + codein;

                    Log.i("My app", "testing button " + finalurl);

                    // Toast.makeText(getApplicationContext(),codein,Toast.LENGTH_SHORT).show();

                    final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, finalurl,


                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("server says", response);

                                    String returnaction = response.toString();
                                /*
                                getout = getout.trim();
                                String[] pieces = getout.split(Pattern.quote("~"));
                                String returnaction = pieces[0];
                                String thisdeck = pieces[1];
                              */
                                    returnaction = returnaction.trim();
                                    String thisdeck = "Online";


                                    switch (returnaction) {
                                        case "1":


                                            autoSave = 1;
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putInt("key", autoSave);
                                            editor.putString("passthis", codein);
                                            editor.putString("thisdeck", thisdeck);
                                            editor.apply();


                                            Intent intent = new Intent(MainActivity.this, OptionActivity.class);
                                            //intent.putExtra("passthis",codein);
                                            startActivity(intent);
                                            Log.i("right response", response);
                                            break;
                                        case "2":
                                            Log.i("wrong response", response);
                                            // StyleableToast.makeText(getApplicationContext(), "Hello World!", R.style.exampleToast).show();
                                            Toast.makeText(getApplicationContext(), "Wrong Access Code..", Toast.LENGTH_SHORT).show();
                                            break;
                                    }


                                    requestQueue.stop();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            textView.setText("Something faulty...");
                            error.printStackTrace();
                            requestQueue.stop();
                        }
                    });

                    requestQueue.add(stringRequest);
                /*requestQueue.se(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
                }
            });


        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }




    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



}





