package com.test.light;

import com.badlogic.gdx.math.Vector3;
import com.test.base.BaseObject;

public abstract class AbsLight extends BaseObject implements Light {
    protected Vector3 color;
    protected float intensity;

    public Vector3 getColor() {
        return color;
    };

    public void setColor(Vector3 color) {
        this.color = color;
    }

    public float[] getColorAsV4() {
        return new float[] { color.x, color.y, color.z, 1 };
    };

    public float getIntensity() {
        return intensity;
    };

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
