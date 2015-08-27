package com.test.gameTest;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public abstract class AbsCamera {
	protected Vector3 position;
	protected Matrix4 projMatrix;
	protected Vector3 orientation;
	
	public Vector3 getPosition() {
		return position;
	}
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	
	public Vector3 getOrientation() {
		return orientation;
	}
	
	public void setOrientation(Vector3 orientation) {
		this.orientation = orientation;
	}
	
	public Matrix4 getProjMatrix() {
		return projMatrix;
	}
	public void setProjMatrix(Matrix4 projMatrix) {
		this.projMatrix = projMatrix;
	}
	
	public abstract void setProjection(float size);
	
	public abstract void lookAt(Vector3 position);
	
}
