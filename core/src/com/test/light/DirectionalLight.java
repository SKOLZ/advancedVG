package com.test.light;

import com.badlogic.gdx.math.Vector3;

public class DirectionalLight extends AbsLight{

	public DirectionalLight(Vector3 rotation, Vector3 color, float intensity) {
        super();
        setRotation(rotation);
        setColor(color);
        setIntensity(intensity);
    }
}
