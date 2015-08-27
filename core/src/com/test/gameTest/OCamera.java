package com.test.gameTest;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class OCamera extends AbsCamera {
	
	public OCamera(float x, float y, float far, float near, Vector3 pos, Vector3 orient) {
		createProjectionMatrix(x, y, far, near);
		this.setPosition(position);
		this.setOrientation(orient);
	}

	@Override
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

	@Override
	public void lookAt(Vector3 position) {
		
	}
	
}
