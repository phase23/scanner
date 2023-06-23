  package com.axcess.axcessaiqrscanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

  public class scan_success extends AppCompatActivity {

      TextView textView;
      Button button;
      String responseBody;
String passthisto;
      SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_success);
        textView = (TextView) findViewById(R.id.msgscanned);


        final SharedPreferences shared = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();



         passthisto = shared.getString("passthis", "");
        int j = shared.getInt("key", 0);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final String dopass = getIntent().getExtras().getString("passthis");
        String scanresult = getIntent().getExtras().getString("message");

        String[] separated = scanresult.split(Pattern.quote("|"));

        System.out.println("number arr: " + Arrays.toString(separated));
        String rekstat = separated[0];
        System.out.println("out number " + rekstat);
        int myNum = 0;

        try {
            myNum = Integer.parseInt(rekstat);
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        System.out.println("number " + myNum);

        if (myNum == 2) {
            String collect = separated[1];
            String[] dishout = collect.split("~");
            String event = dishout[0];
            String ticket = dishout[1];
            String releasename = dishout[2];
            String scannedin = dishout[3];


            String putall = "" + event + "<br>" + ticket
                    + "<br>"
                    + "<br>" + "<b>" + releasename + "</b>"
                    + "<br>"
                    + "<br>"
                    + scannedin;


            textView.setText(Html.fromHtml(putall));


        }//emd if paper ticket or sms ticket


        if (myNum == 1) {

            // String scanresult = "1|c1d3b009ce2d67f0ac94d1cae5ef6448~James Dunn~General Tickets~Test Event/91c448b7f782e794f9176bd4d198f61d~kelly Bog~boxoffice general~Test Event";

            try {


            String collect = separated[1];
            String[] dishout = collect.split(Pattern.quote("/"));
            System.out.println("number tickets: " + Arrays.toString(dishout));

            createLayoutDynamically(scanresult);

            } catch(ArrayIndexOutOfBoundsException e) {

                textView.setText(Html.fromHtml("No tickets for event"));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                textView.setTypeface(null, Typeface.BOLD);



            }





        }//emd if axcess card

        LinearLayout layout = (LinearLayout) findViewById(R.id.scnf);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.TOP;

        params.setMargins(10, 5, 0, 30);

        int btnid = 250;
        Button rescan = new Button(this);
        rescan.setId(btnid);
        //btn.setTag( token);
        final int id_ = rescan.getId();
        rescan.setText("SCAN AGAIN");
        rescan.setLayoutParams(params);
        rescan.setPadding(20, 5, 20, 5 );
        layout.addView(rescan);


        /** Back to options button **/

        int btnio = 252;
        Button btnoptions = new Button(this);
        btnoptions.setId(btnio);
        //btn.setTag( token);
        final int idl = btnoptions.getId();
        btnoptions.setText("GO BACK TO OPTIONS");
        btnoptions.setLayoutParams(params);
        btnoptions.setPadding(20, 5, 20, 5 );
        layout.addView(btnoptions);



        int btnidl = 251;
        Button logout = new Button(this);
        logout.setId(btnidl);
        //btn.setTag( token);
        final int idl_ = logout.getId();
        logout.setText("LOG OUT ");
        logout.setLayoutParams(params);
        logout.setPadding(20, 5, 20, 5 );
        layout.addView(logout);






        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (getIntent().getBooleanExtra("isQr",false)){
                    intent = new Intent(scan_success.this, doScan.class);
                }else {
                    intent = new Intent(scan_success.this, OcrCaptureActivity.class);
                }
                intent.putExtra("passthis",passthisto);
                startActivity(intent);
                ((Activity) scan_success.this).finish();

            }
        });


        btnoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(scan_success.this, OptionActivity.class);
               // intent.putExtra("passthis",dopass);
                startActivity(intent);


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shared.edit().clear().commit();
                Intent intent = new Intent(scan_success.this, MainActivity.class);
                // intent.putExtra("passthis",dopass);
                startActivity(intent);


            }
        });


    }



      public String scansingle( String releaseid ) {

          String[] separated = releaseid.split(Pattern.quote("-"));
          String releaseunq = separated[0];
          String rtoken = separated[1];

          String lrease = releaseunq + "-2";

          //String url = "https://aaxcess.com/getrfid.php?rfid="+uid + "&getdevice="+deviceId;;
          String url = "https://axcess.ai/?action=eventstub&t=eventticket&token="+rtoken+"&subticket="+ lrease;



          Log.i("number release",lrease);
          Log.i("number url",url);
          OkHttpClient client = new OkHttpClient();


          // String contentType = fileSource.toURL().openConnection().getContentType();

          RequestBody requestBody = new MultipartBody.Builder()
                  .setType(MultipartBody.FORM)
                  .addFormDataPart("rid",releaseid )
                  .build();
          Request request = new Request.Builder()

                  .url(url)//your webservice url
                  .post(requestBody)
                  .build();
          try {
              //String responseBody;
              okhttp3.Response response = client.newCall(request).execute();
              // Response response = client.newCall(request).execute();
              if (response.isSuccessful()){
                  Log.i("SUCC",""+response.message());

              }
              String resp = response.message();
              responseBody =  response.body().string();
              Log.i("respBody",responseBody);



              Log.i("MSG",resp);
          } catch (IOException e) {
              e.printStackTrace();
          }

          return  responseBody;
      }


      public String scanconfirm( String releaseid ) {

          String[] separated = releaseid.split(Pattern.quote("-"));
          String releaseunq = separated[0];
          String rtoken = separated[1];

          //String url = "https://aaxcess.com/getrfid.php?rfid="+uid + "&getdevice="+deviceId;;
          String url = "https://axcess.ai/qrconfirm.php?scanid="+releaseunq;



          Log.i("number release",releaseid);
          Log.i("number url",url);
          OkHttpClient client = new OkHttpClient();


          // String contentType = fileSource.toURL().openConnection().getContentType();

          RequestBody requestBody = new MultipartBody.Builder()
                  .setType(MultipartBody.FORM)
                  .addFormDataPart("rid",releaseid )
                  .build();
          Request request = new Request.Builder()

                  .url(url)//your webservice url
                  .post(requestBody)
                  .build();
          try {
              //String responseBody;
              okhttp3.Response response = client.newCall(request).execute();
              // Response response = client.newCall(request).execute();
              if (response.isSuccessful()){
                  Log.i("SUCC",""+response.message());

              }
              String resp = response.message();
              responseBody =  response.body().string();
              Log.i("respBody",responseBody);



              Log.i("MSG",resp);
          } catch (IOException e) {
              e.printStackTrace();
          }

          return  responseBody;
      }



      private void createLayoutDynamically( String scantext) {
          LinearLayout layout = (LinearLayout) findViewById(R.id.scnf);
          layout.setOrientation(LinearLayout.VERTICAL);

          LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

          params.gravity = Gravity.TOP;

          params.setMargins(10, 5, 0, 30);

          //button.setLayoutParams(params);
          System.out.println("number scantxt : "+ scantext );
          String[] separated = scantext.split(Pattern.quote("|"));


          String collect = separated[1];
          String[] dishout = collect.split(Pattern.quote("/"));

          int makebtn = dishout.length ;
          String tline;
          String rid;
          String ticket;
          String releasename;
          String event;
          String token;
          System.out.println(makebtn + "number tickets: " + Arrays.toString(dishout));
          for (int i = 0; i < makebtn; i++) {
              //= "btn"+ i;

              tline = dishout[i] ;

              String[] sbtns = tline.split("~");
              rid = sbtns[0];
              releasename = sbtns[1];
              ticket = sbtns[2];
              event = sbtns[3];
              token = sbtns[4];

              String counttickets =  " " + event
                                          + "<br>"
                                          + makebtn + " tickets on card";


              System.out.println(makebtn + "number sttmg: " +  counttickets);

              textView.setText(Html.fromHtml(counttickets));
              textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
              textView.setTypeface(null, Typeface.BOLD);

              Button btn = new Button(this);
              btn.setId(i);
              btn.setTag(rid + "-" + token);
              final int id_ = btn.getId();
              btn.setText(" " + releasename +  " - " + ticket + " ");
              btn.setLayoutParams(params);
              btn.setPadding(5, 5, 5, 5 );
              btn.setBackgroundColor(Color.rgb(249, 249, 249));
              layout.addView(btn);
              btn = ((Button) findViewById(id_));


              btn.setOnClickListener(new View.OnClickListener() {

                  public void onClick(View view) {

                      final String tagname = (String)view.getTag();



                      AlertDialog.Builder builder = new AlertDialog.Builder(scan_success.this);

                      builder.setTitle("Confirm");

                      String responsethis = scansingle(tagname);

                      String[] separated = responsethis.split(Pattern.quote("|"));
                      String collect = separated[1];
                      String[] dishout = collect.split("~");
                      String event = dishout[0];
                      String ticket = dishout[1];
                      String releasename = dishout[2];
                      String scannedin = dishout[3];

                      //Html.fromHtml(putall)
                      builder.setMessage(Html.fromHtml("Confirm Entrant<br><br>" + releasename + "<br><br>"+ticket));

                      builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                          public void onClick(DialogInterface dialog, int which) {

                              // Do nothing, but close the dialog
                              dialog.dismiss();
                              System.out.println("numbers tag "+ tagname);
                              String releaseid = tagname;
                              Button btn = (Button)findViewById(id_);
                              btn.setBackgroundColor(Color.GREEN);
                              scanconfirm( releaseid );


                          }
                      });

                      builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                          @Override
                          public void onClick(DialogInterface dialog, int which) {

                              // Do nothing
                              dialog.dismiss();
                          }
                      });

                      AlertDialog alert = builder.create();
                      alert.show();

                  }

              });
          }


      }



  }
