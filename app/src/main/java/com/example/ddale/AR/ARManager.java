//================================================================================================================================
//
//  Copyright (c) 2015-2018 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
//  EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
//  and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package com.example.ddale.AR;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import com.example.ddale.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.easyar.CameraCalibration;
import cn.easyar.CameraDevice;
import cn.easyar.CameraDeviceFocusMode;
import cn.easyar.CameraDeviceType;
import cn.easyar.CameraFrameStreamer;
import cn.easyar.Frame;
import cn.easyar.FunctorOfVoidFromPointerOfTargetAndBool;
import cn.easyar.ImageTarget;
import cn.easyar.ImageTracker;
import cn.easyar.Renderer;
import cn.easyar.StorageType;
import cn.easyar.Target;
import cn.easyar.TargetInstance;
import cn.easyar.TargetStatus;
import cn.easyar.Vec2I;
import cn.easyar.Vec4I;

class ARManager
{
    private CameraDevice camera;
    private CameraFrameStreamer streamer;
    private ArrayList<ImageTracker> trackers;
    private Renderer cameraRenderer;
    private boolean viewportChanged = false;
    private Vec2I tailleVue = new Vec2I(0, 0);
    private int rotation = 0;
    private Vec4I viewport = new Vec4I(0, 0, 1280, 720);

    private RectangleRenderer rectangleRenderer;
    private Context contexte;
    private String CAT = "ARManager";
    private Bitmap imageVide;

    /**
     * Constructeur de ARManager
     * initialise l'image vide à afficher par défaut
     * @param contexte : contexte pour l'accès aux ressources de l'application
     */
    ARManager(Context contexte)
    {
        trackers = new ArrayList<>();
        this.contexte = contexte;
        imageVide = BitmapFactory.decodeResource(contexte.getResources(), R.drawable.vide);
    }

    /* -------------------DEBUT DU CODE RECUPERE DEPUIS L'EXAMPLE EASYAR------------------------------*/

    //Début de région chargerCibles

    /**
     * Fonction qui charge dans le tracker indiqué l'image cible
     *
     * @param tracker tracker
     * @param path    le chemin absolu de l'image cible dans le téléphone
     */
    private void chargerDepuisImageLocale(ImageTracker tracker, String path) {
        ImageTarget cible = new ImageTarget();
        String jstr = "{\n"
                + "  \"images\" :\n"
                + "  [\n"
                + "    {\n"
                + "      \"image\" : \"" + path + "\",\n"
                + "      \"name\" : \"" + path.substring(0, path.indexOf(".")) + "\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        cible.setup(jstr, StorageType.Absolute | StorageType.Json, "");
        tracker.loadTarget(cible, new FunctorOfVoidFromPointerOfTargetAndBool() {
            @Override
            public void invoke(Target cible, boolean status) {
                Log.i(CAT, String.format("load cible (%b): %s (%d)",
                        status, cible.name(), cible.runtimeID()));
            }
        });
    }
    //Fin de Région chargerCibles

    //Début de Région Cycle de Vie

    /**
     * Fonction d'initialisation du Manager
     * initialise la camera, sa taille et le streamer (qui transmet les images)
     * @return un boolean qui qualifie l'état de l'initialisation
     */
    boolean initialiser() {
        camera = new CameraDevice();
        streamer = new CameraFrameStreamer();
        streamer.attachCamera(camera);
        boolean status;
        status = camera.open(CameraDeviceType.Default);
        camera.setSize(new Vec2I(1280, 720));
        return status;
    }

    /**
     * Démarre les différents objets nécéssaires (camera, transmetteur, et trackers)
     */
    void demarrer() {
        camera.start();
        streamer.start();
        camera.setFocusMode(CameraDeviceFocusMode.Continousauto);
        for (ImageTracker tracker : trackers) {
            tracker.start();
        }
    }

    /**
     * Arrete les trackers, la camera et son transmetteur
     */
    void arreter() {
        for (ImageTracker tracker : trackers) {
            tracker.stop();
        }
        streamer.stop();
        camera.stop();
    }

    /**
     * Une fois arreter, les objets peuvent êtres éliminés pour libérer l'espace mémoire
     */
    void eliminer() {
        for (ImageTracker tracker : trackers) {
            tracker.dispose();
        }
        trackers.clear();

        rectangleRenderer = null;

        if (cameraRenderer != null) {
            cameraRenderer.dispose();
            cameraRenderer = null;
        }
        if (streamer != null) {
            streamer.dispose();
            streamer = null;
        }
        if (camera != null) {
            camera.dispose();
            camera = null;
        }
    }
    //Fin de région Cycle de Vie

    //Début de région Actions du Manager

    /**
     * Initialisation des renderer (video et rectangle)
     * Démarre les moteurs video (pour la camera) et openGl
     */
    void initialiserGL()
    {
        if (cameraRenderer != null) {
            cameraRenderer.dispose();
        }
        cameraRenderer = new Renderer();

        rectangleRenderer = new RectangleRenderer();
        rectangleRenderer.init(imageVide);
    }

