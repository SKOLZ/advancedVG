package com.test.base;

import com.badlogic.gdx.math.Vector3;

public interface GameObject {

    Vector3 getPosition();
    void setPosition(Vector3 pos);
    float [] getPositionAsV4();

    Vector3 getRotation();
    void setRotation(Vector3 rot);

    Vector3 getScale();
    void setScale(Vector3 scale);
}
