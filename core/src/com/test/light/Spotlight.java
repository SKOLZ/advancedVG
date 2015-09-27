package com.test.light;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by naki on 27/09/15.
 */
public class Spotlight extends AbsLight {
    public float maxAngle;

    public Spotlight(Vector3 position, Vector3 rotation, Vector3 color, float intensity, float maxAngle) {
        super();
        setPosition(position);
        setRotation(rotation);
        setColor(color);
        setIntensity(intensity);
        setMaxAngle(maxAngle);
    }

    public float getMaxAngle() { return maxAngle; }
    public void setMaxAngle(float maxAngle) {
        this.maxAngle = maxAngle;
    }
}
