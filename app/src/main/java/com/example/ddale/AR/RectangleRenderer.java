package com.example.ddale.AR;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import cn.easyar.Matrix44F;
import cn.easyar.Vec2F;

class RectangleRenderer {

    private String CAT = "Rectangle";
    private int mProgram;

    private int refCoordRect;
    private int refMatriceProj;
    private int refMatriceTransformation;
    private int refBufferCoordRect;

    private int refDataTexture;
    private int refCoordTexture;
    private int refBufferCoordTexture;
    private float hauteurCalque;
    private float largeurCalque;

    private int refCouleur;
    private float[] couleur;

    private short[] ordreDessin;
    private ShortBuffer bufferOrdreDessin;

    private final String vertexShaderCode;
    private final String fragmentShaderCode;

    RectangleRenderer() {
        vertexShaderCode =
                "uniform mat4 uMatTrans;\n" +
                // This matrix member variable provides a hook to manipulate
                // the coordinates of the objects that use this vertex shader
                "uniform mat4 uMatProj;\n" +
                "attribute vec4 aCoordRect;\n" +
                "attribute vec2 aCoordTex;\n" +
                "attribute vec4 aCouleur;\n" +
                "varying vec2 vCoordTex; \n" +
                "varying vec4 vCouleur;\n" +
                "void main() {\n" +
                "    vCoordTex = aCoordTex;\n" +
                "    vCouleur = aCouleur;\n" +
                // the matrix must be included as a modifier of gl_Position
                // Note that the uMVPMatrix factor *must be first* in order
                // for the matrix multiplication product to be correct.
                "    gl_Position = uMatProj * uMatTrans * aCoordRect;\n" +
                "}";
        fragmentShaderCode =
                "precision mediump float;\n" +
                "uniform vec4 vCouleur;\n" +
                "uniform sampler2D uTexture;\n" +
                "varying vec2 vCoordTex;\n" +
                "void main() {" +
                "  gl_FragColor = vCouleur * texture2D(uTexture, vCoordTex);" +
                "}";
        couleur = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        ordreDessin = new short[]{0, 1, 2, 0, 2, 3};
        bufferOrdreDessin = ByteBuffer.allocateDirect(ordreDessin.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer().put(ordreDessin);
    }

    void init(Bitmap bitmapPremierCalque) {

        /* Initialisation du programme OpenGL **/
        mProgram = GLES20.glCreateProgram();

        /* Compilation des Shaders */
        int vertShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertShader, vertexShaderCode);
        GLES20.glCompileShader(vertShader);
        int fragShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragShader, fragmentShaderCode);
        GLES20.glCompileShader(fragShader);
        GLES20.glAttachShader(mProgram, vertShader);
        GLES20.glAttachShader(mProgram, fragShader);
        GLES20.glLinkProgram(mProgram);
        //autorisations des fonctions de blending(mélange des couches)
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        /* Lancement du programme */
        GLES20.glUseProgram(mProgram);

        /* Récupération des poignées */
        refCoordRect = GLES20.glGetAttribLocation(mProgram, "aCoordRect");
        refMatriceProj = GLES20.glGetUniformLocation(mProgram, "uMatProj");
        refMatriceTransformation = GLES20.glGetUniformLocation(mProgram,"uMatTrans");
        refCouleur = GLES20.glGetUniformLocation(mProgram, "vCouleur");

        refCoordTexture = GLES20.glGetAttribLocation(mProgram, "aCoordTex");

        refDataTexture = creerTexture(bitmapPremierCalque);

