//================================================================================================================================
//
//  Copyright (c) 2015-2018 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
//  EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
//  and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package com.example.ddale.AR;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import cn.easyar.Engine;

public class GLView extends GLSurfaceView
{
    /**
     * Objet qui sert de controlleur pour l'affichage de la camera et des objets AR
     */
    private final ARManager ARManager;

    /* -------------------DEBUT DU CODE RECUPERE DEPUIS L'EXAMPLE EASYAR------------------------------*/

    /**
     * Constructeur
     *
     * @param context contexte pour l'accès aux ressurces de l'application
     */
    public GLView(Context context) {
        super(context);
        setEGLContextFactory(new ContextFactory());
        setEGLConfigChooser(new ConfigChooser());

        ARManager = new ARManager(context);

        //le Renderer de la view se charge de "render" (calculer) les objets à afficher dans la view
        //Ici, on crée un renderer qui exploite notre ARManager (appelle les fonction d'ARManager
        //Comme ARManager s'éxécute dans un autre thread, on fait appel à "synchronized"
        this.setRenderer(new Renderer() {

            /** Actions à la création de la view
             * parametres inutilisés
             */
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                synchronized (ARManager) {
                    ARManager.initialiserGL();
                }

            }

            /** Actions à la modification de la view (ie du cadre d'affichage, donc des dimensions)
             * @param gl inutilisé
             * @param w nouvelle largeur
             * @param h nouvelle hauteur
             */
            @Override
            public void onSurfaceChanged(GL10 gl, int w, int h) {
                synchronized (ARManager) {
                    ARManager.redimensionnerGL(w, h);
                }
            }

            /** Action de dessin
             * @param gl inutilisé
             */
            @Override
            public void onDrawFrame(GL10 gl) {
                synchronized (ARManager) {
                    ARManager.render();
                }
            }

        });
        this.setZOrderMediaOverlay(true);
        //this.setOnClickListener(this);
    }

    //Fin de region comportement de la vue
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        synchronized (ARManager) {
            if (ARManager.initialiser()) {
                ARManager.demarrer();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        synchronized (ARManager) {
            ARManager.arreter();
            ARManager.eliminer();
        }
        super.onDetachedFromWindow();
    }
    //End of region comportement de la vue

    //Start of region Cycle de Vie de la vue
    @Override
    public void onResume() {
        super.onResume();
        Engine.onResume();
    }

    @Override
    public void onPause() {
        Engine.onPause();
        super.onPause();
    }
    //Fin de region Cycle de Vie de la vue

    /**
     * Notifie l'ARManager pour lui permettre d'initialiser son tracker
     *
     * @param cheminCible url de l'image cible à télécharger
     */
    public void notifier(final String cheminCible) {
        queueEvent(new Runnable() {
            // This method will be called on the rendering thread:
            public void run() {
                ARManager.notifier(cheminCible);
            }
        });
    }

    private static class ConfigChooser implements EGLConfigChooser {
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display)
        {
            final int EGL_OPENGL_ES2_BIT = 0x0004;
            final int[] attrib = { EGL10.EGL_RED_SIZE, 4, EGL10.EGL_GREEN_SIZE, 4,
                    EGL10.EGL_BLUE_SIZE, 4, EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                    EGL10.EGL_NONE };

            int[] num_config = new int[1];
            egl.eglChooseConfig(display, attrib, null, 0, num_config);

            int numConfigs = num_config[0];
            if (numConfigs <= 0)
                throw new IllegalArgumentException("fail to choose EGL configs");

            EGLConfig[] configs = new EGLConfig[numConfigs];
            egl.eglChooseConfig(display, attrib, configs, numConfigs,
                    num_config);

            for (EGLConfig config : configs)
            {
                int[] val = new int[1];
                int r = 0, g = 0, b = 0, a = 0, d = 0;
                if (egl.eglGetConfigAttrib(display, config, EGL10.EGL_DEPTH_SIZE, val))
                    d = val[0];
                if (d < 16)
                    continue;

                if (egl.eglGetConfigAttrib(display, config, EGL10.EGL_RED_SIZE, val))
                    r = val[0];
                if (egl.eglGetConfigAttrib(display, config, EGL10.EGL_GREEN_SIZE, val))
                    g = val[0];
                if (egl.eglGetConfigAttrib(display, config, EGL10.EGL_BLUE_SIZE, val))
                    b = val[0];
                if (egl.eglGetConfigAttrib(display, config, EGL10.EGL_ALPHA_SIZE, val))
                    a = val[0];
                if (r == 8 && g == 8 && b == 8 && a == 0)
                    return config;
            }

            return configs[0];
        }
    }
    //Fin de Region Classes Internes
    /* ---------------------FIN DU CODE RECUPERE DEPUIS L'EXAMPLE EASYAR------------------------------*/

    /*
    GLView et ArManager ont des threads différents, on utilise donc la méthode queueEvent pour
    appeler une fonction de ARManager
     */

    /**
     * Demande à l'ARManager de changer de claque
     *
     * @param cheminCalque url de l'image du calque à télécharger
     */
    public void changerCalque(final String cheminCalque) {
        queueEvent(new Runnable() {
            // This method will be called on the rendering thread:
            public void run() {
                ARManager.changerCalque(cheminCalque);
            }
        });
    }

    /**
     * Demande à l'ARManager de ne rien afficher
     */
    public void afficherVide() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                ARManager.afficherVide();
            }
        });

    }

    //Début de Region Classes Internes
    private static class ContextFactory implements EGLContextFactory {
        private static int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {
            EGLContext context;
            int[] attrib = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
            context = egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib);
            return context;
        }

        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
            egl.eglDestroyContext(display, context);
        }
    }

}
