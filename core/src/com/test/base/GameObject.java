package com.test.base;

import com.badlogic.gdx.math.Vector3;

public interface GameObject {

    public Vector3 getPosition();
    public void setPosition(Vector3 pos);

    public Vector3 getRotation();
    public void setRotation(Vector3 rot);

    public Vector3 getScale();
    public void setScale(Vector3 scale);
}
