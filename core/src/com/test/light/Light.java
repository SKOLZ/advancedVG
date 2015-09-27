package com.test.light;

import com.badlogic.gdx.math.Vector3;
import com.test.base.GameObject;

public interface Light extends GameObject {
    Vector3 getColor();
    float[] getColorAsV4();
    void setColor(Vector3 color);

    float getIntensity();
    void setIntensity(float intensity);
}
