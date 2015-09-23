package com.test.light;

import com.badlogic.gdx.math.Vector3;
import com.test.base.BaseObject;

/**
 * Created by naki on 23/09/15.
 */
public abstract class AbsLight extends BaseObject implements Light {
    protected Vector3 color;

    public Vector3 getColor() {
        return color;
    };

    public void setColor(Vector3 color) {
        this.color = color;
    }
}
