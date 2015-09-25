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
import com.test.camera.GameCamera;
import com.test.camera.MoveableOCamera;
import com.test.camera.MoveablePCamera;
import com.test.light.PointLight;

/**
 * Created by naki on 25/09/15.
 */
public class PointLightScene extends ApplicationAdapter {
    Mesh spaceshipMesh;
    ShaderProgram shaderProgram;
    GameCamera camera;
    Texture texture;
    PointLight pointLight;

    @Override
    public void create () {
        String vs = Gdx.files.internal("defaultVS.glsl").readString();
        String fs = Gdx.files.internal("point-phong-FS.glsl").readString();
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
        pointLight = new PointLight(new Vector3(0, 3, 0), new Vector3(0.3f, 1.0f, 0.3f), 1);
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
        shaderProgram.setUniformMatrix("u_model", new Matrix4());
        shaderProgram.setUniformMatrix("u_mvp", camera.getProjection());
        shaderProgram.setUniform4fv("u_light_pos", pointLight.getPositionAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", pointLight.getColorAsV4(), 0, 4);
        shaderProgram.setUniformf("u_light_in", pointLight.getIntensity());
        shaderProgram.setUniformf("u_shininess", 0.6f);
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
    }
}
