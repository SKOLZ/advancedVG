package com.test.camera;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.test.base.BaseObject;

public abstract class AbsCamera extends BaseObject implements GameCamera {

	protected Matrix4 projMatrix;

	public Matrix4 getProjMatrix() {
		return projMatrix;
	}
	public void setProjMatrix(Matrix4 projMatrix) {
		this.projMatrix = projMatrix;
	}

	public Matrix4 getProjection() {
		Matrix4 view = getTRS().inv();
		Matrix4 projMatrix = new Matrix4(getProjMatrix());
		return projMatrix.mul(view);
	}

	//public abstract void lookAt(Vector3 position);
	
}
