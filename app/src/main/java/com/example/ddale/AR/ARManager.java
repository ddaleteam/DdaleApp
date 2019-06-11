//================================================================================================================================
//
//  Copyright (c) 2015-2018 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
//  EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
//  and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package com.example.ddale.AR;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.example.ddale.R;

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
    private int idCalque;
    private String CAT = "ARManager";

    /**
     * Constructeur de ARManager
     * @param contexte : contexte contexte qui sera transmis aux renderer
     */
    ARManager(Context contexte)
    {
        trackers = new ArrayList<>();
        this.contexte = contexte;
        idCalque = R.drawable.fresk2048;
    }

    //demarrer region chargerCibles
    private void chargerDepuisImage(ImageTracker tracker, String path)
    {
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
        cible.setup(jstr, StorageType.Assets | StorageType.Json, "");
        tracker.loadTarget(cible, new FunctorOfVoidFromPointerOfTargetAndBool() {
            @Override
            public void invoke(Target cible, boolean status) {
                Log.i(CAT, String.format("load cible (%b): %s (%d)",
                        status, cible.name(), cible.runtimeID()));
            }
        });
    }

    private void chargerDepuisJson(ImageTracker tracker, String path, String ciblename)
    {
        ImageTarget cible = new ImageTarget();
        cible.setup(path, StorageType.Assets, ciblename);
        tracker.loadTarget(cible, new FunctorOfVoidFromPointerOfTargetAndBool() {
            @Override
            public void invoke(Target cible, boolean status) {
                Log.i(CAT, String.format("load cible (%b): %s (%d)",
                        status, cible.name(), cible.runtimeID()));
            }
        });
    }

    private void chargerTousJson(ImageTracker tracker, String path)
    {
        for (ImageTarget cible : ImageTarget.setupAll(path, StorageType.Assets)) {
            tracker.loadTarget(cible, new FunctorOfVoidFromPointerOfTargetAndBool() {
                @Override
                public void invoke(Target cible, boolean status) {
                    Log.i(CAT, String.format("load cible (%b): %s (%d)",
                            status, cible.name(), cible.runtimeID()));
                }
            });
        }
    }
    //end region chargerCibles

    //demarrer region cycle de Vie
    /**
     * fonction d'initialisation du Manager
     * initialise la camera, sa taille et le streamer (qui transmet les images)
     *
     * @return un boolean qui qualifie l'état de l'initialisation
     */
    boolean initialiser()
    {
        camera = new CameraDevice();
        streamer = new CameraFrameStreamer();
        streamer.attachCamera(camera);

        boolean status;
        status = camera.open(CameraDeviceType.Default);
        camera.setSize(new Vec2I(1280, 720));

        if (!status)
            return status;

        else{
            ImageTracker tracker = new ImageTracker();
            tracker.attachStreamer(streamer);

            chargerDepuisImage(tracker, "stones.jpg");
            chargerDepuisImage(tracker, "smallmeduse.jpeg");
            chargerDepuisImage(tracker, "sammllrezo.jpeg");
            trackers.add(tracker);

            return status;
        }
    }

    void eliminer()
    {
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

    void demarrer()
    {
        camera.start();
        streamer.start();
        camera.setFocusMode(CameraDeviceFocusMode.Continousauto);
        for (ImageTracker tracker : trackers) {
            tracker.start();
        }
    }

    void arreter()
    {
        for (ImageTracker tracker : trackers) {
            tracker.stop();
        }
        streamer.stop();
        camera.stop();
    }
    //end region cycle de Vie

    //demarrer region Actions

    /**
     * Initialisation des renderer (video et rectangle)
     */
    void initialiserGL()
    {
        if (cameraRenderer != null) {
            cameraRenderer.dispose();
        }
        cameraRenderer = new Renderer();

        rectangleRenderer = new RectangleRenderer();
        rectangleRenderer.init(contexte, idCalque);
    }

    /**
     * Mise à jour des dimensions
     * @param width nouvelle largeur
     * @param height nouvelle hauteur
     */
    void redimensionnerGL(int width, int height)
    {
        tailleVue = new Vec2I(width, height);
        viewportChanged = true;
    }

    /**
     * Mise a jour du viewPort
     * Gestion de tailleVue
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
     */
    void render()
    {
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
        }
        finally {
            frame.dispose();
        }
    }

    /**
     * Passage au calque suivant (à adapter selon les besoins)
     */
    void calqueSuivant() {
        int nextId = R.drawable.fres;
        switch(idCalque){
            case R.drawable.fromage :
                nextId = R.drawable.fresk2048;
                idCalque = nextId;
                break;
            case R.drawable.fresk2048 :
                nextId = R.drawable.medusecalque;
                idCalque = nextId;
                break;
            case R.drawable.medusecalque :
                nextId = R.drawable.fromage;
                idCalque = nextId;
                break;
        }
        rectangleRenderer.imageSuivante(contexte,nextId);
    }

    //end region Actions
}
