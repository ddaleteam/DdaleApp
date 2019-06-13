package com.example.ddale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ddale.API.APIClient;
import com.example.ddale.API.APIInterface;
import com.example.ddale.modele.Oeuvre;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private FrameLayout mCameraView;
    private String CAT="QR";
    private TextView consigne;
    private Oeuvre oeuvre;

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
        Log.v(CAT, result.getText());
        Log.v(CAT,result.getBarcodeFormat().toString());
        recupererOeuvre(Integer.parseInt(result.getText()));

    }

    public void recupererOeuvre(int id){
        APIInterface api = APIClient.createService(APIInterface.class);
        Call<Oeuvre> call = api.appelAPIOeuvre(id);
        call.enqueue(new Callback<Oeuvre>() {
            @Override
            public void onResponse(Call<Oeuvre> call, Response<Oeuvre> response) {
                if (response.isSuccessful()) {
                    Log.i("Oeuvre : " +CAT,response.body().toString());
                    QRActivity.this.oeuvre = new Oeuvre(response.body());
                    AlertDialog.Builder builder = new AlertDialog.Builder(QRActivity.this);
                    builder.setTitle("Scan Result");
                    builder.setMessage("Titre de l'oeuvre : " +oeuvre.getTitre()+"\nEt l'incroyable auteur : " +oeuvre.getAuteur());
                    AlertDialog alert1 = builder.create();
                    alert1.show();
                } else {
                    Log.i(CAT,"Erreur lors de l'appel à l'API pour récupérer l'oeuvre");
                }
            }

            @Override
            public void onFailure(Call<Oeuvre> call, Throwable t) {
                Log.i(CAT, "Erreur lors de l'appel à l'API pour récupérer l'oeuvre : timeout");
            }
        });
        Log.i(CAT, "fin");


    }

}
