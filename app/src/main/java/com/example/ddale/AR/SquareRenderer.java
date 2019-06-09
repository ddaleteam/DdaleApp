package com.example.ddale.AR;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import cn.easyar.Matrix44F;
import cn.easyar.Vec2F;

class SquareRenderer {

    private String TAG = "Square";

    private int mProgram;

    /** This will be used to pass in model texture coordinate information. */
    private int mTextureCoordinateHandle;
    /** This is a handle to our texture data. */
    private int mTextureDataHandle;

    private int positionHandle; // = pos_coord_square
    private int colorHandle;    // = pos_color_square
    private int vPMatrixHandle; // = pos_proj_square
    private int transHandle;    // = pos_trans_square
    private int vbo_coord_square;
    private int vbo_text_square;

    private float height;
    private float width;

    private short[] drawOrder = {0, 1, 2, 0, 2, 3}; // order to draw vertices
    // Set color with red, green, blue and alpha (opacity) values
    private float[] color = {1.0f, 1.0f, 1.0f, 0.6f};

    private ShortBuffer drawListBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2)
            .order(ByteOrder.nativeOrder()).asShortBuffer().put(drawOrder);

    private final String vertexShaderCode =
            "uniform mat4 trans;\n" +
                    // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_TexCoordinate;\n" +
                    "attribute vec4 color;\n" +
                    "varying vec2 v_TexCoordinate; \n" +
                    "varying vec4 vcolor;\n" +
                    "void main() {" +
                    "    v_TexCoordinate = a_TexCoordinate;\n" +
                    "    vcolor = color;\n" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * trans * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vcolor;" +
                    "uniform sampler2D u_Texture;\n" +
                    "varying vec2 v_TexCoordinate;\n" +
                    "void main() {" +
                    "  gl_FragColor = vcolor * texture2D(u_Texture, v_TexCoordinate);\n" +
                    "}";


    //region utils
    /**
     * Constructeur de buffer vide
     */
    private int generateOneBuffer() {
        int[] buffer = {0};
        GLES20.glGenBuffers(1, buffer, 0);
        return buffer[0];
    }

    /**
     * Fonction qui "applatis" des tableaux de coordonnes
     * pour en faire des "listes" lisible par des buffers
     * @param a tableau de floats à "applatir"
     * @return une "liste" contenant les floatants présnets dans le tableau
     */
    private float[] flatten(float[][] a) {
        int size = 0;
        for (float[] floats : a) {
            size += floats.length;
        }
        float[] l = new float[size];
        int offset = 0;

        for (float[] floats : a) {
            System.arraycopy(floats, 0, l, offset, floats.length);
            offset += floats.length;
        }
        return l;
    }

    private int loadImage(final Context context, final int resourceId){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;   // No pre-scaling
        // Read in the resource
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        return createTexture(bitmap);
    }

    private int createTexture(Bitmap bitmap){
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        updateTexture(textureHandle[0], bitmap);
        return textureHandle[0];
    }

    private void updateTexture(int textureName, Bitmap bitmap) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureName);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }
    // end region utils

    void init(final Context context,final int resourceId) {

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

        /* Lancement du programme */
        GLES20.glUseProgram(mProgram);

        /* Récupération des handles */
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vcolor");
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        transHandle = GLES20.glGetUniformLocation(mProgram,"trans");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");

        /* Chargement de la texture (et binding) */
        /* int[] imageInfos = loadTexture(context,resourceId);
        width = imageInfos[1];
        height = imageInfos[2]; */


        mTextureDataHandle = loadImage(context,resourceId);

        /* Binding des coordonnées de la texture */
        vbo_text_square = generateOneBuffer();
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo_text_square);
        float[][] textureCoords = {
                {0, 0},
                {1, 0},
                {1, 1},
                {0, 1},
        };
        FloatBuffer square_text_buffer = FloatBuffer.wrap(flatten(textureCoords));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, square_text_buffer.limit() * 4, square_text_buffer, GLES20.GL_DYNAMIC_DRAW);

        /* Binding des coordonnées du rectangle */
        vbo_coord_square = generateOneBuffer();
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo_coord_square);
        float[][] square_vertices = {
                {-1.0f/2, -1.0f/2, 0.01f},
                {1.0f /2, -1.0f/2, 0.01f},
                {1.0f /2, 1.0f /2, 0.01f},
                {-1.0f/2, 1.0f /2, 0.01f},
        };
        FloatBuffer square_vertices_buffer = FloatBuffer.wrap(flatten(square_vertices));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, square_vertices_buffer.limit() * 4, square_vertices_buffer, GLES20.GL_DYNAMIC_DRAW);
        Log.i(TAG, "init: initialized square");
    }

    void render(Matrix44F projMatrix, Matrix44F cameraview, Vec2F size) {
        float targetWidth = size.data[0];
        float targetHeight = size.data[1];
        float size0 = width/height * targetHeight;
        //noinspection UnnecessaryLocalVariable
        float size1 = targetHeight; //Variable disponible pour modification future

        Log.i(TAG, "render: w" + size0 + " h" + size1);

        //Mise à jour du buffer des dimensions du carré
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo_coord_square);
        float height = (float) 0.01;
        float[][] square_vertices = {
                {size0/2 , size1/2 , height }, //top left of real, bottom right of tex
                {-size0/2 , size1/2 , height }, // top right
                {-size0/2 , -size1/2 , height }, // bot right
                {size0/2 , -size1/2 , height }, // bot left
        };
        FloatBuffer square_vertices_buffer = FloatBuffer.wrap(flatten(square_vertices));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, square_vertices_buffer.limit() * 4,
                square_vertices_buffer, GLES20.GL_DYNAMIC_DRAW);

        //autorisations des fonctions de blending(mélange des couches) et appel au programme
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glUseProgram(mProgram);

        // Mise à jour des coordonnées du carré
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo_coord_square);
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3,
                GLES20.GL_FLOAT, false, 0, 0);

        //Mise à jour des coordonnées de la texture
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo_text_square);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,
                2, GLES20.GL_FLOAT, false, 0, 0);

        //Mise à jour des matrices de couleur, de transfromation Caméra, et de projection
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
        GLES20.glUniformMatrix4fv(transHandle, 1, false, cameraview.data, 0);
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, projMatrix.data, 0);

        drawListBuffer.position(0);
        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
    }

    /**
     * Fonction qui récupère le calque suivant et le met dans la texture affichée
     * @param context nécéssaire pour accéder au fichier bitmap dans les ressources
     * @param resourceId id du calque a importer
     */
    void imageSuivante(final Context context, final int resourceId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;   // No pre-scaling
        // Read in the resource
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        Log.i(TAG, "imageSuivante: w"  + width + " h" + height);
        updateTexture(mTextureDataHandle,bitmap);
    }
}

