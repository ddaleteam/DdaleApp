package com.example.ddale;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ddale.API.APIClient;
import com.example.ddale.API.APIInterface;
import com.example.ddale.AR.GLView;
import com.example.ddale.modele.Oeuvre;

import cn.easyar.Engine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ARActivity extends AppCompatActivity implements View.OnClickListener {

    /* ARActivity est une activité qui gère la vue AR et les permissions de la caméra */
    /* Son layout ne contient que la Vue camera + AR */

    private GLView glView;
    private String CAT = "AR";
    private TextView description;
    private Oeuvre oeuvre;
    private int idOeuvre;

    //Start of region Cycle de Vie
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idOeuvre = getIntent().getIntExtra("idOeuvre", 1);
        Log.i(CAT, "onCreate: " + idOeuvre);


        setContentView(R.layout.activity_ar);

        /* Désactive la mise en veille automatique de l'écran */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* Lancement du moteur easyAR */
        /*
         * Steps to create the key:
         *  1. login www.easyar.com
         *  2. create app with
         *      Name: Appname
         *      Package Name: com.example.______
         *  3. find the created item in the list and show key
         *  4. set key string bellow
         */
        String key = "vl5HjBWiRl0uX6K50xMlmAjUVNJvviGO66U94rz3ou61TV2mq9mWfbYMVp8fCUHWB4c8qR08Wv" +
                "IoOHWoe0cOVqBpGqwEoHnwCeUOOw0ARB1oAB4ioHC4mLvwwSHOAdRpuWrov5l8QYuiqtHGT4we36BGPf" +
                "2Puj0uZfDdfd9OIOEKgvHzzBn1aBwoAnABTKkNiklKN2RC";
        if (!Engine.initialize(this, key)) {
            Log.e(CAT, "Initialization Failed.");
        }



        Button btnSuivant = findViewById(R.id.btnSuivant);
        Button btnPrecedent = findViewById(R.id.btnPrecedent);
        Button btnInfo = findViewById(R.id.btnInfo);
        btnSuivant.setOnClickListener(this);
        btnPrecedent.setOnClickListener(this);
        btnInfo.setOnClickListener(this);

        description = findViewById(R.id.description);

        /*Création de la Vue spéciale AR */
        glView = new GLView(ARActivity.this);
        glView.setOnClickListener(ARActivity.this);
        /* Demande des permissions camera */
        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {
                ViewGroup group = findViewById(R.id.preview);
                group.addView(glView,new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onFailure() {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recupererOeuvre(idOeuvre);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) { glView.onResume(); }
    }

    @Override
    protected void onPause() {
        if (glView != null) { glView.onPause(); }
        super.onPause();
    }
    //end of region Cycle de Vie


    //Start of region interface
    private interface PermissionCallback {
        void onSuccess();
        void onFailure();
    }
    //end of region interface

    //Start of region fonctions utilitaires
    private SparseArray<PermissionCallback> permissionCallbacks = new SparseArray<>();
    private int permissionRequestCodeSerial = 0;

    @TargetApi(23)
    private void requestCameraPermission(PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionCallbacks.get(requestCode) != null) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    // end of region fonctions utilitaires

    //Debut de region API
    public void recupererOeuvre(int id){
        APIInterface api = APIClient.createService(APIInterface.class);
        Call<Oeuvre> call = api.appelAPIOeuvre(id);
        call.enqueue(new Callback<Oeuvre>() {
            @Override
            public void onResponse(Call<Oeuvre> call, Response<Oeuvre> response) {
                if (response.isSuccessful()) {
                    Log.i("Oeuvre : " +CAT,response.body().toString());
                    ARActivity.this.oeuvre = new Oeuvre(response.body());
                    AlertDialog.Builder builder = new AlertDialog.Builder(ARActivity.this);
                    builder.setTitle("Scan Result");
                    builder.setMessage("Titre de l'oeuvre : " +oeuvre.getTitre()+"\nEt l'incroyable auteur : " +oeuvre.getAuteur());
                    AlertDialog alert1 = builder.create();
                    alert1.show();

                    glView.notifier();

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
    //Fin de region API

    //Debut region onClick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInfo:
                if(description.getVisibility() == View.INVISIBLE)
                    description.setVisibility(View.VISIBLE);
                else
                    description.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnPrecedent:
                description.setText("description précédente");
                glView.precedent();
                break;
            case R.id.btnSuivant:
                description.setText("description suivante");
                glView.suivant();
                break;
            default:
                description.setVisibility(View.INVISIBLE);
                break;
        }

    }
    //Fin region onClick


}
