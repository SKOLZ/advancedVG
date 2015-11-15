package com.test.light;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by naki on 27/09/15.
 */
public class Spotlight extends AbsLight {
    public float maxAngle;
    private Matrix4 projection;
    
    public Spotlight(Vector3 position, Vector3 rotation, Vector3 color, float intensity, float maxAngle) {
        super();
        setPosition(position);
        setRotation(rotation);
        setColor(color);
        setIntensity(intensity);
        setMaxAngle(maxAngle);
    }
    
    public Spotlight(Vector3 position, Vector3 rotation, Vector3 color, float intensity, float maxAngle, float nearZ, float farZ) {
        super();
        setPosition(position);
        setRotation(rotation);
        setColor(color);
        setIntensity(intensity);
        setMaxAngle(maxAngle);
        generateProjection(nearZ, farZ, 60, 60);
    }
    
    public void generateProjection(float nearZ, float farZ, float fovX, float fovY) {
    	projection = new Matrix4(new float[] {
				(float)Math.atan(fovX / 2), 0, 0, 0,
				0, (float)Math.atan(fovY / 2), 0, 0,
				0, 0, -(farZ + nearZ) / (farZ - nearZ), -2 * (nearZ * farZ) / (farZ - nearZ),
				0, 0, -1, 0
		}).tra();
	}
    
    public Matrix4 getProjection() {
    	return projection.cpy().mul(getTRS().inv());
    }

    public float getMaxAngle() { return maxAngle; }
    public void setMaxAngle(float maxAngle) {
        this.maxAngle = maxAngle;
    }
}
