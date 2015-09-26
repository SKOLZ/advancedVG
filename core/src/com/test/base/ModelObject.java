package com.test.base;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by naki on 26/09/15.
 */
public class ModelObject extends BaseObject {

    // Model's shininess for Phong Specular Lighting
    public float shininess;
    // Model's mesh
    public Mesh mesh;

    public ModelObject(Mesh mesh, float shininess, Vector3 position, Vector3 rotation) {
        super(position, rotation);
        setMesh(mesh);
        setShininess(shininess);
    }

    public Mesh getMesh() { return mesh; }
    public void setMesh(Mesh mesh) { this.mesh = mesh; }

    public float getShininess() { return shininess; }
    public void setShininess(float shininess) { this.shininess = shininess; }
}
