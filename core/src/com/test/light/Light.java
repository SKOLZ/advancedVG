package com.test.light;

import com.badlogic.gdx.math.Vector3;
import com.test.base.GameObject;

/**
 * Created by naki on 23/09/15.
 */
public interface Light extends GameObject {
    Vector3 getColor();

    void setColor(Vector3 color);
}