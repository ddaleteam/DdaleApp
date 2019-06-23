package com.example.ddale;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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

/**
 * <b>Classe ARActivity</b>
 * Cette classe représente l'activité de réalité augmentée de notre application
 * Elle permet de générer la vue AR grâce au moteur EasyAR
 *
 * @author ddaleteam
 * @version 1.0
 */
public class ARActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * <b>glView</b> la vue spéciale AR
     */
    private GLView glView;
    /**
     * <b>description</b> le champ texte qui contient les informations sur l'oeuvre à afficher
     */
    private TextView description;
    /**
     * <b>oeuvre</b> l'oeuvre récupérée auprès de l'API
     */
    private Oeuvre oeuvre;
    /**
     * <b>idOeuvre</b> l'identifiant associé à l'oeuvre récupérée
     */
    private int idOeuvre;
    /**
     * <b>indiceCalqueActif</b> l'identifiant du calque actif
     * Si l'identifiant vaut -1, le calque correspondant dispose d'une image vide
     *      (calque associé aux informations générales)
     */
    private int indiceCalqueActif;
    /**
     * <b>nbCalques</b> le nombre de calques associés à l'oeuvre
     */
    private int nbCalques;
    /**
     * <b>btnImgAudio</b> le bouton de lecture/mise en pause du fichier audio
     * Ce bouton est caché par défaut et ne s'affiche que si le calque dispose d'un fichier audio
     */
    private ImageButton btnImgAudio;
    /**
     * <b>audio</b> l'objet permettant de lire le fichier audio
     */
    private MediaPlayer audio;
    /**
     * <b>downloadManager</b> l'objet permettant de télécharger localmement lee fichier audio
     *      depuis Internet
     */
    private DownloadManager downloadManager;
    /**
     * <b>chemin</b> le chemin local de stockage du fichier audio
     */
    private Uri chemin;
    /**
     * <b>onComplete</b> objet qui permet de gérer la réponse de l'objet downloadManager
     */
    private BroadcastReceiver onComplete;

    private String CAT = "AR";



    /* -------------------- Début de la région du cycle de vie de l'activité -------------------- */
    /**
     * Fonction onCreate appelée lors de le création de l'activité
     * @param savedInstanceState données à récupérer si l'activité est réinitialisée après
     *          avoir planté
     * Lie l'activité à son layout et récupère les éléments avec lesquels on peut intéragir
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        // Récupération de l'identifiant de l'oeuvre envoyé par l'activité précédente
        idOeuvre = getIntent().getIntExtra("idOeuvre", 1);
        Log.i(CAT, "onCreate: " + idOeuvre);

        // Désactive la mise en veille automatique de l'écran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* Lancement du moteur EasyAR */
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

        // Initialisation du gestionnaire de téléchargement
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        /* Mise en place du gestionnaire de la réponse du gestionnaire de téléchargement
            Lorsque le téléchargement du fichier audio est terminé, on initialise l'objet permettant
            de lire ce fichier, et on affiche le bouton de lecture
         */
        onComplete= new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                audio = MediaPlayer.create(ARActivity.this, chemin);
                btnImgAudio.setVisibility(View.VISIBLE);
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        /* Initialisation des boutons */
        Button btnSuivant = findViewById(R.id.btnSuivant);
        Button btnPrecedent = findViewById(R.id.btnPrecedent);
        Button btnInfo = findViewById(R.id.btnInfo);
        btnImgAudio = findViewById(R.id.imgBtnAudio);
        /* Mise en place d'écouteurs d'évènements sur ces boutons */
        btnImgAudio.setOnClickListener(this);
        btnSuivant.setOnClickListener(this);
        btnPrecedent.setOnClickListener(this);
        btnInfo.setOnClickListener(this);

        /* Initialisation du champ texte d'affichage des informations sur l'oeuvre */
        description = findViewById(R.id.description);

        /* Création de la vue spéciale AR */
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

    /**
     * Fonction onStart appelée lors du démarrage de l'activité
     * Récupère l'oeuvre d'identifiant idOeuvre
     */
    @Override
    protected void onStart() {
        super.onStart();
        recupererOeuvre(idOeuvre);
    }

    /** Fonction onResume appelée après la création de l'activité et à chaque retour sur l'activité
     *      courante
     * Permet de reprendre la vue spéciale AR
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) { glView.onResume(); }
    }

    /** Fonction onPause appelée lors de la mise en pause de l'activité courante
     *      (changemnt d'activité)
     * Permet de mettre en pause la vue spéciale AR
     */
    @Override
    protected void onPause() {
        if (glView != null) { glView.onPause(); }
        super.onPause();
    }

    /** Fonction onDestroy appelée lors de la destruction de l'activité
     * Permet de libérer l'espace mémoire utilisé par l'objet audio et de supprimer l'écoute sur la
     *      réponse du gestionnaire d'évènements
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
        if (audio != null) {
            audio.release();
            audio = null;
        }
    }
    /* --------------------- Fin de la région du cycle de vie de l'activité --------------------- */



    /* --------------------- Début de la région de gestion des permissions ---------------------- */
    private interface PermissionCallback {
        void onSuccess();
        void onFailure();
    }

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
    /* ---------------------- Fin de la région de gestion des permissions ----------------------- */



    /* --------------------------------- Début de la région API --------------------------------- */
    /**
     * Fonction permettant de récupérer l'oeuvre auprès de l'API
     * @param id l'identifiant de l'oeuvre à récupérer
     */
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
                    builder.setMessage("Titre de l'oeuvre : " + oeuvre.getTitre()+"\nPar " +
                            oeuvre.getAuteur());
                    AlertDialog alert1 = builder.create();
                    alert1.show();
                    Log.i(CAT, "onResponse: " + oeuvre.getCalques());
                    indiceCalqueActif = -1;
                    nbCalques = oeuvre.getCalques().size() -1;
                    glView.notifier("https://ddale.rezoleo.fr/" +
                            oeuvre.getUrlImageCible());
                    String descriptionOeuvre = oeuvre.getTitre() + ", " + oeuvre.getAnnee() +"\n" +
                            oeuvre.getTechnique()
                            +  "\n" +  oeuvre.getAuteur() + "\n" + oeuvre.getHauteur() + " cm × " +
                            oeuvre.getLargeur() + " cm";
                    description.setText(descriptionOeuvre);
                    glView.afficherVide();
                    if(!oeuvre.getUrlAudio().isEmpty()){
                        chemin = telechargeAudio("https://ddale.rezoleo.fr/" +
                                oeuvre.getUrlAudio());
                        Log.i(CAT, "onResponse: " + chemin);
                        audio = MediaPlayer.create(ARActivity.this,chemin);
                        btnImgAudio.setVisibility(View.VISIBLE);
                    }

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
    /* ---------------------------------- Fin de la région API ---------------------------------- */



    /* --------------------------- Début de la région gestion du clic --------------------------- */
    /**
     * Gestion d'un clic sur un élément interactif
     * @param v la vue associée à l'élément cliqué
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /* Si on clique sur le bouton "Informations générales", on affiche/cache la description
                associée le cas échéant
             */
            case R.id.btnInfo:
                if(description.getVisibility() == View.GONE)
                    description.setVisibility(View.VISIBLE);
                else
                    description.setVisibility(View.GONE);
                break;
            /* Si on clique sur le bouton "Précédent", on revient sur le calque précédent et on
                l'affiche (changement cyclique)
            */
            case R.id.btnPrecedent:
                indiceCalqueActif = (indiceCalqueActif == -1) ? nbCalques : indiceCalqueActif -1;
                changer(indiceCalqueActif);
                break;
            /* Si on clique sur le bouton "Suivant", on passe au calque suivant et on l'affiche
                (changement cyclique)
            */
            case R.id.btnSuivant:
                indiceCalqueActif = (indiceCalqueActif == nbCalques) ? -1 : indiceCalqueActif +1;
                changer(indiceCalqueActif);
                break;
            /* Si on clique sur le bouton de lecture du fichier audio, on joue ou met en pause le
                fichier audio le cas échéant, et on change l'image associée au bouton
            */
            case R.id.imgBtnAudio:
                if (audio.isPlaying()) {
                    btnImgAudio.setImageResource(android.R.drawable.ic_media_pause);
                    audio.pause();
                }
                else{
                    btnImgAudio.setImageResource(android.R.drawable.ic_media_play);
                    audio.start();
                }
             /* Si on clique n'importe où d'autre, on cache les informations de l'écran
            */
            default:
                description.setVisibility(View.GONE);
                break;
        }

    }

    /**
     * Fonction qui permet de changer de calque
     * @param index l'indice correspondant à l'identifiant du calque actuel
     */
    private void changer(int index){
        // Cas où on affiche les informations générales sur l'oeuvre (calque vide)
        if(index == -1){
            String descriptionOeuvre = oeuvre.getTitre() + ", " + oeuvre.getAnnee() +"\n" +
                    oeuvre.getTechnique() +  "\n" +  oeuvre.getAuteur() + "\n" +
                    oeuvre.getHauteur() + " cm × " + oeuvre.getLargeur() + " cm";
            description.setText(descriptionOeuvre);

            glView.afficherVide();

            if(!oeuvre.getUrlAudio().isEmpty()){
                chemin = telechargeAudio("https://ddale.rezoleo.fr/" + oeuvre.getUrlAudio());
                Log.i(CAT, "onResponse: " + chemin);
            }
        }

        else{
            description.setText(oeuvre.getCalques().get(index).getDescription());
            glView.changerCalque("https://ddale.rezoleo.fr/"
                    + oeuvre.getCalques().get(index).getUrlCalque());
            if(!oeuvre.getCalques().get(index).getUrlAudio().isEmpty()) {
                if (audio != null) {
                    audio.stop();
                }
                chemin = telechargeAudio( "https://ddale.rezoleo.fr/" +
                        oeuvre.getCalques().get(index).getUrlAudio());
                Log.i(CAT, "onResponse: " + chemin);
            }
            else
                btnImgAudio.setVisibility(View.GONE);
        }
    }

    /**
     * Fonction qui permet de télécharger le fichier audio depuis Internet
     * @param url l'URL pointant vers le fichier audio
     * @return l'URI de stockage local du fichier audio une fois téléchargé
     */
    private Uri telechargeAudio(String url){
        Log.i(CAT, "telechargeAudio: " + url);
        Uri uriServeur = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uriServeur);
        Uri uriLocale = Uri.parse(getCacheDir().getAbsolutePath() + "/audio/" +
                uriServeur.getLastPathSegment());
        Log.i(CAT, "telechargeAudio: " + uriLocale);
        request.setDestinationUri(uriLocale);
        downloadManager.enqueue(request);
        return uriLocale;

    }
    /* ---------------------------- Fin de la région gestion du clic ---------------------------- */
}
