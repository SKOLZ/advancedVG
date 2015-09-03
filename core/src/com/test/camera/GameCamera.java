package com.test.camera;

import com.badlogic.gdx.math.Matrix4;
import com.test.base.GameObject;

public interface GameCamera extends GameObject {

    public void update();

    public Matrix4 getProjection();
}
