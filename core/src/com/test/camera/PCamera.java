package com.test.camera;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.test.camera.AbsCamera;

public class PCamera extends AbsCamera implements GameCamera {

    public static final float DEFAULT_X = 10.0f;
    public static final float DEFAULT_Y = 10.0f;
    public static final float DEFAULT_FAR = 100.0f;
    public static final float DEFAULT_NEAR = 0.01f;
    public static final Vector3 DEFAULT_POS = new Vector3(0, 0, 5);
    public static final Vector3 DEFAULT_ROT = new Vector3(0, 0, 0);

    public PCamera() {
        this(DEFAULT_X, DEFAULT_Y, DEFAULT_FAR, DEFAULT_NEAR, DEFAULT_POS, DEFAULT_ROT);
    }

    public PCamera(float x, float y, float far, float near, Vector3 pos, Vector3 rot) {
        createProjectionMatrix(near, far, x, y);
        this.setPosition(pos);
        this.setRotation(rot);
    }

    private void createProjectionMatrix(float near, float far, float x, float y) {

        Matrix4 matrix = new Matrix4(new float[] {
                1 / (float)Math.tan(x / 2), 0, 0, 0,
                0, 1 / (float)Math.tan(y / 2), 0, 0,
                0, 0, -(near + far) / (far - near), -2 * (near * far) / (far - near),
                0, 0, -1, 0 });
        setProjMatrix(matrix.tra());
    }

    @Override
    public void update() {

    }

}
