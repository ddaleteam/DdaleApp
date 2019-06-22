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

/**
 * <b>Classe QRActivity</b>
 * Cette classe représente l'activité de scan de QRCode
 *
 * @author ddaleteam
 * @version 1.0
 */
public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    /**
     * <b>mScannerView</b> l'objet qui permet de scanner un QRCode, la vue du scanner
     */
    private ZXingScannerView mScannerView;

    private String CAT = "QR";

    /**
     * Fonction onCreate appelée lors de le création de l'activité
     * @param savedInstanceState données à récupérer si l'activité est réinitialisée après
     *          avoir planté
     * Lie l'activité à son layout, récupère les éléments avec lesquels on peut intéragir et les
     *          initialise
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        // Récupération des éléments du layout
        TextView consigne = findViewById(R.id.consigneQR);
        consigne.bringToFront();
        FrameLayout mCameraView = findViewById(R.id.camera_preview);

        // Demande de permission d'accès à la caméra
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                1);

        // Initialisation de la vue du scanner et association au layout
        mScannerView = new ZXingScannerView(getApplicationContext());
        mCameraView.addView(mScannerView);

        // Mise en place du gestionnaire du résultat du scan
        mScannerView.setResultHandler(this);
    }

    /**
     * Fonction onStart appelée lors du démarrage de l'activité
     * Démarre la caméra du scan de QRCode
     */
    @Override
    protected void onStart() {
        super.onStart();
        mScannerView.startCamera();
    }

    /**
     * Gestionnaire du résultat du scan
     * Ouverture de l'activité ARActivity et transmission de l'identifiant de l'oeuvre scannée
     *         (le texte associé au QRCode)
     * @param result le résultat du scan
     */
    public void handleResult(Result result){
        Log.i(CAT, result.getText());
        Intent arIntent = new Intent(QRActivity.this, ARActivity.class);
        arIntent.putExtra("idOeuvre", Integer.parseInt(result.getText()) );
        startActivity(arIntent);
    }

}