        /* Binding des coordonnées de la texture */
        refBufferCoordTexture = genererBuffer();
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, refBufferCoordTexture);
        float[][] coordTexture = {
                {0, 0},
                {1, 0},
                {1, 1},
                {0, 1},
        };
        FloatBuffer bufferCoordTexture = FloatBuffer.wrap(applatir(coordTexture));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, bufferCoordTexture.limit() * 4,
                bufferCoordTexture, GLES20.GL_DYNAMIC_DRAW);

        /* Binding des coordonnées du rectangle */
        refBufferCoordRect = genererBuffer();
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, refBufferCoordRect);
        float[][] coordRectangle = {
                {-1.0f/2, -1.0f/2, 0.01f},
                {1.0f /2, -1.0f/2, 0.01f},
                {1.0f /2, 1.0f /2, 0.01f},
                {-1.0f/2, 1.0f /2, 0.01f},
        };
        FloatBuffer bufferCoordRectangle = FloatBuffer.wrap(applatir(coordRectangle));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, bufferCoordRectangle.limit() * 4,
                bufferCoordRectangle, GLES20.GL_DYNAMIC_DRAW);

    }

    void render(Matrix44F matriceProjection, Matrix44F matriceVueCamera, Vec2F tailleCible) {

        //float largeurCible = tailleCible.data[0];
        float hauteurCible = tailleCible.data[1];
        float largeur = largeurCalque / hauteurCalque * hauteurCible;
        float hauteur = 1 * hauteurCible; //Variable disponible pour modification future
        float profondeur = (float) 0.01;

        //Mise à jour du buffer des dimensions du carré
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, refBufferCoordRect);
        float[][] coordRectangle = {
                {largeur /2 , hauteur /2 , profondeur}, // haut droit
                {-largeur /2 , hauteur /2 , profondeur}, // haut gauche
                {-largeur /2 , -hauteur /2 , profondeur}, // bas gauche
                {largeur /2 , -hauteur /2 , profondeur}, // bas droit
        };
        FloatBuffer bufferCoordRectangle = FloatBuffer.wrap(applatir(coordRectangle));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, bufferCoordRectangle.limit() * 4,
                bufferCoordRectangle, GLES20.GL_DYNAMIC_DRAW);


        GLES20.glUseProgram(mProgram);

        // Mise à jour des coordonnées du carré
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, refBufferCoordRect);
        GLES20.glEnableVertexAttribArray(refCoordRect);
        GLES20.glVertexAttribPointer(refCoordRect, 3,
                GLES20.GL_FLOAT, false, 0, 0);

        //Mise à jour des coordonnées de la texture
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, refBufferCoordTexture);
        GLES20.glEnableVertexAttribArray(refCoordTexture);
        GLES20.glVertexAttribPointer(refCoordTexture,
                2, GLES20.GL_FLOAT, false, 0, 0);

        //Mise à jour des matrices de couleur, de transfromation Caméra, et de projection
        GLES20.glUniform4fv(refCouleur, 1, couleur, 0);
        GLES20.glUniformMatrix4fv(refMatriceTransformation, 1, false,
                matriceVueCamera.data, 0);
        GLES20.glUniformMatrix4fv(refMatriceProj, 1, false,
                matriceProjection.data, 0);

        bufferOrdreDessin.position(0);
        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, ordreDessin.length,
                GLES20.GL_UNSIGNED_SHORT, bufferOrdreDessin);
    }

    //region utils
    /**
     * Constructeur de buffer vide
     */
    private int genererBuffer() {
        int[] buffer = {0};
        GLES20.glGenBuffers(1, buffer, 0);
        return buffer[0];
    }

    /**
     * Fonction qui "applatis" les tableaux de coordonnes
     * pour en faire des tableaux passable dans un buffer (1 ligne)
     * @param a tableau à applatir
     * @return un tableau à une ligne contenant les valeurs du tableau initial
     */
    private float[] applatir(float[][] a) {
        int tailleTotale = 0;
        for (float[] floats : a) {
            tailleTotale += floats.length;
        }
        float[] tableauFinal = new float[tailleTotale];
        int offset = 0;

        for (float[] floats : a) {
            System.arraycopy(floats, 0, tableauFinal, offset, floats.length);
            offset += floats.length;
        }
        return tableauFinal;
    }

    private int creerTexture(Bitmap bitmapCalque){
        largeurCalque = bitmapCalque.getWidth();
        hauteurCalque = bitmapCalque.getHeight();

        final int[] refDataTexture = new int[1];
        GLES20.glGenTextures(1, refDataTexture, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, refDataTexture[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        majTexture(refDataTexture[0], bitmapCalque);
        return refDataTexture[0];
    }

    private void majTexture(int refDataTexture, Bitmap bitmap) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, refDataTexture);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    /**
     * Fonction qui récupère le calque suivant et le met dans la texture affichée
     *
     */
    void changerCalque(Bitmap bitmap) {
        largeurCalque = bitmap.getWidth();
        hauteurCalque = bitmap.getHeight();
        majTexture(refDataTexture,bitmap);
    }

    // end region utils
}

