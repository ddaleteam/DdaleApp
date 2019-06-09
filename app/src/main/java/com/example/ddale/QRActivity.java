package com.example.ddale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private FrameLayout mCameraView;
    private String CAT="QR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);


        mCameraView = findViewById(R.id.camera_preview);

        mScannerView = new ZXingScannerView(getApplicationContext());

        mCameraView.addView(mScannerView);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    public void handleResult(Result result){
        Log.v(CAT, result.getText());
        Log.v(CAT,result.getBarcodeFormat().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

}
