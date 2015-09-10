package com.test.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.test.camera.AbsCamera;

public class OCamera extends AbsCamera implements GameCamera {

	public static final float DEFAULT_X = 10.0f;
	public static final float DEFAULT_Y = 10.0f;
	public static final float DEFAULT_FAR = 100.0f;
	public static final float DEFAULT_NEAR = 0.01f;
	public static final Vector3 DEFAULT_POS = new Vector3(0, 0 ,0);
	public static final Vector3 DEFAULT_ROT = new Vector3(0, 0 ,0);

	public OCamera() {
		this(DEFAULT_X, DEFAULT_Y, DEFAULT_FAR, DEFAULT_NEAR, DEFAULT_POS, DEFAULT_ROT);
	}

	public OCamera(float x, float y, float far, float near, Vector3 pos, Vector3 rot) {
		createProjectionMatrix(x, y, far, near);
		this.setPosition(position);
		this.setRotation(rot);
	}

	public void setProjection(float size) {
		float[] values = projMatrix.getValues();
		values[0] *= size;
		values[5] *= size;
		setProjMatrix(new Matrix4(values));
	}
	
	public void createProjectionMatrix(float x, float y, float far, float near) {
		float[] values = {
				2 / x, 0, 0, 0,
				0, 2 / y, 0, 0,
				0, 0, -2 / (far - near), (far + near) / (far - near),
				0, 0, 0, 1
		};
		setProjMatrix(new Matrix4(values));
	}


	public void lookAt(Vector3 position) {
		
	}

	@Override
	public void update() {

	}
}
