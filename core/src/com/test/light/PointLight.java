package com.test.light;

import com.badlogic.gdx.math.Vector3;

public class PointLight extends AbsLight {

    public PointLight(Vector3 position, Vector3 color, float intensity) {
        super();
        setPosition(position);
        setColor(color);
        setIntensity(intensity);
    }
}
