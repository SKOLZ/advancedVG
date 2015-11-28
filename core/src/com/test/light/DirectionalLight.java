package com.test.light;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class DirectionalLight extends AbsLight{
	
	private Matrix4 projection;

	public DirectionalLight(Vector3 rotation, Vector3 color, float intensity) {
        super();
        setRotation(rotation);
        setColor(color);
        setIntensity(intensity);
    }
	
	public DirectionalLight(Vector3 rotation, Vector3 color, float intensity, float nearZ, float farZ) {
        super();
        setRotation(rotation);
        setColor(color);
        setIntensity(intensity);
        generateProjection(nearZ, farZ, 20, 20);
    }
	
	public Matrix4 getViewProjection() {
    	return projection.cpy().mul(getTRS().inv());
    }
	
	public void generateProjection(float nearZ, float farZ, float height, float width) {
		projection = new Matrix4(new float[] { 
				1 / width, 0, 0, 0, 
				0, 1 / height, 0, 0, 
				0, 0, -2 / (farZ - nearZ), -(nearZ + farZ) / (farZ - nearZ), 
				0, 0, 0, 1
		}).tra();
	}
}
