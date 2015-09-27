package com.test.gameTest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.test.base.ModelObject;
import com.test.camera.GameCamera;
import com.test.camera.MoveableOCamera;
import com.test.camera.MoveablePCamera;
import com.test.light.PointLight;
import com.test.light.Spotlight;

/**
 * Created by naki on 25/09/15.
 */
public class SpotlightScene extends ApplicationAdapter {
    Mesh spaceshipMesh;
    ShaderProgram shaderProgram;
    GameCamera camera;
    Texture texture;
    Spotlight spotlight;
    ModelObject model;

    @Override
    public void create () {
        String vs = Gdx.files.internal("defaultVS.glsl").readString();
        String fs = Gdx.files.internal("spot-phong-FS.glsl").readString();
        shaderProgram = new ShaderProgram(vs, fs);
        if (!shaderProgram.isCompiled()) {
            System.out.println(shaderProgram.getLog());
        }
        ModelLoader<?> loader = new ObjLoader();
        ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
        texture = new Texture("ship.png");
        spaceshipMesh = new Mesh(true,
                data.meshes.get(0).vertices.length,
                data.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        spaceshipMesh.setVertices(data.meshes.get(0).vertices);
        spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);
        camera = new MoveablePCamera();
        model = new ModelObject(spaceshipMesh, 0.6f, new Vector3(0.5f, 0.5f, 0), new Vector3(0, 0, 0));
        spotlight = new Spotlight(new Vector3(0, 3, 0), new Vector3(0, (float)Math.PI/4 + 0.3f, 0), new Vector3(0.3f, 1.0f, 0.3f), 1, (float)Math.PI / 4);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        texture.bind();
        camera.update();
        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_model", model.getTRS());
        shaderProgram.setUniformMatrix("u_mvp", model.getTRS().mul(camera.getProjection()));
        shaderProgram.setUniform4fv("u_light_pos", spotlight.getPositionAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", spotlight.getColorAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_dir", spotlight.getRotationAsV4(), 0, 4);
        shaderProgram.setUniformf("u_light_in", spotlight.getIntensity());
        shaderProgram.setUniformf("u_light_max_angle_cos", (float)Math.cos((double)spotlight.getMaxAngle()));
        shaderProgram.setUniformf("u_shininess", model.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
    }
}
