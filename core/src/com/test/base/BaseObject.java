package com.test.base;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class BaseObject implements GameObject {

    protected Vector3 position;
    protected Vector3 rotation;
    protected Vector3 scale;

    public BaseObject() {
        this(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
    }

    public BaseObject(Vector3 pos, Vector3 rot, Vector3 scale) {
        setPosition(pos);
        setRotation(rot);
        setScale(scale);
    }

    public BaseObject(Vector3 pos, Vector3 rot) {
        this(pos, rot, new Vector3(1, 1, 1));
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector3 pos) {
        this.position = pos;
    }

    @Override
    public Vector3 getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(Vector3 rot) {
        this.rotation = rot;
    }

    @Override
    public Vector3 getScale() {
        return scale;
    }

    @Override
    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    protected Matrix4 getXRotationMatrix() {
        return new Matrix4(new float[] { 1, 0, 0, 0,
                0, (float)Math.cos(rotation.x), (float) Math.sin(rotation.x), 0,
                0, - (float) Math.sin(rotation.x), (float) Math.cos(rotation.x), 0,
                0, 0, 0, 1 });
    }

    protected Matrix4 getYRotationMatrix() {
        return new Matrix4(new float[] { (float)Math.cos(rotation.y), 0, - (float)Math.sin(rotation.y), 0,
                0, 1, 0, 0,
                (float)Math.sin(rotation.y), 0, (float) Math.cos(rotation.y), 0,
                0, 0, 0, 1 });
    }

    protected Matrix4 getZRotationMatrix() {
        return new Matrix4(new float[] { (float)Math.cos(rotation.z), (float)Math.sin(rotation.z), 0, 0,
                - (float)Math.sin(rotation.z), (float)Math.cos(rotation.z), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1 });
    }

    public Matrix4 getTranslationMatrix() {
        return new Matrix4(
                new float[] { 1, 0, 0, 0,
                        0, 1, 0, 0,
                        0, 0, 1, 0,
                        position.x, position.y, position.z, 1 });
    }

    public Matrix4 getScaleMatrix() {
        return new Matrix4(
                new float[] { scale.x, 0, 0, 0,
                        0, scale.y, 0, 0,
                        0, 0, scale.z, 0,
                        0, 0, 0, 1 });
    }
}
