  package com.axcess.axcessaiqrscanner;

  import android.content.Intent;
  import android.content.SharedPreferences;
  import android.os.Bundle;
  import android.support.v7.app.AppCompatActivity;
  import android.view.View;
  import android.widget.Button;
  import android.widget.TextView;

  public class OptionActivity extends AppCompatActivity {
String passthisto;
      Button dologout;
      TextView thisdeck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_layout);

        final SharedPreferences shared = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = shared.edit();
        dologout = (Button)findViewById(R.id.llog);
        thisdeck = (TextView)findViewById(R.id.deck);


        passthisto = shared.getString("passthis", "");
        String getdeck = shared.getString("thisdeck", "");

        int j = shared.getInt("key", 0);

        thisdeck.setText(getdeck);


        findViewById(R.id.ll_mscannig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OptionActivity.this, ManualScanActivity.class);
                intent.putExtra("passthis",passthisto);
                startActivity(intent);


            }
        });

        dologout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shared.edit().clear().commit();
                Intent intent = new Intent(OptionActivity.this, MainActivity.class);
                // intent.putExtra("passthis",dopass);
                startActivity(intent);


            }
        });


        findViewById(R.id.ll_qrscannig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OptionActivity.this, doScan.class);
                intent.putExtra("passthis",passthisto);
                startActivity(intent);

            }
        });


        /*
        findViewById(R.id.ll_smsscannig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OptionActivity.this, OcrCaptureActivity.class);
                intent.putExtra("passthis",getIntent().getStringExtra("passthis"));
                startActivity(intent);

            }
        });

         */


    }
}
