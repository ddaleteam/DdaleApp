package com.example.ddale;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private FrameLayout mCameraView;
    private String CAT="QR";
    private TextView consigne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        consigne = findViewById(R.id.consigneQR);
        mCameraView = findViewById(R.id.camera_preview);
        mScannerView = new ZXingScannerView(getApplicationContext());
        mCameraView.addView(mScannerView);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);
        consigne.bringToFront();
        mScannerView.setResultHandler(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mScannerView.startCamera();

    }

    public void handleResult(Result result){
        Log.i(CAT, result.getText());
        Intent arIntent = new Intent(QRActivity.this, ARActivity.class);
        arIntent.putExtra("idOeuvre", Integer.parseInt(result.getText()) );
        startActivity(arIntent);
    }



}