    /**
     * Mise à jour des dimensions
     * @param largeur nouvelle largeur
     * @param hauteur nouvelle hauteur
     */
    void redimensionnerGL(int largeur, int hauteur) {
        tailleVue = new Vec2I(largeur, hauteur);
        viewportChanged = true;
    }

    /**
     * Mise a jour du viewPort
     * Gestion de la taille de l'affichage
     */
    private void majViewport()
    {
        CameraCalibration calib = camera != null ? camera.cameraCalibration() : null;
        int rotation = calib != null ? calib.rotation() : 0;
        if (rotation != this.rotation) {
            this.rotation = rotation;
            viewportChanged = true;
        }
        if (viewportChanged) {
            Vec2I size = new Vec2I(1, 1);
            if ((camera != null) && camera.isOpened()) {
                size = camera.size();
            }
            if (rotation == 90 || rotation == 270) {
                size = new Vec2I(size.data[1], size.data[0]);
            }
            float scaleRatio = Math.max((float) tailleVue.data[0] / (float) size.data[0],
                    (float) tailleVue.data[1] / (float) size.data[1]);
            Vec2I viewport_size = new Vec2I(Math.round(size.data[0] * scaleRatio), 
                                             Math.round(size.data[1] * scaleRatio));
            viewport = new Vec4I((tailleVue.data[0] - viewport_size.data[0]) / 2,
                                (tailleVue.data[1] - viewport_size.data[1]) / 2,
                                viewport_size.data[0],
                                viewport_size.data[1]);

            if ((camera != null) && camera.isOpened())
                viewportChanged = false;
        }
    }

    /**
     * Lancement du calcul des objets à afficher
     * Nettoie les éléments affichés,puis
     * Vérifie le bon fonctionnement des objets et les dimensions de l'affichage
     */
    void render() {
        GLES20.glClearColor(1.f, 1.f, 1.f, 1.f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (cameraRenderer != null) {
            Vec4I default_viewport = new Vec4I(0, 0, tailleVue.data[0], tailleVue.data[1]);
            GLES20.glViewport(default_viewport.data[0], default_viewport.data[1],
                    default_viewport.data[2], default_viewport.data[3]);
            if (cameraRenderer.renderErrorMessage(default_viewport)) {
                return;
            }
        }

        if (streamer == null) { return; }
        Frame frame = streamer.peek();
        try {
            majViewport();
            GLES20.glViewport(viewport.data[0], viewport.data[1],
                    viewport.data[2], viewport.data[3]);

            if (cameraRenderer != null) {
                cameraRenderer.render(frame, viewport);
            }

            for (TargetInstance instanceCible : frame.targetInstances()) {
                int status = instanceCible.status();
                if (status == TargetStatus.Tracked) {
                    Target cible = instanceCible.target();
                    ImageTarget imageCible =
                            cible instanceof ImageTarget ? (ImageTarget) (cible) : null;
                    if (imageCible == null) {
                        continue;
                    }
                    if (rectangleRenderer != null) {
                        rectangleRenderer.render(camera.projectionGL(0.2f, 500.f),
                                instanceCible.poseGL(), imageCible.size());
                    }

                }
            }
        } finally {
            frame.dispose();
        }
    }
    /* ---------------------FIN DU CODE RECUPERE DEPUIS L'EXAMPLE EASYAR------------------------------*/

    /**
     * Change l'image affichée pour la nouvelle image voulue
     * @param cheminImage Url de l'image à télécharger
     * les calques sont gérés en cache par Picasso
     */
    void changerCalque(String cheminImage) {
        Log.i(CAT, "changerCalque: " + cheminImage);
        Bitmap imageCalque;
        try {
            imageCalque = Picasso.get().load(cheminImage).get();
            rectangleRenderer.changerCalque(imageCalque);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Notifie l'ARManager
     * Récupère l'image cible depuis son url et la sauvegarde localement
     * Puis charge l'image dans le tracker
     * @param cheminCible url de l'image de la cible
     */
    void notifier(String cheminCible) {
        Log.i(CAT, "notifier: " + cheminCible);

        ImageTracker tracker = new ImageTracker();
        tracker.attachStreamer(streamer);

        Bitmap bitmap = null;
        try {
            bitmap = Picasso.get().load(cheminCible).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = sauvegarderFichierEnLocal(bitmap,
                cheminCible.substring(cheminCible.lastIndexOf("/")+1));
        Log.i("ALLER", "notifier: " + path);

        chargerDepuisImageLocale(tracker, path);

        trackers.add(tracker);

        tracker.start();
    }
    //Fin de region Actions du Manager

    /**
     * Sauvegarde le bitmap donnée dans le cache local
     * @param bitmapImage bitmap à sauvegarder
     * @param nomImage nom donné à l'image en local
     * @return le chemin absolu de l'image sauvegardée
     */
    private String sauvegarderFichierEnLocal(Bitmap bitmapImage, String nomImage) {
        File directory = contexte.getCacheDir();
        File chemin = new File(directory, nomImage);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(chemin);
            // Compression du bitmap en png
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" + nomImage;
    }

    /**
     * Affiche une image vide dans le rectangle rendu
     */
    void afficherVide() {
        rectangleRenderer.changerCalque(imageVide);
    }
}
