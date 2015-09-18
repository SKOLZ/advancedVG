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
		Matrix4 trans = getTranslationMatrix();
		Matrix4 rotX = getXRotationMatrix();
		Matrix4 rotY = getYRotationMatrix();
		Matrix4 rotZ = getZRotationMatrix();
		rotX.mul(rotY).mul(rotZ);
		Matrix4 ans = new Matrix4(getProjMatrix());
		Matrix4 view = new Matrix4(trans.mul(rotX)).inv();
		return ans.mul(view);
	}

	//public abstract void lookAt(Vector3 position);
	
}
